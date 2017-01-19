package dheeraj.com.trafficsolution;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by prasang7 on 28/12/16.
 */
public class ParkingFragment extends Fragment {

    View view;
    Button bt_find_2wheeler_Parking, bt_find_4wheeler_Parking;
    TextView tv_title, tv_content;

    public ParkingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_parking, container, false);
        init();

        bt_find_2wheeler_Parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ParkingSearchResult.class);
                i.putExtra("key", "2 Wheeler");
                startActivity(i);
            }
        });

        bt_find_4wheeler_Parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ParkingSearchResult.class);
                i.putExtra("key", "4 Wheeler");
                startActivity(i);
            }
        });

        return view;
    }

    void init() {
        bt_find_2wheeler_Parking = (Button) view.findViewById(R.id.bt_find_2wheeler_parking);
        bt_find_4wheeler_Parking = (Button) view.findViewById(R.id.bt_find_4wheeler_parking);

        tv_title = (TextView)view.findViewById(R.id.tv_parkingFinder_title);
        tv_content = (TextView)view.findViewById(R.id.tv_parkingFinder_content);

        Typeface MontReg = Typeface.createFromAsset(getActivity().getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getActivity().getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        bt_find_2wheeler_Parking.setTypeface(MontBold);
        bt_find_4wheeler_Parking.setTypeface(MontBold);

        tv_title.setTypeface(MontBold);
        tv_content.setTypeface(MontReg);
    }
}