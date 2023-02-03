package com.example.project_sem_4.database.dto.typeService;

import com.example.project_sem_4.database.entities.ServiceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TypeServiceForOrderDTO {
    private int id;
    private String name;
    @OneToMany(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "typeService", fetch = FetchType.LAZY)
    private Set<ServiceModel> services;
}
