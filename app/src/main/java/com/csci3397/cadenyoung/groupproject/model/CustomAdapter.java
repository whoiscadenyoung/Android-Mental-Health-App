package com.csci3397.cadenyoung.groupproject.model;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csci3397.cadenyoung.groupproject.R;

import org.jetbrains.annotations.NotNull;

/**
 * Provide views to RecyclerView with data from dataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Stat[] stats;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView statName;
        private final ImageView statImage;
        private final TextView statDesc;
        private final ProgressBar statBar;
        private final Resources res;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Adaptor", "Element " + getAdapterPosition() + " clicked.");
                }
            });
            statName = v.findViewById(R.id.statName);
            statImage = v.findViewById(R.id.statImage);
            statDesc = v.findViewById(R.id.statDesc);
            statBar = v.findViewById(R.id.statBar);
            res = v.getResources();
        }

        public TextView getStatName() {
            return statName;
        }
        public ImageView getStatImage() {return statImage;}
        public TextView getStatDesc() {return statDesc;}
        public ProgressBar getStatBar() {return statBar;}
        public Resources getRes() {return res;}
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)
    // Initialize data set for adaptor in the constructor
    public CustomAdapter(Stat[] stats) {
        this.stats = stats;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view from the layout
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);
        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d("Adaptor", "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view with that element
        Stat stat = stats[position];

        // Load in the resources from the view
        Resources resources = viewHolder.getRes();

        // Load in the name and set it in the view
        String statName = stat.getName();
        viewHolder.getStatName().setText(statName);

        // Load in the image and set it in the view
        int imageId = stat.getImageId();
        Drawable image = ResourcesCompat.getDrawable(resources, imageId, null);
        viewHolder.getStatImage().setImageDrawable(image);

        // Load in the description ID, pull description from strings, set text in view
        int descId = stat.getDescId();
        String statDesc = resources.getString(R.string.emotion_desc);
        viewHolder.getStatDesc().setText(statDesc);

        // Set the progress bar status and color
        viewHolder.getStatBar().setProgress(stat.getProgress());
        int colorId = stat.getColorId();
        int color = resources.getColor(colorId);
        viewHolder.getStatBar().setProgressTintList(ColorStateList.valueOf(color));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return stats.length;
    }
}