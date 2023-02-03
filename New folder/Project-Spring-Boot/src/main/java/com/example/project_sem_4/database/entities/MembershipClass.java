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
@Table(name = "membership_classes")
public class MembershipClass extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "member_ship_class_id", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Account> accounts;

    public MembershipClass() {
        super();
    }
}
