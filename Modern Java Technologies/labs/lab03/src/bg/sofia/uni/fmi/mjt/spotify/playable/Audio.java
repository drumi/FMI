package bg.sofia.uni.fmi.mjt.spotify.playable;

public class Audio extends BasePlayable {

    private int totalPlays;

    public Audio(String title, String artist, int year, double duration) {
        super(title, artist, year, duration);
    }

    @Override
    public String play() {
        totalPlays++;
        /* Doing audio magic playback */
        return "Currently playing audio content: " + super.getTitle();
    }

    @Override
    public int getTotalPlays() {
        return totalPlays;
    }

}
