package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {

    private Context context;
    private ArrayList<Word> array;
    private int mColorID;
    private ViewGroup container, imageContainer;

    public WordAdapter(@NonNull Context context, @NonNull ArrayList<Word> objects, int colorID) {
        super(context, R.layout.list_item, objects);

        this.context = context;
        array = objects;
        mColorID = colorID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(R.layout.list_item, parent, false);

        TextView mWord = (TextView) v.findViewById(R.id.miwok_translation);
        TextView dWord = (TextView) v.findViewById(R.id.default_translation);
        ImageView img = (ImageView) v.findViewById(R.id.word_image);
        container = (ViewGroup) v.findViewById(R.id.translation_container);
        imageContainer = (ViewGroup) v.findViewById(R.id.image_container);

        if (array.get(position).getmImageID() == Word.NO_IMAGE) {
            imageContainer.setVisibility(View.GONE);
        } else {
            img.setImageResource(array.get(position).getmImageID());
        }

        mWord.setText(array.get(position).getMiwokTranslation());
        dWord.setText(array.get(position).getDefaultTranslation());
        container.setBackgroundColor(mColorID);

        return v;
    }
}
