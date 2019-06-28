package com.weizhixiang.imagetest.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.weizhixiang.imagetest.R;

import java.io.File;
import java.util.List;

public class load_image_adapter extends RecyclerView.Adapter<load_image_adapter.MyViewHolder> {
    private Context mContext;
    private List<String> list;

    public load_image_adapter(Context context,List<String> list) {
        this.mContext = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.load_image_item,viewGroup, false);
        return new load_image_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        MyViewHolder holder2 = (MyViewHolder)viewHolder;
        Uri uri = Uri.fromFile(new File(list.get(i)));
        holder2.userAvatar.setImageURI(uri);
        holder2.userAvatar.getLayoutParams().height = 300;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView userAvatar;

        public MyViewHolder(View itemView){
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.load_image);
        }
    }
}
