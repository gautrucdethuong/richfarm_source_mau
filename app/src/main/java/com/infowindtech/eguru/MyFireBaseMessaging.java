package com.infowindtech.eguru;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.infowindtech.eguru.StudentPanel.StudentHomeActivity;
import com.infowindtech.eguru.TeacherPanel.Models.UserSessionManager;

import java.util.HashMap;

public class MyFireBaseMessaging extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    UserSessionManager userSessionManager;
    String usertype="0";
    Intent notificationIntent;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        userSessionManager = new UserSessionManager(MyFireBaseMessaging.this);
        final HashMap<String, String> user = userSessionManager.getUserDetails();
        usertype = user.get(UserSessionManager.KEY_USERTYPE);
        Log.d(TAG, "onMessageReceived: "+usertype);
        if(usertype==null)
        {
            usertype="0";
        }

        Log.d(TAG, "onMessageReceived: "+usertype);
        FirebaseApp.initializeApp(this);

//
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
////       Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("message")+  "" +
////               "     "+remoteMessage.getData().get("body")+"      "+remoteMessage.getNotification().getBody());
//
//
////            String message =remoteMessage.getNotification().getBody();
////             String title = remoteMessage.getNotification().getTitle();
////            Log.d(TAG, "onMessageReceived:   data "+message+" "+title);
////             sendNotification(message,title);
//
//
//        String message =remoteMessage.getData().get("body");
//        String title = remoteMessage.getData().get("title");
//        Log.d(TAG, "onMessageReceived:   data "+message+" "+title);
//        sendNotification(message,title);
//
//








        Log.d(TAG, "From: " + remoteMessage.getFrom()+"  "+remoteMessage.getData().get("title")+"  "+remoteMessage.getData().get("body"));

        if (remoteMessage.getData() != null) {


          //  Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


            sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));


        }


//        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        String channelId = "Default";
//        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(channel);
//        }
//        manager.notify(0, builder.build());








//
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            AlertClass.DispToast(this,"data payload");
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                sendNotification(remoteMessage.getData().get("message"));
//            } else {
//                // Handle message within 10 seconds
//                sendNotification(remoteMessage.getData().get("message"));
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            AlertClass.DispToast(this,"notification payload");
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody());
//        }


    }


    @Override
    public void onNewToken(String token) {

        Log.e("newToken:", token);

        sendRegistrationToServer(token);
    }



    private void sendNotification(String messageBody, String title) {

        if (usertype.equals("1")) {
            notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else if (usertype.equals("2")) {
            notificationIntent = new Intent(this, StudentHomeActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            notificationIntent = new Intent(this, Login_Fragment.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(MyFireBaseMessaging.this, 0 /* Request code */, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.final_logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }else {

        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendRegistrationToServer(String token) {

        // TODO: Implement this method to send token to your app server.

    }
}

//    private static final String TAG = "MyFireBaseMsgService ";
//    private LogConfig logConfig = LogConfig.getInstance();
//    private boolean isAPP_FOREGROUND = false;
//    private int id = 0;
//    private Context mContext;
//
//    /**
//     * Called when message is received.
//     *
//     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
//     */
//    // [START receive_message]
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        //this.mContext = this;
//
//        logConfig.printP(TAG, "From: " + remoteMessage.getFrom());
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            logConfig.printP(TAG, "Message data payload: " + remoteMessage.getData());
//            String message = remoteMessage.getData().get("message");
//            String type = remoteMessage.getData().get("type");
//            String chat_data = remoteMessage.getData().get("chat_data");
//            // Also if you intend on generating your own notifications as a result of a received FCM
//            // message, here is where that should be initiated. See sendNotification method below.
////            int count = (1 + mSharedPrefUtility.getNotificationCount());
////            if(type.equals("chat")) {
////                Intent itAction = new Intent(WebUrls.ACTION_CHAT);
////                itAction.putExtra("type", type);
////                itAction.putExtra("chat_data", chat_data);
////                mContext.sendBroadcast(itAction);
////            } else {
////                mSharedPrefUtility.setNotificationCount(count);
////                String childId = "" + (1 + id);
////                Intent itAction = new Intent(WebUrls.ACTION_NOTIFICATION);
////                mContext.sendBroadcast(itAction);
////            }
//
//            logConfig.printP("MyFireBaseMessagingService ", "isAPP_FOREGROUND: "+isAPP_FOREGROUND+" message:"+message);
//
//            sendNotification(message); //remoteMessage.getNotification().getBody()
//            if (isAPP_FOREGROUND) {
//                //createNotificationForeground(mContext, message);
//            } else {
//                //createNotification(mContext, message, childId);
//                //generateNotification(context, message, childId);
//                setBadge(mContext, 1);
//            }
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            logConfig.printP(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//
//        sendNotification(remoteMessage.getNotification().getBody());
//    }
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the InstanceID token
//     * is initially generated so this is where you would retrieve the token.
//     */
//    @Override
//    public void onNewToken(String token) {
//        logConfig.printP(TAG, "Refreshed token: " + FirebaseInstanceId.getInstance().getToken());
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
//
//    }
//
//
//    /**
//     * Persist token to third-party servers.
//     * <p>
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private void sendRegistrationToServer(String token) {
//        // TODO: Implement this method to send token to your app server.
//    }
//
//    /**
//     * Create and show a simple notification containing the received FCM message.
//     *
//     * @param messageBody FCM message body received.
//     */
//
//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, CustomerHomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_list_icon)
//                        .setContentTitle("HYP")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
//
//
//
//
//    private void sendNotification(String messageBody, String type, String channelId) {
//
//        String title = "HYP";//mContext.getString(R.string.app_name);
//
//        Intent intent = new Intent(mContext, SplashActivity.class);
//        intent.putExtra("Notification",""+messageBody);
//        intent.putExtra("Notification_Type", type);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */,
//                intent, PendingIntent.FLAG_ONE_SHOT);
//
//        //String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(mContext, channelId)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(title)
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//        Log.d(TAG, "sendNotification: ");
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
//
//    public boolean isApplicationSentToBackground(final Context context) {
//
//        try {
//            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            @SuppressWarnings("deprecation")
//            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
//            if (!tasks.isEmpty()) {
//                ComponentName topActivity = tasks.get(0).topActivity;
//                if (!topActivity.getPackageName().equals(context.getPackageName())) {
//                    return true;
//                }
//            }
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public void setBadge(Context context, int count) {
//        String launcherClassName = getLauncherClassName(context);
//        if (launcherClassName == null) {
//            return;
//        }
//        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
//        intent.putExtra("badge_count", count);
//        intent.putExtra("badge_count_package_name", context.getPackageName());
//        intent.putExtra("badge_count_class_name", launcherClassName);
//        context.sendBroadcast(intent);
//    }
//
//    public static String getLauncherClassName(Context context) {
//
//        PackageManager pm = context.getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
//        for (ResolveInfo resolveInfo : resolveInfos) {
//            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
//            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
//                String className = resolveInfo.activityInfo.name;
//                return className;
//            }
//        }
//        return null;
//    }
//}
