package net.rickiekarp.reddit.mail;

import org.apache.http.client.HttpClient;

import net.rickiekarp.reddit.common.RedditIsFunHttpClientFactory;
import net.rickiekarp.reddit.settings.RedditSettings;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * This is an example of implementing an application service that will run in
 * response to an alarm, allowing us to move long duration work out of an
 * intent receiver.
 * 
 * @see AlarmService
 * @see AlarmService_Alarm
 */
public class EnvelopeService extends Service {
    NotificationManager mNM;
    private RedditSettings mSettings = new RedditSettings();
    private HttpClient mClient = RedditIsFunHttpClientFactory.getGzipHttpClient();

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mSettings.loadRedditPreferences(this, mClient);
        new PeekEnvelopeServiceTask(this, mClient, mSettings.getMailNotificationStyle()).execute();
    }
    
    private class PeekEnvelopeServiceTask extends PeekEnvelopeTask {
    	public PeekEnvelopeServiceTask(Context context, HttpClient client, String mailNotificationStyle) {
    		super(context, client, mailNotificationStyle);
    	}
    	@Override
    	public void onPostExecute(Object count) {
    		super.onPostExecute(count);
    		EnvelopeService.this.stopSelf();
    	}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * This is the object that receives interactions from clients.  See RemoteService
     * for a more complete example.
     */
    private final IBinder mBinder = new Binder();

    public static void resetAlarm(Context context, long interval) {
        // Create an IntentSender that will launch our service, to be scheduled
        // with the alarm manager.
        PendingIntent alarmSender = PendingIntent.getService(context, 0, new Intent(context, EnvelopeService.class), 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(alarmSender);
        if (interval != 0)
        	am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, alarmSender);
    }
}

