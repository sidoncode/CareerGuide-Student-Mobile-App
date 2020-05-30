package com.careerguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.careerguide.adapters.LivesessionAdapter;
import com.careerguide.youtubeVideo.CommonEducationAdapter;
import com.careerguide.youtubeVideo.CommonEducationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CounsellorProfileExpertLevelAdapter extends RecyclerView.Adapter<CounsellorProfileExpertLevelAdapter.MyViewHolder>  {

    private Context mContext;
    private List<CounsellorProfileExpertLevelModel> student_education_level_list;
    private List<CommonEducationModel> commonEducationModelList=new ArrayList<>();
    private RelativeLayout parentRelativeLayout;
    private LivesessionAdapter livesessionAdapter;
    private String sortOnKey="";

    HashMap<String, String> expertLevelMap = new HashMap<>();




    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExpertLevel;
        RelativeLayout parentRelativeLayout;

        MyViewHolder(View view) {
            super(view);
            textViewExpertLevel = view.findViewById(R.id.textViewExpertLevel);
            parentRelativeLayout=view.findViewById(R.id.parentRelativeLayout);
            
        }
    }


    public CounsellorProfileExpertLevelAdapter(Context mContext, List<CounsellorProfileExpertLevelModel> student_education_level_list,  LivesessionAdapter livesessionAdapter) {
        this.mContext = mContext;
        Log.e("#adapter" , "-->" +student_education_level_list);
        this.student_education_level_list = student_education_level_list;
        this.livesessionAdapter=livesessionAdapter;

        expertLevelMap.put("All","All");
        expertLevelMap.put("9th","NINE");
        expertLevelMap.put("10th","TEN");
        expertLevelMap.put("11th","ELEVEN");
        expertLevelMap.put("12th","TWELVE");
        expertLevelMap.put("B.Com","GRADUATE");
        expertLevelMap.put("B.Sc","GRADUATE");
        expertLevelMap.put("B.Tech","GRADUATE");
        expertLevelMap.put("B.A","GRADUATE");
        expertLevelMap.put("MBA","POSTGRA");
        expertLevelMap.put("Masters","POSTGRA");
        expertLevelMap.put("Working Professionals","WORKING");

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counselor_profile_expert_level_recyclerview_single_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CounsellorProfileExpertLevelModel objDataModels = student_education_level_list.get(position);
        holder.textViewExpertLevel.setText(objDataModels.getExpertLevel());
        if (parentRelativeLayout==null&position==0) {
            parentRelativeLayout = holder.parentRelativeLayout;
            parentRelativeLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_corner_blue));//default first is selected
        }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonEducationModelList.size()==0){

                }else {
                        parentRelativeLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_corner_grey));//change the old selected color to grey
                        parentRelativeLayout=holder.parentRelativeLayout;//update the newly select item
                        parentRelativeLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_corner_blue));//set this item as selected



                    sortOnKey=expertLevelMap.get(holder.textViewExpertLevel.getText());
                    new TaskSortList().execute();
                    Log.i("keyselected:",expertLevelMap.get(holder.textViewExpertLevel.getText()));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e("#adapterdd" , "-->" +student_education_level_list.size());
        return student_education_level_list.size();
    }

    public void setListOfPastSession(List<CommonEducationModel> commonEducationModelList) {
        Log.i("sssssssiiii",commonEducationModelList.size()+"__"+this.commonEducationModelList.size());
        this.commonEducationModelList.addAll(commonEducationModelList);
        Log.i("sssssssiiii",commonEducationModelList.size()+"__"+this.commonEducationModelList.size());
    }




    private class TaskSortList extends AsyncTask<String, Void, Void> {
        List<CommonEducationModel> sorted_student_education_level_list=new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {

            if (sortOnKey.contentEquals("All")){
                livesessionAdapter.updateList(commonEducationModelList);

                return null;
            }

            for (int i =0;i<commonEducationModelList.size();i++){
                if (commonEducationModelList.get(i).getVideoCategory().contentEquals(sortOnKey)){
                    sorted_student_education_level_list.add(commonEducationModelList.get(i));
                    Log.i("i","inside"+i);
                }
                Log.i("i","out"+i);
            }

            Log.i("sizeeee",sorted_student_education_level_list.size()+"____");
            livesessionAdapter.updateList(sorted_student_education_level_list);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            livesessionAdapter.notifyDataSetChanged();
            if (sorted_student_education_level_list.size()==0&!sortOnKey.contentEquals("All")){
                Toast.makeText(mContext,"Nothing under this category!",Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(mContext, "Sorted!", Toast.LENGTH_LONG).show();
        }
    }

}
