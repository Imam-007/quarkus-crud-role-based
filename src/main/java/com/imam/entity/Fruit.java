package com.imam.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Cacheable
public class Fruit extends PanacheEntity {

    @Column(unique = true)
    public String name;
}
