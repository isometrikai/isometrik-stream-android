package io.isometrik.gs.ui.gifts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.builder.gift.GiftByCategoryQuery;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.gifts.response.Gift;
import io.isometrik.gs.ui.gifts.response.GiftCategory;
import io.isometrik.gs.ui.utils.UserSession;

public class GiftFragment extends Fragment {
    private GiftCategory category;
    private int position = -1;
    private GiftAdapter giftAdapter;
    private static GiftAdapter.GiftListener listener;

    RecyclerView rvGifts;

    public static Fragment newInstance(int position, GiftCategory category, GiftAdapter.GiftListener giftListener) {
        GiftFragment myFragment = new GiftFragment();

        Bundle args = new Bundle();
        args.putSerializable("category", category);
        args.putSerializable("position", position);
        myFragment.setArguments(args);
        listener = giftListener;
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            category = (GiftCategory) bundle.getSerializable("category");
            position = (int) bundle.getSerializable("position");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        rvGifts = view.findViewById(R.id.rvGifts);
        initView(view);
        return view;
    }

    private void initView(View view) {
        if (category != null) {
            giftAdapter = new GiftAdapter(getContext(), listener, category.getGiftTitle());
            rvGifts.setLayoutManager(new GridLayoutManager(getContext(), 3));
            rvGifts.setAdapter(giftAdapter);
            gifts(category.getId());
        }
    }

    private int getLayoutId() {
        return R.layout.fragment_layout;
    }

    private void gifts(String categoryId) {
        if (position == 0) {
            UserSession sessionManager = IsometrikStreamSdk.getInstance().getUserSession();

            if (!sessionManager.getGiftData().isEmpty()) {
                TypeToken<ArrayList<Gift>> listGift = new TypeToken<ArrayList<Gift>>() {
                };
                List<Gift> gifts = new Gson().fromJson(sessionManager.getGiftData(), listGift.getType());

                if (gifts != null) {
                    giftAdapter.setGifts(gifts);

                    return;
                }
            }
        }
        fetchGiftsByCategory(categoryId);
    }

    public void fetchGiftsByCategory(String categoryId) {
        GiftByCategoryQuery.Builder giftCategoriesQuery = new GiftByCategoryQuery.Builder()
                .setCategoryId(categoryId)
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken());

        IsometrikStreamSdk.getInstance().getIsometrik().getRemoteUseCases().getGiftUseCases()
                .fetchGiftByCategory(giftCategoriesQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        List<Gift> gifts = var1.getGits();

                        giftAdapter.setGifts(gifts);
                        Log.e("gifts ", "Fetched and store");

                    } else {
                        Log.e("Error: ", "gifts");

                    }
                }));
    }

}
