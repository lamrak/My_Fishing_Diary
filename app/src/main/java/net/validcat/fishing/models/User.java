package net.validcat.fishing.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    public String userAvatarUrl;

    public User() {}

    public User(String username, String email, String userAvatarUrl) {
        this.username = username;
        this.email = email;
        this.userAvatarUrl = userAvatarUrl;

    }

}
