package android.example.com.squawker.fcm;

import android.content.ContentValues;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.example.com.squawker.utils.NotificationUtils;
import android.os.AsyncTask;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        if (data != null){
            storeMessageInDb(data);
            sendNotification(data);
        }
    }

    private void storeMessageInDb (final Map<String, String> data){
        AsyncTask<Void, Void, Void> insertSquawk = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                ContentValues values = new ContentValues();
                values.put(SquawkContract.COLUMN_AUTHOR, data.get(SquawkContract.COLUMN_AUTHOR));
                values.put(SquawkContract.COLUMN_AUTHOR_KEY, data.get(SquawkContract.COLUMN_AUTHOR_KEY));
                values.put(SquawkContract.COLUMN_MESSAGE, data.get(SquawkContract.COLUMN_MESSAGE));
                values.put(SquawkContract.COLUMN_DATE, data.get(SquawkContract.COLUMN_DATE));

                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, values);

                return null;
            }
        };

        insertSquawk.execute();
    }

    private void sendNotification(Map<String, String> data){
        String message = data.get(SquawkContract.COLUMN_MESSAGE);

        NotificationUtils.newMessageNotification(getApplicationContext(),
                getResources().getString(R.string.notification_message), message);
    }
}
