package com.example.bankingapp.util;

import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.Transaction;
import com.example.bankingapp.entity.User;
import lombok.Locked;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Component
public class CsvFileGenerator {
    public void writeUserToCsv(List<User> userList, Writer writer) {
        try {

            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for (User users : userList) {
                printer.printRecord(users.getId(), users.getRole()
                        , users.getOrganisationName(),
                        users.getAge(),users.getEmail()
                        , users.getPassword()
                        ,users.getFirstName() ,
                        users.getLastName()
                        ,users.getCreationTimeStamp());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}