package com.example.lab1_20206303;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class GameRecordAdapter extends RecyclerView.Adapter<GameRecordAdapter.ViewHolder> {

    private List<GameRecord> records;

    public GameRecordAdapter(List<GameRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameRecord record = records.get(position);
        
        String text;
        int color;

        if (record.isInProgress()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", java.util.Locale.getDefault());
            text = String.format(java.util.Locale.getDefault(), "%s | Inicio: %s | En curso",
                    record.getDifficulty(),
                    sdf.format(record.getStartTime()));
            color = Color.parseColor("#FFA500"); // Naranja
        } else {
            text = String.format(java.util.Locale.getDefault(), "%s | Tiempo: %ds | Pistas: %d | Puntaje: %d",
                    record.getDifficulty(),
                    record.getDurationMillis() / 1000,
                    record.getHintsUsed(),
                    record.getScore());
            
            if (record.isCancelled()) {
                text += " | Canceló";
                color = Color.parseColor("#FFA500"); // Naranja
            } else if (record.getScore() >= 0) {
                color = Color.GREEN;
            } else {
                color = Color.RED;
            }
        }
        
        holder.textViewInfo.setText(text);
        holder.textViewInfo.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInfo = itemView.findViewById(R.id.textViewGameInfo);
        }
    }
}
