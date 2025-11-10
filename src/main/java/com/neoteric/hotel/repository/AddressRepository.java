package com.neoteric.hotel.repository;

import com.neoteric.hotel.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity,Long> {

    @Query("SELECT a FROM AddressEntity a WHERE a.hotelId = :hotelId")
    Optional<AddressEntity> findAddressByHotelId(@Param("hotelId") String hotelId);

}
