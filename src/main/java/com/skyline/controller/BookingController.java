package com.skyline.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.skyline.dto.BookingRequest;
import com.skyline.dto.BookingResponse;
import com.skyline.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public BookingResponse createBooking(@RequestBody BookingRequest request){
        log.info("Start::createBooking()inside the bookingController with request, {} ", request);
        return bookingService.createBooking(request);
    }
    @GetMapping("/get/{bookingId}")
    public BookingResponse findBookingById(@PathVariable int bookingId){
        log.info("Start::findBookingById()inside the bookingController with id, {} ", bookingId);
        return bookingService.findBookingById(bookingId);
    }
    @GetMapping("/list")
    public List<BookingResponse> findAllBookings(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy, @RequestParam String sortDir){
        log.info("Start::findAllBookings() inside BookingController | page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        return bookingService.findAllBooking(page, size, sortBy, sortDir);
    }
    @DeleteMapping("/delete/{bookingId}")
    public BookingResponse deleteBookingById(@PathVariable int bookingId){
        log.info("Start::deleteBookingById()inside the bookingController with id, {} ", bookingId);
        return bookingService.deleteBookingById(bookingId);
    }
    @PutMapping("/update/{bookingId}")
    public BookingResponse updateBookingById(@PathVariable int bookingId, @RequestBody BookingRequest request){
        log.info("Start::updateBookingById()inside the bookingController with id, {} ", bookingId);
        return bookingService.updateBookingById(bookingId, request);
    }

}
