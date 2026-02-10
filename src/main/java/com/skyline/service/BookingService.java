package com.skyline.service;

import com.skyline.dto.BookingRequest;
import com.skyline.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);

    BookingResponse findBookingById(int bookingId);

    List<BookingResponse> findAllBooking(int page, int size, String sortBy, String sortDir);

    BookingResponse deleteBookingById(int bookingId);

    BookingResponse updateBookingById(int bookingId, BookingRequest request);
}
