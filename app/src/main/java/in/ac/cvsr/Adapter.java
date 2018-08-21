package in.ac.cvsr.newapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<Item> {
    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    public Adapter(Context context, ArrayList<Item> itemsArrayList) {
        super(context, R.layout.row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);
        TextView pid = (TextView) rowView.findViewById(R.id.pid);
        TextView pname = (TextView) rowView.findViewById(R.id.pname);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        pid.setText(""+itemsArrayList.get(position).getId());
        pname.setText(""+itemsArrayList.get(position).getName());
        price.setText(""+itemsArrayList.get(position).getPrice());
        return rowView;
    }
}

