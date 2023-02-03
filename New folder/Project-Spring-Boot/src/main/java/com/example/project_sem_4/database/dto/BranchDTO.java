package com.example.project_sem_4.database.dto;
import com.example.project_sem_4.database.entities.Booking;
import com.example.project_sem_4.database.entities.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
public class BranchDTO {

    @NotEmpty(message = "Thiếu địa chỉ")
    private String address;
    @NotEmpty(message = "Thiếu Số điện thoại")
    @Size(min = 10, max = 10,message = "Sai định dạng điện thoại")
    private String hot_line;
    @NotEmpty(message = "Thiếu ảnh")
    private String thumbnail;

    @NotEmpty(message = "Thiếu tên")
    private String name;
    private Set<Booking> bookings;

    private int status;
    private Date createdAt;
    private Date updatedAt;

    public BranchDTO(Branch branch) {
        this.address = branch.getAddress();
        this.hot_line = branch.getHot_line();
        this.thumbnail = branch.getThumbnail();
        this.bookings = branch.getBookings();
        this.status = branch.getStatus();
        this.createdAt = branch.getCreated_at();
        this.updatedAt = branch.getUpdated_at();
    }

}
