package com.example.project_sem_4.database.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "vouchers")
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String voucherCode;
    private int voucherCodeInt;
    private String name;
    private Double discount;
    private Date expired_date;
    private boolean is_used;
    public Voucher() {
        super();
    }
}
