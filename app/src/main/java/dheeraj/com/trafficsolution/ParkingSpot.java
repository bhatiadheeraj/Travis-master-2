package dheeraj.com.trafficsolution;
/**
 * Created by prasang7 on 5/1/17.
 */
public class ParkingSpot {

    private String parkingName;
    Long freeSlots;
    Double distance, latitude, longitude;
    public ParkingSpot() {}

    public ParkingSpot(String parkingName, Long freeSlots, Double distance, Double latitude, Double longitude) {
        this.parkingName = parkingName;
        this.freeSlots = freeSlots;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getparkingName() {
        return parkingName;
    }
    public void setparkingName(String parkingName) {
        this.parkingName = parkingName;
    }
    public Long getFreeSlots() {
        return freeSlots;
    }
    public void setFreeSlots(Long freeSlots) {
        this.freeSlots = freeSlots;
    }
    public Double getDistance() {
        return distance;
    }
    public void setDistance(Double distance) {
        this.distance = distance;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}