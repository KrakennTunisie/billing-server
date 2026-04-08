package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.SequenceNumberType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "document_number_counters",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"sequence_type", "year"})
        }
)
@Getter
@Setter
public class SequenceNumberCounterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sequence_type", nullable = false)
    private SequenceNumberType sequenceNumberType;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "last_sequence", nullable = false)
    private Long lastSequence;
}
