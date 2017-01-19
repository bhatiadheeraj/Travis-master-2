package dheeraj.com.trafficsolution;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dheeraj.com.trafficsolution.Utils.SharedPreferenceMethods;

/**
 * Created by prasang7 on 28/12/16.
 */
public class GeoFenceFragment extends Fragment {

    View view;
    TextView tv_title, tv_content;
    Button bt_startService;
    boolean pedestrian_mode;

    public GeoFenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_geofence, container, false);

        init();

        //TODO: REMOVE THIS LINE OF CODE!
        SharedPreferenceMethods.setBoolean(getActivity(), "PEDESTRIAN_MODE", false);


        // Get pedestrian mode data from sharedprefs
        if (SharedPreferenceMethods.getBoolean(getActivity(), "PEDESTRIAN_MODE") == false) {
            pedestrian_mode = false;
        }
        else if (SharedPreferenceMethods.getBoolean(getActivity(), "PEDESTRIAN_MODE") == true) {
            pedestrian_mode = true;
        }

        setUIAccordingToMode(pedestrian_mode);

        bt_startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pedestrian_mode == true) {
                    pedestrian_mode = false;
                    stopPedestrianMode();
                }
                else if (pedestrian_mode == false) {
                    pedestrian_mode = true;
                    startPedestrianMode();
                }

                setUIAccordingToMode(pedestrian_mode);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    void startPedestrianMode() {
        Toast.makeText(getActivity(), "Pedestrian Mode Activated", Toast.LENGTH_LONG).show();

        getActivity().startService(new Intent(getActivity(), GeoFenceService.class));
    }

    void stopPedestrianMode() {
        Toast.makeText(getActivity(), "Pedestrian Mode Deactivated", Toast.LENGTH_LONG).show();

        getActivity().stopService(new Intent(getActivity(), GeoFenceService.class));
    }

    void init() {

        tv_title = (TextView)view.findViewById(R.id.tv_pedestrian_title);
        tv_content = (TextView)view.findViewById(R.id.tv_pedestrian_content);
        bt_startService = (Button)view.findViewById(R.id.bt_geoFence_startService);

        Typeface MontReg = Typeface.createFromAsset(getActivity().getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getActivity().getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        tv_title.setTypeface(MontBold);
        tv_content.setTypeface(MontReg);
        bt_startService.setTypeface(MontBold);
    }

    void setUIAccordingToMode(boolean pedestrian_mode_value) {
        if (pedestrian_mode_value == true) {
            bt_startService.setText("Deactivate Pedestrian Mode");
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        else if (pedestrian_mode_value == false) {
            bt_startService.setText("Activate Pedestrian Mode");
            view.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
        }
    }
}