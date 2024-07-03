package ar.edu.uade.municipio_frontend.Utilities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Comercio;
import ar.edu.uade.municipio_frontend.R;

public class AdapterCormercios extends ArrayAdapter<Comercio> {
    public AdapterCormercios(Context context, List<Comercio> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comercio item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }

        TextView nombre = convertView.findViewById(R.id.itemNombre);
        TextView direccion = convertView.findViewById(R.id.itemDireccion);
        TextView inicio = convertView.findViewById(R.id.itemDescripcion);
        TextView fin = convertView.findViewById(R.id.itemApellido);
        nombre.setText(item.getNombre());
        direccion.setText(item.getDireccion());
        inicio.setText(item.getApertura().substring(0,5));
        fin.setText(item.getCierre().substring(0,5));

        return convertView;
    }
}

