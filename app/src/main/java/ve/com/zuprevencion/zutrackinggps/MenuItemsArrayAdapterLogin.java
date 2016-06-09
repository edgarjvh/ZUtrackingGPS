package ve.com.zuprevencion.zutrackinggps;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuItemsArrayAdapterLogin extends ArrayAdapter<MenuItemsLogin> {

    Context context;
    int layoutResourceId;
    MenuItemsLogin[] data = null;

    public MenuItemsArrayAdapterLogin(Context context, int layoutResourceId, MenuItemsLogin[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.iconoItem);
            holder.txtTitle = (TextView)row.findViewById(R.id.tituloItem);

            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        MenuItemsLogin weather = data[position];

        holder.txtTitle.setText(weather.title);
        holder.imgIcon.setImageResource(weather.icon);

        return row;
    }

    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}