package com.careerguide.Book_One_To_One.adapter;

        import android.app.Activity;
        import android.content.Context;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.graphics.Color;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.Priority;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;
        import com.bumptech.glide.request.RequestOptions;
        import com.careerguide.Book_One_To_One.activity.OneToOneSessionActivity;
        import com.careerguide.Book_One_To_One.model.OneToOneChatModel;
        import com.careerguide.R;

        import org.jetbrains.annotations.NotNull;

        import java.util.ArrayList;
        import java.util.List;

        import de.hdodenhof.circleimageview.CircleImageView;

public class OneToOneMessageContainer {
    protected final int ANCHOR_UID = Integer.MAX_VALUE;
    private static final int MAX_MSG_COUNT = 20;
    private Context mContext;
    private LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<OneToOneChatModel> mMessageList;

    public OneToOneMessageContainer(RecyclerView listView) {
        mRecyclerView = listView;
        mContext = mRecyclerView.getContext();
        mInflater = LayoutInflater.from(mContext);
        mMessageList = new ArrayList<OneToOneChatModel>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mAdapter = new MyAdapter(mMessageList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void addMessage(OneToOneChatModel message) {

        mMessageList.add(mMessageList.size(),message);
        ((OneToOneSessionActivity)mRecyclerView.getContext()).runOnUiThread(()->{
            mAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mMessageList.size()-1);
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<OneToOneChatModel> mMessageList;

        MyAdapter(List<OneToOneChatModel> list) {
            mMessageList = list;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(mInflater.inflate(R.layout.one_to_one_chat_recycler_single_view, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            OneToOneChatModel m = mMessageList.get(position);

            if (m.getmMessage().contains("Video-Call")||m.getmMessage().contains("Audio-Call")){
                holder.itemView.findViewById(R.id.host_message_window).setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.mentee_message_window).setVisibility(View.GONE);


                TextView logs=holder.itemView.findViewById(R.id.logs);
                logs.setVisibility(View.VISIBLE);
                logs.setText(m.getmMessage());
            }else {


                if (m.getmUid() == ANCHOR_UID) {//host message
                    holder.itemView.findViewById(R.id.host_message_window).setVisibility(View.VISIBLE);
                    holder.itemView.findViewById(R.id.mentee_message_window).setVisibility(View.GONE);

                    TextView message = holder.itemView.findViewById(R.id.host_message);
                    TextView timeSent = holder.itemView.findViewById(R.id.host_message_time_sent);
                    message.setText(m.getmMessage());
                    timeSent.setText(m.getmtimeSent());
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.loading)
                            .priority(Priority.HIGH)
                            .dontAnimate()
                            .dontTransform();
                    Glide.with(holder.itemView.getContext()).load(m.getmUserImage()).apply(options).into((CircleImageView) holder.itemView.findViewById(R.id.hostprofileImage));

                } else {

                    holder.itemView.findViewById(R.id.host_message_window).setVisibility(View.GONE);
                    holder.itemView.findViewById(R.id.mentee_message_window).setVisibility(View.VISIBLE);

                    TextView message = holder.itemView.findViewById(R.id.mentee_message);
                    TextView timeSent = holder.itemView.findViewById(R.id.mentee_message_time_sent);
                    TextView menteeprofileImage = holder.itemView.findViewById(R.id.menteeprofileImage);
                    menteeprofileImage.setText((m.getmUserImage()));

                    message.setText(m.getmMessage());
                    timeSent.setText(m.getmtimeSent());

                    if (!m.getmSent_Received()) {
                        timeSent.setText("Failed!");
                        timeSent.setTextColor(Color.RED);
                    }
                }
            }

        }
        @Override
        public int getItemCount() {
            return mMessageList.size();
        }
    }
}
