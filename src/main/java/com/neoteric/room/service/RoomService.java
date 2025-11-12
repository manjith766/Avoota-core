package com.neoteric.room.service;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.common.ui.AvootaResponseStatus;
import com.neoteric.common.ui.AvootaUtil;
import com.neoteric.hotel.entity.HotelEntity;
import com.neoteric.hotel.repository.HotelRepository;
import com.neoteric.room.entity.RoomEntity;
import com.neoteric.room.model.Room;
import com.neoteric.room.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.neoteric.common.ui.AvootaResponseStatus.SUCCESS;

@Slf4j
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public ApiResponse<String> saveOrUpdateRoom(Room room) {
        log.info("Save/Update Room initiated for roomId={}", room.getRoomId());

        try {
            if (room.getRoomId() == null || room.getRoomId().trim().isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.INVALID_INPUT,
                        "Room ID cannot be empty."
                );
            }

            // Find existing room or create new
            Optional<RoomEntity> existingOpt = roomRepository.findByRoomId(room.getRoomId());
            RoomEntity targetRoom = existingOpt.orElseGet(RoomEntity::new);
            targetRoom.setRoomId(room.getRoomId());

            //  Link hotel
            if (room.getHotelId() != null) {
                Optional<HotelEntity> hotelOpt = hotelRepository.findByHotelId(room.getHotelId());
                if (hotelOpt.isEmpty()) {
                    return AvootaUtil.failure(
                            AvootaResponseStatus.FailureCode.INVALID_INPUT,
                            "Hotel not found with ID: " + room.getHotelId()
                    );
                }
                targetRoom.setHotel(hotelOpt.get());
            }

            //  Partial update logic
            if (room.getRoomName() != null) targetRoom.setRoomName(room.getRoomName());
            if (room.getRoomType() != null) targetRoom.setRoomType(room.getRoomType());
            if (room.getRoomView() != null) targetRoom.setRoomView(room.getRoomView());
            if (room.getRoomSize() != null) targetRoom.setRoomSize(room.getRoomSize());
            if (room.getDescription() != null) targetRoom.setDescription(room.getDescription());
            if (room.getNumberOfRooms() > 0) targetRoom.setNumberOfRooms(room.getNumberOfRooms());

            //  Save or update
            roomRepository.save(targetRoom);

            String action = existingOpt.isPresent() ? "updated" : "created";

            return ApiResponse.<String>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Room " + action + " successfully.")
                    .data("Room " + action + " with ID: " + room.getRoomId())
                    .build();

        } catch (Exception ex) {
            log.error("Error saving/updating roomId={}", room.getRoomId(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while saving/updating room."
            );
        }
    }

    public ApiResponse<List<RoomEntity>> getRoomsByHotel(String hotelId) {
        try {
            List<RoomEntity> rooms = roomRepository.findByHotel_HotelId(hotelId);
            if (rooms.isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND,
                        "No rooms found for hotelId: " + hotelId
                );
            }

            rooms.forEach(r -> r.setHotel(null));
            return ApiResponse.<List<RoomEntity>>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Rooms fetched successfully.")
                    .data(rooms)
                    .build();

        } catch (Exception ex) {
            log.error("Error fetching rooms for hotelId={}", hotelId, ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while fetching rooms."
            );
        }
    }
}
