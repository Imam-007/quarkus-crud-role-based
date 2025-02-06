package com.imam.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public int age;

    @Column(nullable = false, unique = true)
    public String mobile;

    @Column(nullable = false, unique = true)
    public String mail;

    @Column(nullable = false)
    public String role;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    public Organization organization;
}
