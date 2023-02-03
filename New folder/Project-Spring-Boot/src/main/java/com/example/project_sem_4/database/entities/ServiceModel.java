package com.example.project_sem_4.database.entities;

import com.example.project_sem_4.database.dto.ServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "services")
public class ServiceModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnail;
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER, cascade =
            {CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "type_service_id")
    private TypeService typeService;
    @Column(insertable = false, updatable = false)
    private int type_service_id;

    public ServiceModel() {
        super();
    }

    public ServiceModel(ServiceDTO serviceDTO, TypeService typeService1) {
        this.name = serviceDTO.getName();

        this.description = serviceDTO.getDescription();
        this.typeService = typeService1;
        this.thumbnail = serviceDTO.getThumbnail();
        this.price = serviceDTO.getPrice();
    }
}
