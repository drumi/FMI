package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;

abstract class BaseVehicle implements Vehicle {

    private final double pricePerMinute;
    private final String type;

    private final String id;
    private final Location location;
    private LocalDateTime endOfReservationPeriod;

    public BaseVehicle(String id, Location location, String type, double pricePerMinute) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.pricePerMinute = pricePerMinute;
    }

    @Override
    public double getPricePerMinute() {
        return pricePerMinute;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public LocalDateTime getEndOfReservationPeriod() {
        if (endOfReservationPeriod == null) {
            return LocalDateTime.now();
        }

        return endOfReservationPeriod;
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime until) {
        endOfReservationPeriod = until;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseVehicle that = (BaseVehicle) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
