package com.assessment.phorest.dao;

import com.assessment.phorest.dto.TopClientDTO;
import com.assessment.phorest.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface ClientRepository extends GenericRepository<Client> {

    @Query("SELECT NEW com.assessment.phorest.dto.TopClientDTO(c.id, c.firstName, c.email, COALESCE(SUM(p.loyaltyPoints), 0)" +
            " + COALESCE(SUM(s.loyaltyPoints), 0) AS total_loyalty_points) " +
            "FROM Client c " +
            "LEFT JOIN c.appointments a " +
            "LEFT JOIN a.purchases p " +
            "LEFT JOIN a.services s " +
            "WHERE c.banned = false " +
            "AND a.startTime >= :startDate " +
            "GROUP BY c.id, c.firstName " +
            "ORDER BY total_loyalty_points DESC")
    Page<TopClientDTO> findTopClientsWithMostLoyaltyPoints(OffsetDateTime startDate, Pageable pageable);

}
