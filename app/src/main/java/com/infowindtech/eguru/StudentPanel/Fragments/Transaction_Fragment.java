package com.infowindtech.eguru.StudentPanel.Fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Adapters.EarningListAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.WalletListModel;
import com.infowindtech.eguru.databinding.FragJobAppliedTeacherlist1Binding;
import com.infowindtech.eguru.databinding.FragStudentCalender1Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Transaction_Fragment extends Fragment {
    FragJobAppliedTeacherlist1Binding binding;
    String user_id,lastid = "", last = "";
    UserSessionManager userSessionManager;
    EarningListAdapter earningListAdapter;
    ArrayList<WalletListModel> earningList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragJobAppliedTeacherlist1Binding.inflate(inflater, container, false);

        binding.tvHeading.setText("Transactions");
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        HashMap<String, String> lang = userSessionManager.getLanguage();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);

        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StudentHomeActivity) getActivity()).callDrawer();
            }
        });
        earningListAdapter = new EarningListAdapter(earningList, getActivity());
        earningListAdapter.notifyDataSetChanged();
        binding.lvTeachers.setAdapter(earningListAdapter);
        last = "";
        EarningTransaction();

        binding.lvTeachers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == scrollState) {
                    if (binding.lvTeachers.getLastVisiblePosition() == earningListAdapter.getCount() - 1) {
                        user_id = user.get(userSessionManager.KEY_USERID);
                        last = lastid;
                        EarningTransaction();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });




        return binding.getRoot();
    }






    public void EarningTransaction() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.StudentTransaction,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRR", "EarningTransaction  : response" + response);
                        try {
                            if (last.equals("")) {
                               earningList.clear();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (flag.equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    lastid = obj.getString("order_id").toString();
                                    String name = obj.getString("name").toString();
                                    String amt = obj.getString("amount").toString();
                                    String type = obj.getString("payment_type").toString();
                                    String status = obj.getString("payment_status").toString();
                                    String transaction = obj.getString("transaction_id").toString();
                                    String currency = obj.getString("currency").toString();

                                    String date = obj.getString("payment_date").toString();

                                    WalletListModel walletListModel = new WalletListModel();
                                    walletListModel.setName(name);
                                    walletListModel.setAmount(amt);
                                    walletListModel.setCreated_at(date);
                                    walletListModel.setSelected_method(type);
                                    walletListModel.setStatus(status);
                                    walletListModel.setTransaction(transaction);
                                    walletListModel.setWithdrawal_id(lastid);
                                    walletListModel.setCurrency(currency);


                                  earningList.add(walletListModel);
                                   earningListAdapter.notifyDataSetChanged();
                                }
                                last = lastid;

                            } else if (flag.equals("false")) {

                                if(earningList.size()==0)
                                {
                                    binding.tvNoclass.setVisibility(View.VISIBLE);
                                }
                                else {
                                    binding.tvNoclass.setVisibility(View.GONE);
                                }
                                //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "EarningTransaction  :  exception" + e);
                            //AlertClass.alertDialogShow(getActivity(),e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NetworkError) {
                            String message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof ServerError) {
                            String message = getString(R.string.servererror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof AuthFailureError) {
                            String message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof ParseError) {
                            String message = getString(R.string.parsingerror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof NoConnectionError) {
                            String message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        } else if (error instanceof TimeoutError) {
                            String message = getString(R.string.networkerror);
                            AlertClass.DispToast(getActivity(), message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_id", user_id);
                params.put("last_id", last);
                params.put("status", "");

                Log.d("SSSSS", "getParams: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }





}

