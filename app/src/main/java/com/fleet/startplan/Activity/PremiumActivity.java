package com.fleet.startplan.Activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.fleet.startplan.R;
import com.fleet.startplan.SharedPreference.PreferenceManager;
import com.fleet.startplan.Model.Products;

import java.util.ArrayList;
import java.util.List;


public class PremiumActivity extends AppCompatActivity {

    private TextView mPayExplain;
    private TextView mPayText;

    private View mPayPremium;

    private BillingClient billingClient;
    private SkuDetails removeADSku;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);


        TextView mCostTitle = findViewById(R.id.tv_premium_price_title);
        mPayExplain = findViewById(R.id.tv_premium_pay_explain);
        ImageView mPremiumExit = findViewById(R.id.iv_premium_exit);
        mPayPremium = findViewById(R.id.v_premium_pay_btn);
        mPayText = findViewById(R.id.tv_premium_pay_text);

        mCostTitle.setPaintFlags(mCostTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mPayExplain.setPaintFlags(mPayExplain.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mPremiumExit.setOnClickListener(v -> {
            finish();
        });

        mPayPremium.setOnClickListener(v -> {
            billingFlow(removeADSku);
        });

        mPayExplain.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"❤️",Toast.LENGTH_SHORT).show();
        });

        initBilling();
        refreshRemoveAD();

    }

    private final PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, list) -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        }
    };

    private void initBilling() {
        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    loadSkuDetail();
                }
            }
        });
    }

    private void loadSkuDetail() {
        final List<String> skuList = new ArrayList<>();
        skuList.add(Products.ITEM_REMOVE_AD_5900);
        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build();

        billingClient.querySkuDetailsAsync(params, (billingResult, list) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK &&
                    list != null) {
                for (SkuDetails detail : list) {
                    if (Products.ITEM_REMOVE_AD_5900.equals(detail.getSku())) {
                        removeADSku = detail;
                    }
                }
            }
        });
    }

    private void billingFlow(SkuDetails details) {

        BillingFlowParams params = BillingFlowParams.newBuilder()
                .setSkuDetails(details)
                .build();

        billingClient.launchBillingFlow(this, params);
    }


    //
    private void handlePurchase(Purchase purchase) {
        progressPurchase(purchase);

        ConsumeParams params = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();

        billingClient.consumeAsync(params, (billingResult, s) -> {
        });
    }

    //결제완료 여부를 확인
    private void progressPurchase(Purchase purchase) {
        if (purchase.getSkus().contains(Products.ITEM_REMOVE_AD_5900)) {
            PreferenceManager.setBoolean(getApplicationContext(), Products.PREMIUM_USER, true);
            refreshRemoveAD();
        }
    }

    private void refreshRemoveAD() {

        boolean checkPremiumUser = PreferenceManager.getBoolean(getApplicationContext(), Products.PREMIUM_USER);

        if (checkPremiumUser) {
            mPayExplain.setText("감사합니다.");
            mPayPremium.setVisibility(View.GONE);
            mPayText.setText(null);
        } else {
            mPayExplain.setText("노트 한 권 가격으로 한 달간 편하게 사용하세요.");
            mPayPremium.setVisibility(View.VISIBLE);
            mPayText.setText("결제하기");
        }


    }

    @Override
    protected void onDestroy() {
        if (billingClient != null) {
            billingClient.endConnection();
        }
        super.onDestroy();

    }
}
