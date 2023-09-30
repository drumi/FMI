package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class UserPlaylist implements Playlist {

    private static final int MAX_SIZE = 20;

    private final String name;
    private final Playable[] playables = new Playable[MAX_SIZE];
    private int currentSize = 0;

    public UserPlaylist(String name) {
        this.name = name;
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        if (currentSize == MAX_SIZE) {
            throw new PlaylistCapacityExceededException();
        }

        addIntoArray(playable);
    }

    private void addIntoArray(Playable playable) {
        playables[currentSize] = playable;
        currentSize++;
    }

    @Override
    public String getName() {
        return name;
    }

}
