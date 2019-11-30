package com.careerguide;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "fragment_blank";
    private int layoutResId;
    private int pic;
    private String title, header;
    private String desc;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(layoutResId, container, false);
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.desc)).setText(desc);
        ((ImageView) view.findViewById(R.id.pic)).setImageResource(pic);
        if(header != null)
        {
            ((TextView) view.findViewById(R.id.header)).setText(header);
            view.findViewById(R.id.header).setVisibility(View.VISIBLE);
        }
        return view;
    }

    public static BlankFragment newInstance(int pic, int layoutResId, String header, String title, String desc) {
        BlankFragment sampleSlide = new BlankFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        args.putInt("pic",pic);
        args.putString("title", title);
        args.putString("desc",desc);
        args.putString("header",header);
        sampleSlide.setArguments(args);
        return sampleSlide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
            pic = getArguments().getInt("pic");
            title = getArguments().getString("title");
            desc = getArguments().getString("desc");
            header = getArguments().getString("header");
        }
    }

}
