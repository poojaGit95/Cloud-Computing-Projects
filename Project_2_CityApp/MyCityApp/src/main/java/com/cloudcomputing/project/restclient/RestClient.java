package com.cloudcomputing.project.restclient;

import com.cloudcomputing.project.exceptions.InvalidResourceException;
import com.cloudcomputing.project.exceptions.ServerNotActiveException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


public class RestClient {

    private HttpClient client;
    int retryCount;

    public RestClient(){
        this.client = HttpClient.newHttpClient();
        this.retryCount = 0;
    }

    public String getHttpResponse(String url, Optional<String> apiKey, Optional<String> apiToken) throws IOException, InterruptedException, InvalidResourceException, ServerNotActiveException {
        String defaultValue = "";
        String apiKeyValue = apiKey.orElse(defaultValue);
        String apiTokenValue = apiToken.orElse(defaultValue);
        HttpRequest request=null;

        if(apiKeyValue.isEmpty() && apiTokenValue.isEmpty()){
            request = HttpRequest.newBuilder().uri(URI.create(url))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder().uri(URI.create(url))
                    .GET().setHeader(apiKeyValue, apiTokenValue)
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if(response.statusCode()>=400 && response.statusCode()<500){
            JSONObject jsonObject = new JSONObject(response.body());
            if (jsonObject.has("error")){
                throw new InvalidResourceException(jsonObject.get("error").toString());
            }
            if (jsonObject.has("message")){
                throw new InvalidResourceException(jsonObject.get("message").toString());
            }
        } else if (response.statusCode()>=500){
            expontentialBackOff(url, apiKey, apiToken);
            throw new ServerNotActiveException("Server not available, try later");
        }
        return response.body();
    }

    private void expontentialBackOff(String url, Optional<String> apiKey, Optional<String> apiToken) throws InterruptedException, IOException, InvalidResourceException, ServerNotActiveException {
        if(retryCount==0){
            Thread.sleep(retryCount);
            retryCount = 2;
            getHttpResponse(url, apiKey, apiToken);
        } else if(retryCount == 2) {
            Thread.sleep(retryCount);
            retryCount = retryCount*2;
            getHttpResponse(url, apiKey, apiToken);
        }else if(retryCount == 4) {
            Thread.sleep(retryCount);
            retryCount = retryCount*2;
            getHttpResponse(url, apiKey, apiToken);
        }else if(retryCount == 8) {
            Thread.sleep(retryCount);
            retryCount = retryCount*2;
            getHttpResponse(url, apiKey, apiToken);
        }else{
            retryCount=0;
            return;
        }
    }

}
