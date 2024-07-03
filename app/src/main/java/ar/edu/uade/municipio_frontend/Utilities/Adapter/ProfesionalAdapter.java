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
import ar.edu.uade.municipio_frontend.R;

public class ProfesionalAdapter extends ArrayAdapter<Profesional> {

    public ProfesionalAdapter(Context context, List<Profesional> items) {
        super(context, 0, items);

    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Profesional item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }

        TextView nombre = convertView.findViewById(R.id.itemNombre);
        TextView direccion = convertView.findViewById(R.id.itemDireccion);
        TextView inicio = convertView.findViewById(R.id.itemDescripcion);
        TextView fin = convertView.findViewById(R.id.itemApellido);
        assert item != null;
        nombre.setText(item.getNombre());
        direccion.setText(item.getDireccion());
        inicio.setText(item.getInicioJornada().substring(0,5));
        fin.setText(item.getFinJornada().substring(0,5));

        return convertView;
    }
}