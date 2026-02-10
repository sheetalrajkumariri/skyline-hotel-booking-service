package com.skyline.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponse {
    private int bookingId;
    private String hotelName;
    private String customerName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfRooms;
    private String status;
}
