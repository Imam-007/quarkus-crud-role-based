package com.imam.resource;

import com.imam.entity.Organization;
import com.imam.entity.User;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import java.util.List;

@Path("/user")
@ApplicationScoped
public class UserResource {

    @GET
    public Uni<List<User>> getAllUsers(){
        return User.listAll();
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<User>> getUserById(@PathParam("id") Long id){
        return User.<User>findById(id)
                .onItem().ifNotNull().transform(RestResponse::ok)
                .onItem().ifNull().continueWith(RestResponse.status(RestResponse.Status.NOT_FOUND, null));
    }

    @POST
    @Path("/org/{orgId}")
    public Uni<RestResponse<User>> addUser(@PathParam("orgId") Long orgId, User user){
        return Organization.<Organization>findById(orgId)
                .onItem().ifNotNull().transformToUni(org -> {
                    user.organization = org;
                    return Panache.withTransaction(user::persist)
                            .replaceWith(RestResponse.status(RestResponse.Status.CREATED, user));
                })
                .onItem().ifNull().continueWith(RestResponse.status(RestResponse.Status.NOT_FOUND, null));
    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<String>> deleteUser(@PathParam("id") Long id){
        return Panache.withTransaction(() -> User.deleteById(id))
                .map(deleted -> deleted
                        ? RestResponse.status(Response.Status.OK, "User Deleted Successfully")
                        : RestResponse.status(RestResponse.Status.NOT_FOUND, "User Not Found"));
    }

    @PATCH
    @Path("/{id}")
    public Uni<RestResponse<User>> updateUser(@PathParam("id") Long id, User updatedUser){
        return Panache.withTransaction(() ->
                User.<User>findById(id)
                        .onItem().ifNotNull().transformToUni(user -> {
                            user.name = updatedUser.name != null ? updatedUser.name : user.name;
                            user.age = updatedUser.age > 0 ? updatedUser.age : user.age;
                            user.mobile = updatedUser.mobile != null ? updatedUser.mobile : user.mobile;
                            user.mail = updatedUser.mail != null ? updatedUser.mail : user.mail;
                            user.role = updatedUser.role != null ? updatedUser.role : user.role;
                            return user.persistAndFlush().replaceWith(RestResponse.ok(user));
                        })
                        .onItem().ifNull().continueWith(RestResponse.status(RestResponse.Status.NOT_FOUND, null))
        );
    }
}
