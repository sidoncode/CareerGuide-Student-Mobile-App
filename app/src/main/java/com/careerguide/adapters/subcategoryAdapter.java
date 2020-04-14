package com.careerguide.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.careerguide.HomeActivity;
import com.careerguide.R;
import com.careerguide.models.Subcategories;

import java.util.List;


public class subcategoryAdapter extends RecyclerView.Adapter<subcategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<Subcategories> albumList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_goal;
        ImageView iv_icon;
        TextView tv_name,tv_sub_name;


        MyViewHolder(View view) {
            super(view);
            ll_goal = itemView.findViewById(R.id.ll_goal);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sub_name = itemView.findViewById(R.id.tv_sub_name);
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
        holder.tv_sub_name.setText(submodel.getVideo_count() + " Career Class");
        Log.e("#tv" , "-->" +submodel.getName());
        holder.ll_goal.setOnClickListener(v -> {
            Intent bIntent = new Intent(mContext, HomeActivity.class);
            bIntent.putExtra("type","updateGoal");
            Log.e("##uidis " ,"--> " +submodel.getUid());
            bIntent.putExtra("subcat_uid",submodel.getUid());
            bIntent.putExtra("subcat_title",submodel.getName());
            bIntent.putExtra("parent_cat_title",submodel.getparent_cat());
            bIntent.putExtra("icon_url",submodel.getIcon_url());
            v.getContext().startActivity(bIntent);
        });
        //Glide.with(mContext).load(submodel.getIcon_url()).into(holder.iv_icon);
    }



//    private void showPopupMenu(View view) {
//
//        Log.e("url" , "-->" );
//         inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//
//
//    }
    @Override
    public int getItemCount() {
        Log.e("#adapteree" , "-->" +albumList.size());
        return albumList.size();
    }

}
