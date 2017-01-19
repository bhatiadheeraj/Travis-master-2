package dheeraj.com.trafficsolution;

/**
 * Created by prasang7 on 5/1/17.
 */
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import android.content.Context;
import android.widget.Toast;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.MyViewHolder> {

    private Context mContext;
    private List<ParkingSpot> parkingList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_parkingName, tv_freeSlots, tv_distance, tv_latitude, tv_longitude;
        public Button bt_openInMap;

        public MyViewHolder(View view) {
            super(view);
            tv_parkingName = (TextView) view.findViewById(R.id.tv_parkingSpot_parkingName);
            tv_freeSlots = (TextView) view.findViewById(R.id.tv_parkingSpot_freeSpots);
            tv_distance = (TextView) view.findViewById(R.id.tv_parkingSpot_distance);
            bt_openInMap = (Button) view.findViewById(R.id.bt_parkingSpot_openInMap);
        }
    }

    public ParkingAdapter(Context mContext, List<ParkingSpot> parkingList) {
        this.mContext = mContext;
        this.parkingList = parkingList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ParkingSpot parkingSpot = parkingList.get(position);
        holder.tv_parkingName.setText(parkingSpot.getparkingName());
        holder.tv_freeSlots.setText("No of Free Parking Spots: " + parkingSpot.getFreeSlots().toString());
        holder.tv_distance.setText("Distance from your location: " + parkingSpot.getDistance().toString() + " km");

        holder.bt_openInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = holder.getAdapterPosition();
                openMaps(parkingSpot.getLatitude(), parkingSpot.getLongitude());
            }
        });
    }

    void openMaps(Double lat_value, Double lon_value) {
        String url = "http://maps.google.com/?q=" + lat_value + "," + lon_value;
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }
}
