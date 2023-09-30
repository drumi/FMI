package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Bicycle extends BaseVehicle {

    private static final double PRICE_PER_MINUTE = 0.20d;
    private static final String TYPE = "BICYCLE";

    public Bicycle(String id, Location location) {
        super(id, location, TYPE, PRICE_PER_MINUTE);
    }

}
