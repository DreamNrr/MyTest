package adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wzh.mytest.R;

import java.util.ArrayList;

import domain.MediaItem;
import utils.Utils;

/**
 * Created by WZH on 2017/5/19.
 */

public class LocalVideoAdapter extends BaseAdapter {
    private ArrayList<MediaItem> datas;
    private Context context;
    private Utils utils;
    public LocalVideoAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.context = context;
        this.datas = mediaItems;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public MediaItem getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null) {
            view = View.inflate(context, R.layout.item_local_video, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_size = (TextView) view.findViewById(R.id.tv_size);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        MediaItem mediaItem = datas.get(i);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
        viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));
        return view;
    }
    static class ViewHolder{
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;

    }
}
