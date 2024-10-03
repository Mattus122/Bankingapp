package com.example.bankingapp.validation;
import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.dto.UserFilterDTO;
import com.example.bankingapp.entity.*;
import com.example.bankingapp.exception.ForbiddenRequestException;
import com.example.bankingapp.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserValidationService {
    private final RoleHierarchyValidator roleHierarchyValidator;
    private final UserAndAccountRoleValidation userAndAccountRoleValidation;
    private final JwtService jwtService;
    private final TokenValidation tokenValidation;
    public void validateDeletion(String token, User user){
        final String hardcodedToken =tokenValidation.removeBearer(token);
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        if(user.getEmail().equals(jwtService.getEmailFromToken(hardcodedToken))){
            throw new ForbiddenRequestException("Self Deletion Not Possible");
        }
        roleHierarchyValidator.validateRoleHierarchy(roleFromToken,user.getRole());
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,user.getOrganisationName());
    }
    public void validatePostCreation(String token,UserDTO userDTO) {
        final String hardcodedToken =tokenValidation.removeBearer(token);
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        roleHierarchyValidator.validateRoleHierarchy(roleFromToken,userDTO.getRole());
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,userDTO.getOrganisationName());
    }
    public void validateUpdation(String token, User existingUser) {
        final String hardcodedToken =tokenValidation.removeBearer(token);
        if(existingUser.getEmail().equals(jwtService.getEmailFromToken(hardcodedToken))){
            throw new ForbiddenRequestException("Self Update Not Possible");
        }
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        roleHierarchyValidator.validateRoleHierarchy(roleFromToken,existingUser.getRole());
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,existingUser.getOrganisationName());
    }
    public void getUserById(String token, User user) {
        final String hardcodedToken =tokenValidation.removeBearer(token);
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,user.getOrganisationName());
        userAndAccountRoleValidation.checkIfSameUser(roleFromToken,jwtService.getEmailFromToken(hardcodedToken),user.getEmail());
    }
    public OrganisationName validateGetAllUserByOrgName(String token) {
        final String hardcodedToken =tokenValidation.removeBearer(token);
        return OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
    }
    public UserFilterDTO validateFilterUser(String token, OrganisationName organisationName, Date startDate, Date endDate, String firstName) {
        final String hardcodedToken = tokenValidation.removeBearer(token);
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        UserFilterDTO userFilterDTO = new UserFilterDTO();
        if (roleFromToken.equals(Role.BANKADMIN) && organisationName == null) {
            organisationName = organisationNameFromToken;
        }
        if (roleFromToken.equals(Role.BANKADMIN) && organisationName != null && !organisationName.equals(organisationNameFromToken)) {
            throw new ForbiddenRequestException("Cannot access data from another organisation.");
        }
        if (roleFromToken.equals(Role.SUPERADMIN) && organisationName == null) {
            organisationName = null;
        }
        userFilterDTO.setFirstName(firstName);
        userFilterDTO.setStartDate(startDate);
        userFilterDTO.setEndDate(endDate);
        userFilterDTO.setOrganisationName(organisationName);
        return userFilterDTO;
    }

}

