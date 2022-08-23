import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * This class crawls through the html response from start url to hop to next url
 * till hop count is reached or no more urls found.
 *
 * @author Pooja Nadagouda
 *
 */
public class WebCrawler {

    private List<String> hopUrls;

    private List<String> allVisitedUrls;

    private String startUrl;

    private int hopsCount;

    private int retryCount = -1;

    /***
     * class constructor.
     * Initialises the start url, hop count and list to store visited urls.
     * @param startUrl - start point url from where the crawl begins.
     * @param hopsCount - number of hops required.
     */
    public WebCrawler(String startUrl, int hopsCount) {
        this.startUrl = startUrl;
        this.hopsCount = hopsCount;
        this.hopUrls = new ArrayList<String>();
        this.allVisitedUrls = new ArrayList<String>();
    }

    /***
     * This method gets the http response from the passed url.
     * @param stringUrl - link to be connected
     */
    private void getHttpResponse(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int statusCode = connection.getResponseCode();
            statusCodeActions(statusCode, stringUrl, connection);
        } catch (MalformedURLException e) {
            System.out.println("Enter well formed valid url");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.out.println("Enter well formed valid url");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method performs actions based on the http status code obtained.
     * @param statusCode - http status code obtained.
     * @param url - url to which http connection is established.
     * @param connection - http connection
     */
    private void statusCodeActions(int statusCode, String url, HttpURLConnection connection) {
        try {
            if (statusCode >= 200 && statusCode < 300) {
                if (hopsCount > 0) {
                    hopUrls.add(url);
                    allVisitedUrls.add(url);
                    System.out.println("Hop " + (hopUrls.size() - 1) + ": " + url);
                    hopsCount--;
                    InputStream content = (InputStream) connection.getInputStream();
                    getNextHopUrl(content);
                } else {
                    System.exit(0);
                }
            } else if (statusCode >= 300 && statusCode < 400) {
                allVisitedUrls.add(url);
                System.out.println("Got code " + statusCode + " from " + url + " ,Going to redirected Url");
                String redirectedUrl = connection.getHeaderField("Location");
                if (null == redirectedUrl || allVisitedUrls.contains(redirectedUrl))
                    return;
                System.out.println("Redirected url: " + redirectedUrl);
                getHttpResponse(redirectedUrl);
            } else if (statusCode >= 400 && statusCode < 500) {
                allVisitedUrls.add(url);
                hopUrls.add(url);
                hopsCount--;
                System.out.println("Hop " + (hopUrls.size()-1) +": " + "Got code " + statusCode + " from " + url + " ,Going to next Url");
                return;
            } else if (statusCode >= 500) {
                this.retryCount++;
                retryFor500Code(url, retryCount);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retries connection to url after wait when 500 status code is returned.
     * @param url   - url to which http connection is established.
     * @param count - number of times retry is called.
     */
    private void retryFor500Code(String url, int count) {
        try {
            if (count <= 2) {
                Thread.sleep(count * 1000);
                getHttpResponse(url);
            } else {
                allVisitedUrls.add(url);
                hopUrls.add(url);
                hopsCount--;
                System.out.println("Hop " + (hopUrls.size()-1) +": " + "Got code 500" + " even after retrys from " + url + " ,Going to next Url");
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method parses and fetches next url from the html response.
     * @param content - html response content
     */
    private void getNextHopUrl(InputStream content) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        String line;
        if (hopsCount <= 0) {
            System.exit(0);
        }
        try {
            while ((line = reader.readLine()) != null) {
                List<String> links = getUrls(line);
                if (links != null && !links.isEmpty()) {
                    for (String url : links) {
                        String hopUrlWithoutSlash;
                        String hopUrlWithSlash;
                        if (!url.startsWith("http") && !url.startsWith("https"))
                            continue;
                        if (url.substring(url.length() - 1).equals("/")) {
                            hopUrlWithoutSlash = url.substring(0, url.length() - 1);
                            hopUrlWithSlash = url;
                        } else {
                            hopUrlWithSlash = url + "/";
                            hopUrlWithoutSlash = url;
                        }
                        if (!allVisitedUrls.contains(hopUrlWithSlash) && !allVisitedUrls.contains(hopUrlWithoutSlash)) {
                            getHttpResponse(url);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * This class returns list of matches, matching the regular expression pattern
     * in the input string.
     * @param regexString - regular expression string
     * @param string - input string from which pattern to be fetched
     * @return - returns list of strings matching the regular expression pattern.
     */
    private List<String> regexChecker(String regexString, String string) {
        List<String> tags = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regexString);
        Matcher match = pattern.matcher(string);
        while (match.find()) {
            tags.add(match.group());
        }
        return tags;
    }

    /***
     * This method returns list of <a href urls from the html resposne line passed.
     * @param line - html response line
     * @return - list of urls
     */
    private List<String> getUrls(String line) {
        String aTagRegex = "(<a)(.*?)(\">|\\/\")";
        String hrefTagRegex = "(?<=href=\"|HREF=\").*?(?=\")";
        List<String> hrefTagResults = new ArrayList<>();
        List<String> atagResults = regexChecker(aTagRegex, line);
        for (String s : atagResults) {
            hrefTagResults.addAll(regexChecker(hrefTagRegex, s));
        }
        return hrefTagResults;
    }

    /***
     * Execution begins from this methos.
     * @param args 0 - string url to start crawl.
     *             1 - number of hops
     */
    public static void main(String[] args) {
        try {
            String startUrl = args[0];
            int hops = Integer.parseInt(args[1]);
            WebCrawler webCrawler = new WebCrawler(startUrl, hops);
            webCrawler.getHttpResponse(startUrl);
            if (webCrawler.hopsCount>0){
                System.out.println("No more urls available to crawl");
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter valid number for hop count");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Enter a valid url and number of hops");
            e.printStackTrace();
        }
    }
}
