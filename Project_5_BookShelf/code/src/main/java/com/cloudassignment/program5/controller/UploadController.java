package com.cloudassignment.program5.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.cloudassignment.program5.cloudoperations.CloudWatchOperation;
import com.cloudassignment.program5.cloudoperations.DynamoDBOperation;
import com.cloudassignment.program5.cloudoperations.S3Operation;
import com.cloudassignment.program5.cloudoperations.SNSOperation;
import com.cloudassignment.program5.entity.Book;
import com.cloudassignment.program5.queryparser.QueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {
    private S3Operation s3Operation;
    private DynamoDBOperation dynamoDBOperation;
    private SNSOperation snsOperation;
    private QueryParser queryParser;
    private CloudWatchOperation cloudWatchOperation;

    private static String UPLOADED_FOLDER = "/tmp/";

    @Autowired
    public UploadController(S3Operation s3Operation, DynamoDBOperation dynamoDBOperation, SNSOperation snsOperation, QueryParser queryParser, CloudWatchOperation cloudWatchOperation) {
        this.s3Operation = s3Operation;
        this.dynamoDBOperation = dynamoDBOperation;
        this.snsOperation = snsOperation;
        this.queryParser = queryParser;
        this.cloudWatchOperation = cloudWatchOperation;
    }

    @RequestMapping(value="/upload", method= RequestMethod.GET)
    public String uploadGet() {
        return "upload";
    }

    public String uploadOldCode(ModelMap model, @RequestParam String bookTitleU, @RequestParam String localFilePath) {
        if (bookTitleU.isEmpty() || localFilePath.isEmpty()){
            model.put("uploadErrorMsg", "Enter Book title and file location for upload");
            return "upload";
        }
        String bookFilePath[] = localFilePath.split("/");
        String bookFileName = bookFilePath[bookFilePath.length-1];
        Book bookToBeUploaded = new Book(bookTitleU, bookFileName);
        Book result = queryParser.isBookAvailableInShelf(dynamoDBOperation, bookTitleU);
        if (result!=null){
            model.put("uploadErrorMsg", "Book is available in shelf");
            cloudWatchOperation.publishBookMetrics(bookTitleU, "QUERY.SUCCESSFUL");
            return "upload";
        }
        try{
            s3Operation.uploadBook(bookFileName, localFilePath);
            dynamoDBOperation.insertBookEntryIntoTable(bookToBeUploaded);
            snsOperation.sendMessageToSubscribers(bookTitleU);
            model.put("uploadMsg", "Book uploaded successfully");
            cloudWatchOperation.publishBookMetrics(bookTitleU, "UPLOADS");
        }catch (AmazonServiceException e){
            model.put("uploadErrorMsg", e.getErrorMessage());
        }catch (SdkClientException e){
            model.put("uploadErrorMsg", e.getLocalizedMessage());
        }
        return "upload";
    }

    @RequestMapping(value="/upload", method= RequestMethod.POST, params = "upload")
    public String singleFileUpload(ModelMap model, @RequestParam String bookTitleU, @RequestParam("file") MultipartFile file) {

        if (bookTitleU.isEmpty()){
            model.put("uploadErrorMsg", "Enter Book title for upload");
            return "upload";
        }

        Path path = null;
        if (file.isEmpty()) {
            model.put("uploadErrorMsg", "Choose a file for upload");
            return "upload";
        }

        String bookFileName = file.getOriginalFilename();
        Book bookToBeUploaded = new Book(bookTitleU, bookFileName);
        Book result = queryParser.isBookAvailableInShelf(dynamoDBOperation, bookTitleU);
        if (result!=null){
            model.put("uploadErrorMsg", "Book is available in shelf");
            cloudWatchOperation.publishBookMetrics(bookTitleU, "QUERY.SUCCESSFUL");
            return "upload";
        }

        try {
            byte[] bytes = file.getBytes();
            path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            s3Operation.uploadBook(bookFileName, path.toString());
            dynamoDBOperation.insertBookEntryIntoTable(bookToBeUploaded);
            snsOperation.sendMessageToSubscribers(bookTitleU);
            model.put("uploadMsg", "Book uploaded successfully");
            cloudWatchOperation.publishBookMetrics(bookTitleU, "UPLOADS");
            model.put("uploadMsg", "Book uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }catch (AmazonServiceException e){
            model.put("uploadErrorMsg", e.getErrorMessage());
        }catch (SdkClientException e){
            model.put("uploadErrorMsg", e.getLocalizedMessage());
        }

        return "upload";
    }

}
