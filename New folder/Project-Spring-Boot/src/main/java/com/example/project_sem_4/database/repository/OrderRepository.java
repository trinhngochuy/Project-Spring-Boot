package com.example.project_sem_4.database.repository;

import com.example.project_sem_4.database.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "Select" +
            " orders.*"+
            " From orders JOIN bookings ON orders.booking_id = bookings.id"+
            " WHERE bookings.date_booking = ?1" +
            " AND bookings.status = 1 " +
            " AND bookings.time_booking = ?2 " +
            " AND orders.status = 1" +
            " AND bookings.user_id != 1 AND bookings.user_id != 0"
            ,nativeQuery = true)
    List<Order> findOrderByStatusAndBooking_idInHour(String date_booking, String time_booking);
}
