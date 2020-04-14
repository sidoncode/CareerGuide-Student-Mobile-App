package com.careerguide;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MessageContainer {
    private static final int MAX_MSG_COUNT = 20;
    private Context mContext;
    private LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<LiveChatMessage> mMessageList;

    public MessageContainer(RecyclerView listView) {
        mRecyclerView = listView;
        mContext = mRecyclerView.getContext();
        mInflater = LayoutInflater.from(mContext);
        mMessageList = new ArrayList<LiveChatMessage>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mAdapter = new MyAdapter(mMessageList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void addMessage(LiveChatMessage message) {
        while (mMessageList.size() >= MAX_MSG_COUNT) {
            mMessageList.remove(mMessageList.size() - 1);
        }

        mMessageList.add(0, message);
        mAdapter.notifyDataSetChanged();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<LiveChatMessage> mMessageList;

        MyAdapter(List<LiveChatMessage> list) {
            mMessageList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(mInflater.inflate(R.layout.chat_list_item, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            int adapterPosition = holder.getAdapterPosition();
            LiveChatMessage m = mMessageList.get(adapterPosition);
            TextView name = holder.itemView.findViewById(R.id.chat_item_name);
            TextView content = holder.itemView.findViewById(R.id.chat_item_content);
            name.setText(m.getName());
            content.setText(m.getContent());
        }
        @Override
        public int getItemCount() {
            return mMessageList.size();
        }
    }
}
