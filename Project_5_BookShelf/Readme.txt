
BookShelf

BookShelf is a web application which stores books on the server. Users can upload and download books to/ from repository. All users can subscribe to receive notifications when new books are uploaded. Also, this application uses the REST API exposed by the New York Times, to list the information top 5 books of the month.
Details of services used in BookShelf application:• Download: The books are stored in AWS S3 storage. When a user click enters the title of a book, application verifies if this book is available in S3 by querying the DynamoDB and downloads the copy of the book on user’s PC from S3 storage.
• Upload: When user wants to contribute to the BookShelf repository, he can upload books. The user needs to enter the book title and the path of the book on his local storage. The application adds the book title, book file name to Dynamo DB and uploads this book to the AWS S3 storage.
• Subscribe: If the user wants to be notified of new books added to the repository, he can use subscribe option. When user enters a valid email ID, a mail is sent to his inbox, where he needs to confirm his subscription. Every time a book is uploaded an email is sent to all the subscribers.
• Top Books Info: The application gives information of top 5 educational books listed by New York Times using their Rest API. It lists the book title, author, and short description of the book.
• The application is hosted on the AW Elastic BeanStalk.
• Also, the AWS CloudWatch service is used to report the application metrics i.e., total number of downloads, number of downloads/book, total number of uploads, total number of subscribers. Through this information we can observe the books that are frequently downloaded, we can upload all books from that author, we can observe which users contributed most by uploading books.  
          

      


