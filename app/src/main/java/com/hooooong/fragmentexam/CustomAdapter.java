package com.hooooong.fragmentexam;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Android Hong on 2017-09-27.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    List<String> data;
    Context context;
    ListFragment.CallBack callBack;

    public CustomAdapter(Context context, ListFragment.CallBack callBack, List<String> data) {
        this.context = context;
        this.callBack = callBack;
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.setTextView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        private TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.goDetail(textView.getText().toString());
                }
            });
        }

        public void setTextView(String text) {
            textView.setText(text);
        }
    }
}
