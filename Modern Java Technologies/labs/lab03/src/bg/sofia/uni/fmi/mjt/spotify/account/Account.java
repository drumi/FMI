package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public sealed abstract class Account permits PremiumAccount, FreeAccount {

    private final Library library;
    private int totalPlays = 0;
    private double totalListenTime = 0.0;

    public Account(Library library) {
        this.library = library;
    }

    /**
     * Returns the number of ads listened to.
     * - Free accounts get one ad after every 5 pieces of content played
     * - Premium accounts get no ads
     */
    public abstract int getAdsListenedTo();

    /**
     * Returns the account type as an enum with possible values FREE and PREMIUM
     */
    public abstract AccountType getType();

    /**
     * Simulates listening of the specified content.
     * This should increment the total number of content listened and the total listen time for this account.
     *
     * @param playable
     */
    public void listen(Playable playable) {
        playable.play();
        totalPlays++;
        totalListenTime += playable.getDuration();
    }

    public void like(Playable playable) throws PlaylistCapacityExceededException {
        this.getLibrary()
            .getLiked()
            .add(playable);
    }

    /**
     * Returns the library for this account.
     */
    public Library getLibrary() {
        return library;
    }

    /**
     * Returns the total listen time for this account. The time for any ads listened is not included.
     */
    public double getTotalListenTime() {
        return totalListenTime;
    }

    protected int getTotalPlays() {
        return totalPlays;
    }

}
