package com.assessment.phorest.controller;


import com.assessment.phorest.dto.ClientDTO;
import com.assessment.phorest.dto.TopClientDTO;
import com.assessment.phorest.service.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Client Controller")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable String clientId) {
        Optional<ClientDTO> clientDTO = clientService.getClientById(clientId);
        return clientDTO.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/top-clients")
    public Page<TopClientDTO> getTopClientsWithLoyaltyPoints(
            //todo: describe api and data format
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("limit") int limit) {
        return clientService.listTopClients(startDate, limit);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClientById(@PathVariable String clientId) {
        clientService.deleteClientById(clientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
