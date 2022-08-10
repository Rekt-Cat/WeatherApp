package com.maid.weatherapp;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

    public class SpacingDecor extends RecyclerView.ItemDecoration {
        int horizontalSpaing;
        int horizontalLeftSpacing;

        public SpacingDecor(int horizontalSpaing, int horizontalLeftSpacing) {
            this.horizontalSpaing = horizontalSpaing;
            this.horizontalLeftSpacing = horizontalLeftSpacing;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.right=horizontalSpaing;
            outRect.left= horizontalLeftSpacing;
        }
    }

