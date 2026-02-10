package com.skyline.repository;

import com.skyline.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.skyline.entity.Booking;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByHotel_IdAndCheckInDateAndStatus(
            Integer hotelId,
            LocalDate checkIn,
            BookingStatus status
    );
}
