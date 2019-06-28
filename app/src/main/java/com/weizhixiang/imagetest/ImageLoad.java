package com.weizhixiang.imagetest;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.weizhixiang.imagetest.adapter.SpaceItemDecoration;
import com.weizhixiang.imagetest.adapter.WaterFallAdapter;
import com.weizhixiang.imagetest.adapter.load_image_adapter;
import com.weizhixiang.imagetest.data.User;
import com.weizhixiang.imagetest.data.image;
import com.weizhixiang.imagetest.data.works;
import com.yanzhenjie.album.Album;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ImageLoad extends AppCompatActivity {
    private ImageView mimage;
    private EditText title;
    private EditText descride;
    private Button button_load;
    private Button button_select;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private File tempFile;
    private works work = new works();
    private List<String> list;
    private List<Uri> listuri = new ArrayList<>();
    private User user = new User();
    private String url;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int ACTIVITY_REQUEST_SELECT_PHOTO =3;
    private String path;
    private BmobFile bmobFile;
    private load_image_adapter mAdapter;
    private int i = 0;
    private int isuccess=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "9bfe7b07ff60f8bc689320203b96149d");
        Fresco.initialize(this);//初始化框架
        setContentView(R.layout.activity_image_load);
        mimage=findViewById(R.id.mImage);
        title = findViewById(R.id.title);
        descride = findViewById(R.id.descride);
        button_select = findViewById(R.id.button_select);
        button_load = findViewById(R.id.button_load);
        mRecyclerView = findViewById(R.id.load_Rec);

        Init();

        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.addItemDecoration(new SpaceItemDecoration(1, 1));
        //mAdapter = new load_image_adapter(ImageLoad.this,list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setAdapter(mAdapter);
        button_select.setOnClickListener(selectOnClickListener);
        button_load.setOnClickListener(loadOnClickListener);



    }

    private void Init(){
        Intent intent = getIntent();
        //user.setUsername(intent.getStringExtra(MainActivity.USERNAME));
        BmobQuery<User> query = new BmobQuery<User>();
        //String bql = "select * from _User where username = "+intent.getStringExtra(MainActivity.USERNAME);
        //query.setSQL(bql);
        query.addWhereEqualTo("username",intent.getStringExtra(MainActivity.USERNAME));
        query.findObjects(this,new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list!=null && list.size()>0){
                    user = list.get(0);
                }else{
                    Log.i("smile", "查询成功，无数据返回");
                }
            }
            @Override
            public void onError(int i, String s) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateworks(String createdAt){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = null;
        try {
            createdAtDate = sdf.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
        BmobQuery<works> worksBmobQuery = new BmobQuery<>();
        worksBmobQuery.addWhereGreaterThan("createdAt", bmobCreatedAtDate);
        worksBmobQuery.findObjects(ImageLoad.this, new FindListener<works>() {
            @Override
            public void onSuccess(List<works> list) {
                //work = list.get(0);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private Button.OnClickListener selectOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            //getPicFromAlbm();
            Album.startAlbum(ImageLoad.this,ACTIVITY_REQUEST_SELECT_PHOTO, 9);
        }
    };


    private Button.OnClickListener loadOnClickListener = new Button.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            long currentTime = System.currentTimeMillis();
            String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
            Toast.makeText(ImageLoad.this, createdAt,3*1000).show();
            work.setTitle(title.getText().toString());
            work.setDescribe(descride.getText().toString());
            work.setUser(user);
            work.setUsername(user.getUsername());
            work.save(ImageLoad.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(ImageLoad.this, "save success",3*1000).show();

                }
                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(ImageLoad.this, s,3*1000).show();

                }
            });
            updateworks(createdAt);
            Toast.makeText(ImageLoad.this, work.getObjectId(),3*1000).show();
            final String[] listpath=list.toArray(new String[list.size()]);
            bmobFile.uploadBatch(ImageLoad.this,listpath, new UploadBatchListener() {
                @Override
                public void onSuccess(final List<BmobFile> list, final List<String> list1) {
                    image image = new image();
                    image.setWork(work);
                    image.setImage(list.get(i));
                    image.setUrl(list.get(i).getUrl());
                    image.save(ImageLoad.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onFailure(int i, String s) {
                        }
                    });
                    if(list.size()==listpath.length){//如果数量相等，则代表文件全部上传完成
                        Toast.makeText(ImageLoad.this, "上传成功",3*1000).show();
                        ImageLoad.this.finish();
                    }
                    if (i == 0)
                    {
                        work.setUrl(list.get(0).getUrl());
                        work.update(ImageLoad.this);
                    }
                    i++;
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {
                }

                @Override
                public void onError(int i, String s) {

                }

            });
//            for (i=0;i<list.size();i++){
//                bmobFile = new BmobFile(new File(list.get(i)));
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (i==isuccess){
//                isuccess=0;
//                Toast.makeText(ImageLoad.this, "上传成功",3*1000).show();
//            }
//            else {
//                isuccess=0;
//                Toast.makeText(ImageLoad.this, "上传失败", 3 * 1000).show();
//            }
        }
    };
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
            case ACTIVITY_REQUEST_SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    list = Album.parseResult(intent);
//                     //Toast.makeText(ImageLoad.this, list.get(0),3*1000).show();
//                     if (list!=null&&list.size()>0) {
//                         if(listuri.size()>0)
//                             listuri.clear();
//                         for (int i = 1; i < list.size(); i++) {
//                             listuri.add(getMediaUriFromPath(ImageLoad.this, list.get(i)));
//                         }
//                     }
                    Fresco.initialize(this);//初始化框架
                    //mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new load_image_adapter(ImageLoad.this,list);
                    mRecyclerView.setAdapter(mAdapter);
                    //Toast.makeText(ImageLoad.this, listuri.get(0).toString(),3*1000).show();
                }
                else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                    // 根据需要提示用户取消了选择。
                }
                break;
            //调用相册后返回
            case ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    //cropPhoto(uri);//裁剪图片
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
                        mimage.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    path = getPath(ImageLoad.this,uri);

                }
                break;

        }
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    private String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }private String getDataColumn(Context context, Uri uri, String selection,
                                  String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }/**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[] {path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if(cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        cursor.close();
        return uri;
    }

}
