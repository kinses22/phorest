package com.assessment.phorest.service.implementation

import com.assessment.phorest.dao.ClientRepository
import com.assessment.phorest.dto.ClientDTO
import com.assessment.phorest.dto.TopClientDTO
import com.assessment.phorest.entity.Client
import com.assessment.phorest.mapper.ClientMapper
import com.assessment.phorest.service.implementation.ClientServiceImpl
import org.mockito.Mock
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class ClientServiceImplSpec extends Specification {

    @Subject
    ClientServiceImpl clientService

    @Mock
    ClientRepository clientRepository = Mock(ClientRepository)

    @Mock
    ClientMapper clientMapper = Mock(ClientMapper)

    def setup() {
        clientService = new ClientServiceImpl(clientRepository, clientMapper)
    }

    def "getClientById should return optional ClientDTO"() {
        given:
        UUID clientId = UUID.randomUUID()
        Client client = new Client(id: clientId)
        clientRepository.findById(clientId) >> Optional.of(client)
        clientMapper.mapToDTO(client) >> new ClientDTO(id: clientId.toString())

        when:
        def result = clientService.getClientById(clientId.toString())

        then:
        result.isPresent()
        result.get().id == clientId.toString()
    }

    def "listTopClients should call repository method"() {
        given:
        def startDate = LocalDate.of(2020, 10, 1)
        def limit = 10
        def startDateAndTime = LocalDateTime.of(startDate, LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC)
        def topClientDTO = new TopClientDTO(UUID.fromString('008ef001-239d-4a1e-9588-6dbceadee521'), 'ste', 'kin', 10);
        def topClientDTOList = new ArrayList<>()
        topClientDTOList.add(topClientDTO)
        def pagedResponse = new PageImpl(topClientDTOList);
        def pageable = PageRequest.of(0, limit);

        1 * clientRepository.findTopClientsWithMostLoyaltyPoints(startDateAndTime, pageable) >> pagedResponse

        when:
        def result = clientService.listTopClients(startDate, limit)

        then:
        assert result.asList().get(0) == topClientDTO
    }

    def "listTopClients invalid limit causes illegal argument exception"() {
        given:
        def startDate = LocalDate.of(2020, 10, 1)

        when:
        def exception = null
        try {
            clientService.listTopClients(startDate, limit)
        } catch (IllegalArgumentException e) {
            exception = e
        }

        then:
        assert exception instanceof IllegalArgumentException
        assert exception.message == expectedMessage

        where:
        limit    | expectedMessage
        0        | 'Limit: 0 must be greater than zero and less than 1000'
        10000000 | 'Limit: 10000000 must be greater than zero and less than 1000'
    }

    def "listTopClients invalid start date causes illegal argument exception"() {
        given:
        def exception = null

        when:
        try {
            clientService.listTopClients(startDate, 5)
        } catch (IllegalArgumentException e) {
            exception = e
        }

        then:
        assert exception instanceof IllegalArgumentException
        assert exception.message == expectedMessage

        where:
        startDate                | expectedMessage
        null                     | 'Start date can not be null and must be in the past'
        LocalDate.of(3000, 1, 1) | 'Start date can not be null and must be in the past'
    }


    def "deleteClientById should call repository method"() {
        given:
        def clientId = UUID.randomUUID()

        when:
        clientService.deleteClientById(clientId.toString())

        then:
        1 * clientRepository.deleteById(UUID.fromString(clientId.toString()))
    }

}
