package ar.edu.uade.municipio_frontend.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import ar.edu.uade.municipio_frontend.R;

public class ImagenPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Uri> imageUris;

    public ImagenPagerAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @Override
    public int getCount() {
        return imageUris.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_imagen, container, false);
        ImageView imageView = view.findViewById(R.id.imageViewItem);
        Uri imageUri = imageUris.get(position);
        Glide.with(context).load(imageUri).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
