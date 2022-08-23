package com.cloudassignment.program5.restclient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewYorkTimesRestClient {

    private static final String API_KEY = "**123";

    private static final String URL = "https://api.nytimes.com/svc/books/v3/lists/current/Education?api-key=";

    private HttpClient client;

    public NewYorkTimesRestClient(){

        this.client = HttpClient.newHttpClient();
    }

    public Map<String, ArrayList<String>> getTopFiveNYTEducationBooksToString(){
        Map<String, ArrayList<String>> topBooks = getTopFiveNYTEducationBooksMap();
        String topBooksList="";
        for (String book: topBooks.keySet()) {
            String bookName = book;
            String bookAuthor = topBooks.get(book).get(0);
            String bookDescription = topBooks.get(book).get(1);
            topBooksList = topBooksList + "Book: " + bookName +
                    "\nAuthor: " + bookAuthor +
                    "\nBook Description: " + bookDescription;
        }
        return topBooks;
    }

    private Map<String, ArrayList<String>> getTopFiveNYTEducationBooksMap(){
        Map<String, ArrayList<String>> topBooks = null;
        try {
            String response = getHttpResponse();
            topBooks = parseHttpResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return topBooks;
    }

    private Map<String, ArrayList<String>> parseHttpResponse(String response){
        ArrayList<String> bookDetails;
        Map<String, ArrayList<String>> topBooks = new HashMap();
        JSONObject jsonObject = new JSONObject(response);
        JSONObject results = (JSONObject) jsonObject.get("results");
        JSONArray booksArray = results.getJSONArray("books");
        for (int i=0; i<5; i++){
            bookDetails = new ArrayList<>();
            JSONObject book = booksArray.getJSONObject(i);
            String bookName = book.get("title").toString();
            String bookAuthor = book.get("author").toString();
            String bookDescription = book.get("description").toString();
            bookDetails.add(bookAuthor);
            bookDetails.add(bookDescription);
            topBooks.put(bookName, bookDetails);
        }
        return topBooks;
    }

    private String getHttpResponse() throws IOException, InterruptedException  {
        String defaultValue = "";
        HttpRequest request=null;
        request = HttpRequest.newBuilder().uri(URI.create(URL +API_KEY))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()>=500){
                 //Retry logic
        }
        return response.body();
    }

}
