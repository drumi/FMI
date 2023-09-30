package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalService implements  RentalServiceAPI {

    private Vehicle[] vehicles;

    public RentalService(Vehicle[] vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public double rentUntil(Vehicle vehicle, LocalDateTime until) {
        final double INVALID_REQUEST = -1.0d;

        for (var v : vehicles) {
            if (v.equals(vehicle)) {
                if (isVehicleBooked(v)) {
                    return INVALID_REQUEST;
                }

                v.setEndOfReservationPeriod(until);
                return calculatePriceForRenting(v);
            }
        }

        return INVALID_REQUEST;
    }

    private boolean isVehicleBooked(Vehicle vehicle) {
        return LocalDateTime.now().isBefore(vehicle.getEndOfReservationPeriod());
    }

    private double calculatePriceForRenting(Vehicle vehicle) {
        long reservedForMinutes = ChronoUnit.MINUTES.between(vehicle.getEndOfReservationPeriod(), LocalDateTime.now());
        return vehicle.getPricePerMinute() * reservedForMinutes;
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        for (var vehicle : vehicles) {
            if (vehicle.getType().equals(type) &&
                    Location.isDistanceLessOrEqualTo(location, vehicle.getLocation(), maxDistance) &&
                    !isVehicleBooked(vehicle)) {
                return vehicle;
            }
        }

        return null;
    }

}
