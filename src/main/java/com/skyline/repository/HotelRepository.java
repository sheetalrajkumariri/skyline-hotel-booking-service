package com.skyline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.skyline.entity.Hotel;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {


    List<Hotel> findByCityAndName(String city, String name);

    List<Hotel> findByName(String name);

    List<Hotel> findByCity(String city);
}
