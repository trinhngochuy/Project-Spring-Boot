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
@Table(name = "branchs")
public class Branch extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    private String name;
    private String hot_line;
    private String thumbnail;

    @JsonIgnore
    @OneToMany(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "branch_id", fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    public Branch() {
        super();
    }
}
