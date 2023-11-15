package com.assessment.phorest.service;


import com.assessment.phorest.dto.ClientDTO;
import com.assessment.phorest.dto.TopClientDTO;
import com.assessment.phorest.dto.request.ClientRequestDTO;
import com.assessment.phorest.entity.Client;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface ClientService {

    Optional<ClientDTO> getClientById(String clientID);

    Page<TopClientDTO> listTopClients(LocalDate startDate, int limit);

    void deleteClientById(String clientID);

    ClientDTO updateClient(ClientDTO clientDTO, ClientRequestDTO newClientDto);

}
