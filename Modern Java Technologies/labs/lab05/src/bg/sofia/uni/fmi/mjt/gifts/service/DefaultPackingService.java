package bg.sofia.uni.fmi.mjt.gifts.service;

import bg.sofia.uni.fmi.mjt.gifts.Utils;
import bg.sofia.uni.fmi.mjt.gifts.gift.BirthdayGift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Priceable;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.*;

public class DefaultPackingService<T extends Priceable> implements PackingService<T> {

    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T item) {
        Utils.requireNonNull(sender);
        Utils.requireNonNull(receiver);
        Utils.requireNonNull(item);

        return createGift(sender, receiver, item);
    }

    private Gift<T> createGift(Person<?> sender, Person<?> receiver, T... items) {
        List<T> list = createList(items);
        return new BirthdayGift<>(sender, receiver, list);
    }

    private List<T> createList(T... items) {
        return new ArrayList<T>(Arrays.asList(items));
    }

    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T... items) {
        Utils.requireNonNull(sender);
        Utils.requireNonNull(receiver);
        Utils.requireNonNull(items);

        return createGift(sender, receiver, items);
    }

    @Override
    public Collection<T> unpack(Gift<T> gift) {
        Utils.requireNonNull(gift);
        return gift.getItems();
    }

}
