package com.mycompany.poepractice;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        Message.populateTestData();
        Message[] sent = Message.getSentMessagesArray();
        assertEquals(2, sent.length);
        assertEquals("+27817123163", sent[0].getRecipient());
        assertEquals("Did you get the cake?", sent[0].getText());
        assertEquals("0817123163", sent[1].getRecipient());
        assertEquals("It is dinner time !", sent[1].getText());
    }

    @Test
    public void testDisplayLongestStoredMessage() {
        Message.populateTestData();
        Message longest = Message.findLongestStoredMessage();
        assertNotNull(longest);
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest.getText());
        assertEquals("Longest stored message: Where are you? You are late! I have asked you to be on time.", Message.displayLongestStoredMessage());
    }

    @Test
    public void testSearchByMessageID() {
        Message.populateTestData();
        Message found = Message.findMessageById("002");
        assertNotNull(found);
        assertEquals("+27817123163", found.getRecipient());
        assertEquals("Where are you? You are late! I have asked you to be on time.", found.getText());
    }

    @Test
    public void testSearchAllMessagesForRecipient() {
        Message.populateTestData();
        List<Message> matches = Message.findMessagesForRecipient("+27817123163");
        assertEquals(3, matches.size());
        assertTrue(matches.stream().anyMatch(m -> m.getText().equals("Where are you? You are late! I have asked you to be on time.")));
        assertTrue(matches.stream().anyMatch(m -> m.getText().equals("Ok, I am leaving without you.")));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        Message.populateTestData();
        assertTrue(Message.deleteMessageByHash("M2"));
        assertNull(Message.findMessageById("002"));
        assertEquals(1, Message.getStoredMessagesArray().length);
    }

    @Test
    public void testDisplayReport() {
        Message.populateTestData();
        String report = Message.displayReport();
        assertTrue(report.contains("Stored Messages Report"));
        assertTrue(report.contains("Message ID: 002"));
        assertTrue(report.contains("Message Hash: M2"));
        assertTrue(report.contains("Recipient: +27817123163"));
        assertTrue(report.contains("Message: Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testMessageLengthSuccess() {
        String msg = "Hello there";
        String res = Message.checkMessageLength(msg);
        assertEquals("Message ready to send.", res);
    }

    @Test
    public void testMessageLengthFailure() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 260; i++) sb.append('a');
        String res = Message.checkMessageLength(sb.toString());
        assertTrue(res.startsWith("Message exceeds 250 characters by"));
    }

    @Test
    public void testRecipientFormatSuccess() {
        assertTrue(Message.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testLocalRecipientFormatSuccess() {
        assertTrue(Message.checkRecipientCell("0821234567"));
    }

    @Test
    public void testRecipientFormatFailure() {
        assertFalse(Message.checkRecipientCell("12345abc"));
    }

    @Test
    public void testHashCreation() {
        String id = "0012345678";
        int num = 0;
        String message = "Hi Mike, can you join us for dinner tonight?";
        String hash = Message.createMessageHash(id, num, message);
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testHashCreationWhitespaceText() {
        String hash = Message.createMessageHash("AB", 3, "   ");
        assertEquals("AB:3:", hash);
    }

    @Test
    public void testMessageIDSuccess() {
        assertTrue(Message.checkMessageID("1234567890"));
    }

    @Test
    public void testMessageIDFailure() {
        assertFalse(Message.checkMessageID("12345678901"));
    }

    @Test
    public void testHashCreationWithNullText() {
        String hash = Message.createMessageHash("A", 5, null);
        assertEquals("A:5:", hash);
    }

    @Test
    public void testHashCreationPunctuation() {
        String hash = Message.createMessageHash("XY", 2, "Hello, world!!!");
        assertEquals("XY:2:HELLOWORLD", hash);
    }

    @Test
    public void testMarkAsSentIncrementsTotal() {
        Message.clearCollections();
        int before = Message.returnTotalMessages();
        Message m = new Message("01", "+27710000000", "hi", 0);
        m.markAsSent();
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    @Test
    public void testMessageLengthNull() {
        String res = Message.checkMessageLength(null);
        assertEquals("Please enter a message of less than 250 characters.", res);
    }
}
