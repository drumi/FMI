package bg.sofia.uni.fmi.mjt.twitch.validation;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;

import static bg.sofia.uni.fmi.mjt.twitch.utils.Utils.requireNonEmpty;
import static bg.sofia.uni.fmi.mjt.twitch.utils.Utils.requireNonNull;

public class TwitchValidator implements Validator {

    @Override
    public Validator validateUsername(String username) {
        requireNonNull(username, "username cannot be null");
        requireNonEmpty(username, "username cannot be empty");
        return this;
    }

    @Override
    public Validator validateTitle(String title) {
        requireNonNull(title, "title cannot be null");
        requireNonEmpty(title, "title cannot be empty");
        return this;
    }

    @Override
    public Validator validateCategory(Category category) {
        requireNonNull(category, "category cannot be null");
        return this;
    }

    @Override
    public Validator validateStream(Stream stream) {
        requireNonNull(stream, "stream cannot be null");
        return this;
    }

    @Override
    public Validator validateContent(Content content) {
        requireNonNull(content, "content cannot be null");
        return this;
    }
}
