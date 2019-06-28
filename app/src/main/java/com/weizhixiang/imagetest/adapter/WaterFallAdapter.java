package com.weizhixiang.imagetest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.weizhixiang.imagetest.OnItemClickListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import com.weizhixiang.imagetest.MainActivity;
import com.weizhixiang.imagetest.R;
import com.weizhixiang.imagetest.data.works;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class WaterFallAdapter extends RecyclerView.Adapter<WaterFallAdapter.MyViewHolder> {

    private Context mContext;
    private List<works> works;
    private OnItemClickListener mClickListener;
    //private List<image> images;//定义数据源

    //定义构造方法，默认传入上下文和数据源
    public WaterFallAdapter(Context context, List<works> works) {
        this.mContext = context;
        this.works = works;
        //Fresco.initialize(mContext);
        //this.images = images;
    }

    @Override  //将ItemView渲染进来，创建ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent, false);
        //view.setOnClickListener(this);
        return new MyViewHolder(view,mClickListener);
    }

    @Override  //将数据源的数据绑定到相应控件上
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //MyViewHolder holder2 = (MyViewHolder) holder;
        com.weizhixiang.imagetest.data.works work  = new works();
        work = works.get(position);
        Log.e(TAG, "onBindViewHolder: "+work.getUrl());
        Uri uri = Uri.parse(work.getUrl());
        holder.userAvatar.setImageURI(uri);
        //Picasso.with(mContext).load(works.get(position).getUrl()).into(holder.userAvatar);
        holder.userAvatar.getLayoutParams().height =(position % 2) * 100 + 400;
        holder.userName.setText(work.getUsername());
        holder.title.setText(work.getTitle());
    }

    @Override
    public int getItemCount() {
        return works.size();

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }


    //定义自己的ViewHolder，将View的控件引用在成员变量上
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SimpleDraweeView userAvatar;
        TextView userName;
        TextView title;
        ImageView work_favorite;
        TextView work_numbers;
        private OnItemClickListener mListener;

        public MyViewHolder(View itemView ,OnItemClickListener listener) {
            super(itemView);
            userAvatar = (SimpleDraweeView)itemView.findViewById(R.id.user_avatar);
            userName =  (TextView)itemView.findViewById(R.id.work_username);
            title = (TextView)itemView.findViewById(R.id.work_title);
            work_favorite = (ImageView)itemView.findViewById(R.id.work_favorite);
            work_numbers = (TextView)itemView.findViewById(R.id.work_numbers);

            mListener = listener;
            // 为ItemView添加点击事件
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getAdapterPosition());
        }
    }
}
