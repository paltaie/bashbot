package com.paltaie.bashbot.service;

import com.paltaie.bashbot.model.Action;
import com.paltaie.bashbot.model.AttachmentWithActions;
import me.ramswaroop.jbot.core.slack.models.Attachment;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class MessageMakerService {

    private static final String RESPONSE_TEMPLATE_GOOD = "@%s has shared bash.org quote #%s (Score: `%s`)\n```%s```\nView on bash.org: http://bash.org/?%s";
    private static final String RESPONSE_TEMPLATE_BAD = "Sorry @%s, I couldn't find a bash.org quote with ID `%s`";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("+#;-#");

    private BashQuoteService bashQuoteService;

    @Autowired
    public MessageMakerService(BashQuoteService bashQuoteService) {
        this.bashQuoteService = bashQuoteService;
    }

    public RichMessage createRichMessage(boolean ephemeral, String username, String quoteId) {
        RichMessage richMessage = new RichMessage();
        if (!ephemeral) {
            richMessage.setResponseType("in_channel");
        }
        richMessage.setText(String.format(RESPONSE_TEMPLATE_BAD, username, quoteId));
        bashQuoteService.getQuoteById(quoteId).ifPresent(bashQuote -> {
            richMessage.setAttachments(buildConfirmAttachment(quoteId));
            richMessage.setText(String.format(RESPONSE_TEMPLATE_GOOD,
                    username,
                    quoteId,
                    DECIMAL_FORMAT.format(bashQuote.getScore()),
                    bashQuote.getText(),
                    quoteId)
            );
        });
        return richMessage;
    }

    private Attachment[] buildConfirmAttachment(String quoteId) {
        AttachmentWithActions attachmentWithActions = new AttachmentWithActions();
        attachmentWithActions.setText("Want to publish this quote here?");
        attachmentWithActions.setFallback("Sorry, your client is too old to post bash.org quotes!");
        attachmentWithActions.setCallbackId(quoteId);
        attachmentWithActions.setActions(new Action[] {
                Action.builder().type("button").name("option").text("Post to channel").value("post").style("primary").build(),
                Action.builder().type("button").name("option").text("Cancel").value("cancel").build()
        });
        return new Attachment[] {attachmentWithActions};
    }
}
