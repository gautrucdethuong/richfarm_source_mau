package com.infowindtech.eguru;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;


public class MineVolleyGlobal {
    public JSONObject jsonObject;
    MineVolleyListener volleyListener = null;
    Context context;
    RequestQueue requestQueue;

    public MineVolleyGlobal(Context context, MineVolleyListener volleyListener) {
        this.context = context;
        this.volleyListener = volleyListener;
        requestQueue = Volley.newRequestQueue(context);
    }

    public MineVolleyGlobal(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void resultVolley(final Context context, String url) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("loading");
        dialog.setCancelable(false);
        dialog.show();
        StringRequest objectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (null != response) {
                            volleyListener.onVolleyResponse(response);
                        } else {
                            AlertClass.DispToast(context,"something went wrong");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                AlertClass.DispToast(context, String.valueOf(error));
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        objectRequest.setShouldCache(false);
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        objectRequest.setRetryPolicy(policy);
        requestQueue.add(objectRequest);
    }


    public void resultVolley2(final Context context, String url) {

        StringRequest objectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (null != response) {
                            volleyListener.onVolleyResponse(response);
                        } else {
                            AlertClass.DispToast(context,"something went wrong");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                AlertClass.DispToast(context, String.valueOf(error));
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        objectRequest.setShouldCache(false);
        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        objectRequest.setRetryPolicy(policy);
        requestQueue.add(objectRequest);
    }


    public void parseVollyStringRequest(final String url, final int method, final Map<String, String> params, final MineVolleyListener h) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        if (AlertClass.checkConnection(context)) {
            Log.e("parseVolley", "url-" + url + "\nmethod-" + method + "\nparams-" + params);

            if (method == 0 || method == 1) {
                final StringRequest jsObjRequest = new StringRequest
                        (method, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null || response.length() > 0) {
                                    h.onVolleyResponse(response);
                                    Log.e("parseVolley", "response(" + url + ")\n---" + response);
                                    dialog.dismiss();
                                } else {
                                    Log.e("parseVolley", "response type error" + response);
                                    dialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String err = (error.getMessage() == null) ? "Parse Fail" : error.getMessage();

                                h.onVolleyResponse(err);
                               dialog.dismiss();
                                Toast.makeText(context,"something went wrong", Toast.LENGTH_SHORT).show();
                                Log.e("parseVolley", "" + err + "\nFor Url:-" + url + "\nWith Param:-" + params);

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        if (method == 0) {
                            return null;
                        } else {
                            return params;
                        }
                        //returning parameters

                    }
                };
                // Adding request to request queue

                requestQueue = Volley.newRequestQueue(context);
                jsObjRequest.setShouldCache(false);
                RetryPolicy policy = new DefaultRetryPolicy(35000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                requestQueue.add(jsObjRequest);

            } else {
                Log.d("RRRRRR", "Invalid request");
                Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        } else {
            Toast.makeText(context, "Please check you connection", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }





}
