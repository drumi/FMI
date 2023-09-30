package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.StreamingServiceException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class Spotify implements StreamingService {

    private final static double REVENUE_PER_AD = 0.1;
    private final static double PREMIUM_SUBSCRIPTION_FEE = 25.0;

    private final Account[] accounts;
    private final Playable[] playables;

    public Spotify(Account[] accounts, Playable[] playableContent) {
        this.accounts = accounts;
        this.playables = playableContent;
    }

    @Override
    public void play(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException {
        requireNonNull(account);
        requireNonEmpty(title);

        Playable playableToPlay = findByTitle(title);
        Account accountInSystem = findAccount(account);
        accountInSystem.listen(playableToPlay);
    }

    private void requireNonEmpty(String s) {
        if(s == null || s.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void requireNonNull (Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }

    private Account findAccount(Account account) throws AccountNotFoundException {
        for (Account a : accounts) {
            if (a.equals(account)) {
                return a;
            }
        }

        throw new AccountNotFoundException();
    }

    @Override
    public void like(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException, StreamingServiceException {
        requireNonNull(account);
        requireNonEmpty(title);

        Account accountInSystem = findAccount(account);
        Playable playableToLike = findByTitle(title);
        like(accountInSystem, playableToLike);
    }

    private void like(Account account, Playable playable) throws StreamingServiceException {
        try {
            account.like(playable);
        } catch (PlaylistCapacityExceededException e) {
            throw new StreamingServiceException();
        }
    }

    @Override
    public Playable findByTitle(String title) throws PlayableNotFoundException {
        requireNonEmpty(title);

        for (Playable playable : playables) {
            if (playable.getTitle().equals(title)) {
                return playable;
            }
        }

        throw new PlayableNotFoundException();
    }

    @Override
    public Playable getMostPlayed() {
        if (playables.length == 0) {
            return null;
        }

        Playable currentMostPlayed = playables[0];

        for (int i = 1; i < playables.length; i++) {
            if (playables[i].getTotalPlays() > currentMostPlayed.getTotalPlays()) {
                currentMostPlayed = playables[i];
            }
        }
        return currentMostPlayed;
    }

    @Override
    public double getTotalListenTime() {
        double totalListenTime = 0;

        for (Account a : accounts) {
            totalListenTime += a.getTotalListenTime();
        }

        return totalListenTime;
    }

    @Override
    public double getTotalPlatformRevenue() {
        double totalRevenue = 0.0;

        for (Account a : accounts) {
            totalRevenue += switch (a.getType()) {
                case FREE -> REVENUE_PER_AD * a.getAdsListenedTo();
                case PREMIUM -> PREMIUM_SUBSCRIPTION_FEE;
            };
        }

        return totalRevenue;
    }

}
