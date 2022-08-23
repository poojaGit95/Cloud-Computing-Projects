package com.cloudassignment.program5.entity;

public class Book {

    private String bookTitle;

    private String bookFileName;

    public Book(String bookTitle, String bookFileName){
        this.bookTitle = bookTitle;
        this.bookFileName = bookFileName;
    }

    public Book(){
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookFileName(String bookFileName) {
        this.bookFileName = bookFileName;
    }

    public String getBookFileName() {
        return bookFileName;
    }



}
