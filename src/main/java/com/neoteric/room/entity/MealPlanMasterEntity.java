package com.neoteric.room.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meal_plan_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealPlanMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String mealPlanCode;  // e.g. "FB", "BB", "MAP", "EP"

    @Column(nullable = false)
    private String mealPlanName;  // e.g. "Free Breakfast", "All Meals Included"

    private String description;   // optional
}
