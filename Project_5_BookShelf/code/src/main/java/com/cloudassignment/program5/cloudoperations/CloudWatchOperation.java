package com.cloudassignment.program5.cloudoperations;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;

public class CloudWatchOperation {

    private final AmazonCloudWatch cloudWatchClient;

    public CloudWatchOperation(){
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("xyz**", "123**");
        cloudWatchClient = AmazonCloudWatchClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_WEST_2)
                .build();
    }

    public void publishBookMetrics(String bookTitle, String metricName){
        Dimension dimension = new Dimension()
                .withName("TITLE")
                .withValue(bookTitle);
        MetricDatum datum = new MetricDatum()
                .withMetricName(metricName)
                .withUnit(StandardUnit.None)
                .withValue(1D)
                .withDimensions(dimension);
        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("SITE/TRAFFIC")
                .withMetricData(datum);
        PutMetricDataResult response = cloudWatchClient.putMetricData(request);
    }

    public void publishSubscriberMetrics(String subscriber, String metricName){
        Dimension dimension = new Dimension()
                .withName("EMAIL")
                .withValue(subscriber);
        MetricDatum datum = new MetricDatum()
                .withMetricName(metricName)
                .withUnit(StandardUnit.None)
                .withValue(1D)
                .withDimensions(dimension);
        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("SITE/TRAFFIC")
                .withMetricData(datum);
        PutMetricDataResult response = cloudWatchClient.putMetricData(request);
    }
}
