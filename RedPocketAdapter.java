package com.yunongwang.yunongwang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.yunongwang.yunongwang.R;
import com.yunongwang.yunongwang.bean.Redpocket_Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANGDONGFEI on 2018/5/20.
 */

public class RedPocketAdapter extends BaseAdapter {
    Context context;
    public List<Integer>imageViews = new ArrayList<>();

    public RedPocketAdapter(Context context, List<Integer> imageViews) {
        this.context = context;
        this.imageViews = imageViews;
    }
    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public Object getItem(int position) {
        return imageViews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.redpocketitem, null);
//            holder.img=(ImageView) convertView.findViewById(R.id.redpocket_img);
            holder.img = (ImageView) convertView.findViewById(R.id.img_redpacket);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
//        Redpocket_Bean bean = imageViews.get(position);
        holder.img.setImageResource(imageViews.get(position));
        return convertView;
    }
    private class ViewHolder{
        public ImageView img;
    }
}
