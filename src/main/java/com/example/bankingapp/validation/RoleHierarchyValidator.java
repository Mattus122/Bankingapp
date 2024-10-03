package com.example.bankingapp.validation;

import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.exception.ForbiddenRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class RoleHierarchyValidator{
    boolean hasPermission(Role currentRole, Role targetRole) {
        return currentRole.getLevel() >= targetRole.getLevel();
    }
    void validateRoleHierarchy(Role currentRole, Role targetRole) {
        if (!hasPermission(currentRole, targetRole)) {
            throw new ForbiddenRequestException("Insufficient permissions to create or manage this role");
        }
    }
    boolean hasSameOrganisation( Role role, OrganisationName jwtTokenUsersOrganisation,OrganisationName orgName){
        if(role.equals(Role.SUPERADMIN)){
            return true;
        }
        if(jwtTokenUsersOrganisation.equals(orgName)
                && (role.equals(Role.BANKADMIN)||role.equals(Role.USER))){
            return true;
        }
        else{
            throw new ForbiddenRequestException("Operation not allowed: Access to this organization is restricted.");
        }
    }
}

