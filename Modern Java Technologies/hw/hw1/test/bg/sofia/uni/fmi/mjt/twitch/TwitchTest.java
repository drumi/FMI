package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.*;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwitchTest {

    StreamingPlatform platform;

    @Test
    void testStartStream_whenArgumentsAreNullOrEmpty_thenThrowsIllegalArgumentException(@Mock UserService service) {
        platform = new Twitch(service);

        assertThrows(IllegalArgumentException.class, () -> platform.startStream(null, "title", Category.GAMES));
        assertThrows(IllegalArgumentException.class, () -> platform.startStream("Ivan", null, Category.GAMES));
        assertThrows(IllegalArgumentException.class, () -> platform.startStream("Ivan", "title", null));

        assertThrows(IllegalArgumentException.class, () -> platform.startStream("", "title", Category.GAMES));
        assertThrows(IllegalArgumentException.class, () -> platform.startStream("Ivan", "", null));
    }

    @Test
    void testStartStream_whenUserIsNotFoundInService_thenThrowsUserNotFoundException(@Mock UserService service) {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan"),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
                ));

        platform = new Twitch(service);

        assertThrows(UserNotFoundException.class, () -> platform.startStream("Parker", "title", Category.GAMES));
    }

    @Test
    void testStartStream_whenUserIsAlreadyStreaming_thenThrowsUserStreamingException(@Mock UserService service) {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        assertThrows(UserStreamingException.class, () -> platform.startStream("Ivan", "title", Category.GAMES));
    }

    @Test
    void testStartStream_whenUserIsOffline_thenStartsStreaming(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        User streamer = new BasicUser("Joe");
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", streamer
            ));

        platform = new Twitch(service);

        platform.startStream("Joe", "title", Category.GAMES);

        assertEquals(streamer.getStatus(), UserStatus.STREAMING);
    }

    @Test
    void testEndStream_doesNotFail(@Mock UserService service) throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);

        platform.endStream("Joe", stream);
    }

    @Test
    void testEndStream_throwsWhenNotStreaming(@Mock UserService service) throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);

        assertThrows(UserStreamingException.class, ()-> platform.endStream("Hans", stream));
    }

    @Test
    void testWatch(@Mock UserService service) throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);


        assertEquals(5, v.getNumberOfViews());
    }

    @Test
    void testWatch_whenStreaming_thenException(@Mock UserService service) throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);

        assertThrows(UserStreamingException.class, () -> platform.watch("Joe", stream));
    }

    @Test
    void getMostWatchedStreamer_whenNone_thenNull(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);

        platform.endStream("Joe", stream);

        assertNull(platform.getMostWatchedStreamer());
    }

    @Test
    void getMostWatchedStreamer_normal(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Hans", v2);

        assertEquals(platform.getMostWatchedStreamer().getName(), "Joe");
    }

    @Test
    void getMostWatchedContent_whenNone_thenNull(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        platform.endStream("Joe", stream);
        platform.endStream("Scot", stream2);

        assertNull(platform.getMostWatchedContent());
    }

    @Test
    void getMostWatchedContent_whenNothing_thenNull(@Mock UserService service) {
        platform = new Twitch(service);
        assertNull(platform.getMostWatchedContent());
    }

    @Test
    void getMostWatchedContent_normal3(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        Stream stream3 = platform.startStream("Scot", "title", Category.GAMES);
        Video v3 = platform.endStream("Scot", stream3);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Petar", v2);
        platform.watch("Petar", v3);
        platform.watch("Petar", v3);
        platform.watch("Petar", v3);
        platform.watch("Petar", v3);
        platform.watch("Petar", v3);

        assertEquals(platform.getMostWatchedContent(), v3);
    }

    @Test
    void getMostWatchedContent_normalButStream(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        Stream stream3 = platform.startStream("Scot", "title", Category.GAMES);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Petar", v2);
        platform.watch("Petar", stream3);
        platform.watch("Petar", stream3);
        platform.watch("Petar", stream3);
        platform.watch("Petar", stream3);
        platform.watch("Petar", stream3);

        assertEquals(platform.getMostWatchedContent(), stream3);
    }

    @Test
    void getMostWatchedContent_normalButStreamEnded(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        Stream stream3 = platform.startStream("Scot", "title", Category.GAMES);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Petar", v2);
        platform.watch("Petar", stream3);
        platform.watch("Petar", stream3);
        platform.watch("Petar", stream3);
        platform.watch("Petar", stream3);
        platform.watch("Petar", stream3);

        platform.endStream("Scot", stream3);

        assertEquals(platform.getMostWatchedContent(), v);
    }

    @Test
    void getMostWatchedContent_normal2(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Petar", v2);
        platform.watch("Hans", v2);

        assertEquals(platform.getMostWatchedContent(), v2);
    }

    @Test
    void getMostWatchedContent_normal(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Hans", v2);

        assertEquals(platform.getMostWatchedContent(), v);
    }

    @Test
    void getMostWatchedContentFrom_whenNone_thenNull(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Hans", v2);

        assertNull(platform.getMostWatchedContentFrom("Petar"));
    }

    @Test
    void getMostWatchedContentFrom_normal(@Mock UserService service)
        throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.GAMES);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        Stream stream3 = platform.startStream("Scot", "title", Category.GAMES);
        Video v3 = platform.endStream("Scot", stream3);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Petar", v2);
        platform.watch("Petar", v3);
        platform.watch("Petar", v3);
        platform.watch("Petar", v3);
        platform.watch("Petar", v3);
        platform.watch("Petar", v3);

        assertEquals(platform.getMostWatchedContent(), v3);

        assertEquals(platform.getMostWatchedContentFrom("Scot"), v3);
    }

    @Test
    void getMostWatchedCategoriesBy_normal(@Mock UserService service) throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.MUSIC);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Hans", v2);

        List<Category> ls = platform.getMostWatchedCategoriesBy("Hans");
        assertEquals(ls.get(0), Category.GAMES);
        assertEquals(ls.get(1), Category.MUSIC);
        assertEquals(ls.size(), 2);
    }

    @Test
    void getMostWatchedCategoriesBy_normal2(@Mock UserService service) throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.MUSIC);

        Video v = platform.endStream("Joe", stream);
        Video v2 = platform.endStream("Scot", stream2);

        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v);
        platform.watch("Hans", v2);
        platform.watch("Hans", v2);

        List<Category> ls = platform.getMostWatchedCategoriesBy("Hans");
        assertEquals(ls.get(0), Category.GAMES);
        assertEquals(ls.get(1), Category.MUSIC);
        assertEquals(ls.size(), 2);
    }

    @Test
    void getMostWatchedCategoriesBy_whenNone_thenEmpty(@Mock UserService service) throws UserNotFoundException, UserStreamingException {
        when(service.getUsers())
            .thenReturn(Map.of(
                "Ivan", new BasicUser("Ivan", UserStatus.STREAMING),
                "Petar", new BasicUser("Petar"),
                "Scot", new BasicUser("Scot"),
                "Hans", new BasicUser("Hans"),
                "Joe", new BasicUser("Joe")
            ));

        platform = new Twitch(service);

        Stream stream = platform.startStream("Joe", "title", Category.GAMES);
        Stream stream2 = platform.startStream("Scot", "title", Category.MUSIC);

        platform.endStream("Joe", stream);
        platform.endStream("Scot", stream2);

        List<Category> ls = platform.getMostWatchedCategoriesBy("Hans");
        assertEquals(ls.size(), 0);
    }
}
