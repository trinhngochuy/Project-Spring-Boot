package com.example.project_sem_4.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "type_services")
public class TypeService extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Thiếu tên")
    private String name;

    @OneToMany(cascade =
            {CascadeType.MERGE, CascadeType.REFRESH},
            mappedBy = "typeService", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ServiceModel> services;

    public TypeService() {
        super();
    }

    public TypeService(String name, int status) {
        this.name = name;
        this.setStatus(status);
        this.setCreated_at(new Date());
    }

    public TypeService(int id, String name, int status) {
        this.id = id;
        this.name = name;
        this.setStatus(status);
        this.setCreated_at(new Date());
    }
}
