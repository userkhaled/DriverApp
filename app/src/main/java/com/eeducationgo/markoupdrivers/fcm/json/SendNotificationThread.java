package com.eeducationgo.markoupdrivers.fcm.json;

import android.os.AsyncTask;
import android.util.Log;


import com.eeducationgo.markoupdrivers.util.Constance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotificationThread {


    private static final String TAG = "SendNotificationThread";

    private static SendNotificationThread instance;

    private static OkHttpClient client;

    private SendNotificationThread() {
        instance = this;
    }

    public static SendNotificationThread getInstance() {
        if (instance == null) {
            instance = new SendNotificationThread();
            client = new OkHttpClient().newBuilder()
                    .build();
        }
        return instance;
    }

    public void sendNotificationTo(String name, String body, String token, String id) {
        new AsyncTaskNotification(name, body, token, id).execute();
    }

    public static class AsyncTaskNotification extends AsyncTask<String, Void, Void> {
        private String name, body, token;

        public AsyncTaskNotification(String name, String body, String token, String id) {
            this.name = name;
            this.body = body;
            this.token = token;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {

                MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
                JSONObject mainNotificationObject = new JSONObject();
                JSONObject bodyNotificationObject = new JSONObject();
                JSONObject idNotificationObject = new JSONObject();
                //JSONObject idBodyNotificationObject = new JSONObject();
                try {
                    mainNotificationObject.put(Constance.TO_HEADER, token);
                    bodyNotificationObject.put(Constance.TITLE_HEADER, name);
                    bodyNotificationObject.put(Constance.BODY_HEADER, body);
                    mainNotificationObject.put(Constance.NOTIFICATION_OBJECT_HEADER, bodyNotificationObject);
                   // mainNotificationObject.put("data", idBodyNotificationObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "doInBackground: " + mainNotificationObject.toString());
                RequestBody body = RequestBody.create(mediaType, mainNotificationObject.toString());
                Request request = new Request.Builder()
                        .url(Constance.BASE_URL)
                        .method(Constance.REQUEST_METHOD, body)
                        .addHeader(Constance.CONTENT_TYPE_HEADER, Constance.CONTENT_TYPE)
                        .addHeader(Constance.AUTH_HEADER, Constance.AUTH)
                        .build();
                Response response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
