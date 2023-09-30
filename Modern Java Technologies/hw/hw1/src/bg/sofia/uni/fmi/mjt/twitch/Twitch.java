package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.BasicStream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.BasicVideo;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import bg.sofia.uni.fmi.mjt.twitch.validation.TwitchValidator;
import bg.sofia.uni.fmi.mjt.twitch.validation.Validator;

import java.time.LocalDateTime;
import java.util.*;

public class Twitch implements StreamingPlatform {

    private final UserService userService;
    private final Map<User, Collection<Content>> userToProducedContent = new HashMap<>();
    private final Map<User, Map<Category, Integer>> userToWatchedCategories = new HashMap<>();
    private final Validator validator = new TwitchValidator();

    public Twitch(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Stream startStream(String username, String title, Category category)
        throws UserNotFoundException, UserStreamingException {
        validator.validateUsername(username)
                 .validateTitle(title)
                 .validateCategory(category);

        User user = getUserIfPresent(username);

        ensureUserExists(user);
        ensureUserCanStream(user);

        user.setStatus(UserStatus.STREAMING);
        Stream stream = createStream(new Metadata(title, category, user), LocalDateTime.now());
        addContent(user, stream);

        return stream;
    }

    private User getUserIfPresent(String username) {
        Map<String, User> usernameToUser = userService.getUsers();
        return usernameToUser.get(username);
    }

    private void ensureUserExists(User user) throws UserNotFoundException {
        if (user == null) {
            throw new UserNotFoundException("Cannot find user in the service");
        }
    }

    private void ensureUserCanStream(User user) throws UserStreamingException {
        if (user.getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException("User is already streaming");
        }
    }

    private Stream createStream(Metadata metadata, LocalDateTime createdAt) {
        return new BasicStream(metadata, createdAt);
    }

    private void addContent(User user, Content content) {
        Collection<Content> contents = userToProducedContent.getOrDefault(user, createEmptyCollection());
        contents.add(content);
        userToProducedContent.put(user, contents);
    }

    private void removeContent(User user, Content content) {
        Collection<Content> contents = userToProducedContent.get(user);
        contents.remove(content);
    }

    private <T> Collection<T> createEmptyCollection() {
        return new ArrayList<>();
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        validator.validateUsername(username)
                 .validateStream(stream);

        User user = getUserIfPresent(username);

        ensureUserExists(user);
        ensureUserCanStopStreaming(user);

        user.setStatus(UserStatus.OFFLINE);
        Video video = createVideoFromStream(stream);
        removeContent(user, stream);
        addContent(user, video);

        return video;
    }

    private void ensureUserCanStopStreaming(User user) throws UserStreamingException {
        if (user.getStatus() != UserStatus.STREAMING) {
            throw new UserStreamingException("User is not streaming");
        }
    }

    private Video createVideoFromStream(Stream stream) {
        return new BasicVideo(stream.getMetadata(), stream.getDuration());
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        validator.validateUsername(username)
                 .validateContent(content);

        User user = getUserIfPresent(username);

        ensureUserExists(user);
        ensureUserCanWatchContent(user);

        content.startWatching(user);
        increaseCategoryWatches(user, content.getMetadata().category());
    }

    private void increaseCategoryWatches(User user, Category category) {
        Map<Category, Integer> categoriesToWatchCount =
            userToWatchedCategories.getOrDefault(user, new EnumMap<>(Category.class));
        Integer timesWatched = categoriesToWatchCount.getOrDefault(category, 0);
        categoriesToWatchCount.put(category, timesWatched + 1);
        userToWatchedCategories.put(user, categoriesToWatchCount);
    }

    private void ensureUserCanWatchContent(User user) throws UserStreamingException {
        if (user.getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException("User is currently streaming");
        }
    }

    @Override
    public User getMostWatchedStreamer() {
        User mostWatchedUser = null;
        int mostWatchedUserViews = 0;
        Collection<User> users = userService.getUsers().values();

        for (User u : users) {
            int currentViews = getCurrentNumberOfViews(u);

            if (currentViews > mostWatchedUserViews) {
                mostWatchedUser = u;
                mostWatchedUserViews = currentViews;
            }
        }

        return mostWatchedUser;
    }

    private int getCurrentNumberOfViews(User user) {
        Collection<Content> producedContent = userToProducedContent.get(user);

        if (producedContent == null) {
            return 0;
        }

        int numberOfViews = 0;
        for (Content c : producedContent) {
            numberOfViews += c.getNumberOfViews();
        }

        return numberOfViews;
    }

    @Override
    public Content getMostWatchedContent() {
        Content mostWatchedContent = null;
        int mostWatchedContentViews = 0;

        for (Collection<Content> contents : userToProducedContent.values()) {
            for (Content c : contents) {
                if (c.getNumberOfViews() > mostWatchedContentViews) {
                    mostWatchedContent = c;
                    mostWatchedContentViews = c.getNumberOfViews();
                }
            }
        }

        return mostWatchedContent;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        validator.validateUsername(username);

        User user = userService.getUsers().get(username);

        ensureUserExists(user);

        Content mostWatchedContent = null;
        int mostWatchedContentViews = 0;

        Collection<Content> contents = userToProducedContent.getOrDefault(user, Collections.emptyList());

        for (Content c : contents) {
            if (c.getNumberOfViews() > mostWatchedContentViews) {
                mostWatchedContent = c;
                mostWatchedContentViews = c.getNumberOfViews();
            }
        }

        return mostWatchedContent;
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        validator.validateUsername(username);

        User user = userService.getUsers().get(username);

        ensureUserExists(user);

        Map<Category, Integer> categoryToViewCount = userToWatchedCategories.getOrDefault(user, Collections.emptyMap());

        SortedMap<Integer, Category> sortedMap = new TreeMap<>(new Comparator<>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return i2 - i1;
            }
        });

        for (var pair : categoryToViewCount.entrySet()) {
            sortedMap.put(pair.getValue(), pair.getKey());
        }

        return List.copyOf(sortedMap.values());
    }
}
