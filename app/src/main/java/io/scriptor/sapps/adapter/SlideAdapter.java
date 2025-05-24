package io.scriptor.sapps.adapter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class SlideAdapter extends ListAdapter<String, SlideViewHolder> {

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {

                @Override
                public boolean areItemsTheSame(String o, String n) {
                    return o == n;
                }

                @Override
                public boolean areContentsTheSame(String o, String n) {
                    return o.equals(n);
                }
            };

    public SlideAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public void onBindViewHolder(SlideViewHolder holder, int position) {}

    @Override
    public SlideViewHolder onCreateViewHolder(ViewGroup group, int position) {
        return null;
    }
}
