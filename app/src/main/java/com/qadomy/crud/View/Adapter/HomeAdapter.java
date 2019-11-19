package com.qadomy.crud.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qadomy.crud.R;
import com.qadomy.crud.View.Model.Note;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.RecyclerViewAdapter> {

    private Context context;
    private List<Note> notes;
    private ItemClickListener itemClickListener; // interface

    public HomeAdapter(Context context, List<Note> notes, ItemClickListener itemClickListener) {
        this.context = context;
        this.notes = notes;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new RecyclerViewAdapter(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        Note note = notes.get(position);
        holder.mTitle.setText(note.getTitle());
        holder.mNote.setText(note.getNote());
        holder.mDate.setText(note.getDate());

        holder.cardView.setCardBackgroundColor(note.getColor());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitle, mNote, mDate;
        CardView cardView;
        ItemClickListener itemClickListener;

        public RecyclerViewAdapter(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.mTitle);
            mNote = itemView.findViewById(R.id.mNote);
            mDate = itemView.findViewById(R.id.mDate);
            cardView = itemView.findViewById(R.id.card_item);

            this.itemClickListener = itemClickListener;
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
