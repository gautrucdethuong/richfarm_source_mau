package com.infowindtech.eguru;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.infowindtech.eguru.StudentPanel.Fragments.Class_Info_Fragment;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class PaypalRazorPaymentActivity extends Activity implements PaymentResultWithDataListener {
    private static final String TAG = PaypalRazorPaymentActivity.class.getSimpleName();
    String user_id = "", appointmentId = "", fee = "", payment_id = "", order_id = "", email = "", contact = "", wallet = "", signature = "";
    String message;
    ImageView btnRazor;
    ImageView btnPaypal;
    String name = "", description = "", currency = "", createAt = "";
    Intent service;
    String amount = "", converted_amount = "", converted_currency = "";
    JSONObject options = new JSONObject();

    String cur, status, createTime, totalamt;

    /*********** paypal sandbox key ********/

    private static final String CONFIG_CLIENT_ID = "AaaEHao5S7tmwgTzIw1a1WoWce9KYQkZjYQOmV46sSJD4bQHTZx73UzHmvmhIM0g4hxDLEZy2fMhvVFg";
    private static final String CONFIG_SECRET_ID = "EL0JzA0UCSY5kyBnOzq_Eq1PhyvldmWyyZncPvl5nEUPqC--Klr_TpihIi6XVJO5IYzThF9dhou2ner1";
    private static final String GET_PAYPAL_TOKEN = "https://api.sandbox.paypal.com/v1/oauth2/token";
    private static final String CREATE_PAYPAL_PAYMENT = "https://api.sandbox.paypal.com/v1/payments/payment";
    private static final String EXCEUTE_PAYPAL = "https://api.sandbox.paypal.com/v1/payments/payment/";

    /*********** paypal production key********/

//    private static final String CONFIG_CLIENT_ID = "AcYmT5EYkaHaJdC_2AIp3EqNjVja7pyeHnlI-0XOUqOn3UKNpPcb1a2Hh7knUB7u0vRckMEdfpU2rP_g";
//    private static final String CONFIG_SECRET_ID = "EOJeKKLgpIhLNWn4mcTSq4aT7lt7mZ8L595LyP3USgqWs3a_MowgUFn-k_U1MiahAzK30YJQ4nHEdXnX";
//    private static final String GET_PAYPAL_TOKEN = "https://api.paypal.com/v1/oauth2/token";
//    private static final String CREATE_PAYPAL_PAYMENT = "https://api.paypal.com/v1/payments/payment";
//    private static final String EXCEUTE_PAYPAL = "https://api.paypal.com/v1/payments/payment/";

    static PaypalRazorPaymentActivity paypalRazorPaymentActivity;

    public static PaypalRazorPaymentActivity getInstance() {
        return paypalRazorPaymentActivity;
    }

    Checkout co = new Checkout();
    Class_Info_Fragment class_info_fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_payment);

        class_info_fragment = Class_Info_Fragment.getInstance();
        paypalRazorPaymentActivity = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");
            appointmentId = extras.getString("appoint_id");
            fee = extras.getString("fee");
            name = extras.getString("name");
            Log.d(TAG, "onCreate: " + user_id + "   " + appointmentId + "   " + fee);

        }

        String[] curr = fee.split(" ");
        currency = curr[0];
        amount = curr[1];

        Log.d(TAG, "onCreate: " + currency + "   " + amount);

        btnRazor = findViewById(R.id.tv_razor);
        btnPaypal = findViewById(R.id.tv_paypal);


        if (currency.equals("INR")) {
            btnRazor.setVisibility(View.VISIBLE);
            btnPaypal.setVisibility(View.GONE);
        } else if (currency.equals("USD")) {
            btnRazor.setVisibility(View.GONE);
            btnPaypal.setVisibility(View.VISIBLE);
        } else {
            AlertClass.DispToast(getApplicationContext(), "Not Able to get the currency");
        }


        co.preload(getApplicationContext());


        btnRazor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date_time = "";

                SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                date_time = dateFormatGmt.format(new Date());

                // Toast.makeText(getApplicationContext(),"Payment Successful in Demo Version",Toast.LENGTH_SHORT).show();
                //  callOrderNowService(user_id, appointmentId, "razor", "demo_123", amount, "approved", currency, date_time);

                startPayment();
            }
        });


        btnPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String date_time = "";
//                SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
//                date_time = dateFormatGmt.format(new Date());
//                Toast.makeText(getApplicationContext(), "Payment Successful in Demo Version" +
//                      "", Toast.LENGTH_SHORT).show();
//                callOrderNowService(user_id, appointmentId, "paypal", "demo_123", amount, "approved", currency, date_time);

                getPayPalToken(Double.valueOf(amount), name);


//                service = new Intent(getApplicationContext(), PayPalService.class);
//                service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                startService(service);
//                PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
//                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//                startActivityForResult(intent, REQUEST_CODE_PAYMENT);

            }
        });


        final ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

    }

    public void startPayment() {

        final Activity activity = PaypalRazorPaymentActivity.this;

        description = "Demoing Charges";
        createAt = getCurrentTime();
        Log.d(TAG, "startPayment: " + amount + "   dd   " + (amount + 0 + 0) + "     ds      " + (amount + 0 + 0 + 0) + "     dfdf f    " + (amount + 0 + 0 + 0 + 0));
//#528FF0


//
//        try {
//            JSONObject orderRequest = new JSONObject();
//            orderRequest.put("amount", 50000); // amount in the smallest currency unit
//            orderRequest.put("currency", "INR");
//            orderRequest.put("receipt", "order_rcptid_11");
//            orderRequest.put("payment_capture", false);
//
//            Order order = razorpay.Orders.create(orderRequest);
//        } catch (RazorpayException e) {
//            // Handle Exception
//            System.out.println(e.getMessage());
//        }


        try {


            options.put("name", name);
            options.put("description", description);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", currency);
            options.put("amount", amount + 0 + 0);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "enter email");
            preFill.put("contact", "enter number");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            Toast.makeText(this, "Payment Successful: ", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPaymentSuccess: " + paymentData);
            payment_id = paymentData.getPaymentId();
            order_id = paymentData.getOrderId();
            email = paymentData.getUserEmail();
            contact = paymentData.getUserContact();
            wallet = paymentData.getExternalWallet();
            signature = paymentData.getSignature();
            Log.d(TAG, "onActivityResult: " + payment_id + "  " + order_id + "  " + amount + "   " + createAt);
            callOrderNowService(user_id, appointmentId, "razor", payment_id, amount, "approved", currency, createAt);
            btnRazor.setEnabled(false);
            btnPaypal.setEnabled(false);


        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            Toast.makeText(this, "Payment failed: ", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPaymentError: " + paymentData);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        class_info_fragment.AllAppointmentlist("");
    }


    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(amount), currency, name,
                paymentIntent);
        // return new PayPalPayment(new BigDecimal("0.01"),"USD", name,paymentIntent);
    }


    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == REQUEST_CODE_PAYMENT) {
//            if (resultCode == RESULT_OK) {
//                PaymentConfirmation confirm =
//                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirm != null) {
//                    try {
//                        Log.i(TAG, confirm.toJSONObject().toString(4));
//                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
//                        String mTransactionState = confirm.getProofOfPayment().getState();
//                        if (mTransactionState != null && mTransactionState.equalsIgnoreCase("approved")) {
//
//                            payment_id = confirm.getProofOfPayment().getPaymentId();
//                            //  String transactionId = confirm.getProofOfPayment().getTransactionId();
//                            String time = confirm.getProofOfPayment().getCreateTime();
//                            String status = confirm.getProofOfPayment().getState();
//                            String amount = confirm.getPayment().toJSONObject().getString("amount");
//                            String currency = confirm.getPayment().toJSONObject().getString("currency_code");
//                            String dateStr = getConvertDate(time);
//                            btnPaypal.setEnabled(false);
//                            btnRazor.setEnabled(false);
//                            Log.d("RRRR", "onActivityResult: " + appointmentId + " " + status + " " + dateStr + " " + time);
//                            callOrderNowService(user_id, appointmentId, "paypal", payment_id, amount, status, currency, dateStr);
//
//                        }
//
//
//                    } catch (JSONException e) {
//                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i(TAG, "The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        }
//
//    }


    public void getPayPalToken(Double amount, String jobName) {

        ProgressDialog pd = new ProgressDialog(PaypalRazorPaymentActivity.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = GET_PAYPAL_TOKEN;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    // response
                    Log.d("Response", response);
                    try {
                        JSONObject newOBJECT = new JSONObject(response);
                        Log.d("RRRRRRRRRRRR", "doInBackground: " + newOBJECT);
                        String accessToken = newOBJECT.getString("access_token");
                        if (accessToken.equals("") || accessToken.equals(null)) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Authorization Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            createPayment(accessToken, pd, amount, jobName);
                        }
                    } catch (Exception e) {
                        pd.dismiss();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.d("ERROR", "error => " + error.toString());
                            pd.dismiss();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("grant_type", "client_credentials");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    // add headers <key,value>
                    String credentials = CONFIG_CLIENT_ID + ":" + CONFIG_SECRET_ID;
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    Log.d("TAG", "getHeaders: " + headers);
                    return headers;
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void createPayment(String accessToken, ProgressDialog pd, Double amount, String jobName) {
        try {
            String amt = String.format("%.2f", amount);

            JSONObject payerObj = new JSONObject();
            payerObj.put("payment_method", "paypal");

            JSONObject detailObj = new JSONObject();
            detailObj.put("subtotal", amount);
            detailObj.put("tax", "0.0");

            JSONObject amountJson = new JSONObject();
            amountJson.put("currency", "USD");
            amountJson.put("details", detailObj);
            amountJson.put("total", amt);

            JSONObject payOptionObj = new JSONObject();
            payOptionObj.put("allowed_payment_method", "INSTANT_FUNDING_SOURCE");

            JSONObject transactionsJson = new JSONObject();
            transactionsJson.put("amount", amountJson);
            transactionsJson.put("description", "The payment transaction description");
            transactionsJson.put("payment_options", payOptionObj);
            JSONArray transac = new JSONArray();
            transac.put(transactionsJson);


            JSONObject redirectUrlObj = new JSONObject();
            redirectUrlObj.put("return_url", "https://prnt.sc/success");
            redirectUrlObj.put("cancel_url", "https://prnt.sc/cancel");


            JSONObject jsonObj = new JSONObject();
            jsonObj.put("intent", "sale");
            jsonObj.put("transactions", transac);
            jsonObj.put("payer", payerObj);
            jsonObj.put("note_to_payer", "Contact us for any questions on your order.");
            jsonObj.put("redirect_urls", redirectUrlObj);
            //JSONObject Obj = new JSONObject();

            Log.d("TAG", "getParams: " + jsonObj.toString());


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = CREATE_PAYPAL_PAYMENT;
            JsonObjectRequest stringRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());

                            try {
                                Log.d("RRRRRRRRRRRR", "doInBackground: " + response);
                                JSONArray jsonarray = response.getJSONArray("links");
                                // looping through All Contacts
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    if (i == 1) {
                                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                                        String webUrl = jsonobj.getString("href");
                                        Intent intent = new Intent(PaypalRazorPaymentActivity.this, PaypalWebActivity.class);
                                        intent.putExtra("webUrl", webUrl);
                                        intent.putExtra("accessToken", accessToken);
                                        startActivity(intent);
                                        pd.dismiss();
                                    }
                                }

                            } catch (Exception e) {
                                pd.dismiss();
                                Toast.makeText(PaypalRazorPaymentActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                    pd.dismiss();
                                    Log.d("ERROR", "error => " + error.toString());
                                }
                            }
                    ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Bearer " + accessToken);
                    headers.put("Content-Type", "application/json");
                    Log.d("TAG", "getHeaders: " + headers);
                    return headers;
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void exceutePayment(String payerId, String payId, String token) {

        ProgressDialog pd = new ProgressDialog(PaypalRazorPaymentActivity.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        try {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("payer_id", payerId);
            Log.d("TAG", "getParams: " + jsonObj.toString());

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = EXCEUTE_PAYPAL + payId + "/execute";
            JsonObjectRequest stringRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());

                            try {
                                Log.d("RRRRRRRRRRRR", "doInBackground: " + response);
                                String state = response.getString("state");
                                if (state.equalsIgnoreCase("approved")) {
                                    JSONArray jsonArray = response.getJSONArray("transactions");
                                    Log.d("TAG", "onResponse: 111   " + jsonArray.toString());

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Log.d("TAG", "onResponse:222   " + jsonObject.toString());
                                        JSONArray jsonArray1 = jsonObject.getJSONArray("related_resources");
                                        Log.d("TAG", "onResponse: 333   " + jsonArray1.toString());
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                            Log.d("TAG", "onResponse: 444    " + jsonObject1.toString());
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("sale");
                                            Log.d("TAG", "onResponse: 5555   " + jsonObject2.toString());
                                            payment_id = jsonObject2.getString("id");
                                            status = jsonObject2.getString("state");
                                            createTime = jsonObject2.getString("create_time");
                                            Log.d("TAG", "onResponse: transactionID   " + payment_id);
                                            JSONObject jsonobjectamt = jsonObject2.getJSONObject("amount");
                                            totalamt = jsonobjectamt.getString("total");
                                            cur = jsonobjectamt.getString("currency");

                                            Log.d("RRRR", "onActivityResult: " + appointmentId + " " + status + " ");
                                            Log.d("RRRR", "onActivityResult: " + cur + " " + totalamt + " ");

                                        }
                                    }

                                    String dateStr = getConvertDate(createTime);
                                    Log.d("RRRR", "onActivityResult: " + appointmentId + " " + status + " " + dateStr + " ");
                                    Log.d("RRRR", "onActivityResult: " + cur + " " + totalamt + " " + dateStr + " ");
                                    callOrderNowService(user_id, appointmentId, "paypal", payment_id, totalamt, status, cur, dateStr);
//
                                }

                                pd.dismiss();
                            } catch (Exception e) {
                                pd.dismiss();
                                Log.d("TAG", "onResponse: exceute......   " + e);
                            }


                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                    pd.dismiss();
                                    Log.d("ERROR", "error => " + error.toString());
                                }
                            }
                    ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Bearer " + token);
                    headers.put("Content-Type", "application/json");
                    Log.d("TAG", "getHeaders: " + headers);
                    return headers;
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String getConvertDate(String date_server) {
        String serverdateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(serverdateFormat, Locale.UK);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(date_server);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(value);
        return formattedDate;
    }


    public void callOrderNowService(final String user_id, final String appointmentId, final String type, final String transactionId, final String amount, final String status, final String currency, final String dateStr) {
        ProgressDialog pd = new ProgressDialog(PaypalRazorPaymentActivity.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.order,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "classinfofragment  :  response: " + response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            final String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                onBackPressed();


                            } else if (flag.equals("false")) {
                                pd.dismiss();
                                AlertClass.alertDialogShow(getApplicationContext(), msg);
                            }
                        } catch (Exception e) {
                            pd.dismiss();
                            Log.d("RRRRR", "classinfofragment  :  exception: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("appointment_id", appointmentId);
                params.put("transaction_id", transactionId);
                params.put("amount", amount);
                params.put("type", type);
                params.put("status", status);
                params.put("currency", currency);
                params.put("created_at", dateStr);
                Log.d(TAG, "getParams: " + params);
                return params;


            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }


    public void currencyConvertor(final String str_currency, final String str_amount) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ConvertCurrency,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            Log.d("RRRRR", "currency  :  response: " + response);
                            jsonObject = new JSONObject(response);
                            String flag = jsonObject.getString("status");
                            final String msg = jsonObject.getString("message");
                            if (flag.equals("true")) {
//                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                                converted_amount = jsonObject1.getString("converted_amount");
//                                converted_currency = jsonObject1.getString("converted_currency");
//                                Log.d(TAG, "onClick: " + converted_currency + "  " + converted_amount);
//                                PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//                                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
//                                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//                                startActivityForResult(intent, REQUEST_CODE_PAYMENT);

                            } else if (flag.equals("false")) {
                                AlertClass.alertDialogShow(getApplicationContext(), msg);
                            }
                        } catch (Exception e) {
                            Log.d("RRRRR", "currency  :  exception: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof ServerError) {
                            message = getString(R.string.servererror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof AuthFailureError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof ParseError) {
                            message = getString(R.string.parsingerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof NoConnectionError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        } else if (error instanceof TimeoutError) {
                            message = getString(R.string.networkerror);
                            AlertClass.DispToast(getApplicationContext(), message);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("teacher_currency", str_currency);
                params.put("teacher_amount", str_amount);
                Log.d(TAG, "getParams: " + str_currency + " " + str_amount);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setShouldCache(true);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


}

