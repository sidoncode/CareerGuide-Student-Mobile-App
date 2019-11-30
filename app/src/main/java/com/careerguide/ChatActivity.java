package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements Observer
{
    private Activity activity = this;
    private Counsellor counsellor;
    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private StringRequest typingRequest;
    private View typingIndicator;

    private Handler typinghandler;
    private Runnable typingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        counsellor = (Counsellor) getIntent().getSerializableExtra("counsellor");
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowHomeEnabled(false);
        View actionBarView = getLayoutInflater().inflate(R.layout.chat_action_bar,null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //setTitle(counsellor.getName());
        ((TextView) actionBarView.findViewById(R.id.name)).setText(counsellor.getName());
        Log.e("vbdfg",counsellor.getPicUrl());
        Glide.with(activity).load(counsellor.getPicUrl()).into(((ImageView) actionBarView.findViewById(R.id.profileImage)));
        ChatObservable.getInstance(counsellor.getId()).addObserver(this);

        chatAdapter = new ChatAdapter();
        ListView listView = findViewById(R.id.chatListView);
        listView.setAdapter(chatAdapter);

        getHistory();

        findViewById(R.id.cemeraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(activity);
            }
        });

        final EditText msgEditText = findViewById(R.id.msgEditText);
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String text = msgEditText.getText().toString().trim();
                if(text.isEmpty())
                {
                    return;
                }
                else
                {
                    ChatMessage chatMessage = new ChatMessage(counsellor.getId(),"-1",text,"", ChatMessage.ChatType.MSG, Calendar.getInstance().getTime(),false);
                    chatMessages.add(chatMessage);
                    chatAdapter.notifyDataSetChanged();
                    msgEditText.setText("");
                    sendMessage(chatMessage, null);
                }
            }
        });
        msgEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().trim().length();
                if (length > 0)
                {
                    sendTypingNotification();
                }
            }
        });
        typingRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "student_typing", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("typing_res", response);
                /*try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("typing_rerror","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                params.put("counselor_id",counsellor.getId());
                Log.e("typing_req",params.toString());
                return params;
            }
        };
        typingRequest.setTag("typing");
        typingIndicator = findViewById(R.id.typingIndicator);
        typinghandler = new Handler();
        typingRunnable = new Runnable() {
            @Override
            public void run() {
                typingIndicator.setVisibility(View.GONE);
            }};
    }

    private void sendTypingNotification() {
        //typingRequest.cancel();
        VolleySingleton.getInstance(activity).getRequestQueue().cancelAll("typing");
        VolleySingleton.getInstance(activity).addToRequestQueue(typingRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                Log.e("result uri", resultUri.toString());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    ChatMessage chatMessage = new ChatMessage(counsellor.getId(),"-1","","", ChatMessage.ChatType.IMAGE, Calendar.getInstance().getTime(),false);
                    sendMessage(chatMessage,bitmap);
                    //circleImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void getHistory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "get_chat", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("get_chat_res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status) {
                        JSONArray chatJsonArray = jsonObject.optJSONArray("chat");
                        for (int i = 0; chatJsonArray != null && i<chatJsonArray.length(); i++)
                        {
                            JSONObject chatJsonObject = chatJsonArray.optJSONObject(i);
                            String id = chatJsonObject.optString("id");
                            String msg = chatJsonObject.optString("msg");
                            String image = chatJsonObject.optString("image");
                            String userId = chatJsonObject.optString("user_id");
                            String coId = chatJsonObject.optString("co_id");
                            String date = chatJsonObject.optString("date_time");
                            String type = chatJsonObject.optString("type");
                            //Log.e("user-id",userId);
                            chatMessages.add(new ChatMessage(counsellor.getId(),id,msg,image, type.equals(ChatMessage.ChatType.MSG.toString()) ? ChatMessage.ChatType.MSG: ChatMessage.ChatType.IMAGE,Utility.getDateFromString(date),userId.equals("null")));
                        }
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("get_chat_rerror","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                params.put("co_id",counsellor.getId());
                Log.e("get_chat_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    private void sendMessage(final ChatMessage chatMessage, final Bitmap bitmap)
    {
        final ProgressDialogCustom[] progressDialogCustom = new ProgressDialogCustom[1];
        if (bitmap != null)
        {
            progressDialogCustom[0] = new ProgressDialogCustom(activity,"Uploading...");
            progressDialogCustom[0].showIt();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, /*"http://app.careerguide.com/api/counsellor/chat_to_user"*/ Utility.PRIVATE_SERVER + "chat_to_counsellor", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("chat_res", response);
                if (null != progressDialogCustom[0])
                {
                    progressDialogCustom[0].dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status) {
                        String chatId = jsonObject.optString("chat_id");
                        if (bitmap != null)
                        {
                            String image = jsonObject.optString("img");
                            chatMessage.setImage(image);
                            chatMessages.add(chatMessage);
                        }
                        chatMessage.setId(chatId);
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != progressDialogCustom[0])
                {
                    progressDialogCustom[0].dismiss();
                }
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("chat_rerror","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                params.put("co_id",counsellor.getId());
                params.put("msg",chatMessage.getMessage());
                params.put("chat_type",chatMessage.getChatType().toString());
                if (chatMessage.getChatType() == ChatMessage.ChatType.IMAGE) {
                    params.put("image", Utility.getStringImage(bitmap));
                }
                Log.e("chat_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    @Override
    public void update(Observable o, final Object arg) {
        if(o instanceof ChatObservable)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    if (arg instanceof ChatMessage) {
                        final ChatMessage chatMessage = (ChatMessage) arg;
                        chatMessages.add(chatMessage);
                        chatAdapter.notifyDataSetChanged();
                    }
                    else if (arg instanceof String[])
                    {
                        String array[] = (String[]) arg;
                        switch (array[0])
                        {
                            case "typing":
                                typingIndicator.setVisibility(View.VISIBLE);
                                typinghandler.removeCallbacks(typingRunnable);
                                typinghandler.postDelayed(typingRunnable,1000);
                        }
                    }
                }
            });
        }
    }

    private class ChatAdapter extends BaseAdapter
    {

        private LayoutInflater layoutInflater;

        public ChatAdapter() {
            layoutInflater = activity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return chatMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return chatMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatMessage chatMessage = chatMessages.get(position);
            LinearLayout chatView;
            String picUrl;
            if (chatMessage.isReceived())
            {
                chatView = (LinearLayout) layoutInflater.inflate(R.layout.chat_item_receiver,null);
                picUrl = counsellor.picUrl;
            }
            else
            {
                chatView = (LinearLayout) layoutInflater.inflate(R.layout.chat_item_sender,null);
                picUrl = Utility.getUserPic(activity);
            }
            TextView textView = chatView.findViewById(R.id.msg);
            ImageView imageView = chatView.findViewById(R.id.image);
            if (ChatMessage.ChatType.IMAGE == chatMessage.getChatType())
            {
                Glide.with(activity).load(chatMessage.getImage()).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }
            else
            {
                textView.setText(chatMessage.getMessage());
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
            CircleImageView circleImageView = chatView.findViewById(R.id.profileImage);
            TextView dateTextView = chatView.findViewById(R.id.date);
            TextView timeTextView = chatView.findViewById(R.id.time);
            dateTextView.setText(chatMessage.getDateString());
            timeTextView.setText(chatMessage.getTime());
            if (position > 0)
            {
                if (chatMessage.isOnSameDate(chatMessages.get(position-1)))
                {
                    dateTextView.setVisibility(View.GONE);
                    if (chatMessage.isReceived() == chatMessages.get(position-1).isReceived()) {
                        circleImageView.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        circleImageView.setVisibility(View.VISIBLE);
                        if ((picUrl != null && picUrl.length() > 0))
                        {
                            Glide.with(activity).load(picUrl).into(circleImageView);
                        }
                    }
                }
                else
                {
                    dateTextView.setVisibility(View.VISIBLE);
                    circleImageView.setVisibility(View.VISIBLE);
                    if ((picUrl != null && picUrl.length() > 0))
                    {
                        Glide.with(activity).load(picUrl).into(circleImageView);
                    }
                }
            }
            else
            {
                circleImageView.setVisibility(View.VISIBLE);
                if ((picUrl != null && picUrl.length() > 0))
                {
                    Glide.with(activity).load(picUrl).into(circleImageView);
                }
            }
            return chatView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ChatObservable.getInstance(counsellor.getId()).addObserver(this);
        Utility.handleOnlineStatus(this, "online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //ChatObservable.getInstance(counsellor.getId()).deleteObserver(this);
        Utility.handleOnlineStatus(null,"");
    }

    @Override
    public void onBackPressed() {
        ChatObservable.getInstance(counsellor.getId()).deleteObserver(this);
        super.onBackPressed();
    }
}
