package com.weizhixiang.imagetest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.weizhixiang.imagetest.R;
import com.weizhixiang.imagetest.data.works;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class WaterFallAdapter extends RecyclerView.Adapter<WaterFallAdapter.MyViewHolder> {

    private Context mContext;
    private List<works> works;
    //private List<image> images;//定义数据源

    //定义构造方法，默认传入上下文和数据源
    public WaterFallAdapter(Context context, List<works> works) {
        mContext = context;
        this.works = works;
        //this.images = images;
    }

    @Override  //将ItemView渲染进来，创建ViewHolder
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent, false);
        return new MyViewHolder(view);
    }

    @Override  //将数据源的数据绑定到相应控件上
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder holder2 = (MyViewHolder) holder;
        Uri uri = Uri.parse(works.get(position).getUrl());
        Log.d(TAG, "onBindViewHolder: "+works.get(position).getUrl());
        holder2.userAvatar.setImageURI(uri);
        holder2.userAvatar.getLayoutParams().height = works.get(position).height;
        holder2.userName.setText(works.get(position).getUsername());
        holder2.title.setText(works.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (works != null) {
            return works.size();
        }
        return 0;
    }

    //定义自己的ViewHolder，将View的控件引用在成员变量上
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView userAvatar;
        public TextView userName;
        public TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            userAvatar = (SimpleDraweeView) itemView.findViewById(R.id.user_avatar);
            userName = (TextView) itemView.findViewById(R.id.work_username);
            title = itemView.findViewById(R.id.work_title);
        }
    }
}
