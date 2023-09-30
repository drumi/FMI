package bg.sofia.uni.fmi.mjt.twitch.validation;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;

public interface Validator {

    /**
     * Validates the username
     *
     * @param username the username to validate
     * @return this instance
     * @throws IllegalArgumentException if {@code username} is null or empty
     */
    Validator validateUsername(String username);

    /**
     * Validates the title
     *
     * @param title the title to validate
     * @return this instance
     * @throws IllegalArgumentException if {@code title} is null or empty
     */
    Validator validateTitle(String title);

    /**
     * Validates the category
     *
     * @param category the category to validate
     * @return this instance
     * @throws IllegalArgumentException if {@code category} is null
     */
    Validator validateCategory(Category category);

    /**
     * Validates the stream
     *
     * @param stream the stream to validate
     * @return this instance
     * @throws IllegalArgumentException if {@code stream} is null
     */
    Validator validateStream(Stream stream);

    /**
     * Validates the content
     *
     * @param content the content to validate
     * @return this instance
     * @throws IllegalArgumentException if {@code content} is null
     */
    Validator validateContent(Content content);
}
