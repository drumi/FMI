package bg.sofia.uni.fmi.mjt.spotify.playable;

abstract class BasePlayable implements Playable {

    private final String title;
    private final String artist;
    private final int year;
    private final double duration;

    public BasePlayable(String title, String artist, int year, double duration) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.duration = duration;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public double getDuration() {
        return duration;
    }

}
