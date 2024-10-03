package com.example.bankingapp.validation;

import com.example.bankingapp.entity.*;
import com.example.bankingapp.exception.*;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionValidationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenValidation tokenValidation;
    private final RoleHierarchyValidator roleHierarchyValidator;
    public void validateTransactionCreation(String token,
                                            Account debtorAccount,
                                            Account creditorAccount){
        final String hardcodedToken =tokenValidation.removeBearer(token);
        UUID userIdFromToken = UUID.fromString(jwtService.returnUserId(hardcodedToken));
        checkIfSameDebtor(userIdFromToken,debtorAccount.getAccountId());
        if (!creditorAccount.getCurrency().equals(debtorAccount.getCurrency())) {
            throw new InvalidJwtToken("Mismatch in currency Cannot Process Transaction");
        }
    }
    private void checkIfSameDebtor(UUID userIdFromToken,UUID accountId){
        User existingUser = userRepository.findById(userIdFromToken)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: "+userIdFromToken));
        List<Account> accountList = existingUser.getAccounts();
        boolean flag = false;
        for(Account a : accountList){
            if (a.getAccountId().equals(accountId)){
                flag=true;
                break;
            }
        }
        if(!flag){
            throw new NoAccountFoundException("No Account Found for userId"+userIdFromToken);
        }

    }
    public void validateFindTransactionByUserId(User user,String token){
        final String hardcodedToken =tokenValidation.removeBearer(token);
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        UUID userIdFromToken = UUID.fromString(jwtService.returnUserId(hardcodedToken));
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,user.getOrganisationName());
        sameUser(roleFromToken,userIdFromToken,user.getId());

    }
    private void sameUser(Role roleFromToken,UUID userIdFromToken,UUID userId){
        if (roleFromToken.equals(Role.USER)) {
            if(!userIdFromToken.equals(userId)){throw new ForbiddenRequestException("Cannot Access Other User Data");}
        }
    }
    public void validateFindTransactionByAccountId(String token, Account debtorAccount){
        final String hardcodedToken =tokenValidation.removeBearer(token);
        User user = debtorAccount.getUser();
        Role roleFromToken = Role.valueOf(jwtService.returnRole(hardcodedToken));
        OrganisationName organisationNameFromToken = OrganisationName.valueOf(jwtService.returnOrganisationName(hardcodedToken));
        UUID userIdFromToken = UUID.fromString(jwtService.returnUserId(hardcodedToken));
        roleHierarchyValidator.hasSameOrganisation(roleFromToken,organisationNameFromToken,user.getOrganisationName());
        sameUser(roleFromToken,userIdFromToken,user.getId());
    }
}
