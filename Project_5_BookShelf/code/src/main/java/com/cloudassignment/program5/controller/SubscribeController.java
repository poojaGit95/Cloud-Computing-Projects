package com.cloudassignment.program5.controller;

import com.amazonaws.services.sns.model.InvalidParameterException;
import com.cloudassignment.program5.cloudoperations.CloudWatchOperation;
import com.cloudassignment.program5.cloudoperations.SNSOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SubscribeController {

    private SNSOperation snsOperation;
    private CloudWatchOperation cloudWatchOperation;

    @Autowired
    public SubscribeController(SNSOperation snsOperation, CloudWatchOperation cloudWatchOperation){
        this.snsOperation = snsOperation;
        this.cloudWatchOperation = cloudWatchOperation;
    }

    @RequestMapping(value="/subscribe", method= RequestMethod.GET)
    public String subscribeGet() {
        return "subscribe";
    }

    @RequestMapping(value="/subscribe", method= RequestMethod.POST, params = "subscribe")
    public String subscribe(ModelMap model, @RequestParam String email){
        if (email.isEmpty()){
            model.put("subscribeErrorMsg", "Please enter a valid Email ID");
            return "subscribe";
        }
        try{
            snsOperation.subscriptionRequest(email);
            model.put("subscribeMsg", "A message has been sent to the Email ID provided, please check your inbox and click on subscribe in the mail to opt for subscriptions");
            cloudWatchOperation.publishSubscriberMetrics( email, "SUBSCRIBERS");
        }catch(InvalidParameterException e){
            model.put("subscribeErrorMsg", "Please enter a valid Email ID");
        }
        return "subscribe";
    }
}
