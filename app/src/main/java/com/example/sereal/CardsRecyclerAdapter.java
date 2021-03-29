package com.example.sereal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CardsRecyclerAdapter extends RecyclerView.Adapter<CardsRecyclerAdapter.ViewHolder>
{
    List<CardStruct> mCards;
    Context mContext;
    CardsDB mCardsDatabase;
    NotesDB mNotesDatabase;

    int mDay;

    public CardsRecyclerAdapter(@NonNull Context context, CardsDB database, NotesDB notesDB, Integer day)
    {
        mCards = database.getCardsOnDay(day);
        mDay = day;
        mCardsDatabase = database;
        mNotesDatabase = notesDB;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mCard = mCards.get(position);

        holder.mTitle.setText(holder.mCard.getTitle());

        ArrayList<Boolean> days = holder.mCard.getDays();

        holder.mMon.setChecked(days.get(0));
        holder.mTue.setChecked(days.get(1));
        holder.mWed.setChecked(days.get(2));
        holder.mThur.setChecked(days.get(3));
        holder.mFri.setChecked(days.get(4));
        holder.mSat.setChecked(days.get(5));
        holder.mSun.setChecked(days.get(6));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(mContext.getString(R.string.time_format));
        holder.mTime.setText(dtf.format(holder.mCard.getTime()));

        String alarm = String.valueOf(holder.mCard.isAlarm());
        alarm = alarm.substring(0,1).toUpperCase() + alarm.substring(1);

        holder.mAlarm.setText(alarm);

        if(holder.mCard.getNote() != null)
        {
            String content = holder.mCard.getNote().getContents();
            holder.mNoteContent.setText(content);
            holder.mNoteTitle.setText(holder.mCard.getNote().getTitle());
        }
        else {
            holder.mNoteHolder.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public int GetDay() { return mDay; }

    public void SetDay(Integer day)
    {
        mDay = day;
        mCards = mCardsDatabase.getCardsOnDay(day);
        notifyDataSetChanged();
    }

    public void NextDay()
    {
        mDay ++;
        if(mDay == CardsDB.DAY.NULL.ordinal())
        {
            mDay = CardsDB.DAY.MON.ordinal();
        }

        SetDay(mDay);
    }

    public void PrevDay()
    {
        mDay --;
        if(mDay < CardsDB.DAY.MON.ordinal())
        {
            mDay = CardsDB.DAY.SUN.ordinal();
        }

        SetDay(mDay);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        FloatingActionButton mDelete;
        CardStruct mCard;
        CheckBox mMon,mTue,mWed,mThur,mFri,mSat,mSun;
        TextView mTime, mAlarm, mTitle, mNoteTitle, mNoteContent;
        CardView mNoteHolder;
        LinearLayout mDaysButtons;
        CardView mCardHolder;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            //days
            mMon =  itemView.findViewById(R.id.monday);
            mTue =  itemView.findViewById(R.id.tuesday);
            mWed =  itemView.findViewById(R.id.wednesday);
            mThur = itemView.findViewById(R.id.thursday);
            mFri =  itemView.findViewById(R.id.friday);
            mSat =  itemView.findViewById(R.id.saturday);
            mSun =  itemView.findViewById(R.id.sunday);
            mDaysButtons = itemView.findViewById(R.id.linearLayout);
            mCardHolder = itemView.findViewById(R.id.CardHolder);

            // time and alarm
            mTime = itemView.findViewById(R.id.cardTime);
            mAlarm = itemView.findViewById(R.id.cardAlarm);
            mTitle = itemView.findViewById(R.id.cardTitle);

            // hiding or filling in notes data
            mNoteTitle = itemView.findViewById(R.id.noteTitle);
            mNoteContent = itemView.findViewById(R.id.noteContents);
            mNoteHolder = itemView.findViewById(R.id.noteHolder);

            // deleting card
            mDelete = itemView.findViewById(R.id.deleteFAB);
            mDelete.setOnClickListener(v -> {
              mCards.remove(mCard);
              if(mCard.getID() != null)
              {
                  mCardsDatabase.removeCard(mCard);
                  if(mCard.isAlarm())
                  {
                      // cancel alarm associated with this card
                      AlarmsHandler.CancelAlarm(mContext, mCard.getID());
                  }
              }
              notifyItemRemoved(this.getAdapterPosition());
            });

            // We can hide some extra stuff if we're in routine view
            if(mContext.getClass() == MainActivity.class)
            {
                mDaysButtons.setVisibility(View.GONE);
                mDelete.setVisibility(View.GONE);
            }

            // opening activity if we click the card
            mCardHolder.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, CardEditor.class);
                intent.putExtra(mContext.getString(R.string.card_id), mCard.getID());
                mContext.startActivity(intent);
            });
        }
    }


}

