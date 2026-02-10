package com.skyline.dto;

import lombok.Data;

@Data
public class HotelResponse {
    private int id;
    private String name;
    private String address;
    private String city;
    private int totalRooms;
    private String createdBy;
}
