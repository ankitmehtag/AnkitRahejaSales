package com.ChatServerModel;

/**
 * Created by Mohit on 6/21/2018.
 */

public class RegisterNewUser {
    private String user_id;
    private String nickname;
    private String profile_url;
    private String is_active;
    private String last_seen_at;

    public RegisterNewUser(String user_id, String nickname, String profile_url, String is_active, String last_seen_at) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.profile_url = profile_url;
        this.is_active = is_active;
        this.last_seen_at = last_seen_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getLast_seen_at() {
        return last_seen_at;
    }

    public void setLast_seen_at(String last_seen_at) {
        this.last_seen_at = last_seen_at;
    }
}
