package com.test.myapplication2.app;

/**
 * Created by mina on 7/11/16.
 */

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

import java.util.List;

public class CustomAdapter extends BaseAdapter  {
    Context context;
    LayoutInflater layoutInflater;
    List<TaskHolder> textitems;
    ViewPager pager;
    ListAdapterListener mListAdapterListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(int position); // create callback function
    }

    public CustomAdapter(Context context, List<TaskHolder> list , ListAdapterListener mList) {
        super();
        this.textitems = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        mListAdapterListener = mList;
    }

    @Override
    public int getCount() {

        return textitems.size();
    }

    @Override
    public Object getItem(int position) {
        return textitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return textitems.indexOf(getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final ViewHolder holder;
        final int pos = position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.list, null);

            holder.taskName = (TextView) convertView.findViewById(R.id.task_name_inlist);
            holder.targetAndDone = (TextView) convertView.findViewById(R.id.task_target_done);
            holder.start = (ImageButton) convertView.findViewById(R.id.task_start);
            holder.label = (Button) convertView.findViewById(R.id.task_label);
            pager = (ViewPager) convertView.findViewById(R.id.pager);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            pager = (ViewPager) convertView.findViewById(R.id.pager);

        }

        holder.taskName.setText(textitems.get(position).taskName);
        holder.targetAndDone.setText(textitems.get(position).done + " / " + textitems.get(position).target);
        holder.label.setBackgroundColor(textitems.get(position).color);
//        holder.start.setBackgroundResource(R.mipmap.ic_play_grey600_48dp);

        if (textitems.get(position).target <= textitems.get(position).done){
            holder.start.setBackgroundResource(R.mipmap.ic_checkbox_marked_circle_outline_grey600_36dp);
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.start.setBackgroundResource(R.mipmap.ic_play_grey600_36dp);
        }



        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EventBus bus = EventBus.getDefault();
                bus.post(new StartTaskEvent(textitems.get(pos).taskName));

            }
        });

        return convertView;
    }


    class ViewHolder {
        TextView taskName;
        TextView targetAndDone;
        ImageButton start;
        Button label;
    }
}
