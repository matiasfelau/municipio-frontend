package ar.edu.uade.municipio_frontend.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.R;

public class CustomAdapter extends ArrayAdapter<Profesional> {

    public CustomAdapter(Context context, List<Profesional> items) {
        super(context, 0, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Profesional item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }

        TextView textView = convertView.findViewById(R.id.textView);

        textView.setText(item.toString());

        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        return convertView;
    }
}