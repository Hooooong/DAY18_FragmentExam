package com.hooooong.fragmentexam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private TextView textView;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        textView = (TextView)view.findViewById(R.id.textView);
        // 값 꺼내기
        Bundle bundle = getArguments();
        if(bundle != null){
            setTextView(bundle.getString("value"));
        }
    }

    public void setTextView(String text){
        textView.setText(text);
    }

}
