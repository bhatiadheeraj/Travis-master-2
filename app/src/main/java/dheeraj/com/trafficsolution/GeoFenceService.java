package dheeraj.com.trafficsolution;

/**
 * Created by prasang7 on 10/1/17.
 */
import android.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GeoFenceService extends Service {

    // constant
    public static final long NOTIFY_INTERVAL = 15 * 1000; // 10 seconds


    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    double user_lat, user_long;

    ArrayList<Double> inter_lat = new ArrayList<Double>();
    ArrayList<Double> inter_lon = new ArrayList<Double>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();

        addIntersectionLocations();

        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }


    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        mTimer.cancel();
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    checkGeoFence();

                }
            });
        }

        private void checkGeoFence() {
            Toast.makeText(getApplicationContext(), "Checking Geo Fence", Toast.LENGTH_SHORT).show();

            getUserLocation();

            checkIntersection();
        }

        private void getUserLocation() {
            //TODO: update variables user_lat, user_long with user's current location

            GPSTracker gps = new GPSTracker(getApplicationContext());

            // check if GPS enabled
            if(gps.canGetLocation()){

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                user_lat = 22.7524215;
                user_long = 75.8924856;

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Can't get your location!", Toast.LENGTH_SHORT).show();
                //gps.showSettingsAlert();
            }
        }


        private void checkIntersection() {

            for (int i = 0; i < inter_lat.size(); i++){
                if (closeDistance(user_lat, user_long, inter_lat.get(i), inter_lon.get(i))) {
                    alertUser();
                }
            }
        }

        private void alertUser() {
            Toast.makeText(getApplicationContext(), "ALERT, Intersection nearby!", Toast.LENGTH_SHORT).show();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle("Travis")
                            .setContentText("Alert! Potential Traffic Hazard Nearby!");

            Intent notificationIntent = new Intent(getApplicationContext(), FeedsActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }

        private boolean closeDistance(double lat1, double lon1, double lat2, double lon2) {
            double x = round(distance(lat1, lon1, lat2, lon2), 2);

            // if user is 20 in 20m range of, show alert
            if (x > 0.02) {
                return false;
            }
            else {
                return true;
            }
        }

        private double distance(double lat1, double lon1, double lat2, double lon2) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1))
                    * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }
        private double deg2rad(double deg) {return (deg * Math.PI / 180.0);}
        private double rad2deg(double rad) {return (rad * 180.0 / Math.PI);}

        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
        }
    }



    void addIntersectionLocations() {

        // We can add as many intersection loctions as we want here.
        // Right now i'm hard coding locations, to make it quickly work.
        // We can also dynamically add locations.

        // Right now i'm adding some prominent locations from indore

        //location 1
        inter_lat.add(22.7524215);
        inter_lon.add(75.8924856);

        //location 2
        inter_lat.add(22.7551471);
        inter_lon.add(75.8769981);

        //location 3
        inter_lat.add(22.7485914);
        inter_lon.add(75.9014032);

        //location 4
        inter_lat.add(22.7304513);
        inter_lon.add(75.9012492);

        //location 5
        inter_lat.add(22.7336595);
        inter_lon.add(75.8902427);

        //location 6
        inter_lat.add(22.7259321);
        inter_lon.add(75.8878109);
    }
}