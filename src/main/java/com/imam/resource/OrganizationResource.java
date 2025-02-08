package com.imam.resource;

import com.imam.dto.OrganizationDTO;
import com.imam.entity.Organization;
import com.imam.service.OrganizationService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    @Inject
    OrganizationService organizationService;

    @GET
    public Uni<List<Organization>> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GET
    @Path("/{id}")
    public Uni<Organization> getOrganizationById(@PathParam("id") UUID id) {
        return organizationService.getOrganizationById(id);
    }

    @POST
    public Uni<Void> createOrganization(OrganizationDTO dto) {
        Organization org = dto.toEntity();
        return organizationService.createOrganization(org);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Boolean> deleteOrganization(@PathParam("id") UUID id) {
        return organizationService.deleteOrganization(id);
    }
}
