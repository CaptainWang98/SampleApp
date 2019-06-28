package com.weizhixiang.imagetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weizhixiang.imagetest.adapter.SpaceItemDecoration;
import com.weizhixiang.imagetest.adapter.WaterFallAdapter;
import com.weizhixiang.imagetest.data.User;
import com.weizhixiang.imagetest.data.image;
import com.weizhixiang.imagetest.data.works;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.scwang.smartrefresh.*;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private WaterFallAdapter mAdapter;
    private User user = new User();
    private List<works> works = new ArrayList<>() ;
    private List<works> data = new ArrayList<>();
    private works work = new works();
    private List<image> images ;
    private static final int REQUEST_CODE_MAINACTIVITY = 1;
    public static final String USERNAME="com.weizhixang.imagetest.MainActivity.USERNAME";
    private int i;

    static {
        //全局构建器
        //Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.albumWhite);
                return new ClassicsHeader(context);
            }
        });
        //Footer
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "9bfe7b07ff60f8bc689320203b96149d");
        Fresco.initialize(MainActivity.this);
        setContentView(R.layout.activity_main);

        try {
            buildworks();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //初始化下拉刷新
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        fab = findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1, 1));
        mAdapter = new WaterFallAdapter(this, data);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        Intent intent = getIntent();
        user.setUsername(intent.getStringExtra(LoginActivity.USERNAME));
        fab.setOnClickListener(fabClickListeber);
        mRecyclerView.setAdapter(mAdapter);
        Toast.makeText(MainActivity.this,"hello："+user.getUsername(),3*1000).show();
    }

    private FloatingActionButton.OnClickListener fabClickListeber = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,ImageLoad.class);
            if(user.getUsername()!=null){
                intent.putExtra(MainActivity.USERNAME,user.getUsername());
                startActivityForResult(intent, MainActivity.REQUEST_CODE_MAINACTIVITY);
        }
            else{
                Toast.makeText(MainActivity.this,"请登录！",3*1000).show();
            }
        }
    };

    private void buildworks() throws ParseException {
        String createdAt = "2019-6-23 10:30:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = sdf.parse(createdAt);
        BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
        BmobQuery<works> worksBmobQuery = new BmobQuery<>();
        worksBmobQuery.addWhereGreaterThan("createdAt", bmobCreatedAtDate);
        worksBmobQuery.findObjects(MainActivity.this, new FindListener<com.weizhixiang.imagetest.data.works>() {
            @Override
            public void onSuccess(List<com.weizhixiang.imagetest.data.works> list) {
                if (list!=null && list.size()>0){
                    //works = list;
//                    for( i = 0;i<list.size();i++){
//                        work = list.get(i);
//                        BmobQuery<image> imageBmobQuery = new BmobQuery<>();
//                        imageBmobQuery.addWhereEqualTo("work",list.get(i).getObjectId());
//                        //Toast.makeText(MainActivity.this, list.glet(i).getObjectId(),3*1000).show();
//                        //imageBmobQuery.include("url");
//                        imageBmobQuery.findObjects(MainActivity.this, new FindListener<image>() {
//                            @Override
//                            public void onSuccess(List<image> list) {
//                                if (list!=null && list.size()>0) {
//                                    work.setUrl(list.get(0).getUrl());
//                                    work.height = (i % 2) * 100 + 400;
//                                    data.add(work);
//                                }
//                            }
//                            @Override
//                            public void onError(int i, String s) {
//                                Toast.makeText(MainActivity.this, s,3*1000).show();
//                            }
//                        });
//                    }
                    for (i=0;i<list.size();i++){
                        work = list.get(i);
                        work.height= (i % 2) * 100 + 400;
                        data.add(work);
                        //Toast.makeText(MainActivity.this,work.getTitle(),3*1000).show();
                    }
                }
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this, s,3*1000).show();
            }
        });
    }

//    private List<works> buildData() {
//
//        String[] names = {"邓紫棋","范冰冰","杨幂","Angelababy","唐嫣","柳岩"};
//        String[] imgUrs = {"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1477122728&di=21924aeef8f7847a651fc8bf00a28f49&src=http://www.tengtv.com/file/upload/201609/18/232836341.jpg",
//                "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1477122795&di=f740bd484870f9bcb0cafe454a6465a2&src=http://tpic.home.news.cn/xhCloudNewsPic/xhpic1501/M08/28/06/wKhTlVfs1h2EBoQfAAAAAF479OI749.jpg",
//                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg",
//                "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=fd90a83e900a304e4d22a7fae1c9a7c3/d01373f082025aafa480a2f1fcedab64034f1a5d.jpg",
//                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg",
//                "https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=005560fc8b5494ee982208191df4e0e1/c2fdfc039245d68827b453e7a3c27d1ed21b243b.jpg",
//        };
//
//        List<works> list = new ArrayList<>();
//        for(int i=0;i<6;i++) {
//            works p = new works();
//            p.avatarUrl = imgUrs[i];
//            p.name = names[i];
//            p.imgHeight = (i % 2)*100 + 400; //偶数和奇数的图片设置不同的高度，以到达错开的目的
//            list.add(p);
//        }
//
//        return list;
//    }


    /**
     * 回调接口
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CODE_MAINACTIVITY:
                if (resultCode == RESULT_OK) {
                    user.setUsername(intent.getStringExtra(LoginActivity.USERNAME));
                }
        }
    }



}
