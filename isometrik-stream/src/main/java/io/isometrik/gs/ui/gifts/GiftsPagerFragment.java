package io.isometrik.gs.ui.gifts;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.builder.gift.GiftSendQuery;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.gifts.response.Gift;
import io.isometrik.gs.ui.gifts.response.GiftCategory;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.ui.utils.Utilities;


/**
 * Bottomsheet dialog fragment to allow selecting a gift{@link GiftsModel} from a gift
 * category{@link GiftCategoryModel} and send it using a
 * callback method in interface
 * GiftsActionCallback{@link GiftsActionCallback}
 *
 * @see GiftsActionCallback
 * @see GiftCategoryModel
 * @see GiftsModel
 */
public class GiftsPagerFragment extends BottomSheetDialogFragment
        implements  GiftAdapter.GiftListener {


    public static final String TAG = "GiftsFragment";

    TabLayout tlGiftCategories;
    ViewPager viewPager;
    TextView tvBalance;
    FrameLayout flCoinOne;
    FrameLayout flCoinTwo;
    FrameLayout flCoinThree;
    CoordinatorLayout clCoin;
    TextView tvCoinSpent;
    TextView btnBuyCoins;
    ImageButton ibCloseGift;

    private View view;
    private Activity activity;
    private GiftsActionCallback giftsActionCallback;
    private String streamId, receiverId, receiverName;
    private Animation coinAnimOne, coinAnimTwo, coinAnimThree;
    private MediaPlayer mediaPlayer;
    private Dialog loader;
    private boolean isPk = false;
    private String pkId = "";
    private String pkInviteId = "";
    private String reciverUserType = "publisher";
    private String receiverStreamId = "";
    private String receiverStreamUserId = "";

    public GiftsPagerFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.gifts_pager_fragment, container, false);
        } else {

            if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
        }

        tlGiftCategories = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        tvBalance = view.findViewById(R.id.tvBalance);
        flCoinOne = view.findViewById(R.id.flCoinOne);
        flCoinTwo = view.findViewById(R.id.flCoinTwo);
        flCoinThree = view.findViewById(R.id.flCoinThree);
        clCoin = view.findViewById(R.id.clCoin);
        tvCoinSpent = view.findViewById(R.id.tvCoinSpent);
        btnBuyCoins = view.findViewById(R.id.btnBuyCoins);
        ibCloseGift = view.findViewById(R.id.ibCloseGift);

        UserSession userSession = IsometrikStreamSdk.getInstance().getUserSession();
        if (userSession.getCoinBalance() != null && !userSession.getCoinBalance().equals("null")) {
            double balance = Double.parseDouble(userSession.getCoinBalance());
            Log.e("balance", ""+balance);
//            tvBalance.setText(Utilities.formatMoney(balance));
            tvBalance.setText((int) balance+"");
        } else {
            tvBalance.setText(Utilities.formatMoney(0.0));
        }

        loader = new Dialog(activity);
        loader.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader.setContentView(R.layout.dialog_login);

        if (loader.getWindow() != null) {
            loader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        viewPager.setSaveEnabled(false);
        tlGiftCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (activity != null && !activity.isFinishing()) {
            loader.show();
        }

        mediaPlayer = MediaPlayer.create(activity, R.raw.coin_spend);
        coinAnimOne = AnimationUtils.loadAnimation(activity, R.anim.coin_anim_one);
        coinAnimTwo = AnimationUtils.loadAnimation(activity, R.anim.coin_anim_two);
        coinAnimThree = AnimationUtils.loadAnimation(activity, R.anim.coin_anim_three);

        clCoin.setVisibility(View.GONE);

        ibCloseGift.setOnClickListener(v -> {
            closeGiftFragment();
        });
        ibCloseGift.setOnClickListener(v -> {
            closeGiftFragment();
        });
        btnBuyCoins.setOnClickListener(v -> {
            buyCoins();
        });
        TypeToken<ArrayList<GiftCategory>> listCategories = new TypeToken<ArrayList<GiftCategory>>() {
        };
        List<GiftCategory> categories = new Gson().fromJson(IsometrikStreamSdk.getInstance().getUserSession().getGiftCategories(), listCategories.getType());

        onGiftsCategoriesFetched(categories);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
    }

    public void closeGiftFragment() {
        dismiss();
    }

    public void buyCoins() {
//        if (activity != null) startActivity(new Intent(activity, CoinActivity.class));
    }

    public void updateParameters(GiftsActionCallback giftsActionCallback, String streamId,
                                 String receiverId, String receiverName, String receiverStreamId, String receiverStreamUserId) {
        this.giftsActionCallback = giftsActionCallback;
        this.streamId = streamId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverStreamId = receiverStreamId;
        this.receiverStreamUserId = receiverStreamUserId;

    }

    public void updatePkParameters(boolean isPk, String pkId, String receiverId, String receiverName, String pkInviteId, String reciverUserType) {
        this.isPk = isPk;
        this.pkId = pkId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.pkInviteId = pkInviteId;
        this.reciverUserType = reciverUserType;

    }

    public void updatePkId(String pkId) {
        this.pkId = pkId;
    }

    public void onGiftsCategoriesFetched(List<GiftCategory> categories) {
        if (categories.isEmpty()) {
            Toast.makeText(activity, R.string.ism_no_gifts, Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            tlGiftCategories.removeAllTabs();
            for (GiftCategory category : categories) {
                View view = LayoutInflater.from(activity).inflate(R.layout.tab_gift, null);
                ImageView icon = view.findViewById(R.id.icon);
                Glide.with(activity)
                        .asBitmap()
                        .load(category.getGiftImage())
                        .into(new BitmapImageViewTarget(icon));
                TextView title = view.findViewById(R.id.title);
                title.setText(category.getGiftTitle());


                tlGiftCategories.addTab(tlGiftCategories.newTab().setCustomView(view));
            }
            GiftViewPagerAdapter giftViewPagerAdapter =
                    new GiftViewPagerAdapter(getChildFragmentManager(), tlGiftCategories.getTabCount(),
                            categories, this);
            viewPager.setAdapter(giftViewPagerAdapter);
        }

        if (loader != null && loader.isShowing()) loader.dismiss();
    }

    public void onError(String errorMessage) {
        if (loader != null && loader.isShowing()) loader.dismiss();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                if (errorMessage != null) {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, getString(R.string.ism_error),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onGiftSelect(Gift gift, String categoryTitle) {
        double balance =
                Double.parseDouble(IsometrikStreamSdk.getInstance().getUserSession().getCoinBalance());
        double giftCost = Double.parseDouble(String.valueOf(gift.getVirtualCurrency()));
        if (balance >= giftCost) {

            if(receiverId == null || receiverId.isEmpty()){
                Toast.makeText(getContext(),"Receiver id empty!",Toast.LENGTH_LONG).show();
                return;
            }

            String receiverCurrency =  "INR";
            UserSession userSession = IsometrikStreamSdk.getInstance().getUserSession();
            gift.setReciverId(receiverId);
            gift.setReciverStreamUserId(receiverStreamUserId);

            GiftSendQuery giftSendQuery = new GiftSendQuery(streamId,userSession.getUserId(), gift.getGiftImage()
                    ,(int) gift.getVirtualCurrency(),gift.getId(),userSession.getDeviceId(),gift.getGiftAnimationImage()
                    ,receiverId,reciverUserType,receiverName,pkId,receiverStreamId,receiverCurrency,userSession.getUserToken()
                    ,gift.getGiftTitle(),false,"COIN",isPk);

            IsometrikStreamSdk.getInstance().getIsometrik()
                    .getRemoteUseCases().getGiftUseCases()
                    .sendGiftToStreamer(giftSendQuery, (var1, var2) -> {
                        if(var1 != null){
                            onGiftSentSuccessfully(streamId, gift, categoryTitle);
                        }else{
                            if(var2.getHttpResponseCode() == 404){
                                insufficientBalance();
                            }else {
                                String error = var2.getErrorMessage();
                                if(error == null){
                                    error =  "something went wrong";
                                }
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        } else {
            openInsufficientBalancePopup();
        }
    }

    public void insufficientBalance() {
        if (loader != null && loader.isShowing()) loader.dismiss();
        openInsufficientBalancePopup();
    }

    public void onGiftSentSuccessfully(String streamId, Gift gift,
                                        String categoryTitle) {
        if (loader != null && loader.isShowing()) loader.dismiss();

        double giftCost = (double) gift.getVirtualCurrency();

        if (giftCost < 1000) {
            startCoinAnimation(String.valueOf(gift.getVirtualCurrency()));
        }

        try {

            Double coinBalance = Double.parseDouble(IsometrikStreamSdk.getInstance().getUserSession().getCoinBalance());

            double cost = (double) gift.getVirtualCurrency();

            Double remainingBalance = coinBalance - cost;

            IsometrikStreamSdk.getInstance().getUserSession()
                    .setCoinBalance(String.valueOf(remainingBalance));
            tvBalance.setText(
                    getString(R.string.ism_coins_balance, Utilities.formatMoney(remainingBalance)));

        } catch (Exception e) {

        }


        gift.setReceiverName(receiverName);

        Log.e("SentSuccessFully", "receiverId " + gift.getReciverId() + " streamReceiverID " + gift.getReciverStreamUserId());


        if (giftsActionCallback != null)
            giftsActionCallback.onGiftSentSuccessfully(streamId, gift, categoryTitle);
    }

    private void openInsufficientBalancePopup() {
        try {
            if (activity != null) {
                //show insufficient balance
                LowBalanceDialog lowBalanceDialog = new LowBalanceDialog(activity, R.drawable.ic_coin,
                        getString(R.string.insufficient_balance_message));
                lowBalanceDialog.show();
                Button btnRechargeNow = lowBalanceDialog.findViewById(R.id.btnRecharge);
                btnRechargeNow.setOnClickListener(v -> {

//                    startActivity(new Intent(activity, CoinActivity.class))
                        }
                );
            }
        } catch (Exception e) {

        }

    }

    private void startCoinAnimation(String coin) {
        clCoin.setVisibility(View.VISIBLE);
        mediaPlayer.start();
        tvCoinSpent.setText(coin);
        tvCoinSpent.startAnimation(coinAnimTwo);
        flCoinOne.startAnimation(coinAnimOne);
        flCoinTwo.startAnimation(coinAnimTwo);
        flCoinThree.startAnimation(coinAnimThree);
        coinAnimThree.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvCoinSpent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clCoin.setVisibility(View.GONE);
                tvCoinSpent.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
