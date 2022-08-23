package com.cloudassignment.program5.cloudoperations;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.BatchWriteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import com.cloudassignment.program5.entity.Book;

import java.util.*;

public class DynamoDBOperation {

    private static final String DYNAMO_DB_TABLE_NAME = "BOOK_DETAILS";

    private final AmazonDynamoDB dynamoDBClient;

    private DynamoDB dynamoDB;

    public DynamoDBOperation(){
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("xyz**", "123**");

        this.dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_WEST_2)
                .build();

        dynamoDB = new DynamoDB(dynamoDBClient);
        createTable();
    }

    public void createTable(){
        List<AttributeDefinition> attributes = Arrays.asList(
                new AttributeDefinition("BookTitle", ScalarAttributeType.S));
        List<KeySchemaElement> keySchema = Arrays.asList(
                        new KeySchemaElement("BookTitle", KeyType.HASH));
        try{
            CreateTableRequest tableRequest = new CreateTableRequest()
                    .withTableName(DYNAMO_DB_TABLE_NAME)
                    .withAttributeDefinitions(attributes)
                    .withKeySchema(keySchema)
                    .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L));
            Table dbTable = dynamoDB.createTable(tableRequest);
            dbTable.waitForActive();
        } catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }
    }

    public void insertBookEntryIntoTable(Book book){
        Table dbTable = dynamoDB.getTable(DYNAMO_DB_TABLE_NAME);
        Item item = new Item().withPrimaryKey("BookTitle",book.getBookTitle())
                .withString("BookFileName", book.getBookFileName());
        PutItemOutcome outcome = dbTable.putItem(item);
        System.out.println(outcome.getPutItemResult().toString());
    }

    private List<Item> bookToTableItems(List<Book> books){
        List<Item> items = new ArrayList<>();
        for (Book book: books) {
            Item item = new Item().withPrimaryKey("BookTitle",book.getBookTitle())
                    .withString("BookFileName", book.getBookFileName());
            items.add(item);
        }
        return items;
    }

    public String deleteTable() throws ResourceNotFoundException{
        Table dbTable = dynamoDB.getTable(DYNAMO_DB_TABLE_NAME);
        DeleteTableResult deleteTableResult = dynamoDBClient.deleteTable(DYNAMO_DB_TABLE_NAME);
        return deleteTableResult.getTableDescription().getTableStatus();
    }

    public Item queryTableUsingBookTitle(String bookTitleName) throws ResourceNotFoundException {
        Table dbTable = dynamoDB.getTable(DYNAMO_DB_TABLE_NAME);
        Item item = dbTable.getItem("BookTitle", bookTitleName);
        return item;
    }

    public List<String> queryTableUsingFirstNameOnly(String firstName) throws ResourceNotFoundException {
        List<String> queryResults = new ArrayList<>();
        Table table = dynamoDB.getTable(DYNAMO_DB_TABLE_NAME);
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("FirstName = :fname")
                .withValueMap(new ValueMap()
                        .withString(":fname", firstName))
                .withConsistentRead(true);

        ItemCollection<QueryOutcome> items = table.query(spec);
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            String result = iterator.next().toJSONPretty();
            queryResults.add(result);
        }
        return queryResults;
    }

}
