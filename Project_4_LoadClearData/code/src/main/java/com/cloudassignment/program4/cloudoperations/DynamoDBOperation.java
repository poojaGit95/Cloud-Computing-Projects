package com.cloudassignment.program4.cloudoperations;

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
import com.cloudassignment.program4.entity.Person;

import java.util.*;

public class DynamoDBOperation {

    private static final String DYNAMO_DB_TABLE_NAME = "PERSON_DETAILS";

    private final AmazonDynamoDB dynamoDBClient;

    private DynamoDB dynamoDB;

    public DynamoDBOperation(){
        System.out.println("Intializing DDB");
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("accesskeyid", "secretkey");
        this.dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_WEST_2)
                .build();
        dynamoDB = new DynamoDB(dynamoDBClient);
        System.out.println("Intialized DDB");

    }

    public void createTable(){
        System.out.println("Creating Table");
        List<AttributeDefinition> attributes = Arrays.asList(
                new AttributeDefinition("FirstName", ScalarAttributeType.S),
                new AttributeDefinition("LastName", ScalarAttributeType.S));

        List<KeySchemaElement> keySchema = Arrays.asList(
                        new KeySchemaElement("FirstName", KeyType.HASH),
                        new KeySchemaElement("LastName", KeyType.RANGE));

        GlobalSecondaryIndex gsi = new GlobalSecondaryIndex()
                .withIndexName("LastName")
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L))
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL));

        ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();

        indexKeySchema.add(new KeySchemaElement()
                .withAttributeName("LastName")
                .withKeyType(KeyType.HASH));
        gsi.setKeySchema(indexKeySchema);
        try{
            CreateTableRequest tableRequest = new CreateTableRequest()
                    .withTableName(DYNAMO_DB_TABLE_NAME)
                    .withAttributeDefinitions(attributes)
                    .withKeySchema(keySchema)
                    .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
                    .withGlobalSecondaryIndexes(gsi);
            Table dbTable = dynamoDB.createTable(tableRequest);
            dbTable.waitForActive();
        } catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }
        System.out.println("Created Table");
    }

    public void insertEntriesIntoTable(List<Person> persons){
        Table dbTable = dynamoDB.getTable(DYNAMO_DB_TABLE_NAME);
        List<Item> tableItems  = personToTableItems(persons);
        TableWriteItems tableWriteItems = new TableWriteItems(DYNAMO_DB_TABLE_NAME);
        tableWriteItems.withItemsToPut(tableItems);
        BatchWriteItemSpec batchWriteItemSpec = new BatchWriteItemSpec();
        batchWriteItemSpec.withTableWriteItems(tableWriteItems);
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(batchWriteItemSpec);
        do {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            if (outcome.getUnprocessedItems().size() == 0) {
                System.out.println("No unprocessed items found");
            }
            else {
                outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            }
        } while (outcome.getUnprocessedItems().size() > 0);
    }

    private List<Item> personToTableItems(List<Person> persons){
        List<Item> items = new ArrayList<>();
        for (Person p: persons) {
            Item item = new Item().withPrimaryKey("FirstName", p.getFirstName())
                    .withString("LastName", p.getLastName()).withJSON("Details", p.getPersonAttributes().toString());
            items.add(item);
        }
        return items;
    }


    public String deleteTable() throws ResourceNotFoundException{
        Table dbTable = dynamoDB.getTable(DYNAMO_DB_TABLE_NAME);
        DeleteTableResult deleteTableResult = dynamoDBClient.deleteTable(DYNAMO_DB_TABLE_NAME);
        return deleteTableResult.getTableDescription().getTableStatus();
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

    public List<String> queryTableUsingLastNameOnly(String lastName) throws ResourceNotFoundException{
        List<String> queryResults = new ArrayList<>();
        Table table = dynamoDB.getTable(DYNAMO_DB_TABLE_NAME);
        Index index = table.getIndex("LastName");

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("LastName = :lname")
                .withValueMap(new ValueMap()
                        .withString(":lname",lastName));

        ItemCollection<QueryOutcome> items = index.query(spec);
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            String result = iterator.next().toJSONPretty();
            queryResults.add(result);
        }
        return queryResults;
    }

    public List<String> queryTableUsingFirstLastName(String firstName, String lastName) throws ResourceNotFoundException{
        List<String> queryResults = new ArrayList<>();
        Table table = dynamoDB.getTable(DYNAMO_DB_TABLE_NAME);
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("FirstName = :fname and LastName= :lname")
                .withValueMap(new ValueMap()
                        .withString(":fname", firstName).withString(":lname", lastName))
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
