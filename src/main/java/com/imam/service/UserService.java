package com.imam.service;

import com.imam.dao.OrganizationDAO;
import com.imam.dao.UserDAO;
import com.imam.dto.UserDTO;
import com.imam.entity.User;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    @Inject
    UserDAO userDAO;

    @Inject
    OrganizationDAO organizationDAO;

    @WithSession
    public Uni<List<User>> getAllUsers() {
        return userDAO.listAll();
    }

    @WithSession
    public Uni<User> getUserById(UUID id) {
        return userDAO.findById(id);
    }

    @WithTransaction
    public Uni<Void> createUser(UserDTO dto) {
        return organizationDAO.findById(dto.organizationId)
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Organization not found"))
                .onItem().transformToUni(org -> {
                    User user = dto.toEntity();
                    user.setOrganization(org);  // ✅ Assign organization before persisting
                    return userDAO.persist(user).replaceWith(Uni.createFrom().voidItem());
                });
    }

    @WithTransaction
    public Uni<User> updateUser(UUID id, UserDTO dto) {
        System.out.println("Updating user with ID: " + id);

        return userDAO.findById(id)
                .onItem().invoke(user -> {
                    if (user == null) {
                        System.out.println("User not found with ID: " + id);
                        throw new IllegalArgumentException("User not found");
                    } else {
                        System.out.println("User found: " + user.getName());
                    }
                })
                .onItem().transformToUni(existingUser -> {
                    existingUser.setName(dto.name);
                    existingUser.setMail(dto.mail);
                    existingUser.setAge(dto.age);
                    existingUser.setMobile(dto.mobile);
                    existingUser.setRole(dto.role);

                    return userDAO.update(String.valueOf(existingUser))  // Use update instead of persist
                            .chain(userDAO::flush)  // Force commit
                            .replaceWith(existingUser);
                });
    }

    @WithSession
    public Uni<List<User>> getUsersByOrganization(UUID orgId) {
        return userDAO.findByOrganizationId(orgId);
    }

    @WithTransaction
    public Uni<Boolean> deleteUser(UUID id) {
        return userDAO.deleteById(id);
    }
}
