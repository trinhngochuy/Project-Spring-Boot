package com.example.project_sem_4.database.dto.order;

import com.example.project_sem_4.database.entities.Order;
import com.example.project_sem_4.database.entities.ServiceModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Getter
@Setter
public class OrderDetailByOrderIdDTO {

    private Integer order_id;
    private Integer service_id;
    private Integer unit_price;
    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("service_id")
    @JoinColumn(name = "service_id")
    private ServiceModel serviceModel;
}
