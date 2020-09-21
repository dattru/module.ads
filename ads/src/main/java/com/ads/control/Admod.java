package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class Admod {
    private static Admod instance;
    private InterstitialAd mInterstitialAd;
    private UnifiedNativeAd nativeAd;
    private static int numClicked = 0;
    private static final int NUM_SHOW_ADS = 2;


    public static long timeLoad = 0;
    public static long TimeReload = 10 * 1000;

    public static Admod getInstance() {
        if (instance == null) {
            instance = new Admod();
        }
        return instance;
    }

    private Admod() {

    }

    public void init(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
    }

    public void initInterstital(Context context, String id, final AdListener adListener) {
        Log.d("ADMOD.TAG", "initInterstital: " + id);
        if (Pucharse.getInstance(context).isPucharsed()) {
            if (adListener != null) {
                adListener.onAdFailedToLoad(0);
            }
            return;
        }
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(id);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (adListener != null) {
                    adListener.onAdLoaded();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (adListener != null) {
                    adListener.onAdFailedToLoad(errorCode);
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                if (adListener != null) {
                    adListener.onAdClosed();
                } else {
                    loadInterstitialAd();
                }
            }
        });
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        if (mInterstitialAd != null && !mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(getAdRequest());
        }
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("3C94990AA9A387A256D3B2BBBFEA51EA")
                .addTestDevice("6F599887BC401CFB1C7087F15D7C0834")
                .addTestDevice("B543DCF2C7591C7FB8B52A3A1E7138F6")
                .addTestDevice("8619926A823916A224795141B93B7E0B")
                .addTestDevice("6399D5AEE5C75205B6C0F6755365CF21")
                .addTestDevice("2E379568A9F147A64B0E0C9571DE812D")
                .addTestDevice("A0518C6FA4396B91F82B9656DE83AFC7")
                .addTestDevice("C8EEFFC32272E3F1018FC72ECBD46F0C")
                .addTestDevice("FEECD9793CCCE1E0FF8D392B0DB65559")

                .build();
    }

    private RewardedAd rewardedAd;

    public void initVideoAds(Context context, String id, RewardedAdLoadCallback adLoadCallback) {
        if (Pucharse.getInstance(context).isPucharsed()) {
            adLoadCallback.onRewardedAdFailedToLoad(0);
            return;
        }
        rewardedAd = new RewardedAd(context, id);
        rewardedAd.loadAd(getAdRequest(), adLoadCallback);
    }

    public void loadVideoAds(Activity context, RewardedAdCallback adCallback) {
        if (Pucharse.getInstance(context).isPucharsed()) {
            adCallback.onRewardedAdFailedToShow(0);
            return;
        }
        if (rewardedAd.isLoaded()) {
            rewardedAd.show(context, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }
    }

    public void showInterstitialAdByTimes(Context context) {
        if (Pucharse.getInstance(context).isPucharsed()) {
            return;
        }
        numClicked++;
        if (numClicked >= NUM_SHOW_ADS && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.d("ADMOD.TAG", "showed ads: " + mInterstitialAd.getAdUnitId());
            numClicked = 0;
        }
    }


    public void setNumClicked(int numClicked) {
        Admod.numClicked = numClicked;
    }

    public void forceShowInterstitial(Context context) {
        if (Pucharse.getInstance(context).isPucharsed()) {
            return;
        }
        numClicked = NUM_SHOW_ADS;
        showInterstitialAdByTimes(context);
    }

    public void showInterstitialAd(Context context, AdCloseListener adCloseListener) {
        if (Pucharse.getInstance(context).isPucharsed()) {
            adCloseListener.onAdClosed();
            return;
        }
        if ((timeLoad + TimeReload) < System.currentTimeMillis()) {
            if (canShowInterstitialAd()) {
//                this.adCloseListener = adCloseListener;
                mInterstitialAd.show();
                Log.d("ADMOD.TAG", "show Interstital: " + mInterstitialAd.getAdUnitId());
                timeLoad = System.currentTimeMillis();
            } else {
                adCloseListener.onAdClosed();
            }
        } else {
            adCloseListener.onAdClosed();
        }
    }

    private boolean canShowInterstitialAd() {
        return mInterstitialAd != null && mInterstitialAd.isLoaded();
    }

    public interface AdCloseListener {
        void onAdClosed();
    }

    public void loadBanner(final Activity mActivity, String id) {
        final LinearLayout adContainer = (LinearLayout) mActivity.findViewById(R.id.banner_container);
        adContainer.removeAllViews();
        final ShimmerFrameLayout containerShimmer =
                (ShimmerFrameLayout) mActivity.findViewById(R.id.shimmer_container);
        if (Pucharse.getInstance(mActivity).isPucharsed()) {
            containerShimmer.setVisibility(View.GONE);
            return;
        }
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();

        try {
            AdView adView = new AdView(mActivity);
            adView.setAdUnitId(id);
            adContainer.addView(adView);
            AdSize adSize = getAdSize(mActivity);
            // Step 4 - Set the adaptive ad size on the ad view.
            adView.setAdSize(adSize);


            adView.loadAd(getAdRequest());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    adContainer.setVisibility(View.GONE);
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    adContainer.setVisibility(View.VISIBLE);
                }
            });


        } catch (Exception e) {
        }
    }

    private AdSize getAdSize(Activity mActivity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth);

    }

    public void loadBannerFragment(final Activity mActivity, String id, final View rootView) {
        final ShimmerFrameLayout containerShimmer =
                (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);

        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        final LinearLayout adContainer = (LinearLayout) rootView.findViewById(R.id.banner_container);

        try {
            AdView adView = new AdView(mActivity);
            adView.setAdUnitId(id);
            adContainer.addView(adView);
            AdSize adSize = getAdSize(mActivity);
            // Step 4 - Set the adaptive ad size on the ad view.
            adView.setAdSize(adSize);

            adView.loadAd(getAdRequest());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    adContainer.setVisibility(View.GONE);
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    adContainer.setVisibility(View.VISIBLE);


                }
            });


        } catch (Exception e) {
        }
    }

    public void loadNative(final Activity mActivity, String id) {
        final FrameLayout frameLayout =
                mActivity.findViewById(R.id.fl_adplaceholder);
        final ShimmerFrameLayout containerShimmer =
                mActivity.findViewById(R.id.shimmer_container);

        if (Pucharse.getInstance(mActivity).isPucharsed()) {
            containerShimmer.setVisibility(View.GONE);
            return;
        }
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();


        AdLoader adLoader = new AdLoader.Builder(mActivity, id)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);

                        nativeAd = unifiedNativeAd;

                        if (frameLayout != null) {
                            frameLayout.setVisibility(View.VISIBLE);
                            UnifiedNativeAdView adView = (UnifiedNativeAdView) mActivity.getLayoutInflater()
                                    .inflate(R.layout.native_admob_ad, null);
                            populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                    }
                })
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(getAdRequest());
    }

    public void loadNative(final Activity mActivity, final FrameLayout frameLayout, final ShimmerFrameLayout containerShimmer, String id) {
        if (Pucharse.getInstance(mActivity).isPucharsed()) {
            containerShimmer.setVisibility(View.GONE);
            return;
        }
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();


        AdLoader adLoader = new AdLoader.Builder(mActivity, id)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);

                        nativeAd = unifiedNativeAd;

                        if (frameLayout != null) {
                            frameLayout.setVisibility(View.VISIBLE);
                            UnifiedNativeAdView adView = (UnifiedNativeAdView) mActivity.getLayoutInflater()
                                    .inflate(R.layout.native_admob_ad, null);
                            populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                    }
                })
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(getAdRequest());
    }

    public void loadNativeFragment(final Activity mActivity, String id, final View rootView) {
        final ShimmerFrameLayout containerShimmer =
                (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();


        AdLoader adLoader = new AdLoader.Builder(mActivity, id)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);

                        nativeAd = unifiedNativeAd;
                        FrameLayout frameLayout =
                                rootView.findViewById(R.id.fl_adplaceholder);
                        if (frameLayout != null) {
                            frameLayout.setVisibility(View.VISIBLE);
                            UnifiedNativeAdView adView = (UnifiedNativeAdView) mActivity.getLayoutInflater()
                                    .inflate(R.layout.native_admob_ad, null);
                            populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                    }
                })
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new PublisherAdRequest.Builder().build());
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {


        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));

        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

    }

    public void destroyNative() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
    }


}
