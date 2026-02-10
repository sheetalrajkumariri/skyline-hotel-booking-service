package com.skyline.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private Integer hotelId;
    private Integer customerId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfRooms;
}
