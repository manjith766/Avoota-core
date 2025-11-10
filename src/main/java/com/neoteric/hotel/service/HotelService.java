package com.neoteric.hotel.service;

import com.neoteric.hotel.entity.AddressEntity;
import com.neoteric.hotel.entity.HotelEntity;
import com.neoteric.hotel.model.Hotel;
import java.util.List;

public interface HotelService {
    HotelEntity addHotel(Hotel hotel);
    List<HotelEntity> getAllHotels();
    List<HotelEntity> searchHotels(String keyword);
    AddressEntity getAddressByHotelId(String hotelId);
}
