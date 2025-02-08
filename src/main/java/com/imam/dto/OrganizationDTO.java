package com.imam.dto;

import com.imam.entity.Organization;
import java.util.UUID;

public class OrganizationDTO {
    public UUID id;
    public String name;
    public String orgAddress;

    public Organization toEntity() {
        Organization org = new Organization();
        org.setOrgName(this.name);
        org.setOrgAddress(this.orgAddress);
        return org;
    }
}
