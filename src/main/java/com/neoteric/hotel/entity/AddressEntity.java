package com.neoteric.hotel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.neoteric.hotel.model.Hotel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Lazy;

@Entity
@Table(name = "addresses")
@Data
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "country")
    private String country;
    @Column(name = "pinCode")
    private String pinCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_fk_id")
    @JsonBackReference
    private HotelEntity hotel;
}
