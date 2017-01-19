package dheeraj.com.trafficsolution;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasang7 on 5/1/17.
 */
public class ParkingSearchResult extends AppCompatActivity {

    public ParkingSpot a;

    private RecyclerView recyclerView;
    private ParkingAdapter adapter;
    private List<ParkingSpot> parkingList;
    ProgressDialog rd;

    TextView tv_parkingSearchType;

    String parkingType;
    double user_lat, user_lon;

    ArrayList<String> parkingLocations = new ArrayList<String>();
    ArrayList<Double> park_lat = new ArrayList<Double>();
    ArrayList<Double> park_lon = new ArrayList<Double>();
    ArrayList<Long> park_freeSpots = new ArrayList<Long>();
    ArrayList<Double> park_distances = new ArrayList<Double>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        if (getLocation()) {
            getAllParkingData(parkingType, user_lat, user_lon);
        }
        else {
            rd.hide();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParkingSearchResult.this);
            alertDialog.setTitle("Oops!");
            alertDialog.setMessage("Cannot find your location!");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }});
            alertDialog.show();
        }
    }

    boolean getLocation() {
        /*
        TODO: getLocation() function is empty right now.
         */
        GPSTracker gps = new GPSTracker(getApplicationContext());

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            user_lat = latitude;
            user_lon = longitude;

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        // dummy location - Apna Sweets - Vijay Nagar!
        //user_lat = 22.7495949;
        //user_lon = 75.8934653;
        return true;
    }

    void getAllParkingData(final String parking_type, final double usr_lat, final double usr_lon) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parking_spots");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    //Parsing Data
                    parkingLocations.add(postSnapshot.child("name").getValue().toString());
                    park_lat.add((Double) postSnapshot.child("Latitude").getValue());
                    park_lon.add((Double) postSnapshot.child("Longitude").getValue());
                    if (parking_type.equals("2 Wheeler")) {
                        park_freeSpots.add((Long) postSnapshot.child("2_freeSpots").getValue());
                    }
                    else if (parking_type.equals("4 Wheeler")) {
                        park_freeSpots.add((Long) postSnapshot.child("4_freeSpots").getValue());
                    }
                    park_distances.add(distance(usr_lat, usr_lon, park_lat.get(count), park_lon.get(count)));

                    a = new ParkingSpot(parkingLocations.get(count), park_freeSpots.get(count),
                            round(park_distances.get(count), 2), park_lat.get(count), park_lon.get(count));

                    parkingList.add(a);

                    count++;
                }
                adapter.notifyDataSetChanged();
                rd.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ParkingSearchResult.this, "Error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                rd.hide();
            }
        });
    }


    void init() {
        setContentView(R.layout.activity_parking_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        parkingType = intent.getExtras().getString("key");
        tv_parkingSearchType = (TextView)findViewById(R.id.tv_parkingSearch_parkingType);
        tv_parkingSearchType.setText("Showing result for " + parkingType + " parking.");

        rd = new ProgressDialog(ParkingSearchResult.this);
        rd.setTitle("Please Wait!");
        rd.setMessage("Loading Data...");
        rd.setCancelable(false);
        rd.show();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        parkingList = new ArrayList<>();
        adapter = new ParkingAdapter(this, parkingList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ParkingSearchResult.this, FeedsActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
