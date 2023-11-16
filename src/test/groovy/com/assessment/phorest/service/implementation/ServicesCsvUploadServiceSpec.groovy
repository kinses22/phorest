package com.assessment.phorest.service.implementation

import com.assessment.phorest.dao.ServiceRepository
import com.assessment.phorest.dto.ServiceDTO
import com.assessment.phorest.entity.Appointment
import com.assessment.phorest.entity.Service
import com.assessment.phorest.enumeration.Status
import com.assessment.phorest.mapper.ServiceMapper
import com.assessment.phorest.row.ServiceCsvRowMapper
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.Subject

class ServicesCsvUploadServiceSpec extends Specification {

    @Subject
    ServicesCsvUploadService subjectUnderTest;

    def serviceMapper = Mock(ServiceMapper)

    def genericCsvRowMapper = Mock(ServiceCsvRowMapper)

    def genericRepository = Mock(ServiceRepository)

    def validator = Mock(Validator)

    def multipartFile;

    def mockEntity

    def mockDTO

    def setup() {
        subjectUnderTest = new ServicesCsvUploadService(genericRepository, serviceMapper, genericCsvRowMapper, validator)
        multipartFile = new MockMultipartFile("services.csv", "services.csv", "text/csv",
                new FileInputStream(new File("services.csv")));
        mockDTO = mockDTO()
        mockEntity = mockEntity()

    }

    def "should process CSV file and return expected status of processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        2 * validator.validate(mockDTO) >> []
        2 * serviceMapper.mapToEntity(mockDTO) >> mockEntity
        2 * genericRepository.save(mockEntity)

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "services.csv"
        result.status == Status.PROCESSED
        result.recordsProcessed == 2
        assert result.getValidationErrors().isEmpty()
    }

    def "should not process CSV file and return expected status of not processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        def violationMessage = 'name can not be bigger than 40 characters'
        def constraintViolation = Mock(ConstraintViolation)
        Set<ConstraintViolation<?>> violations = new HashSet<>()
        violations.add(constraintViolation);
        2 * validator.validate(mockDTO) >> violations
        2 * constraintViolation.getMessage() >> violationMessage

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "services.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['284c7734-49bc-4c7f-9d22-b8a3fd66baed'] == ['name can not be bigger than 40 characters']
        result.validationErrors['91f9aed8-7245-44b1-addb-a45ad1577e20'] == ['name can not be bigger than 40 characters']
    }

    def "should partially process CSV file and return expected status of partially processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        def violationMessage = 'name can not be bigger than 40 characters'
        def constraintViolation = Mock(ConstraintViolation)
        Set<ConstraintViolation<?>> violations = new HashSet<>()
        violations.add(constraintViolation);
        1 * validator.validate(mockDTO) >> violations
        1 * validator.validate(mockDTO) >> []
        1 * constraintViolation.getMessage() >> violationMessage
        1 * serviceMapper.mapToEntity(mockDTO) >> mockEntity
        1 * genericRepository.save(mockEntity)

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "services.csv"
        result.status == Status.PARTIALLY_PROCESSED
        result.recordsProcessed == 1
        result.validationErrors.size() == 1
        result.validationErrors['284c7734-49bc-4c7f-9d22-b8a3fd66baed'] == ['name can not be bigger than 40 characters']
    }

    def "should not process CSV file as errors from db and return expected status of not processed"() {
        given:
        def service = new Service(UUID.fromString('284c7734-49bc-4c7f-9d22-b8a3fd66baed'), new Appointment(), 'shampoo', 10, 10)
        def service1 = new Service(UUID.fromString('91f9aed8-7245-44b1-addb-a45ad1577e20'), new Appointment(), 'wax', 15, 20)
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        2 * validator.validate(mockDTO) >> []
        1 * serviceMapper.mapToEntity(mockDTO) >> service
        1 * serviceMapper.mapToEntity(mockDTO) >> service1
        1 * genericRepository.save(service) >> { throw new DataIntegrityViolationException("Constraint violation") }
        1 * genericRepository.save(service1) >> { throw new DataIntegrityViolationException("Constraint violation") }

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "services.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['284c7734-49bc-4c7f-9d22-b8a3fd66baed'] == ['Sql constraint error']
        result.validationErrors['91f9aed8-7245-44b1-addb-a45ad1577e20'] == ['Sql constraint error']
    }

    def "should not process CSV file as illegal argument thrown and return expected status of not processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> { throw new IllegalArgumentException("no id") }

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "services.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['284c7734-49bc-4c7f-9d22-b8a3fd66baed'] == ['no id']
        result.validationErrors['91f9aed8-7245-44b1-addb-a45ad1577e20'] == ['no id']
    }

    static ServiceDTO mockDTO() {
        def id = "ef1e649c-e7b2-496f-82a1-38294f81d8de"
        def appointmentId = "ef1e649c-e7b2-496f-82a1-38294f81d822"
        def name = "shampoo"
        def price = 10.00
        def loyaltyPoints = 10

        return new ServiceDTO(id, appointmentId, name, price, loyaltyPoints)
    }

    static Service mockEntity() {
        return new Service()
    }
}
