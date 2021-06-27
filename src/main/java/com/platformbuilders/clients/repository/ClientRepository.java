package com.platformbuilders.clients.repository;

import com.platformbuilders.clients.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Optional<Client> findClientByOid(String oid);

    @Query("SELECT CASE WHEN COUNT(c) > 0 then TRUE ELSE FALSE END FROM Client c WHERE c.email = ?1 AND c.oid <> ?2")
    Boolean emailAlreadyExists(String email, String oid);
}
