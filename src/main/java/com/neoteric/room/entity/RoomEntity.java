package com.neoteric.room.entity;

import com.neoteric.hotel.entity.HotelEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true,nullable = false)
    private String roomId;
    private String roomName;
    private String roomType;
    private String roomView;
    private String roomSize;
    private int numberOfRooms;
    private String description;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "hotel_fk_id")
    private HotelEntity hotel;
    @OneToMany(mappedBy = "room",cascade =CascadeType.ALL,orphanRemoval = true )
    private List<RatePlanEntity>ratePlans = new ArrayList<>();
}
