package pager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wzh.mytest.R;

import java.util.ArrayList;

import activity.SystemVideoPlayerActivity;
import adapter.LocalVideoAdapter;
import domain.MediaItem;
import fragment.BaseFragment;

/**
 * Created by WZH on 2017/5/19.
 */

public class LocalVideoPager extends BaseFragment {
    private ListView lv;
    private TextView tv_nodata;
    private LocalVideoAdapter adapter;
    private ArrayList<MediaItem> mediaItems;
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_local_video_pager, null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MediaItem item = adapter.getItem(i);
                Toast.makeText(context, ""+item.toString(), Toast.LENGTH_SHORT).show();

                //把系统的播放器调起来
                Intent intent = new Intent(context, SystemVideoPlayerActivity.class);
                intent.setDataAndType(Uri.parse(item.getData()),"video/*");
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getData();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size()>0) {
                tv_nodata.setVisibility(View.GONE);
                adapter = new LocalVideoAdapter(context,mediaItems);
                lv.setAdapter(adapter);
            }else {
                tv_nodata.setVisibility(View.INVISIBLE);
            }

        }
    };

    private void getData() {
        new Thread(){
            public void run(){
                mediaItems = new ArrayList<>();
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                  MediaStore.Video.Media.DISPLAY_NAME,
                  MediaStore.Video.Media.DURATION,
                  MediaStore.Video.Media.SIZE,
                  MediaStore.Video.Media.DATA
                };
                Cursor cursor = contentResolver.query(uri, objs, null, null, null);
                if(cursor!=null) {
                    while (cursor.moveToNext()){
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        Long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        Long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        mediaItems.add(new MediaItem(name,duration,size,data));
                        handler.sendEmptyMessage(0);
                    }
                    cursor.close();
                }
            }
        }.start();
    }
}
