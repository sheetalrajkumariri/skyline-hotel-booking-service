package com.skyline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.skyline.enums.BookingStatus;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate checkInDate;
    private LocalDate checkOut;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private int numberOfRooms;

    @ManyToOne //Booking <-> Hotel
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne //Booking ↔ Customer
    @JoinColumn(name = "users_id")
    private Users users;

    private boolean deleted = false;


}
