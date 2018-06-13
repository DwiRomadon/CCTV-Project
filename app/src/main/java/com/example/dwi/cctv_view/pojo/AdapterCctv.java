package com.example.dwi.cctv_view.pojo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dwi.cctv_view.R;

import java.util.List;

public class AdapterCctv extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataCctv> item;

    public AdapterCctv(Activity activity, List<DataCctv> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_cctv, null);


        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView judul = (TextView) convertView.findViewById(R.id.judul);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        TextView ip    = (TextView) convertView.findViewById(R.id.ip);

        id.setText(item.get(position).getId());
        judul.setText(item.get(position).getJudul());
        alamat.setText(item.get(position).getAlamat());
        ip.setText(item.get(position).getIp());

        return convertView;
    }
}
