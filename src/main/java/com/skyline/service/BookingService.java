package com.skyline.service;

import com.skyline.dto.BookingRequest;
import com.skyline.dto.BookingResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);

    BookingResponse findBookingById(int bookingId);

    List<BookingResponse> findAllBooking(int page, int size, String sortBy, String sortDir);

    String deleteBookingById(int bookingId);

    BookingResponse updateBookingById(int bookingId, BookingRequest request);

    String cancelBooking(int bookingId);

    void expireBookings();
}
