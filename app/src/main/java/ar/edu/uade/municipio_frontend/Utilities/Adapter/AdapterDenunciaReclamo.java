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

import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Utilities.IdDescripcion;

public class AdapterDenunciaReclamo extends ArrayAdapter<IdDescripcion> {
    public AdapterDenunciaReclamo(Context context, List<IdDescripcion> items) {
        super(context, 0, items);

    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        IdDescripcion item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.denuncia_list, parent, false);

        }
        TextView id = convertView.findViewById(R.id.itemId);
        TextView descripcion = convertView.findViewById(R.id.itemDescripcion);
        assert item != null;
        id.setText(item.getId());
        descripcion.setText(item.getDescripcion());

        return convertView;
    }

}
