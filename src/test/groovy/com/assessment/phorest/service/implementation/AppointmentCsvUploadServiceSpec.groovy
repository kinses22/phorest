package com.assessment.phorest.service.implementation

import com.assessment.phorest.dao.AppointmentRepository
import com.assessment.phorest.dto.AppointmentDTO
import com.assessment.phorest.entity.Appointment
import com.assessment.phorest.entity.Client
import com.assessment.phorest.enumeration.Gender
import com.assessment.phorest.enumeration.Status
import com.assessment.phorest.mapper.AppointmentMapper
import com.assessment.phorest.row.AppointmentCsvRowMapper
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.Subject

import java.time.OffsetDateTime

class AppointmentCsvUploadServiceSpec extends Specification {

    @Subject
    AppointmentCsvUploadService subjectUnderTest;

    def appointmentMapper = Mock(AppointmentMapper)

    def genericCsvRowMapper = Mock(AppointmentCsvRowMapper)

    def genericRepository = Mock(AppointmentRepository)

    def validator = Mock(Validator)

    def multipartFile;

    def mockEntity

    def mockDTO

    def setup() {
        subjectUnderTest = new AppointmentCsvUploadService(genericRepository, appointmentMapper, genericCsvRowMapper, validator)
        multipartFile = new MockMultipartFile("appointments.csv", "appointments.csv", "text/csv",
                new FileInputStream(new File("appointments.csv")));
        mockDTO = mockDTO()
        mockEntity = mockEntity()

    }

    def "should process CSV file and return expected status of processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        2 * validator.validate(mockDTO) >> []
        2 * appointmentMapper.mapToEntity(mockDTO) >> mockEntity
        2 * genericRepository.save(mockEntity)


        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "appointments.csv"
        result.status == Status.PROCESSED
        result.recordsProcessed == 2
        assert result.getValidationErrors().isEmpty()
    }

    def "should not process CSV file and return expected status of not processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        def violationMessage = 'start date invalid'
        def constraintViolation = Mock(ConstraintViolation)
        Set<ConstraintViolation<?>> violations = new HashSet<>()
        violations.add(constraintViolation);
        2 * validator.validate(mockDTO) >> violations
        2 * constraintViolation.getMessage() >> violationMessage

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "appointments.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['ef1e649c-e7b2-496f-82a1-38294f81d8de'] == ['start date invalid']
        result.validationErrors['a5e0a200-6284-4919-8a7b-96b8ab03df03'] == ['start date invalid']
    }

    def "should partially process CSV file and return expected status of partially processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        def violationMessage = 'start date invalid'
        def constraintViolation = Mock(ConstraintViolation)
        Set<ConstraintViolation<?>> violations = new HashSet<>()
        violations.add(constraintViolation);
        1 * validator.validate(mockDTO) >> violations
        1 * validator.validate(mockDTO) >> []
        1 * constraintViolation.getMessage() >> violationMessage
        1 * appointmentMapper.mapToEntity(mockDTO) >> mockEntity
        1 * genericRepository.save(mockEntity)

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "appointments.csv"
        result.status == Status.PARTIALLY_PROCESSED
        result.recordsProcessed == 1
        result.validationErrors.size() == 1
        result.validationErrors['ef1e649c-e7b2-496f-82a1-38294f81d8de'] == ['start date invalid']
    }

    def "should not process CSV file as errors from db and return expected status of not processed"() {
        given:
        def appointment = new Appointment(UUID.fromString('ef1e649c-e7b2-496f-82a1-38294f81d8de'), new Client(UUID.fromString('e0b8ebfc-6e57-4661-9546-328c644a3764')
                , [], "s", "r", "sds@ema", "1234", Gender.Female ,true) , OffsetDateTime.now() , OffsetDateTime.now(), [], [])
        def appointment1 = new Appointment(UUID.fromString('a5e0a200-6284-4919-8a7b-96b8ab03df03'), new Client(UUID.fromString('e0b8ebfc-6e57-4661-9546-328c644a3764')
                , [], "s", "r", "sds@ema", "1234", Gender.Female ,true) ,OffsetDateTime.now(), OffsetDateTime.now(), [], [])
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        2 * validator.validate(mockDTO) >> []
        1 * appointmentMapper.mapToEntity(mockDTO) >> appointment
        1 * appointmentMapper.mapToEntity(mockDTO) >> appointment1
        1 * genericRepository.save(appointment) >> { throw new DataIntegrityViolationException("Constraint violation") }
        1 * genericRepository.save(appointment1) >> { throw new DataIntegrityViolationException("Constraint violation") }

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "appointments.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['ef1e649c-e7b2-496f-82a1-38294f81d8de'] == ['Sql constraint error']
        result.validationErrors['a5e0a200-6284-4919-8a7b-96b8ab03df03'] == ['Sql constraint error']
    }

    def "should not process CSV file as illegal argument thrown and return expected status of not processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> { throw new IllegalArgumentException("no id") }

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "appointments.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['ef1e649c-e7b2-496f-82a1-38294f81d8de'] == ['no id']
        result.validationErrors['a5e0a200-6284-4919-8a7b-96b8ab03df03'] == ['no id']
    }

    static AppointmentDTO mockDTO() {
        def id = "ef1e649c-e7b2-496f-82a1-38294f81d8de"
        def clientId = "007a2d25-acb0-461d-bb79-ed45750587b5"
        def startTime = "2017-11-10 10:15:00 +0000"
        def endTime = "2017-11-10 10:15:00 +0000"

        return new AppointmentDTO(id, clientId, startTime, endTime)
    }

    static Appointment mockEntity() {
        return new Appointment()
    }

}
