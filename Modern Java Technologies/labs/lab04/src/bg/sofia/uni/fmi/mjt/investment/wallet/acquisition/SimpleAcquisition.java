package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.time.LocalDateTime;

public final class SimpleAcquisition implements Acquisition {

    private final double price;
    private final LocalDateTime timeStamp;
    private final int quantity;
    private final Asset asset;

    public SimpleAcquisition(double price, LocalDateTime timeStamp, int quantity, Asset asset) {
        this.price = price;
        this.timeStamp = timeStamp;
        this.quantity = quantity;
        this.asset = asset;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public Asset getAsset() {
        return asset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleAcquisition that = (SimpleAcquisition) o;

        if (Double.compare(that.price, price) != 0) return false;
        if (quantity != that.quantity) return false;
        if (!timeStamp.equals(that.timeStamp)) return false;
        return asset.equals(that.asset);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(price);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + timeStamp.hashCode();
        result = 31 * result + quantity;
        result = 31 * result + asset.hashCode();
        return result;
    }
}
