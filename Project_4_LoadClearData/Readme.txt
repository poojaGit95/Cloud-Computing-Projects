
Load Clear Query data

Create and host a Website which has three sections: 
1) A button call Load Data 
2) A button call Clear Data 
3) Two input text boxes labeled First Name and Last Name 
4) A button called Query 
 
When the “Load Data” button is hit the website will load data from an object stored at a given 
URL.  I will have the data both in Azure as well as Amazon S3.   The CORS (Cross-Origin Resource 
Sharing) header has been added to the bucket so that the object can be accessed from different 
regions.   
 
First you will copy this data (a test file) into Object storage (S3 or Blob).   You will also be 
required to parse and load into a <Key, Value> NoSQL store (Dynamo DB or Azure Tables).  Note 
that the load button can be hit multiple times.  Each time the test file should be parsed and the 
NoSQL DB should be updated.  Do not erase the existing data in the NoSQL database when the 
Load button is hit.  Add in any new items found and update existing items.  You may overwrite 
the file in S3/Blob storage. 
 
When the “Clear Data” button is hit the blob is removed from the object store.  The NoSQL 
table is also emptied or removed. 
 
Once the data has been loaded in the NoSQL store the Website user can type in either one or 
both a First and Last name.  When the Query button is hit results are shown on the Website.  
For the queries the names should be exact matches.  Note that you do not need to fill in both 
query boxes to query. 
          

      


