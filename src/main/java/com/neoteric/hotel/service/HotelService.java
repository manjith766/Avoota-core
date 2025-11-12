package com.neoteric.hotel.service;
import com.neoteric.common.ui.ApiResponse;
import com.neoteric.common.ui.AvootaResponseStatus;
import com.neoteric.common.ui.AvootaUtil;
import com.neoteric.hotel.entity.AddressEntity;
import com.neoteric.hotel.entity.HotelEntity;
import com.neoteric.hotel.model.Address;
import com.neoteric.hotel.model.Hotel;
import com.neoteric.hotel.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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


    @Transactional
    public ApiResponse<String> saveOrUpdateHotel(Hotel hotel) {
        log.info("Save/Update hotel initiated for hotelId={}", hotel.getHotelId());

        try {
            //  Validate input
            if (hotel.getHotelId() == null || hotel.getHotelId().isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.INVALID_INPUT,
                        "Hotel ID is required."
                );
            }

            Optional<HotelEntity> existingOpt = hotelRepository.findByHotelId(hotel.getHotelId());
            HotelEntity targetHotel = existingOpt.orElseGet(() -> {
                log.info("Creating new hotel with hotelId={}", hotel.getHotelId());
                HotelEntity newHotel = new HotelEntity();
                newHotel.setHotelId(hotel.getHotelId());
                newHotel.setAddresses(new ArrayList<>());
                return newHotel;
            });

            //  Partial field updates
            if (hotel.getHotelName() != null) targetHotel.setHotelName(hotel.getHotelName());
            if (hotel.getStatus() != null) targetHotel.setStatus(hotel.getStatus());

            // Handle address updates/creates
            if (hotel.getAddresses() != null && !hotel.getAddresses().isEmpty()) {
                for (Address addr : hotel.getAddresses()) {
                    AddressEntity existingAddr = targetHotel.getAddresses().stream()
                            .filter(a -> a.getCity().equalsIgnoreCase(addr.getCity())
                                    && a.getPinCode().equalsIgnoreCase(addr.getPinCode()))
                            .findFirst()
                            .orElse(null);

                    if (existingAddr != null) {
                        // Partial update
                        if (addr.getStreet() != null) existingAddr.setStreet(addr.getStreet());
                        if (addr.getState() != null) existingAddr.setState(addr.getState());
                        if (addr.getCountry() != null) existingAddr.setCountry(addr.getCountry());
                    } else {
                        // Add new address
                        AddressEntity newAddr = new AddressEntity();
                        newAddr.setStreet(addr.getStreet());
                        newAddr.setCity(addr.getCity());
                        newAddr.setState(addr.getState());
                        newAddr.setCountry(addr.getCountry());
                        newAddr.setPinCode(addr.getPinCode());
                        newAddr.setHotel(targetHotel);
                        targetHotel.getAddresses().add(newAddr);
                    }
                }
            }

            //  Save or update
            hotelRepository.save(targetHotel);

            String action = existingOpt.isPresent() ? "updated" : "created";
            log.info("Hotel {} successfully with hotelId={}", action, hotel.getHotelId());

            return ApiResponse.<String>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Hotel " + action + " successfully.")
                    .data("Hotel " + action + " with ID: " + hotel.getHotelId())
                    .build();

        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate hotel name detected: {}", hotel.getHotelName());
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DUPLICATE_ENTRY,
                    "Hotel name already exists: " + hotel.getHotelName()
            );
        } catch (Exception ex) {
            log.error("Error during save/update of hotelId={}", hotel.getHotelId(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while saving/updating hotel."
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

            //  Prevent infinite recursion in JSON serialization
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