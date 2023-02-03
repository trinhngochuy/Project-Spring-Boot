package com.example.project_sem_4.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity{

    @Column(columnDefinition = "integer default 0", name = "status",nullable = false)
    private int status;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;
}
