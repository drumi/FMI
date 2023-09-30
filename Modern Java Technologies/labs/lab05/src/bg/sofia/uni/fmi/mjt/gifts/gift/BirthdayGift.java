package bg.sofia.uni.fmi.mjt.gifts.gift;

import bg.sofia.uni.fmi.mjt.gifts.Utils;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BirthdayGift<T extends Priceable> implements Gift<T> {

    Person<?> sender;
    Person<?> receiver;
    List<T> items;

    public BirthdayGift(Person<?> sender, Person<?> receiver, Collection<T> items) {
        this.sender = sender;
        this.receiver = receiver;
        this.items = createListFrom(items);
    }

    private List<T> createListFrom(Collection<T> c) {
        return new ArrayList<T>(c);
    }

    @Override
    public Person<?> getSender() {
        return sender;
    }

    @Override
    public Person<?> getReceiver() {
        return receiver;
    }

    @Override
    public double getPrice() {
        return calculatePrice(items);
    }

    private double calculatePrice(Collection<T> items) {
        double price = 0.0;

        for (T item : items) {
            price += item.getPrice();
        }

        return price;
    }

    @Override
    public void addItem(T t) {
        Utils.requireNonNull(t);
        items.add(t);
    }

    @Override
    public boolean removeItem(T t) {
        return items.remove(t);
    }

    @Override
    public Collection<T> getItems() {
        return List.copyOf(items);
    }

    @Override
    public T getMostExpensiveItem() {
        T mostExpensive = null;
        double mostExpensivePrice = 0.0;

        for (T item : items) {
            if (mostExpensivePrice < item.getPrice()) {
                mostExpensive = item;
                mostExpensivePrice = item.getPrice();
            }
        }

        return mostExpensive;
    }

}
