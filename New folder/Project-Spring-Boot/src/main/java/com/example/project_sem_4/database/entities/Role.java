package com.example.project_sem_4.database.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts;

    public Role() {
        super();
    }
}
