package com.example.llegarlibro;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentPosition = getItem(position);

        TextView bookNameTextView = listItemView.findViewById(R.id.book_name);
        bookNameTextView.setText(String.valueOf(currentPosition.getBookName()));

        TextView bookAuthorTextView = listItemView.findViewById(R.id.book_author);
        bookAuthorTextView.setText(String.valueOf(currentPosition.getBookAuthor()));

        TextView bookPublicationTextView = listItemView.findViewById(R.id.book_publications);
        bookPublicationTextView.setText(String.valueOf(currentPosition.getBookPublication()));

        TextView bookRatingTextView = listItemView.findViewById(R.id.book_rating);
        bookRatingTextView.setText(String.valueOf(currentPosition.getBookRating()));

        TextView bookPagesTextView = listItemView.findViewById(R.id.book_pages);
        bookPagesTextView.setText(String.valueOf(currentPosition.getBookPages()));

        TextView bookNameTypeView = listItemView.findViewById(R.id.book_type);
        bookNameTypeView.setText(String.valueOf(currentPosition.getBookType()));

        return listItemView;

    }

}