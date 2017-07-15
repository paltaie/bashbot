package com.paltaie.bashbot.service;

import com.paltaie.bashbot.model.BashQuote;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;

import static org.junit.Assert.*;

public class BashQuoteServiceTest {

    private BashQuoteService bashQuoteService = new BashQuoteService();

    @Test
    public void shouldGetQuoteForId() throws IOException {
        Optional<BashQuote> bashQuoteOptional = bashQuoteService.getQuoteById("400");
        assertTrue(bashQuoteOptional.isPresent());
        BashQuote bashQuote = bashQuoteOptional.get();
        assertEquals("<MadHatter> I cut my tongue shaving", bashQuote.getText());
        assertEquals(974, bashQuote.getScore().intValue());
    }

    @Test
    public void shouldGetEmptyOptionalForNonexistentQuote() throws IOException {
        Optional<BashQuote> bashQuoteOptional = bashQuoteService.getQuoteById("aaaaa");
        assertFalse(bashQuoteOptional.isPresent());
    }
}
