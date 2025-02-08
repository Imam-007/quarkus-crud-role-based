package com.imam.service;

import com.imam.dao.OrganizationDAO;
import com.imam.entity.Organization;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrganizationService {

    @Inject
    OrganizationDAO organizationDAO;

    @WithSession
    public Uni<List<Organization>> getAllOrganizations() {
        return organizationDAO.listAllOrganizations();
    }

    @WithSession
    public Uni<Organization> getOrganizationById(UUID id) {
        return organizationDAO.findById(id);
    }

    @WithTransaction
    public Uni<Void> createOrganization(Organization org) {
        return organizationDAO.persistOrganization(org);
    }

    @WithTransaction
    public Uni<Boolean> deleteOrganization(UUID id) {
        return organizationDAO.deleteById(id);
    }
}
