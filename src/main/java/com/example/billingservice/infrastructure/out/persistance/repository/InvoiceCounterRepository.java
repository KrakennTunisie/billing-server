package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCounterEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvoiceCounterRepository extends JpaRepository<InvoiceCounterEntity, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from InvoiceCounterEntity c where c.year = :year")
    Optional<InvoiceCounterEntity> findByYearForUpdate(@Param("year") Integer year);
}
