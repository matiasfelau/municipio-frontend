package ar.edu.uade.municipio_frontend.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ar.edu.uade.municipio_frontend.R;

public class ImagenAdapter extends RecyclerView.Adapter<ImagenAdapter.ImagenViewHolder> {

    private List<Uri> imageUris;

    public ImagenAdapter(List<Uri> imageUris) {
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ImagenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagen, parent, false);
        return new ImagenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        Glide.with(holder.itemView.getContext()).load(imageUri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public static class ImagenViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImagenViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
        }
    }
}
