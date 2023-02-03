package com.example.project_sem_4.database.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    private String phone;
    @Column(unique = true)
    private String email;
    private String password;
    private String thumbnail;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String gender;
    private double total_payment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade =
            {CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "member_ship_class_id")
    private MembershipClass membershipClass;
    @Column(insertable = false, updatable = false)
    private int member_ship_class_id;

    @OneToMany(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "customer_id", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Order> orders;

    @JsonIgnore
    @OneToMany(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "account_id", fetch = FetchType.LAZY)
    private Set<Blog> blogs;

    @JsonIgnore
    @OneToMany(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "employee_id", fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "accounts_roles",
            joinColumns = @JoinColumn(
                    name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public Account() {
        super();
    }
}
