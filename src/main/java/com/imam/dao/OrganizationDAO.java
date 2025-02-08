package com.imam.dao;

import com.imam.entity.Organization;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrganizationDAO implements PanacheRepositoryBase<Organization, UUID> {

    public Uni<Organization> findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Uni<Void> persistOrganization(Organization org) {
        return persist(org).replaceWith(Uni.createFrom().voidItem());
    }

    public Uni<List<Organization>> listAllOrganizations() {
        return listAll();
    }

    public Uni<Boolean> deleteById(UUID id) {
        return delete("id", id).map(rowsDeleted -> rowsDeleted > 0);
    }
}
