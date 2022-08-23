package com.cloudassignment.program5;

import com.cloudassignment.program5.cloudoperations.CloudWatchOperation;
import com.cloudassignment.program5.cloudoperations.DynamoDBOperation;
import com.cloudassignment.program5.cloudoperations.S3Operation;
import com.cloudassignment.program5.cloudoperations.SNSOperation;
import com.cloudassignment.program5.controller.WelcomeController;
import com.cloudassignment.program5.queryparser.QueryParser;
import com.cloudassignment.program5.restclient.NewYorkTimesRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public S3Operation s3Operation() {
        return new S3Operation();
    }

    @Bean
    public DynamoDBOperation dynamoDBOperation() {
        return new DynamoDBOperation();
    }

    @Bean
    public SNSOperation snsOperation() {
        return new SNSOperation();
    }

    @Bean
    public QueryParser queryParser(){
        return new QueryParser();
    }

    @Bean
    public NewYorkTimesRestClient newYorkTimesRestClient(){
        return new NewYorkTimesRestClient();
    }

    @Bean
    public CloudWatchOperation cloudWatchOperation() {
        return new CloudWatchOperation();
    }
}
