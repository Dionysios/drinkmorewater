package com.dionpapas.drinkyourwater.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dionpapas.drinkyourwater.R;
import com.dionpapas.drinkyourwater.database.WaterEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.TaskViewHolder>  {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Member variable to handle item clicks
    //final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
    private List<WaterEntry> mwaterEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     * Constructor for the TaskAdapter that initializes the Context.
     *
     * @param context  the current Context

     */
    //public DiaryAdapter(Context context, ItemClickListener listener) {
     public DiaryAdapter(Context context) {
        mContext = context;
        //mItemClickListener = listener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.water_entries_layout, parent, false);

        return new TaskViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        // Determine the values of the wanted data
        WaterEntry waterEntry = mwaterEntries.get(position);
//        result[0] += waterEntries.get(0).getCounter() + "on" + waterEntries.get(0).getUpdatedAt() + "\\n";
       // String description = waterEntry.getDescription();
        int counter = waterEntry.getCounter();
        String updatedAt = dateFormat.format(waterEntry.getUpdatedAt());

        //Set values
        holder.counterView.setText(Integer.toString(counter));
        holder.updatedAtView.setText(updatedAt);

    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mwaterEntries == null) {
            return 0;
        }
        return mwaterEntries.size();
    }

    public List<WaterEntry> getwaterEntries() {
        return mwaterEntries;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setwaterEntries(List<WaterEntry> waterEntries) {
        mwaterEntries = waterEntries;
        notifyDataSetChanged();
    }

//    public interface ItemClickListener {
//        void onItemClickListener(int itemId);
//    }

    // Inner class for creating ViewHolders
    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the task description and priority TextViews
        //TextView taskDescriptionView;
        TextView updatedAtView;
        TextView counterView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public TaskViewHolder(View itemView) {
            super(itemView);

            //taskDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            counterView = itemView.findViewById(R.id.counterTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //int elementId = mwaterEntries.get(getAdapterPosition()).getId();
            //mItemClickListener.onItemClickListener(elementId);
        }
    }
}
