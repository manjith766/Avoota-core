package com.neoteric.room.controller;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.room.entity.RoomEntity;
import com.neoteric.room.model.Room;
import com.neoteric.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/save")
    public ApiResponse<String> saveOrUpdateRoom(@RequestBody Room room) {
        return roomService.saveOrUpdateRoom(room);
    }

    @GetMapping("/{hotelId}")
    public ApiResponse<List<RoomEntity>> getRoomsByHotel(@PathVariable String hotelId) {
        return roomService.getRoomsByHotel(hotelId);
    }
}
