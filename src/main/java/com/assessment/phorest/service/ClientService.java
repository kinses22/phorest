package com.assessment.phorest.service;


import com.assessment.phorest.dto.TopClientDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ClientService {

    Page<TopClientDTO> listTopClients(LocalDate startDate, int limit);

    void deleteClient(String clientID);

}
