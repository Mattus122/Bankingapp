package com.example.bankingapp.validation;

import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.ForbiddenRequestException;
import com.example.bankingapp.exception.NoAccountFoundException;
import com.example.bankingapp.exception.UserNotFoundException;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountValidationService {
    private final JwtService jwtService;
    private final RoleHierarchyValidator roleHierarchyValidator;
    private final UserAndAccountRoleValidation userAndAccountRoleValidation;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TokenValidation tokenValidation;
    public void validateAccountPostCreation(String token,User existingUser) {
        final String hardcodedToken =tokenValidation.removeBearer(token);
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        if(!existingUser.getRole().equals(Role.USER)){
            throw new ForbiddenRequestException("Account Creation possible for only USER Role");
        }
        roleHierarchyValidator.hasSameOrganisation
                (roleFromToken,organisationNameFromToken,existingUser.getOrganisationName());
    }
    public void validateAccountDeletion(String token,Account account){
        final String hardcodedToken =tokenValidation.removeBearer(token);
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        User user = account.getUser();
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,user.getOrganisationName());
    }
    public void validateAccountInfo(String token,User user){
        final String hardcodedToken =tokenValidation.removeBearer(token);
        Role roleFromToken =Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,user.getOrganisationName());
        userAndAccountRoleValidation.checkIfSameUser(roleFromToken,jwtService.getEmailFromToken(hardcodedToken),user.getEmail());
    }
    public void validateAccountUpdation(String token,Account account){
        final String hardcodedToken =tokenValidation.removeBearer(token);
        User user = account.getUser();
        Role roleFromToken =Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,user.getOrganisationName());
    }

}
