package com.paltaie.bashbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.ramswaroop.jbot.core.slack.models.Attachment;

@Data
public class AttachmentWithActions extends Attachment {
    private Action[] actions;
    @JsonProperty("callback_id")
    private String callbackId;
}
