package com.assessment.phorest.mapper;

import com.assessment.phorest.dto.ClientDTO;
import com.assessment.phorest.entity.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper implements GenericMapper<ClientDTO, Client> {

    private final ObjectMapper objectMapper;

    @Autowired
    public ClientMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Client mapToEntity(ClientDTO clientDTO) {
        return objectMapper.convertValue(clientDTO, Client.class);
    }

    public ClientDTO mapToDTO(Client client) {
        return objectMapper.convertValue(client, ClientDTO.class);
    }
}

