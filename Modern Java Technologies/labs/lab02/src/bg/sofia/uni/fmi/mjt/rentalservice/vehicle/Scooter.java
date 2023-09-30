package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Scooter extends BaseVehicle {

    private static final double PRICE_PER_MINUTE = 0.30d;
    private static final String TYPE = "SCOOTER";

    public Scooter(String id, Location location) {
        super(id, location, TYPE, PRICE_PER_MINUTE);
    }

}
