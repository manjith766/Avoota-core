package com.neoteric.hotel.repository;
import com.neoteric.hotel.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface AddressRepository extends JpaRepository<AddressEntity,Long> {
    Optional<AddressEntity> findByHotel_HotelId(String hotelId);

}
