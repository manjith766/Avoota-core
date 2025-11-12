package com.neoteric.room.model;

import lombok.Data;

import java.util.List;

@Data
public class Room {
    private String roomId;
    private String hotelId;
    private String roomName;
    private String roomType;
    private String roomView;
    private String roomSize;
    private int numberOfRooms;
    private String description;
    private List<RatePlan> ratePlans;

}
