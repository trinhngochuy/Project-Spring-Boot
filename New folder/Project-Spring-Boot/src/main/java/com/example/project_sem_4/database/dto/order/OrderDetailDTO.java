package com.example.project_sem_4.database.dto.order;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    @NotNull(message = "Thiếu id hóa đơn")
    @Min(value = 1, message= "Thiếu id hóa đơn")
    private Integer order_id;
    @NotEmpty(message = "Thiếu danh sách hóa đơn")
    private Set<ListServiceDetailDTO> orderDetails;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ListServiceDetailDTO{
        @NotNull(message = "Thiếu id dịch vụ")
        @Min(value = 1, message= "Thiếu id dịch vụ")
        private Integer service_id;
        @NotNull(message = "Thiếu giá tiền tại thời điểm đó")
        @Min(value = 1, message= "Thiếu giá tiền tại thời điểm đó")
        private Double unit_price;
    }

}
