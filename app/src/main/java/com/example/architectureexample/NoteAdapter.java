package com.example.architectureexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
// Instead of above, we are using ListAdapter (androidx wala, Listview wala nai) as it gives some nice animation to list
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    /**
     * The ListAdapter will take care of List<Note> for us.
     */
//    private List<Note> notes = new ArrayList<>();

    private OnItemClickListener listener;

    protected NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == oldItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note currNote = getItem(position);

        holder.title.setText(currNote.getTitle());
        holder.desc.setText(currNote.getDescription());
        holder.priority.setText(String.valueOf(currNote.getPriority()));
    }

    /**
     * We don't need getItemCount, ListAdapter will take care of that for us
     */
//    @Override
//    public int getItemCount() {
//        return notes.size();
//    }

    /**
     * We also don't need setNotes, as we will use diff method for passing the new List<Note>
     */
//    public void setNotes(List<Note> newNotes) {
//        this.notes = newNotes;
//        notifyDataSetChanged();
//    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView desc;
        private TextView priority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            desc = itemView.findViewById(R.id.text_view_description);
            priority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
