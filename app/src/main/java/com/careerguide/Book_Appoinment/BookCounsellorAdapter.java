package com.careerguide.Book_Appoinment;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.careerguide.PlanActivity;
import com.careerguide.R;
import com.careerguide.models.Counsellor;

import java.util.List;

public class BookCounsellorAdapter extends RecyclerView.Adapter<BookCounsellorAdapter.BookCounsellorViewHolder> {
    private Context context;
    private List<Counsellor> CounsellorList;

    class BookCounsellorViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_container;
        ImageView iv_avatar;
        TextView tv_name, tv_live_minutes,tv_follow;


        BookCounsellorViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_live_minutes = itemView.findViewById(R.id.tv_live_minutes);
            tv_follow = itemView.findViewById(R.id.tv_follow);
        }
    }

    BookCounsellorAdapter(Context context, List<Counsellor> CounsellorList) {
        this.context = context;
        this.CounsellorList = CounsellorList;
    }

    @NonNull
    @Override
    public BookCounsellorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book_counsellor, parent, false);
        return new BookCounsellorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookCounsellorViewHolder holder, int position) {
        final Counsellor Counsellor=CounsellorList.get(position);

        holder.tv_name.setText(Counsellor.getFirst_name()+" "+Counsellor.getLast_name());
        Glide.with(context).load(Counsellor.getAvatar()).into(holder.iv_avatar);
        holder.tv_follow.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlanActivity.class);
            intent.putExtra("Uid" , Counsellor.getUid());
            context.startActivity(intent);
        });
//        holder.ll_container.setOnClickListener(v -> {
//            Intent intent = new Intent(context, CounsellorProfile.class);
//            intent.putExtra("host_name" , Counsellor.getFirst_name()+" "+Counsellor.getLast_name());
//            intent.putExtra("host_img" , Counsellor.getAvatar());
//            intent.putExtra("host_email" , Counsellor.getUsername());
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        Log.e("sizeis" ,"-->" +CounsellorList.size());
        return CounsellorList.size();
    }
}
