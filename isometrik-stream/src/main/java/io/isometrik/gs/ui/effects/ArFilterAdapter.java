package io.isometrik.gs.ui.effects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmArFiltersItemBinding;
import io.isometrik.gs.ui.utils.ColorsUtil;
import io.isometrik.gs.ui.utils.TextDrawable;
import io.isometrik.gs.rtcengine.ar.AREffect;
import java.util.ArrayList;

/**
 * Recycler view adapter for the Ar filter items{@link AREffect}.
 *
 * @see AREffect
 */
public class ArFilterAdapter extends RecyclerView.Adapter<ArFilterAdapter.ViewHolderArFilters> {

  private final ArrayList<AREffect> mListData;
  private final int textSize;
  private final Context mContext;

  public ArFilterAdapter(Context activity, ArrayList<AREffect> mListData) {

    this.mContext = activity;
    this.mListData = mListData;

    textSize = (int) (activity.getResources().getDisplayMetrics().density * 24);
  }

  @Override
  public int getItemCount() {
    return mListData.size();
  }

  @NonNull
  @Override
  public ViewHolderArFilters onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolderArFilters(
        IsmArFiltersItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolderArFilters vh, int position) {

    final AREffect arEffect = mListData.get(position);

    if (arEffect != null) {

      vh.ismArFiltersItemBinding.tvFilterName.setText(arEffect.getEffectName());
      if (arEffect.isSelected()) {
        vh.ismArFiltersItemBinding.tvFilterName.setTextColor(
            ContextCompat.getColor(mContext, R.color.ism_end_color));

        vh.ismArFiltersItemBinding.ivFilterSelected.setVisibility(View.VISIBLE);
      } else {
        vh.ismArFiltersItemBinding.tvFilterName.setTextColor(Color.WHITE);

        vh.ismArFiltersItemBinding.ivFilterSelected.setVisibility(View.GONE);
      }

      try {
        vh.ismArFiltersItemBinding.ivFilterImage.setImageDrawable(TextDrawable.builder()

            .beginConfig()
            .textColor(Color.WHITE)
            .useFont(Typeface.DEFAULT)
            .fontSize(textSize)
            .bold()
            .toUpperCase()
            .endConfig()

            .buildRound((arEffect.getEffectName().trim()).charAt(0) + "",
                Color.parseColor(ColorsUtil.getColorCode(position % 19))));
      } catch (Exception ignore) {
      }
    }
  }

  public static class ViewHolderArFilters extends RecyclerView.ViewHolder {

    private final IsmArFiltersItemBinding ismArFiltersItemBinding;

    public ViewHolderArFilters(final IsmArFiltersItemBinding ismArFiltersItemBinding) {
      super(ismArFiltersItemBinding.getRoot());
      this.ismArFiltersItemBinding = ismArFiltersItemBinding;
    }
  }
}