package com.cloudassignment.program4.dataparser;

import com.cloudassignment.program4.entity.Person;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataParser {

    public List<Person> s3DataListToPersonObject(List<String> data){
        List<Person> persons = new ArrayList<>();
        for (String line: data) {
            String[] pData = line.trim().split("\\s+");
            Person p = new Person();
            p.setFirstName(pData[1]);
            p.setLastName(pData[0]);
            JSONObject pAttributes = new JSONObject();
            for(int i=2; i<pData.length; i++){
                String[] jData = pData[i].split("=");
                pAttributes.put(jData[0], jData[1]);
            }
            p.setPersonAttributes(pAttributes);
            persons.add(p);
        }
        return persons;
    }

    public List<Person> dbDataListToPersonObject(List<String> queryResults){
        List<Person> persons = new ArrayList<>();
        for (String result : queryResults) {
            JSONObject personJsonObj = new JSONObject(result);
            String fn = personJsonObj.get("FirstName").toString();
            String ln = personJsonObj.get("LastName").toString();
            JSONObject dt = new JSONObject(personJsonObj.get("Details").toString());
            Person person = new Person();
            person.setFirstName(fn);
            person.setLastName(ln);
            person.setPersonAttributes(dt);
            persons.add(person);
        }
        return persons;
    }
}
