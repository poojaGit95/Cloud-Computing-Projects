package com.cloudassignment.program5.controller;

import com.cloudassignment.program5.cloudoperations.CloudWatchOperation;
import com.cloudassignment.program5.cloudoperations.DynamoDBOperation;
import com.cloudassignment.program5.cloudoperations.S3Operation;
import com.cloudassignment.program5.cloudoperations.SNSOperation;
import com.cloudassignment.program5.dataparser.DataParser;
import com.cloudassignment.program5.queryparser.QueryParser;
import com.cloudassignment.program5.restclient.NewYorkTimesRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {

    private DownloadController dc;
    private UploadController uc;
    private SubscribeController sc;
    private RestServiceController rsc;
    private S3Operation s3Operation;
    private DynamoDBOperation dynamoDBOperation;
    private SNSOperation snsOperation;
    private CloudWatchOperation cloudWatchOperation;
    private NewYorkTimesRestClient newYorkTimesRestClient;
    private  QueryParser queryParser;

    @Autowired
    public WelcomeController(){
        this.dc = new DownloadController(s3Operation, dynamoDBOperation, snsOperation, queryParser, cloudWatchOperation);
        this.uc = new UploadController(s3Operation, dynamoDBOperation, snsOperation, queryParser, cloudWatchOperation);
        this.sc = new SubscribeController(snsOperation, cloudWatchOperation);
        this.rsc = new RestServiceController(newYorkTimesRestClient);
    }

    @RequestMapping(value="/welcome", method= RequestMethod.GET)
    public String welcomeOperationGet() {
        new WelcomeController();
        return "welcome";
    }

    @RequestMapping(value="/download", method= RequestMethod.POST, params = "downloadWelcomePage")
    public String download(){
        return dc.downloadGet();
    }

    @RequestMapping(value="/upload", method= RequestMethod.POST, params = "uploadWelcomePage")
    public String upload(){
        return uc.uploadGet();
    }

    @RequestMapping(value="/subscribe", method= RequestMethod.POST, params = "subscribeWelcomePage")
    public String subscribe(){
        return sc.subscribeGet();
    }

    @RequestMapping(value="/topbooks", method= RequestMethod.POST, params = "top5booksWelcomePage")
    public String TopBooks(){
        return "topbooks";
    }

}
