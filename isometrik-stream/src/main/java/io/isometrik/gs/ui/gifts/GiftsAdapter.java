package io.isometrik.gs.ui.gifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmGiftItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import java.util.ArrayList;

/**
 * Recycler view adapter for the Gifts items{@link GiftsModel}.
 *
 * @see GiftsModel
 */
public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.ViewHolderGifts> {

  private final ArrayList<GiftsModel> gifts;
  private final Context mContext;

  /**
   * Instantiates a new Gifts adapter.
   *
   * @param gifts the gifts
   * @param mContext the m context
   */
  public GiftsAdapter(ArrayList<GiftsModel> gifts, Context mContext) {
    this.gifts = gifts;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public ViewHolderGifts onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new ViewHolderGifts(
        IsmGiftItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolderGifts viewHolder, int position) {
    try {
      GiftsModel gift = gifts.get(position);

      viewHolder.ismGiftItemBinding.tvGiftName.setText(gift.getGiftName());
      viewHolder.ismGiftItemBinding.tvCoinValue.setText(
          mContext.getString(R.string.ism_coins, String.valueOf(gift.getCoinValue())));

      try {

        GlideApp.with(mContext)
            .load(gift.getMessage())
            .fitCenter()
            .into(viewHolder.ismGiftItemBinding.ivGift);
      } catch (IllegalArgumentException | NullPointerException ignore) {

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return gifts.size();
  }

  /**
   * The type View holder gifts.
   */
  static class ViewHolderGifts extends RecyclerView.ViewHolder {

    private final IsmGiftItemBinding ismGiftItemBinding;

    public ViewHolderGifts(final IsmGiftItemBinding ismGiftItemBinding) {
      super(ismGiftItemBinding.getRoot());
      this.ismGiftItemBinding = ismGiftItemBinding;
    }
  }
}