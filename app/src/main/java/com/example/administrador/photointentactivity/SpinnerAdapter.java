package com.example.administrador.photointentactivity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrador on 24/10/2017.
 */
public class SpinnerAdapter extends ArrayAdapter<SpinnerModel> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    ArrayList<SpinnerModel> values;

    public SpinnerAdapter(Context context, int textViewResourceId,
                          ArrayList<SpinnerModel> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    /*public SpinnerAdapter(Context context, int textViewResourceId,
                       String[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }
*/
    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public SpinnerModel getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getName() );


        //////////////////////////////////////////////////////////
        //View view = super.getView(position, convertView, parent);
        //view.setPadding(10, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());

        label.setPadding(values.get(position).getLevel()*10 , label.getPaddingTop()+10, label.getPaddingRight()+10, label.getPaddingBottom()+10);

        //////////////////////////////////////////////////////////

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName() );

        label.setPadding(values.get(position).getLevel()*30,  label.getPaddingTop()+10, label.getPaddingRight()+10, label.getPaddingBottom()+10);

        return label;
    }
}