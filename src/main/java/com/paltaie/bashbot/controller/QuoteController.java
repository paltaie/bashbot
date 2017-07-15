package com.paltaie.bashbot.controller;

import com.paltaie.bashbot.service.BashQuoteService;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;

@Controller
public class QuoteController {

    private static final String RESPONSE_TEMPLATE_GOOD = "Here's bash.org quote #%s (Score: `%s`)\n```%s```\nView on bash.org: http://bash.org/?%s";
    private static final String RESPONSE_TEMPLATE_BAD = "Sorry @%s, I couldn't find a bash.org quote with ID `%s`";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("+#;-#");

    private String slackToken;
    private BashQuoteService bashQuoteService;

    @Autowired
    public QuoteController(@Value("${slash.command.token}") String slackToken, BashQuoteService bashQuoteService) {
        this.slackToken = slackToken;
        this.bashQuoteService = bashQuoteService;
    }

    @PostMapping(value = "/slash-command",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public RichMessage onReceiveSlashCommand(@RequestParam("token") String token,
                                             @RequestParam("team_id") String teamId,
                                             @RequestParam("team_domain") String teamDomain,
                                             @RequestParam("channel_id") String channelId,
                                             @RequestParam("channel_name") String channelName,
                                             @RequestParam("user_id") String userId,
                                             @RequestParam("user_name") String userName,
                                             @RequestParam("command") String command,
                                             @RequestParam("text") String text,
                                             @RequestParam("response_url") String responseUrl) {
        if (!token.equals(slackToken)) {
            return new RichMessage("Sorry! You're not lucky enough to use our slack command.");
        }
        RichMessage richMessage = new RichMessage();
        richMessage.setResponseType("in_channel");
        richMessage.setText(String.format(RESPONSE_TEMPLATE_BAD, userName, text));
        bashQuoteService.getQuoteById(text).ifPresent(bashQuote ->
                richMessage.setText(String.format(RESPONSE_TEMPLATE_GOOD,
                        text,
                        DECIMAL_FORMAT.format(bashQuote.getScore()),
                        bashQuote.getText(),
                        text)
                )
        );
        return richMessage.encodedMessage();
    }
}
