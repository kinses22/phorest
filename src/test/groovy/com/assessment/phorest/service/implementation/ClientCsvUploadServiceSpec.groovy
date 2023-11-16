package com.assessment.phorest.service.implementation

import com.assessment.phorest.dao.ClientRepository
import com.assessment.phorest.dto.ClientDTO
import com.assessment.phorest.entity.Client
import com.assessment.phorest.enumeration.Gender
import com.assessment.phorest.enumeration.Status
import com.assessment.phorest.mapper.ClientMapper
import com.assessment.phorest.row.ClientCsvRowMapper
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.Subject

class ClientCsvUploadServiceSpec extends Specification {

    @Subject
    ClientCsvUploadService subjectUnderTest;

    def clientMapper = Mock(ClientMapper)

    def genericCsvRowMapper = Mock(ClientCsvRowMapper)

    def genericRepository = Mock(ClientRepository)

    def validator = Mock(Validator)

    def multipartFile;

    def mockEntity

    def mockDTO

    def setup() {
        subjectUnderTest = new ClientCsvUploadService(genericRepository, clientMapper, genericCsvRowMapper, validator)
        multipartFile = new MockMultipartFile("clients.csv", "clients.csv", "text/csv",
                new FileInputStream(new File("clients.csv")));
        mockDTO = mockDTO()
        mockEntity = mockEntity()

    }

    def "should process CSV file and return expected status of processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        2 * validator.validate(mockDTO) >> []
        2 * clientMapper.mapToEntity(mockDTO) >> mockEntity
        2 * genericRepository.save(mockEntity)


        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "clients.csv"
        result.status == Status.PROCESSED
        result.recordsProcessed == 2
        assert result.getValidationErrors().isEmpty()
    }

    def "should not process CSV file and return expected status of not processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        def violationMessage = 'email invalid'
        def constraintViolation = Mock(ConstraintViolation)
        Set<ConstraintViolation<?>> violations = new HashSet<>()
        violations.add(constraintViolation);
        2 * validator.validate(mockDTO) >> violations
        2 * constraintViolation.getMessage() >> violationMessage

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "clients.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['e0b8ebfc-6e57-4661-9546-328c644a3764'] == ['email invalid']
        result.validationErrors['104fdf33-c8a2-4f1c-b371-3e9c2facdfa0'] == ['email invalid']
    }

    def "should partially process CSV file and return expected status of partially processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        def violationMessage = 'email invalid'
        def constraintViolation = Mock(ConstraintViolation)
        Set<ConstraintViolation<?>> violations = new HashSet<>()
        violations.add(constraintViolation);
        1 * validator.validate(mockDTO) >> violations
        1 * validator.validate(mockDTO) >> []
        1 * constraintViolation.getMessage() >> violationMessage
        1 * clientMapper.mapToEntity(mockDTO) >> mockEntity
        1 * genericRepository.save(mockEntity)

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "clients.csv"
        result.status == Status.PARTIALLY_PROCESSED
        result.recordsProcessed == 1
        result.validationErrors.size() == 1
        result.validationErrors['e0b8ebfc-6e57-4661-9546-328c644a3764'] == ['email invalid']
    }

    def "should not process CSV file as errors from db and return expected status of not processed"() {
        given:
        def client = new Client(UUID.fromString('e0b8ebfc-6e57-4661-9546-328c644a3764')
                , [], "s", "r", "sds@ema", "1234", Gender.Female ,true)
        def client1 = new Client(UUID.fromString('104fdf33-c8a2-4f1c-b371-3e9c2facdfa0'), [],
                "s", "r", "sds@ema", "1234", Gender.Male ,true)
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        2 * validator.validate(mockDTO) >> []
        1 * clientMapper.mapToEntity(mockDTO) >> client
        1 * clientMapper.mapToEntity(mockDTO) >> client1
        1 * genericRepository.save(client) >> { throw new DataIntegrityViolationException("Constraint violation") }
        1 * genericRepository.save(client1) >> { throw new DataIntegrityViolationException("Constraint violation") }

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "clients.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['e0b8ebfc-6e57-4661-9546-328c644a3764'] == ['Sql constraint error']
        result.validationErrors['104fdf33-c8a2-4f1c-b371-3e9c2facdfa0'] == ['Sql constraint error']
    }

    def "should not process CSV file as illegal argument thrown and return expected status of not processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> { throw new IllegalArgumentException("no id") }

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "clients.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['e0b8ebfc-6e57-4661-9546-328c644a3764'] == ['no id']
        result.validationErrors['104fdf33-c8a2-4f1c-b371-3e9c2facdfa0'] == ['no id']
    }

    ClientDTO mockDTO() {
        def id = "ef1e649c-e7b2-496f-82a1-38294f81d8de"
        def firstName = "Stephen"
        def lastName = "Kinsella"
        def email = "sk@hotmail.com"
        def phone = "04526681"
        def gender = Gender.Male
        def banned = false

        return new ClientDTO(id, [], firstName, lastName, email, phone, gender, banned)
    }

    Client mockEntity() {
        return new Client()
    }
}
