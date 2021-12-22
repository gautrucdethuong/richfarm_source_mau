package com.infowindtech.eguru;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class PaypalWebActivity extends AppCompatActivity {


    private WebView webView;
    String payUrlStr;
    private Intent getIntent;
    String successUrl;
    String accessToken;
    PaypalRazorPaymentActivity paypalRazorPaymentActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paypal_webview);
        getIntent = getIntent();
        payUrlStr = getIntent.getStringExtra("webUrl");
        accessToken = getIntent.getStringExtra("accessToken");
        paypalRazorPaymentActivity = PaypalRazorPaymentActivity.getInstance();
        loadWebViewPaypal();

    }

    private void loadWebViewPaypal() {

        Log.e("payUrlStr", "" + payUrlStr);

        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(payUrlStr);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         Log.e("Loading url...", url);
                                         view.loadUrl(url);
                                         // dialog.dismiss();
                                         String loadWebUrl = view.getUrl();

                                         Log.e("loadWebUrl", "" + loadWebUrl);

                                         if (loadWebUrl.contains("https://prnt.sc/success")) {

                                             String tempPayerId[] = loadWebUrl.split("PayerID=");
                                             String tempPayerFirst = tempPayerId[0].toString();
                                             String tempPayerSecond = tempPayerId[1].toString();

                                             String PayerId = tempPayerSecond;

                                             String tempPaymemtId[] = loadWebUrl.split("paymentId=");
                                             String tempPaymemtIdFirst = tempPaymemtId[0].toString();
                                             String tempPaymemtIdSecond = tempPaymemtId[1].toString();

                                             String tempPayment = tempPaymemtIdSecond;

                                             String tempPaymentVal[] = tempPayment.split("&token=");
                                             String tempPaymentFirst = tempPaymentVal[0].toString();
                                             String tempPaymentSecond = tempPaymentVal[1].toString();

                                             String PayId = tempPaymentFirst;

                                             paypalRazorPaymentActivity.exceutePayment(PayerId, PayId, accessToken);
                                             finish();

                                         }


                                         return true;
                                     }
                                 }


//            @Override
//            public void onPageFinished(WebView view, String url) {
//                Log.e("Finished url...", url);
//
//                String webUrl = view.getUrl();
//
//                Log.e("webUrl", ""+webUrl);
//
//
//                if(webUrl.substring(0,95).equals(successUrl)){
//
//                    Log.e("Getting Success Request", "Test");
//
//                }else{
//
//                    Log.e("Failed to get Request", "Test");
//
//                }
//
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
//
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode,
//                                        String description, String failingUrl) {
//
//                Log.e("Error in url...", description);
//                Log.e("Error in failingUrl...", failingUrl);
//
//            }

                //}
        );

    }


}