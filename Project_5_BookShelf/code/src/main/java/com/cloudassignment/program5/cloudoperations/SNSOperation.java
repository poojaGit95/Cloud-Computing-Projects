package com.cloudassignment.program5.cloudoperations;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.SubscribeRequest;

import java.util.Locale;

public class SNSOperation {

    private final AmazonSNSClient snsClient;

    private static final String TOPIC_NAME = "BookShelf";

    private String topic_arn;

    private static final String EMAIL_SUBJECT = "New Book Available";

    private String EMAIL_MESSAGE = "Hello Subscriber, \n\nNew book \'%s\' is available. Please visit the website to download the book. \n\nBest regards \nBookShelf";

    public SNSOperation(){
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("xyz**", "123**");
        snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_WEST_2)
                .build();
        createTopic();
    }

    private void createTopic(){
        CreateTopicRequest topicRequest = new CreateTopicRequest(TOPIC_NAME);
        CreateTopicResult topicResponse = snsClient.createTopic(topicRequest);
        this.topic_arn = topicResponse.getTopicArn();

    }

    public void subscriptionRequest(String endpoint) throws InvalidParameterException {
        SubscribeRequest subscribeRequest = new SubscribeRequest(topic_arn, "email", endpoint);
        snsClient.subscribe(subscribeRequest);
    }

    public void sendMessageToSubscribers(String bookTitle){
        String message = String.format(EMAIL_MESSAGE, bookTitle.toUpperCase());
        snsClient.publish(topic_arn, message, EMAIL_SUBJECT);
    }


}
