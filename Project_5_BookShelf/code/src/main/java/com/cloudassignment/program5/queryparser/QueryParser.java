package com.cloudassignment.program5.queryparser;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.cloudassignment.program5.cloudoperations.DynamoDBOperation;
import com.cloudassignment.program5.entity.Book;

public class QueryParser {

        public Book isBookAvailableInShelf(DynamoDBOperation dynamDBOperation, String bookTitleName){
            Book book = new Book();
            if(bookTitleName!=null && !bookTitleName.isEmpty()){
                Item item = dynamDBOperation.queryTableUsingBookTitle(bookTitleName);
                if (item!=null){
                    book.setBookTitle(bookTitleName);
                    book.setBookFileName(item.getString("BookFileName"));
                    return book;
                }
            }
            return null;
        }


}
