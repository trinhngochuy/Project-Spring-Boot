package com.example.project_sem_4.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String voucher_id;
    private String booking_id;
    private double total_price;
    @ManyToOne(fetch = FetchType.EAGER, cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    private Account customer;

    @Column(insertable = false, updatable = false)
    private int customer_id;

    @JsonIgnore
    @OneToMany(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "pk.order_id", fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails;

    public Order() {
        super();
    }
}
