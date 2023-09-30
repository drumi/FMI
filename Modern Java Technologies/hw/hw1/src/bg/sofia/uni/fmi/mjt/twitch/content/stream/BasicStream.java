package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.time.LocalDateTime;

public class BasicStream implements Stream {

    private final Metadata metadata;
    private final LocalDateTime startedAt;
    private int numberOfViews = 0;

    public BasicStream(Metadata metadata, LocalDateTime startedAt) {
        this.metadata = metadata;
        this.startedAt = startedAt;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(startedAt, LocalDateTime.now());
    }

    @Override
    public void startWatching(User user) {
        numberOfViews++;
    }

    @Override
    public void stopWatching(User user) {
        numberOfViews--;
    }

    @Override
    public int getNumberOfViews() {
        return numberOfViews;
    }
}
