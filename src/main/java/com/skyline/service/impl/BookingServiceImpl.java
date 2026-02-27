package com.skyline.service.impl;

import com.skyline.exception.BookingCancelException;
import com.skyline.exception.RequestRoomException;
import com.skyline.service.emailService.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.skyline.dto.BookingRequest;
import com.skyline.dto.BookingResponse;
import com.skyline.entity.Booking;
import com.skyline.entity.Users;
import com.skyline.entity.Hotel;
import com.skyline.enums.BookingStatus;
import com.skyline.exception.NotFoundException;
import com.skyline.repository.BookingRepository;
import com.skyline.repository.UsersRepository;
import com.skyline.repository.HotelRepository;
import com.skyline.service.BookingService;

import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private EmailService emailService;


    @Override
    public BookingResponse createBooking(BookingRequest request) {
        log.info("Start::createBooking()inside the BookingServiceImpl with the request, {} ", request);
        Users users = usersRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + request.getCustomerId()));
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new NotFoundException("Hotel not found with id: " + request.getHotelId()));

        List<Booking> bookedRooms = bookingRepository
                .findByHotel_IdAndCheckInDateAndStatus(
                        request.getHotelId(),
                        request.getCheckIn(),
                        BookingStatus.BOOKED
                );
        int alreadyBookedRooms = bookedRooms.stream()
                .mapToInt(Booking::getNumberOfRooms)
                .sum();

        int availableRooms = hotel.getTotalRooms() - alreadyBookedRooms;

        if (request.getNumberOfRooms() > availableRooms) {
            throw new RequestRoomException(
                    "Only " + availableRooms + " rooms available on " + request.getCheckIn()
            );
        }
        Booking booking = new Booking();
        booking.setCheckInDate(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setNumberOfRooms(request.getNumberOfRooms());
        booking.setUsers(users);
        booking.setHotel(hotel);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setCustomerEmail(users.getEmail());

        booking = bookingRepository.save(booking);

        try {
            emailService.sendBookingConfirmation(booking.getCustomerEmail(), booking);
            log.info("Booking confirmation email sent successfully to {}", booking.getCustomerEmail());
        } catch (Exception e) {
            log.error("Failed to send booking confirmation email to {}: {}", booking.getCustomerEmail(), e.getStackTrace(), e);
        }


        BookingResponse response = modelMapper.map(booking, BookingResponse.class);
        response.setBookingId(booking.getId());
        response.setHotelName(booking.getHotel().getName());
        response.setCustomerName(booking.getUsers().getUsername());
        response.setStatus(booking.getStatus().name());
        log.info("Start::createBooking()inside the BookingServiceImpl with the request, {} ", request);
        return response;
    }

    @Override
    public BookingResponse findBookingById(int bookingId) {
        log.info("Start:: findBookingById()inside the BookingServiceImpl with id, {} ", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));
        BookingResponse response = modelMapper.map(booking, BookingResponse.class);
        if (booking.getUsers() != null) {
            response.setCustomerName(booking.getUsers().getUsername());
        }

        if (booking.getHotel() != null) {
            response.setHotelName(booking.getHotel().getName());
        }
        response.setBookingId(booking.getId());

        response.setStatus(booking.getStatus().name());

        log.info("End:: findBookingById() inside BookingServiceImpl with id: {}", bookingId);

        return response;
    }

    @Override
    public List<BookingResponse> findAllBooking(int page, int size, String sortBy, String sortDir) {
        log.info("Start:: findAllBooking() inside BookingServiceImpl");

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }


        String username = authentication.getName();
        log.info("Logged in Username: {}", username);

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();

        log.info("User Roles: {}", roles);

        Page<Booking> bookingPage;

        boolean isAdmin = roles.stream().anyMatch(name -> name.equals("ADMIN"));

        log.info("Is Admin: {}", isAdmin);
        if (isAdmin) {
            bookingPage = bookingRepository.findAll(pageable);
        } else {
            bookingPage = bookingRepository.findByUsers_Username(username, pageable);
        }

        return bookingPage.getContent()
                .stream()
                .map(booking -> {

                    BookingResponse response = new BookingResponse();

                    response.setBookingId(booking.getId());
                    response.setCheckIn(booking.getCheckInDate());
                    response.setCheckOut(booking.getCheckOut());
                    response.setNumberOfRooms(booking.getNumberOfRooms());
                    response.setStatus(booking.getStatus().name());

                    if (booking.getUsers() != null) {
                        response.setCustomerName(booking.getUsers().getUsername());
                    }
                    if (booking.getHotel() != null) {
                        response.setHotelName(booking.getHotel().getName());
                    }

                    return response;
                })
                .toList();
    }

    @Override
    public String deleteBookingById(int bookingId) {
        log.info("Start:: deleteBooking() inside BookingServiceImpl");
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));
        if (booking.isDeleted()) {
            throw new NotFoundException("Booking already deleted with id: " + bookingId);
        }
        booking.setDeleted(true);
        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);

        modelMapper.map(updated, BookingResponse.class);
        log.info("Start:: deleteBooking() inside BookingServiceImpl");

        return "Booking deleted successfully with id: " + bookingId;
    }

    @Override
    public BookingResponse updateBookingById(int bookingId, BookingRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));
        Users users = usersRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + request.getCustomerId()));
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new NotFoundException("Hotel not found with id: " + request.getHotelId()));

        List<Booking> bookedRooms = bookingRepository
                .findByHotel_IdAndCheckInDateAndStatus(
                        request.getHotelId(),
                        request.getCheckIn(),
                        BookingStatus.BOOKED
                );

        int alreadyBookedRooms = bookedRooms.stream().filter(b -> b.getId() != bookingId)
                .mapToInt(Booking::getNumberOfRooms)
                .sum();
        log.info("Already booked rooms (excluding current booking): {}", alreadyBookedRooms);

        int availableRooms = hotel.getTotalRooms() - alreadyBookedRooms;
        log.info("Available rooms: {}", availableRooms);

        if (request.getNumberOfRooms() > availableRooms) {
            throw new RequestRoomException(
                    "Only " + availableRooms + " rooms available on " + request.getCheckIn()
            );
        }

        booking.setCheckInDate(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setNumberOfRooms(request.getNumberOfRooms());
        booking.setUsers(users);
        booking.setHotel(hotel);
        booking.setStatus(BookingStatus.BOOKED);


        Booking update = bookingRepository.save(booking);

        BookingResponse response = modelMapper.map(update, BookingResponse.class);
        response.setBookingId(booking.getId());
        response.setHotelName(booking.getHotel().getName());
        response.setCustomerName(booking.getUsers().getUsername());
        response.setStatus(booking.getStatus().name());

        return response;
    }

    @Override
    public String cancelBooking(int bookingId) {
        log.info("Start:: cancelBooking() inside BookingServiceImpl");

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingCancelException("Booking is already cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);

        modelMapper.map(booking, BookingResponse.class);
        log.info("End:: cancelBooking() inside BookingServiceImpl");

        return "Booking canceled successfully with id: " + bookingId;
    }

    @Async
    @Override
    public void expireBookings() {
        log.info("Start :: expireBookings() method execution");
        List<Booking> bookings = bookingRepository.findBycheckOutBefore(LocalDate.now());
        bookings.forEach(booking -> booking.setStatus(BookingStatus.EXPIRED));
        bookingRepository.saveAll(bookings);
        log.info("End :: expireBookings() method execution");
        log.info(bookings.size() + " of bookings expired successfully");
    }
}
