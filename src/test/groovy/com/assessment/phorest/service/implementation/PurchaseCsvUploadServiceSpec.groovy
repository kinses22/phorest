package com.assessment.phorest.service.implementation

import com.assessment.phorest.dao.PurchaseRepository
import com.assessment.phorest.dto.PurchaseDTO
import com.assessment.phorest.entity.Appointment
import com.assessment.phorest.entity.Purchase
import com.assessment.phorest.enumeration.Gender
import com.assessment.phorest.enumeration.Status
import com.assessment.phorest.mapper.PurchaseMapper
import com.assessment.phorest.row.PurchaseCsvRowMapper
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.Subject

class PurchaseCsvUploadServiceSpec extends Specification {

    @Subject
    PurchaseCsvUploadService subjectUnderTest;

    def purchaseMapper = Mock(PurchaseMapper)

    def genericCsvRowMapper = Mock(PurchaseCsvRowMapper)

    def genericRepository = Mock(PurchaseRepository)

    def validator = Mock(Validator)

    def multipartFile;

    def mockEntity

    def mockDTO

    def setup() {
        subjectUnderTest = new PurchaseCsvUploadService(genericRepository, purchaseMapper, genericCsvRowMapper, validator)
        multipartFile = new MockMultipartFile("purchases.csv", "purchases.csv", "text/csv",
                new FileInputStream(new File("purchases.csv")));
        mockDTO = mockDTO()
        mockEntity = mockEntity()

    }

    def "should process CSV file and return expected status of processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        2 * validator.validate(mockDTO) >> []
        2 * purchaseMapper.mapToEntity(mockDTO) >> mockEntity
        2 * genericRepository.save(mockEntity)

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "purchases.csv"
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
        result.fileName == "purchases.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['008ef001-239d-4a1e-9588-6dbceadee521'] == ['name can not be bigger than 40 characters']
        result.validationErrors['026b2740-b39e-426e-a146-f0883efc1b97'] == ['name can not be bigger than 40 characters']
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
        1 * purchaseMapper.mapToEntity(mockDTO) >> mockEntity
        1 * genericRepository.save(mockEntity)

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "purchases.csv"
        result.status == Status.PARTIALLY_PROCESSED
        result.recordsProcessed == 1
        result.validationErrors.size() == 1
        result.validationErrors['008ef001-239d-4a1e-9588-6dbceadee521'] == ['name can not be bigger than 40 characters']
    }

    def "should not process CSV file as errors from db and return expected status of not processed"() {
        given:
        def purchase = new Purchase(UUID.fromString('008ef001-239d-4a1e-9588-6dbceadee521'), new Appointment(), 'shampoo', 10, 10)
        def purchase1 = new Purchase(UUID.fromString('026b2740-b39e-426e-a146-f0883efc1b97'), new Appointment(), 'wax', 15, 20)
        2 * genericCsvRowMapper.createDTO(_) >> mockDTO
        2 * validator.validate(mockDTO) >> []
        1 * purchaseMapper.mapToEntity(mockDTO) >> purchase
        1 * purchaseMapper.mapToEntity(mockDTO) >> purchase1
        1 * genericRepository.save(purchase) >> { throw new DataIntegrityViolationException("Constraint violation") }
        1 * genericRepository.save(purchase1) >> { throw new DataIntegrityViolationException("Constraint violation") }

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "purchases.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['008ef001-239d-4a1e-9588-6dbceadee521'] == ['Sql constraint error']
        result.validationErrors['026b2740-b39e-426e-a146-f0883efc1b97'] == ['Sql constraint error']
    }

    def "should not process CSV file as illegal argument thrown and return expected status of not processed"() {
        given:
        2 * genericCsvRowMapper.createDTO(_) >> { throw new IllegalArgumentException("no id") }

        when:
        def result = subjectUnderTest.processCsvFiles(multipartFile)

        then:
        result.fileName == "purchases.csv"
        result.status == Status.NOT_PROCESSED
        result.recordsProcessed == 0
        result.validationErrors.size() == 2
        result.validationErrors['008ef001-239d-4a1e-9588-6dbceadee521'] == ['no id']
        result.validationErrors['026b2740-b39e-426e-a146-f0883efc1b97'] == ['no id']
    }

    static PurchaseDTO mockDTO() {
        def id = "ef1e649c-e7b2-496f-82a1-38294f81d8de"
        def appointmentId = "ef1e649c-e7b2-496f-82a1-38294f81d822"
        def name = "shampoo"
        def price = 10.00
        def loyaltyPoints = 10

        return new PurchaseDTO(id, appointmentId, name, price, loyaltyPoints)
    }

    static Purchase mockEntity() {
        return new Purchase()
    }

}
