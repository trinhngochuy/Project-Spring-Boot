package com.example.project_sem_4.database.repository;

import com.example.project_sem_4.database.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query(value = "Select" +
            " od "+
            " From OrderDetail  od where od.pk.order_id = :orderId")
    List<OrderDetail> findAllByOrder_id(@Param("orderId") Integer order_id);
}
