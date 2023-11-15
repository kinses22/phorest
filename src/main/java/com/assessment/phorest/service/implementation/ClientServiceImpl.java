package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.ClientRepository;
import com.assessment.phorest.dto.TopClientDTO;
import com.assessment.phorest.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.UUID;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Page<TopClientDTO> listTopClients(LocalDate startDate, int limit) {
        validateLimit(limit);
        validateStartDate(startDate);
        OffsetDateTime startDateAndTime = LocalDateTime.of(startDate, LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC);
        Pageable pageable = PageRequest.of(0, limit);
        return clientRepository.findTopClientsWithMostLoyaltyPoints(startDateAndTime, pageable);
    }

    @Override
    public void deleteClient(String clientID) {
        clientRepository.deleteById(UUID.fromString(clientID));
    }

    private void validateLimit(int limit) {
        if (limit <= 0 || limit >= 1000 ) {
            log.error("Validation failure on limit: {} . It must be greater than 0 and less than 1000 ", limit);
            throw new IllegalArgumentException("Limit: " + limit +"must be greater than zero and less than 1000");
        }
    }

    private void validateStartDate(LocalDate startDate) {
        if (startDate == null || startDate.isAfter(LocalDate.now())) {
            log.error("Validation failure on start date: {} . It must not be null and must be in the past", startDate);
            throw new IllegalArgumentException("Start date can not be null and must be in the past");
        }
    }


}
