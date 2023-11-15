package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.ClientRepository;
import com.assessment.phorest.dto.ClientDTO;
import com.assessment.phorest.dto.TopClientDTO;
import com.assessment.phorest.dto.request.ClientRequestDTO;
import com.assessment.phorest.entity.Client;
import com.assessment.phorest.enumeration.Gender;
import com.assessment.phorest.mapper.ClientMapper;
import com.assessment.phorest.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }
    @Override
    public Optional<ClientDTO> getClientById(String clientID) {
        Optional<Client> client = clientRepository.findById(UUID.fromString(clientID));
        return client.map(clientMapper::mapToDTO);

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
    public void deleteClientById(String clientID) {
        clientRepository.deleteById(UUID.fromString(clientID));
    }

    @Override
    public ClientDTO updateClient(ClientDTO clientDTO, ClientRequestDTO newClientDto) {

        updateIfNotNullOrNotEmpty(newClientDto.getFirstName(), clientDTO::setFirstName);
        updateIfNotNullOrNotEmpty(newClientDto.getSecondName(), clientDTO::setSecondName);
        updateIfNotNullOrNotEmpty(newClientDto.getEmail(), clientDTO::setEmail);
        updateIfNotNullOrNotEmpty(newClientDto.getPhone(), clientDTO::setPhone);

        if (!newClientDto.getGender().isEmpty() | newClientDto.getGender() != null) {
            clientDTO.setGender(Gender.valueOf(newClientDto.getGender()));
        }
        if (newClientDto.getBanned()) {
            clientDTO.setBanned(newClientDto.getBanned());
        }
        clientRepository.save(clientMapper.mapToEntity(clientDTO));
        return clientDTO;
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

    private void updateIfNotNullOrNotEmpty(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isEmpty()) {
            setter.accept(newValue);
        }
    }

}
