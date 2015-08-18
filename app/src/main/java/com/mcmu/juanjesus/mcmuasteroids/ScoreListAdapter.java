package com.mcmu.juanjesus.mcmuasteroids;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

public class ScoreListAdapter extends BaseAdapter {


    //region Private Member Variables
    private final Activity activity;
    private final Vector<String> list;
    //endregion


    //region Constructors
    public ScoreListAdapter(Activity activity, Vector<String> list) {
        super();
        this.activity = activity;
        this.list = list;
    }
    //endregion

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.elementAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.activity_scores_list_item, null, true);
        TextView textView = (TextView)view.findViewById(R.id.txtv_score_list_item_title);
        textView.setText(list.elementAt(position));
        ImageView imageView = (ImageView)view.findViewById(R.id.img_score_list_item_icon);
        switch (Math.round((float)Math.random()*3)) {
            case 0:
                imageView.setImageResource(R.drawable.asteroid1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.asteroid2);
                break;
            default:
                imageView.setImageResource(R.drawable.asteroid3);
                break;
        }

        return view;

    }
}
