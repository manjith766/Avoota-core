package com.neoteric.hotel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // read-only mirror column (prevents duplicate mapping)
    @Column(name = "hotel_id", insertable = false, updatable = false)
    private String hotelId;

    private String street;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    // The owning side of the relationship
    @OneToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id", nullable = false)
    @JsonBackReference
    private HotelEntity hotel;
}
