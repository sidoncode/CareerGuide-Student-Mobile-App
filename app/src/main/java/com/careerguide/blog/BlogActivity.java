package com.careerguide.blog;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careerguide.R;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlogActivity extends AppCompatActivity {

    RecyclerView rview;
    public static String tContent;
    ProgressBar pb_loading;
    NestedScrollView nsv_items;
    static String[] temp=new String[10];

    public static final String url ="https://institute.careerguide.com/wp-json/wp/v2/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        rview = findViewById(R.id.listofProgs);
        pb_loading = findViewById(R.id.pb_loading);
        nsv_items = findViewById(R.id.nsv_items);
        rview.setLayoutManager(new LinearLayoutManager(this));
        request();

        nsv_items.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                  request();
                }
            }
        });

    }

    public void request(){
        StringRequest stringRequest = new StringRequest(url, response -> {
            Log.i("Ret:", response);
            parsejson(response);
        }, error -> Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show());
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
//       int  MY_SOCKET_TIMEOUT_MS = 20000;
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void parsejson(String response) {

        List<DataMembers> data = new ArrayList<>();
        pb_loading.setVisibility(View.GONE);
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0;i<10;++i) {

                JSONObject jsonObject;
                jsonObject = (JSONObject) jsonArray.get(i);
                Log.i("Imported: ", Integer.toString(jsonObject.getInt("id")));
                JSONObject jsonObject1;
                jsonObject1 = (JSONObject) jsonObject.get("title");
                Log.i("Imported: ",jsonObject1.getString("rendered"));
                JSONObject jsonObject2;
                jsonObject2 = (JSONObject) jsonObject.get("content");
                JSONObject jsonObject3;
                jsonObject3 = (JSONObject) jsonObject.get("excerpt");
                String imgUrl = getFetaureImageUrl(jsonObject2.getString("rendered"));
                DataMembers dataMembers = new DataMembers(jsonObject.getInt("id"), jsonObject1.getString("rendered") , jsonObject2.getString("rendered") ,imgUrl , jsonObject.getString("link") , jsonObject3.getString("rendered"));
                Log.i("Rednnn",dataMembers.postCode);
                temp[i]=dataMembers.postCode;
                Log.i("Imported: ", jsonObject.getString("link"));
                data.add(dataMembers);
            }
            rview.setAdapter(new RecyclerAdapter_Nav(this,data));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getFetaureImageUrl(String rendered) {

        String imgurl;
        Pattern r = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = r.matcher(rendered);
        if (m.find()) {
            Log.i("0",m.group(1));
            imgurl = m.group(1);
        }
        else {
            imgurl = "http://localsplashcdn.wpengine.netdna-cdn.com/wp-content/uploads/2013/04/The-page-wont-load.png";
        }

        return imgurl;
    }

    public void backpress(View view) {
        finish();
    }
}
