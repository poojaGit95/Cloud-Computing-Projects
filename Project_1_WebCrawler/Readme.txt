
Web Crawler

Create a java application, WebCrawl.java, which takes two arguments from the command line: 
1) A URL as a starting point 
2) The number of hops from that URL (num_hops) 
 
Your application will download the html from the starting URL which is provided as the first 
argument to the program.  It will parse the html finding the first <a href  > reference to other 
absolute URLs, for instance https://www.w3schools.com/tags/att_a_href.asp .  Make sure that 
you have not previously visited this page (if you have then skip and find the next reference).  
Look for only http and https URLs.  The application will then download the html from that page 
and repeat the operation.  If that page is not accessible then continue on the current page 
looking for the next reference and visit that reference.  You will do this num_hops times.   
 
Your app should print out to the console the URL of each hop that you visit.  If you encounter a 
page without any accessible embedded references you should stop there and print out the 
result.   
          

      


