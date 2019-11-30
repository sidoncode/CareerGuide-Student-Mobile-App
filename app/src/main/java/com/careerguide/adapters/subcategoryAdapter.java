package com.careerguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.careerguide.HomeActivity;
import com.careerguide.HomeFragment;
import com.careerguide.R;
import com.careerguide.models.Goal;
import com.careerguide.models.Subcategories;

import java.util.List;


public class subcategoryAdapter extends RecyclerView.Adapter<subcategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<Subcategories> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_goal;
        ImageView iv_icon;
        TextView tv_name;


        public MyViewHolder(View view) {
            super(view);
            ll_goal = itemView.findViewById(R.id.ll_goal);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    public subcategoryAdapter(Context mContext, List<Subcategories> subcat) {
        this.mContext = mContext;
        this.albumList = subcat;
        Log.e("#albumadapter" , "---->" +albumList);
    }

    @Override
    public subcategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_subcategory, parent, false);

        return new subcategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final subcategoryAdapter.MyViewHolder holder, int position) {
        final Subcategories submodel=albumList.get(position);
        holder.tv_name.setText(submodel.getName());
        Log.e("#tv" , "-->" +submodel.getName());
        holder.ll_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bIntent = new Intent(mContext, HomeActivity.class);
                bIntent.putExtra("type","updateGoal");
                bIntent.putExtra("subcat_uid",submodel.getUid());
                bIntent.putExtra("subcat_title",submodel.getName());
                bIntent.putExtra("parent_cat_title",submodel.getparent_cat());
                v.getContext().startActivity(bIntent);
            }
        });
        //Glide.with(mContext).load(submodel.getIcon_url()).into(holder.iv_icon);
    }



    private void showPopupMenu(View view) {

        Log.e("url" , "-->" );
        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();


    }



    @Override
    public int getItemCount() {
        Log.e("#adapteree" , "-->" +albumList.size());
        return albumList.size();
    }

}
