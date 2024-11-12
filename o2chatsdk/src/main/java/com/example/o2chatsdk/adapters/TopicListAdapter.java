package com.example.o2chatsdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.o2chatsdk.Events.chatEvents.TopicSelectEvent;
import com.example.o2chatsdk.model.chat.GetTopicsByConversationModel;
import com.example.o2chatsdk.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class TopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context contex;
    private final ArrayList<GetTopicsByConversationModel> item_list;
    public TopicListAdapter(Context contex, ArrayList<GetTopicsByConversationModel> item_list) {
        this.contex = contex;
        this.item_list = item_list;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contex).inflate(R.layout.item_topics, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        GetTopicsByConversationModel getTopicsByChannelIDModel = item_list.get(position);
        if(getTopicsByChannelIDModel.name !=null && !getTopicsByChannelIDModel.name.isEmpty()){
            vh.txtName.setText(getTopicsByChannelIDModel.name);
        }
        vh.itemView.setOnClickListener(v -> {
            EventBus.getDefault().post(new TopicSelectEvent(getTopicsByChannelIDModel,"TopicSelect"));
        });
    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;


        public ViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txtName);

        }
    }


}



