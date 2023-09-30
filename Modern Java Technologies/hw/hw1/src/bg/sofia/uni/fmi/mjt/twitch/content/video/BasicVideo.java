package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;

public class BasicVideo implements Video {

    private final Metadata metadata;
    private final Duration duration;
    private int numberOfViews = 0;

    public BasicVideo(Metadata metadata, Duration duration) {
        this.metadata = metadata;
        this.duration = duration;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void startWatching(User user) {
        numberOfViews++;
    }

    @Override
    public void stopWatching(User user) {
        /* Implemented */
    }

    @Override
    public int getNumberOfViews() {
        return numberOfViews;
    }
}
