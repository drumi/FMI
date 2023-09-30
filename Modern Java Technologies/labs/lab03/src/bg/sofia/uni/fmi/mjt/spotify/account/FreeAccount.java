package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;

public non-sealed class FreeAccount extends Account {

    private final String email;

    FreeAccount(String email, Library library) {
        super(library);
        this.email = email;
    }

    @Override
    public int getAdsListenedTo() {
        return super.getTotalPlays() / 5;
    }

    @Override
    public AccountType getType() {
        return AccountType.FREE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FreeAccount that = (FreeAccount) o;

        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
