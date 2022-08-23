package com.cloudassignment.program4.queryparser;

import com.cloudassignment.program4.cloudoperations.DynamoDBOperation;

import java.util.ArrayList;
import java.util.List;

public class QueryParser {

        public List<String> prepareQuery(DynamoDBOperation dynamDBOperation, String firstName, String lastName){
            List<String> results;
            if(firstName!=null && lastName!=null && !firstName.isEmpty() && !lastName.isEmpty()){
                results = dynamDBOperation.queryTableUsingFirstLastName(firstName, lastName);

            }else if(firstName!=null && !firstName.isEmpty()){
                results = dynamDBOperation.queryTableUsingFirstNameOnly(firstName);

            }else if(lastName!=null && !lastName.isEmpty()){
                results = dynamDBOperation.queryTableUsingLastNameOnly(lastName);

            }else{
                results = new ArrayList<>();
                results.add("Enter either first name, last name or both");
            }
            return results;
        }


}
