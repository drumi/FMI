package bg.sofia.uni.fmi.mjt.twitch.user;

public class BasicUser implements User {

    private String name;
    private UserStatus status;

    public BasicUser(String name) {
        this.name = name;
        status = UserStatus.OFFLINE;
    }

    public BasicUser(String name, UserStatus status) {
        this.name = name;
        this.status = status;
    }
    /**
     * Returns the name of the user.
     *
     * @return the name of the user
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the {@link UserStatus} of the user.
     *
     * @return the {@link UserStatus} of the user
     */
    @Override
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Sets the {@link UserStatus} of the user.
     *
     * @param status the new {@link UserStatus} to set
     */
    @Override
    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
