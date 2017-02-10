package com.bwelco.sumsungsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bwelco on 2016/6/5.
 */
public class MyListAdapter extends ArrayAdapter<String> {
    static HashMap<Integer, Bitmap> bitmaps;
    int res_id;
    MainActivity ac;
    public MyListAdapter(Context context, int resource, List<String> objects, MainActivity ac) {
        super(context, resource, objects);
        this.res_id = resource;
        this.ac = ac;
        bitmaps = new HashMap<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder = new MyHolder();
        String text = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(this.res_id, null);
            holder.textView = (TextView) convertView.findViewById(R.id.Listtext);
            holder.imageview = (ImageView) convertView.findViewById(R.id.image_item) ;
            convertView.setTag(holder);
        }else {
            holder = (MyHolder) convertView.getTag();
        }

        try {
            int bstart = text.indexOf(ac.getTextString());
            int bend = bstart + ac.getTextString().length();

            if (bstart == -1){
                holder.textView.setText(text);
            }else {
                SpannableStringBuilder style = new SpannableStringBuilder(text);
                int color = getContext().getResources().getColor(R.color.textColor);
                style.setSpan(new ForegroundColorSpan(color), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //    style.setSpan(new ForegroundColorSpan(Color.RED),fstart,fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                holder.textView.setText(style);
            }
        }catch (Exception e){

        }

        holder.imageview.setImageBitmap(readBitMap(getContext(), R.mipmap.search));
        return convertView;
    }

    class  MyHolder{
        public TextView textView;
        public ImageView imageview;
    }

    public static Bitmap readBitMap(Context context, int resId){
        if (bitmaps.containsKey(resId)) {
            return bitmaps.get(resId);
        } else {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            InputStream is = context.getResources().openRawResource(resId);
            Bitmap ret = BitmapFactory.decodeStream(is, null, opt);
            bitmaps.put(resId, ret);
            return ret;
        }
    }
}
