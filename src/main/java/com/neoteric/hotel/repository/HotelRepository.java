package com.neoteric.hotel.repository;

import com.neoteric.hotel.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    @Query("""
    SELECT DISTINCT h
    FROM HotelEntity h
    LEFT JOIN h.addresses a
    WHERE LOWER(h.hotelName) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(a.city) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(a.state) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(a.country) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<HotelEntity> searchHotels(@Param("keyword") String keyword);


    Optional<HotelEntity> findByHotelId(String hotelId);
}
