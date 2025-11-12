package com.neoteric.room.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rate_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatePlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ratePlanCode;   // e.g. AP, CP, EP, MAP

    @Column(nullable = false)
    private String mealPlanName;   // e.g. Free Breakfast, Free Breakfast + Dinner

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_fk_id", nullable = false)
    private RoomEntity room;
}
