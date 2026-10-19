package com.mycompany.poepractice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    // test that a short message is accepted
    @Test
    public void testMessageLengthSuccess() {
        String msg = "Hello there";
        String res = Message.checkMessageLength(msg);
        assertEquals("Message ready to send.", res);
    }

    // test that a too-long message gives an error
    @Test
    public void testMessageLengthFailure() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 260; i++) sb.append('a');
        String res = Message.checkMessageLength(sb.toString());
        assertTrue(res.startsWith("Message exceeds 250 characters by"));
    }

    // test a valid international number
    @Test
    public void testRecipientFormatSuccess() {
        assertTrue(Message.checkRecipientCell("+27718693002"));
    }

    // test an invalid number
    @Test
    public void testRecipientFormatFailure() {
        assertFalse(Message.checkRecipientCell("12345abc"));
    }

    // test hash creation with real example from task
    @Test
    public void testHashCreation() {
        String id = "0012345678";
        int num = 0;
        String message = "Hi Mike, can you join us for dinner tonight?";
        String hash = Message.createMessageHash(id, num, message);
        assertEquals("00:0:HITONIGHT", hash);
    }

    // test that a valid message ID (<= 10 chars) passes
    @Test
    public void testMessageIDSuccess() {
        assertTrue(Message.checkMessageID("1234567890"));
    }

    // test that an invalid message ID (> 10 chars) fails
    @Test
    public void testMessageIDFailure() {
        assertFalse(Message.checkMessageID("12345678901"));
    }

    // test hash creation when text is null or empty
    @Test
    public void testHashCreationWithNullText() {
        String hash = Message.createMessageHash("A", 5, null);
        assertEquals("A:5:", hash);
    }

    // test hash creation removes punctuation and concatenates first+last words
    @Test
    public void testHashCreationPunctuation() {
        String hash = Message.createMessageHash("XY", 2, "Hello, world!!!");
        assertEquals("XY:2:HELLOWORLD", hash);
    }

    // test marking a message as sent increments the total counter
    @Test
    public void testMarkAsSentIncrementsTotal() {
        int before = Message.returnTotalMessages();
        Message m = new Message("01", "+27710000000", "hi", 0);
        m.markAsSent();
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    // test null message length handling
    @Test
    public void testMessageLengthNull() {
        String res = Message.checkMessageLength(null);
        assertEquals("Please enter a message of less than 250 characters.", res);
    }
}
