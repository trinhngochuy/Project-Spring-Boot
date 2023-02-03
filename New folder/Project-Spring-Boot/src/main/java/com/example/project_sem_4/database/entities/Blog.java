package com.example.project_sem_4.database.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "blogs")
public class Blog extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    @Type(type = "text")
    private String content;
    private String thumbnail;


    @ManyToOne(fetch = FetchType.EAGER, cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(name = "account_id")
    private Account account;
    @Column(insertable = false, updatable = false)
    private int account_id;

    public Blog() {
        super();
    }

}
