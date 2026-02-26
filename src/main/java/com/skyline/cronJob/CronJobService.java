package com.skyline.cronJob;

import com.skyline.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
public class CronJobService {

    @Autowired
    private  BookingService bookingService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void jobToBookingExpired() {

        log.info("Start:: jobToBookingExpired at "+ LocalDate.now() +" & Thread Name :: "+Thread.currentThread().getName());

        bookingService.expireBookings();

        log.info("End:: jobToBookingExpired at "+ LocalDate.now());
    }
}
