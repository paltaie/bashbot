package com.paltaie.bashbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paltaie.bashbot.model.InteractiveRequest;
import com.paltaie.bashbot.service.MessageMakerService;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Controller
public class QuoteController {

    private MessageMakerService messageMakerService;
    private String slackToken;
    private String slackOauthToken;
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public QuoteController(@Value("${slash.command.token}") String slackToken,
                           @Value("${slack.oauth.token}") String slackOauthToken,
                           MessageMakerService messageMakerService) {
        this.messageMakerService = messageMakerService;
        this.slackOauthToken = slackOauthToken;
        this.slackToken = slackToken;
    }

    @PostMapping("/interactive")
    public ResponseEntity<String> interactive(@RequestParam("payload") String payload) throws IOException {
        InteractiveRequest interactiveRequest = objectMapper.readValue(payload, InteractiveRequest.class);
        String result = "Posted!";
        String actionValue = interactiveRequest.getActions().get(0).getValue();

        if ("post".equals(actionValue)) {
            MultiValueMap<String, String> multiValueMap = getParamMapForPost(interactiveRequest.getChannel().getId(),
                    interactiveRequest.getUser().getName(),
                    interactiveRequest.getCallbackId());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(multiValueMap, getHeaders());
            restTemplate.postForEntity("https://slack.com/api/chat.postMessage", request, String.class);
        } else {
            result = "Post cancelled!";
        }

        restTemplate.postForEntity(interactiveRequest.getResponseUrl(),
                "{\"text\": \"" + result + "\"}",
                String.class);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }

    @PostMapping(value = "/slash-command",
            consumes = APPLICATION_FORM_URLENCODED_VALUE)
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
        RichMessage richMessage = messageMakerService.createRichMessage(true, userName, text);
        return richMessage.encodedMessage();
    }

    private MultiValueMap<String, String> getParamMapForPost(String channelId, String username, String quoteId) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", slackOauthToken);
        map.add("channel", channelId);
        map.set("text", messageMakerService.createRichMessage(false, username, quoteId).encodedMessage().getText());
        return map;
    }
}
