package com.neoteric.hotel.service;

import com.neoteric.common.exception.CustomException;
import com.neoteric.common.ui.ApiResponse;
import com.neoteric.common.ui.AvootaResponseStatus;
import com.neoteric.common.ui.AvootaUtil;
import com.neoteric.hotel.entity.AddressEntity;
import com.neoteric.hotel.entity.HotelEntity;
import com.neoteric.hotel.model.Address;
import com.neoteric.hotel.model.Hotel;
import com.neoteric.hotel.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.neoteric.common.ui.AvootaResponseStatus.SUCCESS;

@Slf4j
@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public ApiResponse<String> addHotel(Hotel hotel) {
        log.info("saving new hotel with hotel ID ={}", hotel.getHotelId());
        try {
            Optional<HotelEntity> existing = hotelRepository.findByHotelId(hotel.getHotelId());
            if (existing.isPresent()) {
                log.warn("Hotel already exists with hotelId={}", hotel.getHotelId());
                throw new CustomException(
                        AvootaResponseStatus.FailureCode.HOTEL_NOT_FOUND,
                        "Hotel already exists with hotelId: " + hotel.getHotelId());
            }
            HotelEntity newHotel = new HotelEntity();
            newHotel.setHotelId(hotel.getHotelId());
            newHotel.setHotelName(hotel.getHotelName());
            newHotel.setStatus(hotel.getStatus());

            List<AddressEntity> addressEntities = new ArrayList<>();
            if (hotel.getAddresses() != null) {
                for (Address addr : hotel.getAddresses()) {
                    AddressEntity addressEntity = new AddressEntity();
                    addressEntity.setStreet(addr.getStreet());
                    addressEntity.setCity(addr.getStreet());
                    addressEntity.setState(addr.getState());
                    addressEntity.setCountry(addr.getCountry());
                    addressEntity.setPinCode(addr.getPinCode());
                    addressEntity.setHotel(newHotel);
                    addressEntities.add(addressEntity);
                }
            }
            newHotel.setAddresses(addressEntities);
            hotelRepository.save(newHotel);
            log.info("Hotel created successfully with hotelId={}", hotel.getHotelId());
            return ApiResponse.<String>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Hotel created successfully.")
                    .data("Hotel created with ID: " + hotel.getHotelId())
                    .build();
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception ex) {
            log.error("Error while creating hotel with hotelId={}", hotel.getHotelId(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR, "Error occurred while creating hotel."
            );
        }
    }

    public ApiResponse<String> updateHotel(Hotel hotel) {
        log.info("Updating hotel details for hotelId={}", hotel.getHotelId());
        try {
            Optional<HotelEntity> existing = hotelRepository.findByHotelId(hotel.getHotelId());
            if (existing.isEmpty()) {
                log.warn("Hotel not found for hotelId={}", hotel.getHotelId());
                throw new CustomException(
                        AvootaResponseStatus.FailureCode.HOTEL_NOT_FOUND, "Hotel not found with hotelId={}" + hotel.getHotelId()
                );
            }
            HotelEntity updateHotel = existing.get();
            updateHotel.setHotelName(hotel.getHotelName());
            updateHotel.setStatus(hotel.getStatus());

            List<AddressEntity> updatedAddresses = new ArrayList<>();
            if (hotel.getAddresses() != null) {
                for (Address addr : hotel.getAddresses()) {
                    AddressEntity addressEntity = new AddressEntity();
                    addressEntity.setStreet(addr.getStreet());
                    addressEntity.setCity(addr.getCity());
                    addressEntity.setState(addr.getState());
                    addressEntity.setCountry(addr.getCountry());
                    addressEntity.setPinCode(addr.getPinCode());
                    addressEntity.setHotel(updateHotel);
                    updatedAddresses.add(addressEntity);
                }
            }
            updateHotel.getAddresses().clear();
            updateHotel.getAddresses().addAll(updatedAddresses);

            hotelRepository.save(updateHotel);
            log.info("Hotel updated with hotelId={}", hotel.getHotelId());
            return ApiResponse.<String>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Hotel updated successfully")
                    .data("Hotel updated with ID: " + hotel.getHotelId())
                    .build();
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception ex) {
            log.error("Error while updating hotel with hotelId={}", hotel.getHotelId());
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR, "Error occurred while updating hotel"
            );
        }

    }

    public ApiResponse<List<HotelEntity>> getAllHotels() {
        log.info("Fetching all hotels");
        try {
            List<HotelEntity> hotels = hotelRepository.findAll();
            if (hotels.isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND, "No hotels found in the system");
            }
            return ApiResponse.<List<HotelEntity>>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Hotels fetched successfully.")
                    .data(hotels)
                    .build();
        } catch (Exception ex) {
            log.error("Error while Fetching hotels", ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR, "Error occurred while fetching hotels."
            );
        }

    }

    public ApiResponse<List<HotelEntity>> searchHotels(String keyword) {
        log.info("Searching hotels with keyword: {}", keyword);

        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.INVALID_INPUT,
                        "Search keyword cannot be empty."
                );
            }

            List<HotelEntity> results = hotelRepository.searchHotels(keyword.trim().toLowerCase());

            if (results.isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND,
                        "No matching hotels found for keyword: " + keyword
                );
            }

            // 🔹 Prevent infinite recursion in JSON serialization
            results.forEach(hotel -> {
                if (hotel.getAddresses() != null) {
                    hotel.getAddresses().forEach(addr -> addr.setHotel(null));
                }
            });

            return ApiResponse.<List<HotelEntity>>builder()
                    .status(AvootaResponseStatus.SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Search successful.")
                    .data(results)
                    .build();

        } catch (Exception ex) {
            log.error("Error searching hotels with keyword={}: {}", keyword, ex.getMessage(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while searching hotels."
            );
        }
    }

}