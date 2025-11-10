package com.neoteric.hotel.service;

import com.neoteric.hotel.entity.*;
import com.neoteric.hotel.model.*;
import com.neoteric.hotel.repository.AddressRepository;
import com.neoteric.hotel.repository.HotelRepository;
import com.neoteric.hotel.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AddressRepository addressRepository;

    public HotelServiceImpl(HotelRepository hotelRepository, AddressRepository addressRepository) {
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public HotelEntity addHotel(Hotel hotel) {
        log.info("Adding new hotel: {}", hotel.getHotelName());

        HotelEntity hotelEntity = new HotelEntity();
        hotelEntity.setHotelId(hotel.getHotelId());
        hotelEntity.setHotelName(hotel.getHotelName());
        hotelEntity.setStatus(hotel.getStatus());

        AddressEntity address = new AddressEntity();
        address.setStreet(hotel.getAddress().getStreet());
        address.setCity(hotel.getAddress().getCity());
        address.setState(hotel.getAddress().getState());
        address.setCountry(hotel.getAddress().getCountry());
        address.setPinCode(hotel.getAddress().getPinCode());

        address.setHotel(hotelEntity);
        hotelEntity.setAddress(address);


        HotelEntity saved = hotelRepository.save(hotelEntity);
        log.info("Hotel '{}' added with ID: {}", saved.getHotelName(), saved.getId());
        return saved;
    }

    @Override
    public List<HotelEntity> getAllHotels() {
        log.debug("Fetching all hotels from DB");
        return hotelRepository.findAll();
    }

    @Override
    public List<HotelEntity> searchHotels(String keyword) {
        log.debug("Searching hotels for keyword: {}", keyword);
        List<HotelEntity> results = hotelRepository.searchHotels(keyword);
        if (results.isEmpty()) {
            log.warn("No hotels found for keyword: {}", keyword);
            throw new ResourceNotFoundException("No hotels found for keyword: " + keyword);
        }
        return results;
    }

    @Override
    public AddressEntity getAddressByHotelId(String hotelId) {
        log.debug("Fetching address for hotelid: {}",hotelId);
        return addressRepository.findAddressByHotelId(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("No address found for hotelId:"+hotelId));
    }
}
