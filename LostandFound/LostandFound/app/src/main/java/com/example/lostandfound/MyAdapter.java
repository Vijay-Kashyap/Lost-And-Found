package com.example.lostandfound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context cnt;
    ArrayList<details> list;
    public MyAdapter(Context applicationContext,ArrayList<details> list)
    {
        this.cnt = applicationContext;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.lostitem.setText(list.get(i).getLostitem());
        myViewHolder.description.setText(list.get(i).getDescription());
        myViewHolder.owner.setText(list.get(i).getOwner());
        Glide.with(cnt).load(list.get(i).getImagepath()).into(myViewHolder.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lostitem,description,owner;
        ImageView iv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lostitem = itemView.findViewById(R.id.lost_tv);
            description = itemView.findViewById(R.id.description_tv);
            iv = itemView.findViewById(R.id.lostimage);
            owner = itemView.findViewById(R.id.o_name);

        }


    }


}