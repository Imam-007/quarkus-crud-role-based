package com.imam.resource;

import com.imam.dto.UserDTO;
import com.imam.entity.User;
import com.imam.service.UserService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public Uni<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    public Uni<User> getUserById(@PathParam("id") UUID id) {
        return userService.getUserById(id);
    }

    @POST
    public Uni<Void> createUser(UserDTO dto) {
        User user = dto.toEntity();
        return userService.createUser(dto);
    }

    @PATCH
    @Path("/{id}")
    public Uni<User> updateUser(@PathParam("id") UUID id, UserDTO dto) {
        return userService.updateUser(id, dto);
    }

    @GET
    @Path("/organization/{orgId}")
    public Uni<List<User>> getUsersByOrganization(@PathParam("orgId") UUID orgId) {
        return userService.getUsersByOrganization(orgId);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Boolean> deleteUser(@PathParam("id") UUID id) {
        return userService.deleteUser(id);
    }
}
