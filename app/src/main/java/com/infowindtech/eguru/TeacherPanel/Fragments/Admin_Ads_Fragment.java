package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.infowindtech.eguru.databinding.FragAdminAdsBinding;

public class Admin_Ads_Fragment extends Fragment{
    FragAdminAdsBinding binding;
    String adurl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragAdminAdsBinding.inflate(inflater,container,false);
        adurl=getArguments().getString("ad_url");
        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new MyBrowser());
        binding.webview.loadUrl(adurl);
        return binding.getRoot();
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}