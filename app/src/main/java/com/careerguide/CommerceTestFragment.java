package com.careerguide;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommerceTestFragment extends TestSuperFragment {

    private View view;


    public CommerceTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_commerce_test, container, false);
        return view;
    }

    void increaseHeight()
    {
        view.findViewById(R.id.parentRL).setPadding(0,Utility.getPx(20),0,Utility.getPx(20));
    }

    void decreaseHeight()
    {
        view.findViewById(R.id.parentRL).setPadding(0,Utility.getPx(40),0,Utility.getPx(40));
    }

}
