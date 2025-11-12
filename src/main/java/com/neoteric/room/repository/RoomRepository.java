package com.neoteric.room.repository;

import com.neoteric.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {

    Optional<RoomEntity> findByRoomId(String roomId);
    List<RoomEntity> findByHotel_HotelId(String hotelId);
}
