package com.example.android.sciencedaily;

public class Stories {
    //author of story
    private String mAuthor;
    //title of story
    private String mTitle;
    //date of story
    private String mDate;
    //section of story
    private String mSection;
    //url of earthquake
    private String mUrl;

    public Stories(String author, String title, String section, String date, String url) {
        mAuthor = author;
        mTitle = title;
        mDate = date;
        mSection = section;
        mUrl = url;
    }

    //Get the author of the story
    public String getAuthor() { return mAuthor; }
    //Get the title of the story
    public String getTitle() { return mTitle; }
    //Get the section of the story
    public String getSection() { return mSection; }
    //Get the date of the story
    public String getDate() { return mDate; }
    //Returns the website URL to find more information about the story
    public String getUrl() { return mUrl;}

}