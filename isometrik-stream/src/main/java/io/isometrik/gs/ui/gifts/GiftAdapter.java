package io.isometrik.gs.ui.gifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.gifts.response.Gift;


public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    private List<Gift> gifts;
    private Context context;
    private GiftListener listener;
    private String categoryTitle;

    public GiftAdapter(Context context, GiftListener listener, String categoryTitle) {
        this.context = context;
        this.listener = listener;
        this.categoryTitle = categoryTitle;
        gifts = new ArrayList<>();
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_livestream_gift, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        Gift gift = gifts.get(position);
        holder.tvCoins.setText(String.valueOf(gift.getVirtualCurrency()));
        Glide.with(context).asBitmap().load(gift.getGiftImage()).centerCrop().into(holder.ivGift);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onGiftSelect(gift,categoryTitle);
        });
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public class GiftViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGift;
        TextView tvCoins;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGift = itemView.findViewById(R.id.ivGift);
            tvCoins = itemView.findViewById(R.id.tvCoins);
        }

    }

    public interface GiftListener {
        void onGiftSelect(Gift gift, String categoryTitle);
    }
}
