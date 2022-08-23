package com.cloudassignment.program4.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.cloudassignment.program4.cloudoperations.DynamoDBOperation;
import com.cloudassignment.program4.cloudoperations.S3Operation;
import com.cloudassignment.program4.dataparser.DataParser;
import com.cloudassignment.program4.entity.Person;
import com.cloudassignment.program4.queryparser.QueryParser;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CloudOperationController {

    private static S3Operation s3Operation;

    private static DynamoDBOperation dynamDBOperation;

    private static DataParser dataParser;

    private static QueryParser queryParser;

    public void initializeCloudResources(){
        s3Operation = new S3Operation();
        dynamDBOperation = new DynamoDBOperation();
        dataParser = new DataParser();
        queryParser = new QueryParser();
    }

    @RequestMapping(value="/index", method= RequestMethod.GET)
    public String cloudLoadOperationGet() {
        System.out.println("Processing Initial Request");
        initializeCloudResources();
        System.out.println("Processed Initial Request");
        return "index";
    }

    @RequestMapping(value="/index", method= RequestMethod.POST, params = "load")
    public String load(ModelMap model) {
        System.out.println("Processing Load Request");
        try{
            String url = s3Operation.downloadFileUsingAccessURL("https://s3-us-west-2.amazonaws.com/css490/input.txt");
            System.out.println(url);
            List<String> data = s3Operation.readDataFromS3Object();
            List<Person> personObjects = dataParser.s3DataListToPersonObject(data);
            dynamDBOperation.createTable();
            dynamDBOperation.insertEntriesIntoTable(personObjects);
            model.put("detailsMsg", "Data from s3 loaded to Dynamo DB successfully");
        }catch (Exception e){
            model.put("detailsMsg", e.getMessage().toString());
        }
        System.out.println("Processed Load Request");
        return "index";
    }

    @RequestMapping(value="/index", method= RequestMethod.POST, params = "clear")
    public String clear(ModelMap model) {
        System.out.println("Processing Clear Request");
        try {
            s3Operation.deleteFileFromS3();
            String status = dynamDBOperation.deleteTable();
            model.put("detailsMsg", "Data from s3 and Dynamo DB cleared successfully");

        }catch(ResourceNotFoundException e){
            model.put("detailsMsg", "Data already cleared, no more data available to clear");
        }catch(AmazonServiceException e){
            model.put("detailsMsg", e.getErrorMessage());
        }catch(SdkClientException e){
            model.put("detailsMsg", e.getMessage());
        }catch (Exception e){
            model.put("detailsMsg", e.getMessage());
        }
        System.out.println("Processed Clear Request");
        return "index";
    }

    @RequestMapping(value="/index", method= RequestMethod.POST, params = "query")
    public String query(ModelMap model, @RequestParam String firstName, @RequestParam String lastName) {
        System.out.println("Processing Query Request");
        List<String> details = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        try{
            if(firstName.isEmpty() && lastName.isEmpty()){
                model.put("detailsMsg", "Please enter first name or last name or both");
                return "index";
            }
            details = queryParser.prepareQuery(dynamDBOperation, firstName, lastName);
            persons = dataParser.dbDataListToPersonObject(details);

            if(persons.isEmpty()){
                model.put("detailsMsg", "No matches found for the details entered");
            }else{
                model.put("detailsMsg", persons.toString());
            }
        }catch (ResourceNotFoundException e){
            model.put("detailsMsg", "No data available, Please load the data before querying using \'LOAD\'  button");
        }catch (Exception e){
            model.put("detailsMsg", e.getMessage());
        }
        System.out.println("Processed Query Request");
        return "index";
    }

}