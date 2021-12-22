package com.infowindtech.eguru.FirebaseChat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.infowindtech.eguru.AlertClass;

import com.infowindtech.eguru.R;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;
import com.infowindtech.eguru.databinding.WalletListitemBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class FirebaseChatAdapter extends BaseAdapter {


    ChildEventListener childEventListener;
    private List<ChatModel> chatMessages ;
    private Context context;
    private UserSessionManager userSessionManager;
    private String str_userid, msg_id, str_userpic, str_username, langcode;
    private ArrayList<ChatModel> arrayList;
    FirebaseChatMessage firebaseChatMessage;
    private ArrayList<String> key;


    public FirebaseChatAdapter(List<ChatModel> chatMessages , final Context context ,
                               FirebaseChatMessage firebaseChatMessage, ArrayList<String> key) {
        this.chatMessages = chatMessages;
        this.context = context;
        this.firebaseChatMessage = firebaseChatMessage;
        this.key = key ;
      //  this.arrayList=new ArrayList<>();
       // this.arrayList.addAll(chatMessages);




    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        userSessionManager = new UserSessionManager(context);
        if (userSessionManager.signIn())
            ((Activity) context).finish();
        HashMap<String, String> lang = userSessionManager.getLanguage();
        langcode = lang.get(userSessionManager.KEY_Lang);
        AlertClass.setLocale(context, langcode);
        View v = null;
        ViewHolder holder1;
        userSessionManager = new UserSessionManager(context);
        if (userSessionManager.signIn())
            ((Activity) context).finish();
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        str_userid = user.get(userSessionManager.KEY_USERID);
        str_userpic = user.get(userSessionManager.KEY_PROFILE);
        str_username = user.get(userSessionManager.KEY_USERNAME);

      //  FirebaseChatModel comment = chatMessages.get(position);

        Log.d("RRRRRRRR" ,"getView: "+chatMessages.get(position).getSender_id()+"    "+str_userid);
        Log.d("RRRRRRRR", "getView: "+chatMessages.get(position).getReceiver_id()+"   "+str_userid);
        Log.d("RRRRRRRR", "getView: "+chatMessages.get(position).getMessage()+"   "+chatMessages.get(position).getTime());



        if (chatMessages.get(position).getSender_id().equals(str_userid)) {
            holder1 = new ViewHolder();
            if (view == null) {

                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.chat_message, null);


               // v = LayoutInflater.from(context).inflate(R.layout.chat_message, viewGroup, false);
                holder1.mainchatlayout = v.findViewById(R.id.mainlayout);
                holder1.rel_outgoing = v.findViewById(R.id.rel_outgoing);
                holder1.rel_incoming = v.findViewById(R.id.rel_incoming);
                holder1.singleMessageContainer = v.findViewById(R.id.outgoinglayout);
                holder1.chatText = v.findViewById(R.id.tv_msg1);
                holder1.timeTextView = v.findViewById(R.id.tv_time1);
                holder1.singleMessageContainer1 = v.findViewById(R.id.incominglayout);
                holder1.chatText1 = v.findViewById(R.id.tv_msg2);
                holder1.timeTextView1 = v.findViewById(R.id.tv_time2);
                holder1.tv_date = v.findViewById(R.id.tv_date);
                holder1.view1 = v.findViewById(R.id.view1);
                holder1.view2 = v.findViewById(R.id.view2);
                holder1.status1 = v.findViewById(R.id.user_reply_status);
                v.setTag(holder1);
            } else {
                v = view;
                holder1 = (ViewHolder) v.getTag();
            }
            holder1.rel_outgoing.setVisibility(View.VISIBLE);
            holder1.rel_incoming.setVisibility(View.GONE);
            holder1.chatText.setText(chatMessages.get(position).getMessage());
            String date1 = chatMessages.get(position).getDate();
            if (position == 0) {
                holder1.tv_date.setVisibility(View.VISIBLE);
                holder1.view1.setVisibility(View.VISIBLE);
                holder1.view2.setVisibility(View.VISIBLE);
                holder1.tv_date.setText(date1);
            } else {
                String date2 = chatMessages.get(position - 1).getDate();
                if (date1.equals(date2)) {
                    holder1.tv_date.setVisibility(View.GONE);
                    holder1.view1.setVisibility(View.GONE);
                    holder1.view2.setVisibility(View.GONE);
                } else {
                    holder1.tv_date.setVisibility(View.VISIBLE);
                    holder1.view1.setVisibility(View.VISIBLE);
                    holder1.view2.setVisibility(View.VISIBLE);
                    holder1.tv_date.setText(date1);
                }
            }
            SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat output = new SimpleDateFormat("MMM dd yyyy");
            Date oneWayTripDate = null;
            try {
                oneWayTripDate = input.parse("005-10-2017");
                String new_order_date = output.format(oneWayTripDate);
                holder1.timeTextView.setText(chatMessages.get(position).getTime());
            } catch (ParseException e) {
                Log.d("PPPPP", "getView: "+e);
                e.printStackTrace();
            }
            if (chatMessages.get(position).getStatus().equals("1")) {
                holder1.status1.setVisibility(View.VISIBLE);
                holder1.status1.setBackground(context.getResources().getDrawable(R.mipmap.ic_check_all_black_18dp));
            } else if (chatMessages.get(position).getStatus().equals("0")) {
                holder1.status1.setVisibility(View.VISIBLE);
                holder1.status1.setBackground(context.getResources().getDrawable(R.mipmap.ic_check_black_18dp));
            }


            holder1.rel_outgoing.setGravity(chatMessages.get(position).getSender_id().equals(str_userid) ? Gravity.RIGHT : Gravity.LEFT);
            final ViewHolder finalHolder = holder1;
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    v.setBackgroundResource(R.color.red);
                    AlertDialog.Builder ab = new AlertDialog.Builder(context);
                    ab.setMessage(context.getString(R.string.askdeletemsg));
                    ab.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finalHolder.singleMessageContainer.setVisibility(View.GONE);
                            finalHolder.tv_date.setVisibility(View.GONE);
                            finalHolder.view1.setVisibility(View.GONE);
                            finalHolder.view2.setVisibility(View.GONE);

                            // String val = chatMessages.get(position).get;
                            Log.d("KEEE", "onClick: " + key);
                            firebaseChatMessage.removeItem(key, position);
                            firebaseChatMessage.refreshList();
                             notifyDataSetChanged();

                            v.setBackgroundResource(R.color.transparent);
                            dialog.cancel();
                        }
                    });
                    ab.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            v.setBackgroundResource(R.color.transparent);
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = ab.create();
                    alert.show();
                    return true;
                }
            });
        }

        else if (chatMessages.get(position).getReceiver_id().equals(str_userid)) {
            holder1 = new ViewHolder();
            if (view == null) {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.chat_message, null);

                //v = LayoutInflater.from(context).inflate(R.layout.chat_message, viewGroup, false);
                holder1.mainchatlayout = v.findViewById(R.id.mainlayout);
                holder1.rel_outgoing = v.findViewById(R.id.rel_outgoing);
                holder1.rel_incoming = v.findViewById(R.id.rel_incoming);
                holder1.singleMessageContainer = v.findViewById(R.id.outgoinglayout);
                holder1.chatText = v.findViewById(R.id.tv_msg1);
                holder1.timeTextView = v.findViewById(R.id.tv_time1);
                holder1.singleMessageContainer1 = v.findViewById(R.id.incominglayout);
                holder1.chatText1 = v.findViewById(R.id.tv_msg2);
                holder1.timeTextView1 = v.findViewById(R.id.tv_time2);
                holder1.status1 = v.findViewById(R.id.user_reply_status);
                holder1.tv_date = v.findViewById(R.id.tv_date);
                holder1.view1 = v.findViewById(R.id.view1);
                holder1.view2 = v.findViewById(R.id.view2);
                v.setTag(holder1);
                Log.d("DDDDDDD", "getView: "+v);
            } else {
                v = view;
                holder1 = (ViewHolder) v.getTag();
                Log.d("DDDDDDDDDDD", "getView: "+holder1);
            }
            holder1.rel_outgoing.setVisibility(View.GONE);
            holder1.rel_incoming.setVisibility(View.VISIBLE);
            holder1.chatText1.setText(chatMessages.get(position).getMessage());
            String date1 = chatMessages.get(position).getDate();
            if (position == 0) {
                holder1.tv_date.setVisibility(View.VISIBLE);
                holder1.view1.setVisibility(View.VISIBLE);
                holder1.view2.setVisibility(View.VISIBLE);
                holder1.tv_date.setText(date1);
            } else {
                String date2 = chatMessages.get(position - 1).getDate();
                if (date1.equals(date2)) {
                    holder1.tv_date.setVisibility(View.GONE);
                    holder1.view1.setVisibility(View.GONE);
                    holder1.view2.setVisibility(View.GONE);
                } else {
                    holder1.tv_date.setVisibility(View.VISIBLE);
                    holder1.view1.setVisibility(View.VISIBLE);
                    holder1.view2.setVisibility(View.VISIBLE);
                    holder1.tv_date.setText(date1);
                }
            }
            SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat output = new SimpleDateFormat("MMM dd yyyy");
            Date oneWayTripDate = null;
            try {
                oneWayTripDate = input.parse("005-10-2017");
                String new_order_date = output.format(oneWayTripDate);
                holder1.timeTextView1.setText(chatMessages.get(position).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("PPPPP", "getView: "+e);
            }

            Log.d("MMMMMM111", "getView: "+chatMessages.get(position).getMessage());

            holder1.status1.setVisibility(View.INVISIBLE);
            holder1.mainchatlayout.setGravity(chatMessages.get(position).getReceiver_id().equals(str_userid) ? Gravity.LEFT : Gravity.RIGHT);

        }
        Log.d("DSDSDSDSDS", "getView: "+v+"    "+view);

//        if(v==null && view==null)
//        {
//            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            v = mInflater.inflate(R.layout.chat_message, null);
//        }


        return v;
    }


    private class ViewHolder {
        RelativeLayout mainchatlayout;
        RelativeLayout rel_outgoing;
        RelativeLayout rel_incoming;
        RelativeLayout singleMessageContainer;
        TextView chatText;
        TextView timeTextView;
        RelativeLayout singleMessageContainer1;
        TextView chatText1;
        TextView timeTextView1;
        ImageView status1;
        TextView tv_date;
        View view1, view2;

    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
