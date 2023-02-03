package com.example.project_sem_4.database.search_body;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class VoucherSearchBody {
    private String name;
    private String voucher_code;
    private Integer is_used;
    private Integer status;
    private Integer limit;
    private Integer page;
    private String sort;

    public VoucherSearchBody() {
        this.limit = 4;
        this.page = 1;
    }
}
