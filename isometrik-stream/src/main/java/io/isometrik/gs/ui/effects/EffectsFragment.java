package io.isometrik.gs.ui.effects;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetEffectsBinding;
import io.isometrik.gs.ui.databinding.IsmDialogDownloadFiltersBinding;
import io.isometrik.gs.ui.utils.RecyclerItemClickListener;
import io.isometrik.gs.rtcengine.ar.AREffect;
import io.isometrik.gs.rtcengine.ar.AROperations;
import io.isometrik.gs.rtcengine.ar.DownloadZipResult;
import io.isometrik.gs.rtcengine.ar.FiltersConfig;
import io.isometrik.gs.rtcengine.ar.ZipUtils;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Effects bottomsheet dialog fragment to allow publisher to apply voice{@link } or AR
 * filters{@link AREffect}.
 *
 * @see AREffect
 * @see
 */
public class EffectsFragment extends BottomSheetDialogFragment implements DownloadZipResult {

  public static final String TAG = "EffectsFragment";
  private Activity activity;

  private ArrayList<AREffect> arFilterItems;
  private ArFilterAdapter arFilterAdapter;
  private AROperations arOperations;


  private int selectedArTabPosition;
  private String selectedSlot = AROperations.SLOT_MASKS;
  private boolean downloadRequired;
  private boolean filtersDownloadedAlready;
  private AlertDialog alertDialogDownloadFilters;
  private ProgressDialog dialog;
  private boolean showAudioEffects;
  private boolean showVideoEffects;

  private IsmBottomsheetEffectsBinding ismBottomsheetEffectsBinding;

  public EffectsFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetEffectsBinding == null) {
      ismBottomsheetEffectsBinding =
          IsmBottomsheetEffectsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetEffectsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetEffectsBinding.getRoot().getParent()).removeView(
            ismBottomsheetEffectsBinding.getRoot());
      }
    }
    filtersDownloadedAlready = FiltersConfig.isFiltersDownloadedAlready();
    downloadRequired = FiltersConfig.isDownloadRequired();

    if (dialog == null) {
      dialog = new ProgressDialog(activity);
      dialog.setCancelable(false);
      arOperations = IsometrikStreamSdk.getInstance().getIsometrik().getAROperations();

      if (showVideoEffects) {
        ismBottomsheetEffectsBinding.tabLayoutFilters.addTab(
            ismBottomsheetEffectsBinding.tabLayoutFilters.newTab()
                .setText(getString(R.string.ism_masks))
                .setIcon(R.drawable.ism_ic_ar_mask));
        ismBottomsheetEffectsBinding.tabLayoutFilters.addTab(
            ismBottomsheetEffectsBinding.tabLayoutFilters.newTab()
                .setText(getString(R.string.ism_effects))
                .setIcon(R.drawable.ism_ic_ar_effect));
        ismBottomsheetEffectsBinding.tabLayoutFilters.addTab(
            ismBottomsheetEffectsBinding.tabLayoutFilters.newTab()
                .setText(getString(R.string.ism_filters))
                .setIcon(R.drawable.ism_ic_ar_filter));

        ismBottomsheetEffectsBinding.rvFilters.setVisibility(View.VISIBLE);
        ismBottomsheetEffectsBinding.rvVoiceFilters.setVisibility(View.GONE);
      } else {
        ismBottomsheetEffectsBinding.rvFilters.setVisibility(View.GONE);
        ismBottomsheetEffectsBinding.rvVoiceFilters.setVisibility(View.VISIBLE);
      }

      if (showAudioEffects) {
        ismBottomsheetEffectsBinding.tabLayoutFilters.addTab(
            ismBottomsheetEffectsBinding.tabLayoutFilters.newTab()
                .setText(getString(R.string.ism_voice_change))
                .setIcon(R.drawable.ism_ic_voice_change));
        ismBottomsheetEffectsBinding.tabLayoutFilters.addTab(
            ismBottomsheetEffectsBinding.tabLayoutFilters.newTab()
                .setText(getString(R.string.ism_reverberation))
                .setIcon(R.drawable.ism_ic_reverberation));
      }

      ismBottomsheetEffectsBinding.rvFilters.setLayoutManager(
          new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

      arFilterItems = new ArrayList<>();
      arFilterItems.addAll(arOperations.getMasks());

      arFilterAdapter = new ArFilterAdapter(activity, arFilterItems);
      ismBottomsheetEffectsBinding.rvFilters.setAdapter(arFilterAdapter);

//      ismBottomsheetEffectsBinding.rvVoiceFilters.setLayoutManager(
//          new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
//
//
//      ismBottomsheetEffectsBinding.rvVoiceFilters.setAdapter(voiceFilterAdapter);

      if (downloadRequired) {
        if (filtersDownloadedAlready) {
          ismBottomsheetEffectsBinding.rlDownload.setVisibility(View.GONE);
        } else {
          ismBottomsheetEffectsBinding.rlDownload.setVisibility(View.VISIBLE);
        }

        ismBottomsheetEffectsBinding.rlDownload.setOnClickListener(v -> {

          AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

          IsmDialogDownloadFiltersBinding ismDialogDownloadFiltersBinding =
              IsmDialogDownloadFiltersBinding.inflate((activity).getLayoutInflater());

          ismDialogDownloadFiltersBinding.tvSize.setText(FiltersConfig.DEEPAR_SIZE);

          ismDialogDownloadFiltersBinding.tvDownloadFilters.setEnabled(true);
          ismDialogDownloadFiltersBinding.tvDownloadFilters.setText(
              getString(R.string.ism_download_filters));
          ismDialogDownloadFiltersBinding.tvCancel.setVisibility(View.VISIBLE);
          ismDialogDownloadFiltersBinding.tvDownloadFilters.setOnClickListener(view -> {
            ismDialogDownloadFiltersBinding.tvDownloadFilters.setEnabled(false);
            ismDialogDownloadFiltersBinding.tvDownloadFilters.setText(
                getString(R.string.ism_downloading_filters));
            ismDialogDownloadFiltersBinding.tvCancel.setVisibility(View.GONE);
            ZipUtils.downloadEffects(0, this, ismDialogDownloadFiltersBinding.pbDownload);
          });

          alertDialog.setView(ismDialogDownloadFiltersBinding.getRoot());
          alertDialog.setCancelable(false);
          alertDialogDownloadFilters = alertDialog.create();
          if (!activity.isFinishing() && !activity.isDestroyed()) alertDialogDownloadFilters.show();
          ismDialogDownloadFiltersBinding.tvCancel.setOnClickListener(
              view -> alertDialogDownloadFilters.dismiss());
        });
      } else {
        ismBottomsheetEffectsBinding.rlDownload.setVisibility(View.GONE);
      }

      ismBottomsheetEffectsBinding.rvFilters.addOnItemTouchListener(
          new RecyclerItemClickListener(activity, ismBottomsheetEffectsBinding.rvFilters,
              new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {

                  if (position >= 0) {
                    AREffect filterItem = arFilterItems.get(position);

                    if (filterItem != null) {

                      if (downloadRequired) {

                        if (filtersDownloadedAlready) {
                          if (filterItem.isSelected()) {

                            deselectCurrentArFilter(position);
                          } else {

                            selectCurrentArFilter(position);
                          }
                        } else {
                          Toast.makeText(activity, getString(R.string.filters_download_required),
                              Toast.LENGTH_SHORT).show();
                        }
                      } else {
                        if (filterItem.isSelected()) {

                          deselectCurrentArFilter(position);
                        } else {

                          selectCurrentArFilter(position);
                        }
                      }
                    }
                  }
                }

                @Override
                public void onItemLongClick(View view, final int position) {

                }
              }));

      ismBottomsheetEffectsBinding.rvVoiceFilters.addOnItemTouchListener(
          new RecyclerItemClickListener(activity, ismBottomsheetEffectsBinding.rvVoiceFilters,
              new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {

                }

                @Override
                public void onItemLongClick(View view, final int position) {

                }
              }));

      ismBottomsheetEffectsBinding.tabLayoutFilters.addOnTabSelectedListener(
          new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              selectedArTabPosition =
                  ismBottomsheetEffectsBinding.tabLayoutFilters.getSelectedTabPosition();
              if (tab.getIcon() != null) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
              }
              switch (selectedArTabPosition) {

                case 0: {

                  if (showVideoEffects) {
                    //Masks selected
                    //tvClearArFilters.setVisibility(View.VISIBLE);
                    ismBottomsheetEffectsBinding.rvFilters.setVisibility(View.VISIBLE);
                    ismBottomsheetEffectsBinding.rvVoiceFilters.setVisibility(View.GONE);
                    selectedSlot = AROperations.SLOT_MASKS;
                    arFilterItems.clear();
                    arFilterItems.addAll(arOperations.getMasks());
                    arFilterAdapter.notifyDataSetChanged();
                  } else {
                    //Voice changer selected
                    //tvClearArFilters.setVisibility(View.INVISIBLE);

                  }
                  break;
                }
                case 1: {

                  if (showVideoEffects) {
                    //Effects selected
                    //tvClearArFilters.setVisibility(View.VISIBLE);
                    ismBottomsheetEffectsBinding.rvFilters.setVisibility(View.VISIBLE);
                    ismBottomsheetEffectsBinding.rvVoiceFilters.setVisibility(View.GONE);
                    selectedSlot = AROperations.SLOT_EFFECTS;
                    arFilterItems.clear();
                    arFilterItems.addAll(arOperations.getEffects());
                    arFilterAdapter.notifyDataSetChanged();
                  } else {
                    //Reverberation selected
                    //tvClearArFilters.setVisibility(View.INVISIBLE);

                  }
                  break;
                }
                case 2: {
                  //Filters selected
                  //tvClearArFilters.setVisibility(View.VISIBLE);
                  ismBottomsheetEffectsBinding.rvFilters.setVisibility(View.VISIBLE);
                  ismBottomsheetEffectsBinding.rvVoiceFilters.setVisibility(View.GONE);
                  selectedSlot = AROperations.SLOT_FILTERS;
                  arFilterItems.clear();
                  arFilterItems.addAll(arOperations.getFilters());
                  arFilterAdapter.notifyDataSetChanged();
                  break;
                }

                case 3: {
                  //Voice changer selected
                  //tvClearArFilters.setVisibility(View.INVISIBLE);

                  break;
                }

                case 4: {
                  //Reverberation selected
                  //tvClearArFilters.setVisibility(View.INVISIBLE);

                  break;
                }
              }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
              if (tab.getIcon() != null) {
                tab.getIcon()
                    .setColorFilter(ContextCompat.getColor(activity, R.color.ism_grey),
                        PorterDuff.Mode.SRC_IN);
              }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
          });

      try {
        ismBottomsheetEffectsBinding.tabLayoutFilters.getTabAt(0)
            .getIcon()
            .setColorFilter(ContextCompat.getColor(activity, R.color.ism_white),
                PorterDuff.Mode.SRC_IN);
      } catch (NullPointerException ignore) {
      }
      try {
        ismBottomsheetEffectsBinding.tabLayoutFilters.getTabAt(0).select();
      } catch (NullPointerException ignore) {
      }
    } else {

      if (downloadRequired) {
        if (filtersDownloadedAlready) {
          ismBottomsheetEffectsBinding.rlDownload.setVisibility(View.GONE);
        } else {
          ismBottomsheetEffectsBinding.rlDownload.setVisibility(View.VISIBLE);
        }
      } else {
        ismBottomsheetEffectsBinding.rlDownload.setVisibility(View.GONE);
      }
    }

    ismBottomsheetEffectsBinding.tvClearArFilters.setOnClickListener(view -> {
      if (selectedArTabPosition == 0 || selectedArTabPosition == 1 || selectedArTabPosition == 2) {
        if (downloadRequired) {
          if (filtersDownloadedAlready) {
            clearAllArFilters();
          } else {
            Toast.makeText(activity, getString(R.string.filters_download_required),
                Toast.LENGTH_SHORT).show();
          }
        } else {
          clearAllArFilters();
        }
      } else {
        clearAllVoiceFilters();
      }
    });

    return ismBottomsheetEffectsBinding.getRoot();
  }

  private void deselectCurrentArFilter(int position) {

    arOperations.applyFilter(selectedSlot, "none");

    AREffect arEffect = arFilterItems.get(position);
    arEffect.setSelected(false);

    arFilterItems.set(position, arEffect);
    arFilterAdapter.notifyItemChanged(position);

    updateArFiltersSelectedStatus();
  }

  private void selectCurrentArFilter(int position) {
    arOperations.applyFilter(selectedSlot, arFilterItems.get(position).getPath());

    AREffect arEffect;
    for (int i = 0; i < arFilterItems.size(); i++) {

      arEffect = arFilterItems.get(i);
      arEffect.setSelected(position == i);
      arFilterItems.set(i, arEffect);
    }
    arFilterAdapter.notifyDataSetChanged();
    updateArFiltersSelectedStatus();
  }

  private void updateArFiltersSelectedStatus() {
    switch (selectedArTabPosition) {
      case 0:
        //Masks selected
        arOperations.setMasks(arFilterItems);
        break;
      case 1:
        //Effects selected
        arOperations.setEffects(arFilterItems);
        break;
      case 2:
        //Filters selected
        arOperations.setFilters(arFilterItems);
        break;
    }
  }

  private void clearAllArFilters() {
    arOperations.clearAllFilters();

    AREffect arEffect;
    for (int i = 0; i < arFilterItems.size(); i++) {

      arEffect = arFilterItems.get(i);
      arEffect.setSelected(false);

      arFilterItems.set(i, arEffect);
    }

    arFilterAdapter.notifyDataSetChanged();
    updateArFiltersSelectedStatus();
    ArrayList<AREffect> arEffects;
    ArrayList<AREffect> values;
    for (int i = 0; i < 3; i++) {
      if (i != selectedArTabPosition) {

        switch (i) {
          case 0:
            arEffects = arOperations.getMasks();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setMasks(values);
            break;
          case 1:
            arEffects = arOperations.getEffects();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setEffects(values);
            break;
          case 2:
            arEffects = arOperations.getFilters();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setFilters(values);
            break;
        }
      }
    }
  }

  private void clearAllVoiceFilters() {
  }

  /**
   * To allow filters download on the fly
   */
  @Override
  public void downloadResult(String result, String filePath) {
    if (alertDialogDownloadFilters != null && alertDialogDownloadFilters.isShowing()) {
      try {
        alertDialogDownloadFilters.dismiss();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (filePath != null) {

      dialog.setMessage(getString(R.string.preparing_filters));
      if (activity != null) {
        if (!activity.isFinishing() && !activity.isDestroyed()) {
          activity.runOnUiThread(() -> dialog.show());
        }
      }
      ZipUtils.extractZip(filePath, 0, this);
    } else {
      if (activity != null) {
        activity.runOnUiThread(() -> Toast.makeText(activity, result, Toast.LENGTH_SHORT).show());
      }
    }
  }

  @Override
  public void zipExtractResult(String result) {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }

    if (result == null) {
      filtersDownloadedAlready = true;

      if (activity != null) {
        activity.runOnUiThread(() -> {
          ismBottomsheetEffectsBinding.rlDownload.setVisibility(View.GONE);
          Toast.makeText(activity, R.string.filters_prepared, Toast.LENGTH_SHORT).show();
        });
      }
    } else {
      if (activity != null) {
        activity.runOnUiThread(
            () -> Toast.makeText(activity, R.string.filters_prepare_failed, Toast.LENGTH_SHORT)
                .show());
      }
    }
  }

  @Override
  public void onAttach(@NotNull Context context) {
    super.onAttach(context);
    activity = getActivity();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activity = null;
  }

  public void updateParameters(boolean showAudioEffects, boolean showVideoEffects) {
    this.showAudioEffects = false; /*showAudioEffects;*/
    this.showVideoEffects = showVideoEffects;
  }
}
