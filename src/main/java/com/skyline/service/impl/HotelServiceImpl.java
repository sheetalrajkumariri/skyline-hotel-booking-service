package com.skyline.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.skyline.dto.HotelRequest;
import com.skyline.dto.HotelResponse;
import com.skyline.entity.Hotel;
import com.skyline.exception.NotFoundException;
import com.skyline.repository.HotelRepository;
import com.skyline.service.HotelService;
import java.util.List;

@Service
@Slf4j
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public HotelResponse createHotel(HotelRequest request) {
        log.info("Start:: createHotel()inside the hotelServiceImpl with request, {} ", request);
        Hotel hotel = modelMapper.map(request, Hotel.class);
        hotel = hotelRepository.save(hotel);
        log.info("End:: createHotel()inside the hotelServiceImpl with request, {} ", request);
        return modelMapper.map(hotel, HotelResponse.class);
    }

    @Override
    public HotelResponse findHotelById(int hotelId) {
        log.info("Start:: FindHotelById()inside the hotelServiceImpl with id, {} ", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("Hotel not found with id: " + hotelId));
        log.info("End:: FindHotelById()inside the hotelServiceImpl with id, {} ", hotelId);
        return modelMapper.map(hotel, HotelResponse.class);
    }

    @Override
    public List<HotelResponse> findAllHotels(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Hotel> hotelPage = hotelRepository.findAll(pageable);

        List<HotelResponse> responseList = hotelPage.getContent()
                .stream()
                .map(hotel -> modelMapper.map(hotel, HotelResponse.class))
                .toList();

        log.info("End:: findAllHotels() inside HotelServiceImpl");

        return responseList;
    }

    @Override
    public String deletedHotelById(int hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("Hotel not found with id: " + hotelId));
        hotelRepository.delete(hotel);
        return "Hotel deleted successfully";
    }

    @Override
    public HotelResponse updateHotelById(int hotelId, HotelRequest request) {
        log.info("Start:: UpdateHotelById()inside the hotelServiceImpl with id, {} ", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("Hotel not found with id: " + hotelId));
        modelMapper.map(request, hotel);
        Hotel update = hotelRepository.save(hotel);
        log.info("End:: UpdateHotelById()inside the hotelServiceImpl with id, {} ", hotelId);
        return modelMapper.map(update, HotelResponse.class);
    }

    @Override
    public List<Hotel> searchHotel(String name, String city) {
        if (name != null && city != null) {
            return hotelRepository
                    .findByCityAndName(city, name);
        }

        if (name != null) {
            return hotelRepository.findByName(name);
        }

        if (city != null) {
            return hotelRepository.findByCity(city);
        }

        return hotelRepository.findAll();
    }


}
