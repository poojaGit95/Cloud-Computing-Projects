package com.cloudassignment.program4.entity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Person {

    String firstName;
    String lastName;
    JSONObject personAttributes;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public JSONObject getPersonAttributes() {
        return personAttributes;
    }

    public void setPersonAttributes(JSONObject personAttributes) {
        this.personAttributes = personAttributes;
    }

    public Map<String, String> getAllAttributes() {
        Map<String, String> personAttributes = new HashMap();
        JSONObject attributes = getPersonAttributes();
        for (String key : attributes.keySet()) {
            String value = attributes.get(key).toString();
            personAttributes.put(key, value);
        }
        return personAttributes;
    }


    @Override
    public String toString() {
        return "Name: " + getFirstName() + "\t" +
                  getLastName() + "\t" +
                  ", Details: " + getAllAttributes() ;
    }
}
