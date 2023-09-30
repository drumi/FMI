package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Car extends BaseVehicle {

    private static final double PRICE_PER_MINUTE = 0.50d;
    private static final String TYPE = "CAR";

    public Car(String id, Location location) {
        super(id, location, TYPE, PRICE_PER_MINUTE);
    }

}
