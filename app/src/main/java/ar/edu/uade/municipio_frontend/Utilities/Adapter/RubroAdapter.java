package ar.edu.uade.municipio_frontend.Utilities.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.Models.Sector;
import ar.edu.uade.municipio_frontend.R;

public class RubroAdapter extends ArrayAdapter<Sector> {

    public RubroAdapter(Context context, List<Sector> items) {
        super(context, 0, items);

    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Sector item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);

        }
        return convertView;
    }
}