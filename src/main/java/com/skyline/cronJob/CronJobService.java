package com.skyline.cronJob;

import com.skyline.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJobService {
    @Autowired
    private  BookingService bookingService;
    @Scheduled(cron = "0 0/1 * * * ?")
    public void runEveryMinute() {
        System.out.println("Cron Job Running Every 1 Minute");

        String result = bookingService.expireBookings();
        System.out.println(result);
    }
}
