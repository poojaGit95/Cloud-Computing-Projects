package com.cloudassignment.program5.cloudoperations;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class S3Operation {
    private static final String BUCKET_NAME = "poojancss436program5";
    private static final String S3_OBJECT_NAME = "input.txt";
    private final AmazonS3 s3Client;
    private Bucket s3Bucket;


    public S3Operation() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("xyz**", "123**");
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_WEST_2)
                .build();
        this.s3Bucket = createBucket();
    }

    public void uploadBook(String s3ObjectName, String localFilePath) throws AmazonServiceException, SdkClientException  {
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, s3ObjectName, new File(localFilePath));
        s3Client.putObject(request);
    }

    public String downloadBook(String s3ObjectName) throws AmazonS3Exception, AmazonServiceException, SdkClientException{
        String home = System.getProperty("user.home");
        String path = home+"/Downloads/"+s3ObjectName;
        ObjectMetadata fullObject = s3Client.getObject(new GetObjectRequest(BUCKET_NAME, s3ObjectName), new File(home+"/Downloads/"+s3ObjectName));
        return path;
    }

    public String downloadFileUsingAccessURL(String URL) {
        URI fileToBeDownloaded = null;
        try {
            fileToBeDownloaded = new URI(URL);
            AmazonS3URI s3URI = new AmazonS3URI(fileToBeDownloaded);
            S3Object senderS3ObjectSender = s3Client.getObject(s3URI.getBucket(), s3URI.getKey());
            s3Client.copyObject(senderS3ObjectSender.getBucketName(), senderS3ObjectSender.getKey(), BUCKET_NAME, S3_OBJECT_NAME);

            AccessControlList acl = s3Client.getObjectAcl(BUCKET_NAME, S3_OBJECT_NAME);
            List<Grant> grants = acl.getGrantsAsList();
            for (Grant grant : grants) {
                System.out.format("  %s: %s\n", grant.getGrantee().getIdentifier(),
                        grant.getPermission().toString());
            }
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            s3Client.setObjectAcl(BUCKET_NAME, S3_OBJECT_NAME, acl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return s3Client.getUrl(BUCKET_NAME, S3_OBJECT_NAME).toString();

    }

    public void deleteFileFromS3() {
        try {
            s3Client.deleteObject(BUCKET_NAME, S3_OBJECT_NAME);
        }catch (AmazonServiceException e){
            e.printStackTrace();
        }catch (SdkClientException e){
            e.printStackTrace();
        }
    }

    public List<String> readDataFromS3Object(){
        List<String> s3ObjectData = new ArrayList<>();
        InputStream inputStream = s3Client.getObject(BUCKET_NAME, S3_OBJECT_NAME).getObjectContent();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        try {
            while((line = reader.readLine()) != null){
                    s3ObjectData.add(line);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return s3ObjectData;
    }

    public List<String> readFileData(){
        List<String> s3ObjectData = new ArrayList<>();
        File file = new File("/Users/poojanadagouda/Desktop/input.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null){
                s3ObjectData.add(st);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s3ObjectData;

    }


    private Bucket createBucket() {
        Bucket bucket = null;
        if (s3Client.doesBucketExistV2(BUCKET_NAME)) {
            bucket = getBucket(BUCKET_NAME);
        } else {
            try {
                bucket = s3Client.createBucket(BUCKET_NAME);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return bucket;
    }

    private Bucket getBucket(String bucketName) {
        Bucket bucket = null;
        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucketName)) {
                bucket = b;
            }
        }
        return bucket;
    }

    private String uploadFileAndReturnAccessURL(String s3ObjectName, String localFilePath) {
        try {
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, s3ObjectName, new File(localFilePath));
            request.withAccessControlList(acl);
            s3Client.putObject(request);

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return s3Client.getUrl(BUCKET_NAME, s3ObjectName).toString();
    }

}
