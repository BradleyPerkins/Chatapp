package com.bradperkins.chatgroupapp.utilities;

// Date 4/11/19
// 
// Bradley Perkins

// AID - 1809

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bradperkins.chatgroupapp.GroupObj;
import com.bradperkins.chatgroupapp.R;

import java.util.ArrayList;

// PerkinsBradley_CE
public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder>{

    private ArrayList<GroupObj> groupsList;
    private final Context context;

//    public AdapterListener mListener;


    public GroupsAdapter(Context context, ArrayList<GroupObj> groupsList) {
        this.context = context;
        this.groupsList = groupsList;
    }

    @NonNull
    @Override
    public GroupsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View gameView = inflater.inflate(R.layout.item_group, viewGroup, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(gameView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.ViewHolder viewHolder, int i) {
        GroupObj groups = groupsList.get(i);
        TextView titleTV = viewHolder.title;
        titleTV.setText(groups.getTitle());

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {

        public TextView title;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.card_title);

        }
    }
}
