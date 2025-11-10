package com.neoteric.hotel.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Hotel {
    private String hotelId;
    private String hotelName;
    private String status;
    private List<Address>addresses;
}
