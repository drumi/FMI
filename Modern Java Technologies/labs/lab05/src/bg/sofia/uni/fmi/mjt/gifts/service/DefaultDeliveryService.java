package bg.sofia.uni.fmi.mjt.gifts.service;

import bg.sofia.uni.fmi.mjt.gifts.Utils;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

public class DefaultDeliveryService implements DeliveryService {
    @Override
    public void send(Person<?> receiver, Gift<?> gift) {
        Utils.requireNonNull(receiver);
        Utils.requireNonNull(gift);

        receiver.receiveGift(gift);
    }
}
