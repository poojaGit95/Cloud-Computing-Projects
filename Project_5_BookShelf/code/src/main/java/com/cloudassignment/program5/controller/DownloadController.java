package com.cloudassignment.program5.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.cloudassignment.program5.cloudoperations.CloudWatchOperation;
import com.cloudassignment.program5.cloudoperations.DynamoDBOperation;
import com.cloudassignment.program5.cloudoperations.S3Operation;
import com.cloudassignment.program5.cloudoperations.SNSOperation;
import com.cloudassignment.program5.entity.Book;
import com.cloudassignment.program5.queryparser.QueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class DownloadController {

    private S3Operation s3Operation;
    private DynamoDBOperation dynamoDBOperation;
    private SNSOperation snsOperation;
    private QueryParser queryParser;
    private CloudWatchOperation cloudWatchOperation;

    @Autowired
    public DownloadController(S3Operation s3Operation, DynamoDBOperation dynamoDBOperation, SNSOperation snsOperation, QueryParser queryParser, CloudWatchOperation cloudWatchOperation){
        this.s3Operation = s3Operation;
        this.dynamoDBOperation = dynamoDBOperation;
        this.snsOperation = snsOperation;
        this.queryParser = queryParser;
        this.cloudWatchOperation = cloudWatchOperation;
    }

    @RequestMapping(value="/download", method= RequestMethod.GET)
    public String downloadGet() {
        return "download";
    }

    @RequestMapping(value="/download", method= RequestMethod.POST, params = "download")
    public ResponseEntity download(ModelMap model, @RequestParam String bookTitleD) {
        String message = "";
        if(bookTitleD.isEmpty()){
            message = "Please enter the Book Title";
        }else{
            Book result = queryParser.isBookAvailableInShelf(dynamoDBOperation, bookTitleD);
            if (result!=null){
                try{
                    String bookFileName = result.getBookFileName();
                    String path = s3Operation.downloadBook(bookFileName);
                    model.put("downloadMsg", "Book downloaded successfully to your downloads folder");
                    cloudWatchOperation.publishBookMetrics(bookTitleD, "DOWNLOADS");
                    cloudWatchOperation.publishBookMetrics(bookTitleD, "QUERY.SUCCESSFUL");
                    return downloadFile(path);
                }catch (AmazonS3Exception e){
                    message = e.getErrorMessage();
                }catch (AmazonServiceException e){
                    message = e.getErrorMessage();
                }catch (SdkClientException e){
                    message = e.getLocalizedMessage();
                }
            }else{
                message = "Book unavailable, use SUBSCRIBE on main page to subscribe to get notified when book becomes available";
                cloudWatchOperation.publishBookMetrics(bookTitleD, "QUERY.UNSUCCESSFUL");
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    public ResponseEntity<Resource> downloadFile(String fileName) {
        // Load file as Resource
        Resource resource;
        Path path = Paths.get(fileName);
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        String contentType = "application/octet-stream";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
