package com.imam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "organization")
public class Organization extends PanacheEntity {

    @Column(unique = true, nullable = false)
    public String orgName;

    @Column(unique = true, nullable = false)
    public String orgAddress;

    @JsonIgnore  // Prevents serialization issues
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<User> users;
}

