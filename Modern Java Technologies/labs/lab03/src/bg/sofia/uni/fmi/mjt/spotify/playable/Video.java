package bg.sofia.uni.fmi.mjt.spotify.playable;

public class Video extends BasePlayable {

    private int totalPlays = 0;

    public Video(String title, String artist, int year, double duration) {
        super(title, artist, year, duration);
    }

    @Override
    public String play() {
        totalPlays++;
        /* Doing video magic playback */
        return "Currently playing video content: " + super.getTitle();
    }

    @Override
    public int getTotalPlays() {
        return totalPlays;
    }

}
