package com.skyline.repository;

import com.skyline.entity.Users;
import com.skyline.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.skyline.entity.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByHotel_IdAndCheckInDateAndStatus(
            Integer hotelId,
            LocalDate checkIn,
            BookingStatus status
    );

//    Optional<Booking> findByIdWithUser(int bookingId);
}
