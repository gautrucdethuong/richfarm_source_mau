package com.infowindtech.eguru.TeacherPanel.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
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
import com.google.android.material.appbar.AppBarLayout;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.MainActivity;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Adapters.EarningListAdapter;
import com.infowindtech.eguru.TeacherPanel.Adapters.WalletListAdapter;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.TeacherPanel.Models.WalletListModel;
import com.infowindtech.eguru.databinding.AmountDialogBinding;
import com.infowindtech.eguru.databinding.FragSetting1Binding;
import com.infowindtech.eguru.databinding.FragWalletBinding;
import com.infowindtech.eguru.databinding.FragWithdrawalListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Wallet_Fragment extends Fragment {
    FragWalletBinding binding;
    UserSessionManager userSessionManager;
    String user_id, profile, name, langcode, account = "", paymentId = "", balance = "",currency_code="";
    int amount=0;
    String user_type, lastid = "", last = "";
    WalletListAdapter adapter;
    ArrayList<WalletListModel> list = new ArrayList<>();
    EarningListAdapter earningListAdapter;
    ArrayList<WalletListModel> earningList = new ArrayList<>();
    FragWithdrawalListBinding fragWithdrawalListBinding ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragWalletBinding.inflate(inflater, container, false);


        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ((MainActivity) getActivity()).callDrawer();

                } catch (Exception ex) {
                    ((StudentHomeActivity) getActivity()).callDrawer();
                }

            }
        });


        binding.tvHeading.setText("Wallet");
        userSessionManager = new UserSessionManager(getActivity());
        if (userSessionManager.signIn())
            getActivity().finish();
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(getActivity(), langcode);
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        user_id = user.get(userSessionManager.KEY_USERID);
        profile = user.get(userSessionManager.KEY_PROFILE);
        name = user.get(userSessionManager.KEY_FULLNAME);
        user_type = user.get(userSessionManager.KEY_USERTYPE);
        currency_code = user.get(userSessionManager.KEY_Currency_Code);
        HashMap<String, String> userAccount = userSessionManager.getAccountInfo();
        account = userAccount.get(userSessionManager.KEY_ACCOUNT);
        paymentId = userAccount.get(userSessionManager.KEY_PAYMENT_ID);

        Log.d("account", "onCreateView: " + account + " " + paymentId);


        if(currency_code.equals("INR"))
            binding.currency.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.rupee));
        else if (currency_code.equals("USD"))
            binding.currency.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.dollor));
        else
            AlertClass.DispToast(getContext(),"CHECK CURRENCY CODE");


        walletBalance();


        if (account.equals("") || account.equals("null")) {
            binding.etAccount.setVisibility(View.GONE);
            binding.account.setVisibility(View.VISIBLE);
            binding.edit.setVisibility(View.VISIBLE);
            binding.save.setVisibility(View.INVISIBLE);
            binding.account.setHint("Please edit your account");
        } else {
            binding.etAccount.setVisibility(View.GONE);
            binding.account.setVisibility(View.VISIBLE);
            binding.edit.setVisibility(View.VISIBLE);
            binding.save.setVisibility(View.INVISIBLE);
            binding.account.setText(account);
        }


        binding.editAmt.setVisibility(View.VISIBLE);
        binding.amount.setVisibility(View.VISIBLE);
        binding.etAmt.setVisibility(View.GONE);
        binding.saveAmt.setVisibility(View.GONE);


        binding.editAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editAmt.setVisibility(View.GONE);
                binding.amount.setVisibility(View.GONE);
                binding.etAmt.setVisibility(View.VISIBLE);
                binding.saveAmt.setVisibility(View.VISIBLE);
            }
        });


        binding.saveAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = binding.edAmt.getText().toString();
                if (amount.equals("") || amount.equals(null)) {
                    AlertClass.DispToast(getContext(), "Please enter withdrawal amount");
                } else {
                    minimumWithdrawal(Integer.parseInt(amount));
                }
            }
        });


//        if(account.equals("") || account.equals(null))
//        {
//            binding.account.setHint("Please enter your account");
//        }

//
//        if(!account.equals("") || !account.equals(null))
//        {
//            binding.etAccount.setVisibility(View.GONE);
//            binding.account.setVisibility(View.VISIBLE);
//            binding.edit.setVisibility(View.VISIBLE);
//            binding.save.setVisibility(View.GONE);
//            binding.account.setText(account);
//        }

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = binding.editAccount.getText().toString();
                if (account.equals("") && account.equals(null)) {
                    AlertClass.DispToast(getContext(), "Please enter your account");
                } else {
                    UpdateAccount(account);
                }
            }
        });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etAccount.setVisibility(View.VISIBLE);
                binding.save.setVisibility(View.VISIBLE);
                binding.edit.setVisibility(View.GONE);
                binding.account.setVisibility(View.GONE);
            }
        });

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+amount);

                if (amount == 0) {
                    Toast.makeText(getContext(),"Please enter withdrawal amount",Toast.LENGTH_SHORT).show();
                } else {

                    withdrawalRequest();
                }
//                final Dialog dialog = new Dialog(getContext(), R.style.FullWidth_Dialog);
//                //dialog.setContentView(R.layout.layout_contact_dialog);
//                final AmountDialogBinding amountDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.amount_dialog, null, false);
//                dialog.setContentView(amountDialogBinding.getRoot());
//                dialog.getWindow().setLayout(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
//
//                amountDialogBinding.btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        String withdraw_amt = amountDialogBinding.withdrawalAmount.getText().toString();
//
//                        if(withdraw_amt.equals("") || withdraw_amt.equals(null)){
//
//                            Toast.makeText(getContext(),"Please enter withdrawal amount",Toast.LENGTH_SHORT).show();
//
//                        }
//                        else {
//
//                          //  minimumWithdrawal(dialog, Integer.parseInt(withdraw_amt));
//                        }
//                    }
//                });
//
//
//                amountDialogBinding.close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//
//                dialog.show();
//
            }
        });


        binding.withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext(), R.style.FullWidth_Dialog);
               fragWithdrawalListBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.frag_withdrawal_list, null, false);
                dialog.setContentView(fragWithdrawalListBinding.getRoot());
                dialog.getWindow().setLayout(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.MATCH_PARENT);
                fragWithdrawalListBinding.tvHeading.setText("Withdrawal Transactions");
                adapter = new WalletListAdapter(list, getActivity());
                adapter.notifyDataSetChanged();
                fragWithdrawalListBinding.recycleview.setAdapter(adapter);
                last = "";
                ViewTransaction();

                fragWithdrawalListBinding.imDrawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                fragWithdrawalListBinding.recycleview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == scrollState) {
                            if (fragWithdrawalListBinding.recycleview.getLastVisiblePosition() == adapter.getCount() - 1) {
                                user_id = user.get(userSessionManager.KEY_USERID);
                                last = lastid;
                                ViewTransaction();
                            }
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });

                dialog.show();
            }
        });


        binding.earning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext(), R.style.FullWidth_Dialog);
                fragWithdrawalListBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.frag_withdrawal_list, null, false);
                dialog.setContentView(fragWithdrawalListBinding.getRoot());
                dialog.getWindow().setLayout(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.MATCH_PARENT);

                fragWithdrawalListBinding.tvHeading.setText("Earning Transactions");

                earningListAdapter = new EarningListAdapter(earningList, getActivity());
                earningListAdapter.notifyDataSetChanged();
                fragWithdrawalListBinding.recycleview.setAdapter(earningListAdapter);
                last = "";
                EarningTransaction();


                fragWithdrawalListBinding.imDrawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                fragWithdrawalListBinding.recycleview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == scrollState) {
                            if (fragWithdrawalListBinding.recycleview.getLastVisiblePosition() == earningListAdapter.getCount() - 1) {
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

                dialog.show();
            }
        });


        return binding.getRoot();
    }


    public void UpdateAccount(final String save_account) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.AddPaymentAccount,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRR", "UpdateAccount  : response" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");


                            if (flag.equals("true")) {
                                JSONObject obj = jsonObject.getJSONObject("data");
                                String account_info = obj.getString("account_info").toString();
                                String payment_id = obj.getString("payment_method_id").toString();
                                userSessionManager.updateAccount(account_info, payment_id);
                                binding.etAccount.setVisibility(View.GONE);
                                binding.account.setVisibility(View.VISIBLE);
                                binding.edit.setVisibility(View.VISIBLE);
                                binding.save.setVisibility(View.INVISIBLE);
                                binding.account.setText(account_info);


                                HashMap<String, String> userAccount = userSessionManager.getAccountInfo();
                                account = userAccount.get(userSessionManager.KEY_ACCOUNT);
                                paymentId = userAccount.get(userSessionManager.KEY_PAYMENT_ID);
                                Log.d("account", "onCreateView: " + account + " " + paymentId);


                            } else if (flag.equals("false")) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "UpdateAccount  :  exception" + e);
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
                params.put("user_id", user_id);
                params.put("account_info", save_account);
                Log.d("SSSSS", "getParams: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }


    public void walletBalance() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherWalletBalance,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRR", "walletBalance  : response" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");


                            if (flag.equals("true")) {
                                JSONObject obj = jsonObject.getJSONObject("data");
                                balance = obj.getString("teacher_wallet_balance").toString();
                                binding.balance.setText(balance);

                            } else if (flag.equals("false")) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "walletBalance  :  exception" + e);
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
                params.put("user_id", user_id);

                Log.d("SSSSS", "getParams: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }


    public void withdrawalRequest() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherWathdrawalRequest,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRR", "withdrawalRequest  : response" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");


                            if (flag.equals("true")) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                            } else if (flag.equals("false")) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "withdrawalRequest  :  exception" + e);
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
                params.put("user_id", user_id);
                params.put("amount", String.valueOf(amount));
                params.put("payment_method_id", paymentId);
                Log.d("SSSSS", "withdrawalRequest: " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }


    public void minimumWithdrawal(final int withdraw_amt) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceClass.MinimumWithdrawalAmount,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRR", "minimumWithdrawal  : response" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");


                            if (flag.equals("true")) {

                                JSONObject obj = jsonObject.getJSONObject("data");
                                int minimum_amt = obj.getInt("minimum_withdrawal_amount");

                                if (withdraw_amt < minimum_amt) {
                                    AlertClass.alertDialogShow(getContext(), "Please withdraw minimum amount- " + minimum_amt + "$");
                                } else if (withdraw_amt > Double.parseDouble(balance)) {
                                    AlertClass.alertDialogShow(getContext(), "Insufficient Balance");
                                } else {
                                    binding.editAmt.setVisibility(View.VISIBLE);
                                    binding.amount.setVisibility(View.VISIBLE);
                                    binding.etAmt.setVisibility(View.GONE);
                                    binding.saveAmt.setVisibility(View.GONE);
                                    binding.amount.setText("" + withdraw_amt);
                                    amount = withdraw_amt;
                                }
                            } else if (flag.equals("false")) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "minimumWithdrawal  :  exception" + e);
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

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }


    public void ViewTransaction() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.WithdrawalList,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("RRRR", "ViewTransaction  : response" + response);
                        try {
                            if (last.equals("")) {
                                list.clear();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (flag.equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    lastid = obj.getString("withdrawal_id").toString();
                                    String amt = obj.getString("amount").toString();
                                    String method = obj.getString("selected_method").toString();
                                    String status = obj.getString("status").toString();
                                    String created = obj.getString("created_at").toString();

                                    if(currency_code.equals("")|| currency_code.equals(null))
                                    {
                                        AlertClass.DispToast(getContext(),"CHECK CURRENCY CODE");
                                    }

                                    WalletListModel walletListModel = new WalletListModel();
                                    walletListModel.setAmount(amt);
                                    walletListModel.setCreated_at(created);
                                    walletListModel.setSelected_method(method);
                                    walletListModel.setStatus(status);
                                    walletListModel.setWithdrawal_id(lastid);
                                    walletListModel.setCurrency(currency_code);

                                    list.add(walletListModel);
                                    adapter.notifyDataSetChanged();
                                }
                                last = lastid;

                            } else if (flag.equals("false")) {

                                if(list.size()==0)
                                {
                                    fragWithdrawalListBinding.tvNoclass.setVisibility(View.VISIBLE);
                                }
                                else {
                                    fragWithdrawalListBinding.tvNoclass.setVisibility(View.GONE);
                                }
                                //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("RRRR", "ViewTransaction  :  exception" + e);
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
                params.put("user_id", user_id);
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


    public void EarningTransaction() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.pleasewait));
        dialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.TeacherTransaction,
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
                                    String date = obj.getString("payment_date").toString();


                                    if(currency_code.equals("")|| currency_code.equals(null))
                                    {
                                        AlertClass.DispToast(getContext(),"CHECK CURRENCY CODE");
                                    }



                                    WalletListModel walletListModel = new WalletListModel();
                                    walletListModel.setName(name);
                                    walletListModel.setAmount(amt);
                                    walletListModel.setCreated_at(date);
                                    walletListModel.setSelected_method(type);
                                    walletListModel.setStatus(status);
                                    walletListModel.setTransaction(transaction);
                                    walletListModel.setWithdrawal_id(lastid);
                                    walletListModel.setCurrency(currency_code);

                                    earningList.add(walletListModel);
                                    earningListAdapter.notifyDataSetChanged();
                                }
                                last = lastid;

                            } else if (flag.equals("false")) {

                                if(earningList.size()==0)
                                {
                                    fragWithdrawalListBinding.tvNoclass.setVisibility(View.VISIBLE);
                                }
                                else {
                                    fragWithdrawalListBinding.tvNoclass.setVisibility(View.GONE);
                                }
                                //   Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                params.put("teacher_id", user_id);
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


