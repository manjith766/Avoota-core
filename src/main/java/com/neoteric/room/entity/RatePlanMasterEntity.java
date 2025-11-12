package com.neoteric.room.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rate_plan_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatePlanMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ratePlanCode;   // e.g. "AP", "MAP", "CP", "EP"

    @Column(nullable = false)
    private String ratePlanName;   // e.g. "American Plan"

    private String description;    // optional
}
