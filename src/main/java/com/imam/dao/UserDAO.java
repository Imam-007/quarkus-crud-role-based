package com.imam.dao;

import com.imam.entity.User;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserDAO implements PanacheRepositoryBase<User, UUID> {

    public Uni<User> findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Uni<List<User>> findByOrganizationId(UUID orgId) {
        return list("organization.id", orgId);
    }
}
