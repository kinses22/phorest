package com.assessment.phorest.service.implementation

import com.assessment.phorest.service.CsvFileUploadService
import org.mockito.Mock
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

class CsvFileProcessingServiceImplSpec extends Specification {

    CsvFileProcessingServiceImpl objectUnderTest

    def clientCsvFileUploader = Mock(ClientCsvUploadService);
    def appointmentCsvFileUploader = Mock(AppointmentCsvUploadService)
    def purchaseCsvFileUploader = Mock(PurchaseCsvUploadService)
    def serviceCsvFileUploader = Mock(ServicesCsvUploadService)

    @Mock
    Map<String, CsvFileUploadService> csvUploadServiceMap = Mock()

    def setup() {
        objectUnderTest = new CsvFileProcessingServiceImpl(csvUploadServiceMap)
    }

    def "uploadCsvFiles should process supported files in the desired order"() {
        given:
        def supportedFile1 = createMultipartFile("clients.csv")
        def supportedFile2 = createMultipartFile("appointments.csv")
        def supportedFile3 = createMultipartFile("services.csv")
        def supportedFile4 = createMultipartFile("purchases.csv")
        def unsupportedFile = createMultipartFile("unsupported.txt")


        when:
        def result = objectUnderTest.uploadCsvFiles(List.of(supportedFile3,
        supportedFile2, unsupportedFile, supportedFile4, supportedFile1))

        then:
        2 * supportedFile1.getOriginalFilename() >> 'clients.csv'
        2 * supportedFile2.getOriginalFilename() >> "appointments.csv"
        2 * supportedFile3.getOriginalFilename() >> "services.csv"
        2 * supportedFile4.getOriginalFilename() >> "purchases.csv"
        1 * unsupportedFile.getOriginalFilename() >> "unsupported.txt"
        1 * csvUploadServiceMap.get('clients.csv') >> clientCsvFileUploader
        1 * csvUploadServiceMap.get('appointments.csv') >> appointmentCsvFileUploader
        1 * csvUploadServiceMap.get('purchases.csv') >> purchaseCsvFileUploader
        1 * csvUploadServiceMap.get('services.csv') >> serviceCsvFileUploader

        result.unSupportedFiles == ["unsupported.txt"]
        result.csvChildProcessingResponseDTOList.size() == 4
    }

    private MultipartFile createMultipartFile(String originalFilename) {
        Mock(MultipartFile) {
            getOriginalFilename() >> originalFilename
        }
    }
}
