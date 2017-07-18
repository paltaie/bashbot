package com.paltaie.bashbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class InteractiveRequest {
    private List<Action> actions;
    @JsonProperty("callback_id")
    private String callbackId;
    private Team team;
    private IdNameType channel;
    private IdNameType user;
    @JsonProperty("action_ts")
    private String actionTimestamp;
    @JsonProperty("message_ts")
    private String messageTimestamp;
    @JsonProperty("attachment_id")
    private String attachmentId;
    private String token;
    @JsonProperty("is_app_unfurl")
    private boolean isAppUnfurl;
    @JsonProperty("response_url")
    private String responseUrl;
}
