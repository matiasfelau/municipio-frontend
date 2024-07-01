package ar.edu.uade.municipio_frontend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Publicacion;
import ar.edu.uade.municipio_frontend.R;

public class PublicacionAdapter extends BaseAdapter {
    private Context context;
    private List<Publicacion> publicaciones;

    public PublicacionAdapter(Context context, List<Publicacion> publicaciones) {
        this.context = context;
        this.publicaciones = publicaciones;
    }

    @Override
    public int getCount() {
        return publicaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return publicaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_publicacion, parent, false);
        }

        Publicacion publicacion = publicaciones.get(position);

        TextView textTituloPublicacion = convertView.findViewById(R.id.textTituloPublicacion);
        TextView textDescripcionPublicacion = convertView.findViewById(R.id.textDescripcionPublicacion);
        TextView textHoraPublicacion = convertView.findViewById(R.id.textHoraPublicacion);
        TextView textAutorPublicacion = convertView.findViewById(R.id.textAutorPublicacion);

        textTituloPublicacion.setText(publicacion.getTitulo());
        textDescripcionPublicacion.setText(publicacion.getDescripcion());
        textHoraPublicacion.setText(publicacion.getFecha());
        textAutorPublicacion.setText(publicacion.getAutor());

        return convertView;
    }
}
