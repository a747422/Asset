package com.example.administrator.assetsapp.DropDownMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.assetsapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class ShowListView  extends BaseAdapter{

    private final Context context;
    private final List<HashMap<String, Object>> list;
    private final int resource;

    public ShowListView(Context context, List<HashMap<String,Object>> list, int resource){
            this.context =context;
            this.list=list;
        this.resource=resource;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view ;
        ViewHolder viewHolder;
        HashMap<String,Object> map = list.get(i);
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.TvCardName = (TextView) view.findViewById(R.id.tv_card_name);
            viewHolder.TvPlaceName = (TextView) view.findViewById(R.id.tv_place_name);
            viewHolder.TvInOut = (TextView) view.findViewById(R.id.tv_in_out);
            viewHolder.TvmASKSYNCV2 = (TextView) view.findViewById(R.id.tv_mASKSYNCV2);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.TvCardName.setText(map.get("CardName").toString());
        viewHolder.TvPlaceName.setText(map.get("PlaceName").toString());
        viewHolder.TvInOut.setText(map.get("Inout").toString());
        viewHolder.TvmASKSYNCV2.setText(map.get("MASKSYNCV2").toString());
        return view;
    }



    class ViewHolder {
        TextView TvCardName;
        TextView TvPlaceName;
        TextView TvInOut;
        TextView TvmASKSYNCV2;
    }
}
