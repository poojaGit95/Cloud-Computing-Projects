package com.cloudassignment.program5.controller;

import com.cloudassignment.program5.restclient.NewYorkTimesRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;

@Controller
public class RestServiceController {

    private NewYorkTimesRestClient newYorkTimesRestClient;
    @Autowired
    public RestServiceController(NewYorkTimesRestClient newYorkTimesRestClient){
        this.newYorkTimesRestClient = newYorkTimesRestClient;
    }

    @RequestMapping(value="/topbooks", method= RequestMethod.GET)
    public String restServiceGet() {
        return "topbooks";
    }

    @RequestMapping(value="/topbooks", method= RequestMethod.POST, params = "topbooks")
    public String topbooks(ModelMap model){
        Map<String, ArrayList<String>> booksList = newYorkTimesRestClient.getTopFiveNYTEducationBooksToString();
        model.put("topbooksMsg", booksList);
        return "topbooks";
    }
}
