package com.example.project_sem_4.database.dto.search.service;

import com.example.project_sem_4.database.entities.TypeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSearchDTO {
    private int service_id;
    private String service_name;
    private String description;
    private Integer price;
    private String thumbnail;
    private int typeServiceId;
    private TypeService type_service;
    private int status;
}
