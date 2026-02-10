package com.skyline.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.skyline.dto.HotelRequest;
import com.skyline.dto.HotelResponse;
import com.skyline.entity.Hotel;
import com.skyline.service.HotelService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/create")
    public HotelResponse createHotel(@RequestBody HotelRequest request){
        log.info("Start:: createHotel()inside the HotelController with the request, {} ",  request);
        return hotelService.createHotel(request);
    }
    @GetMapping("/get/{hotelId}")
    public HotelResponse findHotelById(@PathVariable int hotelId){
        log.info("Start:: FindHotelById()inside the HotelController with the id , {} ",  hotelId);
        return hotelService.findHotelById(hotelId);
    }
    @GetMapping("/list")
    public List<HotelResponse> findAllHotels(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy, @RequestParam String sortDir){
        log.info("Start::findAllHotels() inside HotelController | page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        return hotelService.findAllHotels(page, size, sortBy, sortDir);
    }
    @DeleteMapping("/delete/{hotelId}")
    public String deletedHotelById(@PathVariable int hotelId){
        log.info("Start:: DeletedHotelById()inside the HotelController with the id , {} ",  hotelId);
        return hotelService.deletedHotelById(hotelId);
    }
    @PutMapping("/update/{hotelId}")
    public HotelResponse updateHotelById(@PathVariable int hotelId, @RequestBody HotelRequest request){
        log.info("Start:: UpdatedHotelById()inside the HotelController with the id , {} ",  hotelId);
        return hotelService.updateHotelById(hotelId, request);
    }

    @GetMapping("/search")
    public List<Hotel> searchHotel(@RequestParam String name, @RequestParam String city){
        log.info("Start:: SearchHotel()inside the HotelController | name={}, city={}, ", name, city);
        return hotelService.searchHotel(name, city);
    }

}
