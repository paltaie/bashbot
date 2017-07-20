package com.paltaie.bashbot.service;

import com.paltaie.bashbot.model.BashQuote;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static org.jsoup.parser.Parser.unescapeEntities;

@Service
public class BashQuoteService {
    private static final Logger LOG = LoggerFactory.getLogger(BashQuoteService.class);

    public Optional<BashQuote> getQuoteById(String quoteId) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://bash.org/?" + quoteId).get();
        } catch (IOException e) {
            LOG.error("Encountered an error while trying to retrieve a bash.org quote", e);
        }
        if (doc.getElementsByClass("qt").size() > 0) {
            String text = doc.getElementsByClass("qt").get(0).html().replaceAll("<br> ", "\n");
            int score = Integer.valueOf(doc.getElementsByAttribute("color").get(1).text());
            return Optional.of(new BashQuote(unescapeEntities(text, false), score));
        }
        return Optional.empty();
    }
}
