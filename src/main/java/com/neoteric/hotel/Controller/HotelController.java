package com.neoteric.hotel.Controller;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.hotel.entity.HotelEntity;
import com.neoteric.hotel.model.Hotel;
import com.neoteric.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    // Add new hotel (throws exception if hotel already exists)
    @PostMapping("/add")
    public ApiResponse<String> addHotel(@RequestBody Hotel hotel) {
        return hotelService.addHotel(hotel);
    }

    //  Update hotel details + address
    @PutMapping("/update")
    public ApiResponse<String> updateHotel(@RequestBody Hotel hotel) {
        return hotelService.updateHotel(hotel);
    }

    // Get all hotels
    @GetMapping
    public ApiResponse<List<HotelEntity>> getAllHotels() {
        return hotelService.getAllHotels();
    }

    //  Search hotels by keyword (name, city, etc.)
    @GetMapping("/search")
    public ApiResponse<List<HotelEntity>> searchHotels(@RequestParam String keyword) {
        return hotelService.searchHotels(keyword);
    }


}
