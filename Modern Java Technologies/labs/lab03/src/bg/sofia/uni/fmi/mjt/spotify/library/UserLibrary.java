package bg.sofia.uni.fmi.mjt.spotify.library;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class UserLibrary implements Library {

    private static final int MAX_NUMBER_OF_USER_PLAYLISTS = 20;
    private static final String DEFAULT_PLAYLIST_NAME = "Liked Content";

    private final Playlist[] playlists = new Playlist[MAX_NUMBER_OF_USER_PLAYLISTS];
    private final Playlist defaultPlaylist = new UserPlaylist(DEFAULT_PLAYLIST_NAME);
    private int currentSize = 0;

    @Override
    public void add(Playlist playlist) throws LibraryCapacityExceededException {
        if (currentSize == MAX_NUMBER_OF_USER_PLAYLISTS) {
            throw new LibraryCapacityExceededException();
        }

        addPlaylist(playlist);
    }

    private void addPlaylist(Playlist playlist) {
        playlists[currentSize] = playlist;
        currentSize++;
    }

    @Override
    public void remove(String name) throws PlaylistNotFoundException {
        if (name.equals(DEFAULT_PLAYLIST_NAME)) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < currentSize; i++) {
            if (playlists[i].getName().equals(name)) {
                removePlaylistAt(i);
                return;
            }
        }

        throw new PlaylistNotFoundException();
    }

    private void removePlaylistAt(int i) {
        currentSize--;
        playlists[i] = playlists[currentSize];
        playlists[currentSize] = null;
    }

    @Override
    public Playlist getLiked() {
        return defaultPlaylist;
    }

}
