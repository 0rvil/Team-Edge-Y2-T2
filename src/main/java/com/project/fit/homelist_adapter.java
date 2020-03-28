package com.project.fit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class homelist_adapter extends ArrayAdapter<home_item> {

    public homelist_adapter(Context context, ArrayList list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_screen_layout, parent, false);
        home_item current_home_item = getItem(position);

        ImageView categoryItemImageView = convertView.findViewById(R.id.home_list_image);
        TextView categoryItemTextView = convertView.findViewById(R.id.home_list_text);

        categoryItemImageView.setImageResource(current_home_item.getDrawable());
        categoryItemTextView.setText(current_home_item.getTitle());

        return convertView;

    }
}
