package com.neoteric.hotel.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hotels")
@Data
public class HotelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique business key
    @Column(name = "hotel_id", unique = true, nullable = false)
    private String hotelId;

    private String hotelName;
    private String status;

    // One-to-one relation with AddressEntity using hotel_id
    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private AddressEntity address;
}
