package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.SequenceNumberType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SequenceNumberCounter {

    private UUID id;
    private Integer year;
    private SequenceNumberType sequenceNumberType;
    private Long lastSequence;

    @Override
    public String toString() {
        return "SequenceNumberCounter{" +
                "id=" + id +
                ", year=" + year +
                ", sequenceNumberType=" + sequenceNumberType +
                ", lastSequence=" + lastSequence +
                '}';
    }
}
