package ve.com.zuprevencion.zutrackinggps;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class AdapterPaises extends ArrayAdapter<String>{

    private Activity activity;
    private ArrayList data;
    public Resources res;
    ModelPaises tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public AdapterPaises(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            Resources resLocal
    )
    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.paises_rows, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (ModelPaises) data.get(position);

        TextView nombre = (TextView)row.findViewById(R.id.lblNombrePais);
        TextView codigo = (TextView)row.findViewById(R.id.lblCodigoPais);
        ImageView bandera = (ImageView)row.findViewById(R.id.imgBandera);

        if(position==0){
            // Default selected Spinner item
            nombre.setText(getContext().getResources().getString(R.string.seleccione_pais));
            codigo.setText(null);
            bandera.setImageBitmap(null);
        }
        else
        {   // Set values for spinner each row
            nombre.setText(tempValues.getNombre());
            codigo.setText(tempValues.getCodigo());

            if (tempValues.getBandera() != null){
                bandera.setImageBitmap(tempValues.getBandera());
                //bandera.setImageResource(res.getIdentifier("ve.com.zuprevencion.zutracking:drawable/icononocar",null,null));
            }
        }
        return row;
    }
}