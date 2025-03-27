package com.bwi.onboard.slider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private final List<Integer> slideLayouts = new ArrayList<>();

    public void setSlideLayouts(List<Integer> layouts) {
        slideLayouts.clear();
        slideLayouts.addAll(layouts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(slideLayouts.get(viewType), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Nothing to bind since it's layout-based
    }

    @Override
    public int getItemCount() {
        return slideLayouts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
