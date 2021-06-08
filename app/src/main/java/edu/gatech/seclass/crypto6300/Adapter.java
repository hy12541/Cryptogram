package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    Context ctx;
    ArrayList<Player> players;

    public Adapter(Context context, ArrayList<Player> players){
        this.ctx=context;
        this.players=players;
    }

    @Override
    public int getCount() {
        return this.players.size();
    }

    @Override
    public Object getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.player,null);
        TextView name=(TextView)convertView.findViewById(R.id.userNameP);
        TextView plays=(TextView)convertView.findViewById(R.id.times);
        TextView points=(TextView)convertView.findViewById(R.id.pointsP);

        Player p=players.get(position);

        name.setText(p.getUserName());
        plays.setText(String.valueOf(p.getPlayTimes()));
        points.setText(String.valueOf(p.getPoints()));

        return convertView;
    }
}
