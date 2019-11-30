package com.careerguide;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Gaurav Gupta(9910781299) on 10/Sep/17-Sunday.
 */

public class VoleyErrorHelper
{
    public static String getMessage (Object error , Context context){
        if(error instanceof TimeoutError){
            return ("Time out error");
        }else if (isServerProblem(error)){
            return handleServerError(error ,context);

        }else if(isNetworkProblem(error)){
            return "No Internet";
        }
        return "Something Went wrong";
    }

    private static String handleServerError(Object error, Context context) {

        VolleyError er = (VolleyError)error;
        NetworkResponse response = er.networkResponse;
        if(response != null){
            switch (response.statusCode){

                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error": "Some error occured" }
                        // Use "Gson" to parse the result
                        /*HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());*/
                        String result = (new JSONObject(new String(response.data))).optString("error");

                        if (result != null) {
                            return result;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return ((VolleyError) error).getMessage();

                default:
                    return "Time out error" + response.statusCode;
            }
        }

        return "Something went wrong";
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    private static boolean isNetworkProblem (Object error){
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }
}
