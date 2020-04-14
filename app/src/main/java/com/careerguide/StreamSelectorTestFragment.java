package com.careerguide;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StreamSelectorTestFragment extends TestSuperFragment {

    private View view;


    public StreamSelectorTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stream_selector_test, container, false);
        TextView desc = ((TextView) view.findViewById(R.id.desc));
        String string = "Stream selectorTM test is the scientific way to know your right stream after 10th class. Discover your potential - interest and aptitude!";
        desc.setText(Html.fromHtml("Stream selector<sup><small>TM</small></sup> test is the scientific way to know your right stream after 10th class. Discover your potential - interest and aptitude!"));

        SpannableStringBuilder cs = new SpannableStringBuilder(string);
        cs.setSpan(new SuperscriptSpan(), 15, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        cs.setSpan(new RelativeSizeSpan(0.4f), 15, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        desc.setText(cs);
        final Activity activity = getActivity();


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
