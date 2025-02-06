package com.imam.resource;

import com.imam.entity.Organization;
import com.imam.entity.User;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;
import java.util.List;

@Path("/organization")
@ApplicationScoped
public class OrganizationResource {

    @GET
    public Uni<List<Organization>> getAllOrganizations(){
        return Organization.listAll();
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<Organization>> getOrgById(@PathParam("id") Long id){
        return Organization.<Organization>findById(id)
                .onItem().ifNotNull().transform(RestResponse::ok)
                .onItem().ifNull().continueWith(RestResponse.status(RestResponse.Status.NOT_FOUND, null));
    }

    @POST
    public Uni<RestResponse<Organization>> addOrganization(Organization organization){
        if (organization.orgName == null || organization.orgAddress == null) {
            return Uni.createFrom().item(RestResponse.status(RestResponse.Status.BAD_REQUEST, null));
        }
        return Panache.withTransaction(organization::persist)
                .replaceWith(RestResponse.status(RestResponse.Status.CREATED, organization));
    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<String>> deleteOrganization(@PathParam("id") Long id){
        return Panache.withTransaction(() -> Organization.deleteById(id))
                .map(deleted -> deleted
                        ? RestResponse.status(RestResponse.Status.OK, "Organization deleted successfully")
                        : RestResponse.status(RestResponse.Status.NOT_FOUND, "Organization not found"));
    }

    @PATCH
    @Path("/{id}")
    public Uni<RestResponse<Organization>> updateOrganization(@PathParam("id") Long id, Organization updatedOrg) {
        return Panache.withTransaction(() ->
                Organization.<Organization>findById(id)
                        .onItem().ifNotNull().transformToUni(org -> {
                            org.orgName = updatedOrg.orgName != null ? updatedOrg.orgName : org.orgName;
                            org.orgAddress = updatedOrg.orgAddress != null ? updatedOrg.orgAddress : org.orgAddress;
                            return org.persistAndFlush().replaceWith(RestResponse.ok(org));
                        })
                        .onItem().ifNull().continueWith(RestResponse.status(RestResponse.Status.NOT_FOUND, null))
        );
    }

    @GET
    @Path("/{id}/users")
    public Uni<List<User>> getUsersByOrganization(@PathParam("id") Long id){
        return User.find("organization.id", id).list();
    }
}
