package com.example.library;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Book> {
    public ItemAdapter(@NonNull Context context, List<Book> books) {
        super(context, R.layout.activity_item, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            //add convertView=
            convertView= LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_item, null);

        final Book book = getItem(position);

        ((TextView) convertView.findViewById(R.id.tvTitle))
                .setText(book.getTitle());

        ((TextView) convertView.findViewById(R.id.tvAuthor))
                .setText(book.getAuthor());

        ImageView imageView = convertView.findViewById(R.id.imageView);
        // imageView.setImageURI(Uri.parse(person.image));

        try {
            Glide.with(imageView.getContext())
                    .load(Uri.parse(book.getCover()))
                    .placeholder(R.drawable.no_cover)
                    .error(R.drawable.no_cover)
                    .circleCrop()
                    .into(imageView);
        } catch (Exception e) {
        }
        return convertView;
    }
}