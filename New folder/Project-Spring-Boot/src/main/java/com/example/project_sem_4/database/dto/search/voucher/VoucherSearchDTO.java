package com.example.project_sem_4.database.dto.search.voucher;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherSearchDTO {
    private int id;
    private String voucherCode;
    private int voucherCodeInt;
    private String name;
    private Double discount;
    private Date expired_date;
    @JsonProperty("is_used")
    private int is_used;
}
