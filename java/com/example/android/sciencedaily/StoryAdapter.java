package com.example.android.sciencedaily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


public class StoryAdapter extends ArrayAdapter<Stories> {

    //static string variable to help us split date string into 2 parts
    private static final String DATE_SEPARATOR = "T";

    public StoryAdapter(Context context, List<Stories> stories) {
        super(context, 0, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }
        //Find the story at the given position in the list
        Stories currentStory = getItem(position);

        //Find the TextView with view ID author
        TextView authorView =  listItemView.findViewById(R.id.author);
        authorView.setText(currentStory.getAuthor());

        //Find the TextView with view ID title
        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentStory.getTitle());

        //Find the TextView with view ID section
        TextView sectionView =  listItemView.findViewById(R.id.section);
        sectionView.setText(currentStory.getSection());

        //Declare new originalDate variable for Date object to reformat it
        String originalDate = currentStory.getDate();

        //Declare variables to reformat the date object
        String newDate;
        String newTime;

        //Break the date object into two strings
        if (originalDate.contains(DATE_SEPARATOR)) {
            String[] parts = originalDate.split(DATE_SEPARATOR);
            newDate = parts[0];
            newTime = parts[1];
        } else {
            newDate = "No Date";
            newTime = "No Time";
        }

        //Find the TextView with the view ID date and populate with newDate
        TextView newDateView = listItemView.findViewById(R.id.date);
        newDateView.setText(newDate);

        //Find the TextView with the view ID time and populate with newTime
        TextView newTimeView = listItemView.findViewById(R.id.time);
        newTimeView.setText(newTime);

        return listItemView;
    }
}
