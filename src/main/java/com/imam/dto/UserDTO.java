package com.imam.dto;

import java.util.UUID;
import com.imam.entity.User;

public class UserDTO {
    public UUID id;
    public String name;
    public int age;
    public String mobile;
    public String mail;
    public String role;
    public UUID organizationId;

    public User toEntity() {
        User user = new User();
        user.setName(this.name);
        user.setMail(this.mail);
        user.setAge(this.age);
        user.setMobile(this.mobile);
        user.setRole(this.role);
        return user;
    }
}
