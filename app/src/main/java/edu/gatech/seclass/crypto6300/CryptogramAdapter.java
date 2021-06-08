package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CryptogramAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Cryptogram> cryptograms;

    public CryptogramAdapter(Context context, ArrayList<Cryptogram> cryptograms){
        this.ctx=context;
        this.cryptograms=cryptograms;
    }

    @Override
    public int getCount() {
        return this.cryptograms.size();
    }

    @Override
    public Object getItem(int position) {
        return cryptograms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.cryptorow,null);
        TextView title=(TextView)convertView.findViewById(R.id.titleP);
        TextView creator=(TextView)convertView.findViewById(R.id.creator);
        TextView plays=(TextView)convertView.findViewById(R.id.played);
        TextView win=(TextView)convertView.findViewById(R.id.win);

        Cryptogram c=cryptograms.get(position);

        title.setText(c.getTitle());
        plays.setText(String.valueOf(c.getNumCompleted()));
        creator.setText(c.getCreatorName());
        float winRate;
        if (c.getNumCompleted().equals(0)) {
            winRate = 0;
        } else {
            winRate = (float)c.getNumSolved() / c.getNumCompleted();
        }
        float winRate2d = 100 * round(winRate,2);
        win.setText(String.valueOf(winRate2d) + "%");
        title.setMovementMethod(LinkMovementMethod.getInstance());

        return convertView;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}