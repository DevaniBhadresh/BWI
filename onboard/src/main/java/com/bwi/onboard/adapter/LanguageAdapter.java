package com.bwi.onboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bwi.onboard.R;
import com.bwi.onboard.utils.Language;

import java.util.List;


public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private final List<Language> languageList;
    private int selectedPosition = -1; // No selection by default
    private final OnLanguageSelectedListener listener;
    private final int layoutResId; // Layout resource ID for the item

    public interface OnLanguageSelectedListener {
        void onLanguageSelected(Language selectedLanguage);
    }

    public LanguageAdapter(List<Language> languageList, @LayoutRes int layoutResId, OnLanguageSelectedListener listener) {
        this.languageList = languageList;
        this.listener = listener;
        this.layoutResId = layoutResId;

    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Language language = languageList.get(position);
        holder.flagImageView.setImageResource(language.getFlagResId());
        holder.languageNameTextView.setText(language.getName());
        holder.radioButton.setChecked(position == selectedPosition);

        // Handle row clicks
        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Notify adapter to refresh the selection state
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            // Notify the listener about the selection
            listener.onLanguageSelected(language);
        });
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    static class LanguageViewHolder extends RecyclerView.ViewHolder {
        ImageView flagImageView;
        TextView languageNameTextView;
        RadioButton radioButton;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImageView = itemView.findViewById(R.id.image_flag);
            languageNameTextView = itemView.findViewById(R.id.text_language_name);
            radioButton = itemView.findViewById(R.id.radio_button);
        }
    }
}
