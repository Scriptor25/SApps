package io.scriptor.sapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import io.scriptor.sapps.R;
import java.util.List;

public class SlideAdapter<VH extends RecyclerView.ViewHolder> extends ListAdapter<String, VH> {

    public SlideAdapter(Context context, List<String> data) {
        super(context, R.layout.adapter_slide, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_slide, null);

        ImageView data = view.findViewById(R.id.data);
        Glide.with(getContext()).load(getItem(position)).into(data);

        return view;
    }
}
