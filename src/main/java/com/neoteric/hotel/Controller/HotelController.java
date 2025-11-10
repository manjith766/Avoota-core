package com.neoteric.hotel.Controller;

import com.neoteric.hotel.entity.HotelEntity;
import com.neoteric.hotel.exception.ApiResponse;
import com.neoteric.hotel.model.Hotel;
import com.neoteric.hotel.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hotels")
public class HotelController {
     private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<HotelEntity>> addHotel(@RequestBody Hotel hotel) {
        log.info("Received hotel creation request: {}", hotel.getHotelName());
        HotelEntity created = hotelService.addHotel(hotel);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Hotel created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HotelEntity>>> getAllHotels() {
        log.info("Fetching all hotels");
        return ResponseEntity.ok(ApiResponse.success("Hotels fetched", hotelService.getAllHotels()));
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HotelEntity>>> searchHotels(@RequestParam String keyword) {
        log.info("Searching hotels with keyword: {}", keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results", hotelService.searchHotels(keyword)));
    }
}
