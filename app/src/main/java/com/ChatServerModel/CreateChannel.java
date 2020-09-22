package com.ChatServerModel;

/**
 * Created by Mohit on 6/21/2018.
 */

public class CreateChannel {
    private String channel_id;
    private String name;
    private String custom_type;
    private String channel_url;
    private String created_at;

    public CreateChannel(String channel_id, String name, String custom_type, String channel_url, String created_at) {
        this.channel_id = channel_id;
        this.name = name;
        this.custom_type = custom_type;
        this.channel_url = channel_url;
        this.created_at = created_at;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustom_type() {
        return custom_type;
    }

    public void setCustom_type(String custom_type) {
        this.custom_type = custom_type;
    }

    public String getChannel_url() {
        return channel_url;
    }

    public void setChannel_url(String channel_url) {
        this.channel_url = channel_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
