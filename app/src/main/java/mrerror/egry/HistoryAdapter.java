package mrerror.egry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mrerror.egry.TinyDB;

/**
 * Created by Ahmed on 4/16/2016.
 */
public class HistoryAdapter extends BaseAdapter {

Context mContext ;
    int res ;
    TinyDB tinyDB;
    ArrayList<String> date ;
    ArrayList<String> dis ;
    ArrayList<String> min ;
    public HistoryAdapter(Context context, int resource){
        this.mContext = context;
        this.res = resource;
        tinyDB=new TinyDB(context);
        if(tinyDB.getListString("DateList")!=null) {
            date = tinyDB.getListString("DateList");
            dis = tinyDB.getListString("DisList");
            min = tinyDB.getListString("TimeList");
        }
    }
    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(res, parent, false);
        TextView numView  = (TextView)row.findViewById(R.id.numHis);
        TextView dateView  = (TextView)row.findViewById(R.id.dateHis);
        TextView DisView  = (TextView)row.findViewById(R.id.disHis);
        TextView TimeView  = (TextView)row.findViewById(R.id.timeHis);
        dateView.setText(date.get(position));
        DisView.setText(dis.get(position)+" KM");
        TimeView.setText(min.get(position)+" Min");
        numView.setText(String.valueOf(position+1)+"- ");
        return row;
    }
}
