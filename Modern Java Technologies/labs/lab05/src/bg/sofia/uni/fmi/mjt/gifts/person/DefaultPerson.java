package bg.sofia.uni.fmi.mjt.gifts.person;

import bg.sofia.uni.fmi.mjt.gifts.Utils;
import bg.sofia.uni.fmi.mjt.gifts.exception.WrongReceiverException;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;

import java.util.*;

public class DefaultPerson<I> implements Person<I> {

    private I id;
    private List<Gift<?>> allGiftsReceived = createList();
    private Map<Person<?>,
                List<Gift<?>>> personToReceivedGifts = new HashMap<>();

    public DefaultPerson(I id) {
        this.id = id;
    }

    @Override
    public Collection<Gift<?>> getNMostExpensiveReceivedGifts(int n) {
        Utils.requireNonNegative(n);

        if (n >= allGiftsReceived.size()) {
            return List.copyOf(allGiftsReceived);
        }

        Collections.sort(allGiftsReceived, new Comparator<Gift<?>>() {
            @Override
            public int compare(Gift<?> o1, Gift<?> o2) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });

        var nMostExpensive = allGiftsReceived.subList(allGiftsReceived.size() - n, allGiftsReceived.size());

        return List.copyOf(nMostExpensive);
    }

    @Override
    public Collection<Gift<?>> getGiftsBy(Person<?> person) {
        Utils.requireNonNull(person);
        List<Gift<?>> gifts = personToReceivedGifts.getOrDefault(person, createList());
        return List.copyOf(gifts);
    }

    @Override
    public I getId() {
        return id;
    }

    @Override
    public void receiveGift(Gift<?> gift) {
        if (!this.equals(gift.getReceiver())) {
            throw new WrongReceiverException();
        }

        Person<?> sender = gift.getSender();

        if (personToReceivedGifts.containsKey(sender)) {
            personToReceivedGifts.get(sender).add(gift);
        } else {
            List<Gift<?>> list = createList();
            list.add(gift);
            personToReceivedGifts.put(sender, list);
        }

        allGiftsReceived.add(gift);
    }

    private <T> List<T> createList() {
        return new ArrayList<T>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultPerson)) return false;

        DefaultPerson<?> that = (DefaultPerson<?>) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
