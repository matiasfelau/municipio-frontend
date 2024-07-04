package ar.edu.uade.municipio_frontend.Utilities.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Publicacion;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Utilities.IdDescripcion;

public class AdapterPublicacion  extends ArrayAdapter<Publicacion> {
    private LinearLayout contenedorFotos;
    public AdapterPublicacion(Context context, List<Publicacion> items) {
        super(context, 0, items);

    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Publicacion item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_publicacion, parent, false);

        }
        TextView titulo = convertView.findViewById(R.id.textTituloPublicacion);
        TextView descripcion = convertView.findViewById(R.id.textDescripcionPublicacion);
        TextView hora = convertView.findViewById(R.id.textHoraPublicacion);
        TextView autor = convertView.findViewById(R.id.textAutorPublicacion);
        contenedorFotos = convertView.findViewById(R.id.contenedorFotos);
        assert item != null;
        titulo.setText(item.getTitulo());
        descripcion.setText(item.getDescripcion());
        hora.setText(item.getFecha().substring(0, 14));
        autor.setText(item.getAutor());
        for (String url : item.getImageUris()) {
            addImageToLayout(url);
        }
        return convertView;
    }
    private void addImageToLayout(String url) {

        if (contenedorFotos == null) {
            System.out.println("El contenedor de fotos es nulo");
            return;
        }

        ImageView imageView = new ImageView(this.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(params);
        contenedorFotos.addView(imageView);


        // Usar Glide para cargar la imagen desde la URL
        Glide.with(this.getContext())
                .load(url)
                .placeholder(R.drawable.placeholder) // Asegúrate de tener un recurso placeholder
                .error(R.drawable.error) // Asegúrate de tener un recurso de error
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (e != null) {
                            e.logRootCauses("Glide");
                        }
                        System.out.println("Error al cargar la imagen: " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Imagen cargada exitosamente
                        return false;
                    }
                })
                .into(imageView);
        //Picasso.get().load(url).into(imageView);
    }
}
