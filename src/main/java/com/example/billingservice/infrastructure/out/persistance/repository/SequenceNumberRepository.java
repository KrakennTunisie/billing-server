package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.infrastructure.out.persistance.entity.SequenceNumberCounterEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SequenceNumberRepository extends JpaRepository<SequenceNumberCounterEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT c
        FROM SequenceNumberCounterEntity c
        WHERE c.sequenceNumberType = :type
        AND c.year = :year
    """)
    Optional<SequenceNumberCounterEntity> findByTypeAndYearForUpdate(
            @Param("type") SequenceNumberType type,
            @Param("year") Integer year
    );
}
