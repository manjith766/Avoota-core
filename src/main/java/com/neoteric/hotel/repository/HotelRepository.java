package com.neoteric.hotel.repository;

import com.neoteric.hotel.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    @Query("SELECT h FROM HotelEntity h WHERE LOWER(h.hotelName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(h.address.city) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(h.address.state) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<HotelEntity>searchHotels(@Param("keyword") String keyword);
}
