package com.skyline.service;

import com.skyline.dto.HotelRequest;
import com.skyline.dto.HotelResponse;
import com.skyline.entity.Hotel;

import java.util.List;

public interface HotelService {
    HotelResponse createHotel(HotelRequest request);

    HotelResponse findHotelById(int hotelId);

    List<HotelResponse> findAllHotels(int page, int size, String sortBy, String sortDir);

    String deletedHotelById(int hotelId);

    HotelResponse updateHotelById(int hotelId, HotelRequest request);


    List<Hotel> searchHotel(String name, String city);
}
