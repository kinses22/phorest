package com.assessment.phorest.service.implementation

import com.assessment.phorest.dao.ClientRepository
import com.assessment.phorest.dto.ClientDTO
import com.assessment.phorest.entity.Client
import com.assessment.phorest.mapper.ClientMapper
import com.assessment.phorest.service.implementation.ClientServiceImpl
import org.mockito.Mock
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spock.lang.AutoCleanup
import spock.lang.Shared
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
        clientMapper.mapToDTO(_) >> new ClientDTO(id: clientId.toString())

        when:
        def result = clientService.getClientById(clientId.toString())

        then:
        result.isPresent()
        result.get().id == clientId.toString()
    }

    def "listTopClients should call repository method"() {
        given:
        def startDate = LocalDate.of(2023, 1, 1)
        def limit = 10
        def startDateAndTime = LocalDateTime.of(startDate, LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC)
        def pageable = Mock(Pageable)
        clientRepository.findTopClientsWithMostLoyaltyPoints(startDateAndTime, pageable) >> Mock(Page)

        when:
        clientService.listTopClients(startDate, limit)

        then:
        1 * clientRepository.findTopClientsWithMostLoyaltyPoints(startDateAndTime, pageable)
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
