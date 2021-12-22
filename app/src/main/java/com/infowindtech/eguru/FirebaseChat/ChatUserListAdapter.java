package com.infowindtech.eguru.FirebaseChat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.swipe.SwipeLayout;
import com.infowindtech.eguru.AlertClass;
import com.infowindtech.eguru.R;
import com.infowindtech.eguru.ServiceClass;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatUserListAdapter extends BaseAdapter {
    private List<ChatUserListData> mListenerList;
    Context mContext;
    RequestQueue requestQueue;
    String fromid, toid, name, username, str_profile, langcode;
    ChatUsersListFragment fragment;
    String chat_id;
    UserSessionManager userSessionManager;

    public ChatUserListAdapter(List<ChatUserListData> mListenerList, Context mContext, ChatUsersListFragment fragment) {
        this.mListenerList = mListenerList;
        this.mContext = mContext;
        this.fragment = fragment;
    }

    public class ViewHolder {
        ImageView impic;
        TextView username;
        TextView unread_msg;
        SwipeLayout swipeLayout;
        private View btnDelete;
        RelativeLayout pastAdapter;
    }

    @Override
    public int getCount() {
        return mListenerList.size();
    }

    @Override
    public Object getItem(int position) {
        return mListenerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        userSessionManager = new UserSessionManager(mContext);
        if (userSessionManager.signIn())
            ((Activity) mContext).finish();
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(mContext, langcode);
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_users_listitem, null);
            holder = new ViewHolder();
            holder.impic = view.findViewById(R.id.im_profile);
            holder.username = view.findViewById(R.id.tv_pname);
            holder.unread_msg = view.findViewById(R.id.tv_unread_status);
            holder.swipeLayout = view.findViewById(R.id.swipe_layout);
            holder.btnDelete = view.findViewById(R.id.delete);
            holder.pastAdapter = view.findViewById(R.id.past_adapter);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        str_profile = mListenerList.get(position).getProfile();
        Glide.with(mContext).load(str_profile)
                .thumbnail(0.5f)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.impic);
        name = mListenerList.get(position).getName().substring(0, 1).toUpperCase() + mListenerList.get(position).getName().substring(1);
        holder.username.setText(name);
        final int unreadmsg = mListenerList.get(position).getUnreadmsg();
        String str_unread_count = String.valueOf(unreadmsg);
        Log.d("UUUUUU", "getView: " + unreadmsg);
        if (unreadmsg > 0) {
            holder.unread_msg.setVisibility(View.VISIBLE);
            holder.unread_msg.setText(str_unread_count);
        } else {
            holder.unread_msg.setVisibility(View.GONE);
        }
        holder.pastAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mListenerList.get(position).getName();
                chat_id = mListenerList.get(position).getChatId();
                username = mListenerList.get(position).getUsername();
                str_profile = mListenerList.get(position).getProfile();
                if (unreadmsg > 0 && holder.unread_msg.getVisibility() == View.VISIBLE) {
                    fromid = mListenerList.get(position).getUsertoid();
                    toid = mListenerList.get(position).getUserfromid();
                    //API call
                    ReadUnreadMsgTask();
                    holder.unread_msg.setVisibility(View.GONE);
                } else {
                    fragment.visibilitygone();
                    fromid = mListenerList.get(position).getUsertoid();


                    Intent intent = new Intent(mContext, FirebaseChatMessage.class);
                    intent.putExtra("sender_id", fromid);
                    intent.putExtra("sender_name", name);
                    intent.putExtra("sender_username", username);
                    intent.putExtra("sender_pic", str_profile);
                    intent.putExtra("room_id", chat_id);

                    mContext.startActivity(intent);

                     }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromid = mListenerList.get(position).getUsertoid();
                toid = mListenerList.get(position).getUserfromid();
                DeleteChatUserTask();
                mListenerList.remove(position);
                fragment.updateAdapter();

            }
        });
        return view;
    }

    private void ReadUnreadMsgTask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.ReadMsgStatusURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                Log.d("chatuser", "onResponse: " + response);
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {

                                    Intent intent = new Intent(mContext, FirebaseChatMessage.class);
                                    intent.putExtra("sender_id", fromid);
                                    intent.putExtra("sender_name", name);
                                    intent.putExtra("sender_username", username);
                                    intent.putExtra("sender_pic", str_profile);
                                    intent.putExtra("room_id", chat_id);

                                    mContext.startActivity(intent);

                                } else {
                                    AlertClass.alertDialogShow(mContext, msg);
                                }
                            }
                        } catch (Exception e) {
                            AlertClass.alertDialogShow(mContext, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_from_id", fromid);
                params.put("user_to_id", toid);
                params.put("lang_code", langcode);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void DeleteChatUserTask() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceClass.DeleteChatUSERURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (null != response) {
                                JSONObject obj = new JSONObject(response);
                                String flag = obj.getString("status").toString();
                                String msg = obj.getString("message").toString();
                                if (flag.equals("true")) {
                                    Toast.makeText(mContext, mContext.getString(R.string.contactdelete), Toast.LENGTH_LONG).show();
                                } else {
                                    AlertClass.alertDialogShow(mContext, msg);
                                }
                            }
                        } catch (Exception e) {
                            AlertClass.alertDialogShow(mContext, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("delete_request_id", toid);
                params.put("user_from_id", toid);
                params.put("user_to_id", fromid);
                params.put("lang_code", langcode);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
}
