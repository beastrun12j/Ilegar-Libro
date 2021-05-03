package com.example.llegarlibro;

public class Book {
    private final String mBookName;
    private final String mBookAuthor;
    private final String mBookPublication;
    private final int mBookRating;
    private final int mBookPages;
    private final String mBookType;
    private final String mUrl;

    public Book(String BookName, String BookAuthor, String BookPublication, int BookRating, int BookPages, String BookType, String Url) {
        mBookName = BookName;
        mBookAuthor = BookAuthor;
        mBookPublication = BookPublication;
        mBookRating = BookRating;
        mBookPages = BookPages;
        mBookType = BookType;
        mUrl = Url;
    }

    public String getBookName() {
        return mBookName;
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }

    public String getBookPublication() {
        return mBookPublication;
    }

    public int getBookRating() {
        return mBookRating;
    }

    public int getBookPages() {
        return mBookPages;
    }

    public String getBookType() {
        return mBookType;
    }

    public String getUrl() {
        return mUrl;
    }

}