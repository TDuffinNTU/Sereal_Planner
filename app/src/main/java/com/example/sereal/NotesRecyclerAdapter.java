package com.example.sereal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder>
{
    List<NoteStruct> mNotes;
    Context mContext;
    NotesDB mNotesDatabase;
    DateTimeFormatter mDTFormat;


    public NotesRecyclerAdapter(@NonNull Context context, NotesDB database)
    {
        mNotes = database.getAllNotes();
        mNotesDatabase = database;
        mContext = context;
        mDTFormat = DateTimeFormatter.ofPattern(mContext.getString(R.string.date_format));
    }

    public void AddNewNote()
    {
        NoteStruct n = new NoteStruct("New Note", mDTFormat.format(LocalDateTime.now()),"Write your note...");
        mNotes.add(0, n);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.note_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mNote = mNotes.get(position);
        holder.mContents.setText(mNotes.get(position).getContents());
        holder.mTitle.setText(mNotes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void SaveNote(NoteStruct n)
    {
        if(n.getID() == null)
        {
            // add to db, then refresh notes
            mNotesDatabase.addNote(n);
            n.setID(mNotes.size());

        } else {
            // update record instead
            mNotesDatabase.updateNote(n);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        EditText mContents, mTitle;
        FloatingActionButton mDelete;
        NoteStruct mNote;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mContents = itemView.findViewById(R.id.noteContents);
            mTitle = itemView.findViewById(R.id.noteTitle);
            mDelete = itemView.findViewById(R.id.deleteFAB);

            mDelete.setOnClickListener(v -> {

              mNotes.remove(mNote);
              if(mNote.getID() != null)
              {
                  mNotesDatabase.removeNote(mNote);
              }
              notifyItemRemoved(this.getAdapterPosition());
            });

            mContents.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus)
                {
                    mNote.setContents(String.valueOf(mContents.getText()));
                    SaveNote(mNote);
                }
            });

            mTitle.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus)
                {
                    mNote.setTitle(String.valueOf(mTitle.getText()));
                    SaveNote(mNote);
                }
            });

        }


    }
}

