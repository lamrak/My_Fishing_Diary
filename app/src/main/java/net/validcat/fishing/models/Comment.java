package net.validcat.fishing.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;
    public String userAvatarUrl;

    public Comment() {}

    public Comment(String uid, String author, String text, String userAvatarUrl) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.userAvatarUrl = userAvatarUrl;
    }

}
