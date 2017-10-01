package com.hooooong.fragmentexam;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private CallBack callBack;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        if(context instanceof CallBack){
            this.callBack = (CallBack) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        init(view);
        return view;
    }


    private void init(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        adapter = new CustomAdapter(context, callBack,load() );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private List<String> load(){
        List<String> data = new ArrayList<>();

        for(int i = 0; i<100; i++){
            data.add(i+"");
        }
        return data;
    }

    public interface CallBack{
        void goDetail(String value);
    }
}