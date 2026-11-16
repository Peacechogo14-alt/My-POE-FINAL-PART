package com.mycompany.poepractice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private String id;
    private String hash;
    private String recipient;
    private String text;
    private int number;
    private static int totalSent = 0;

    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIds = new ArrayList<>();

    public Message(String id, String recipient, String text, int number) {
        this(id, recipient, text, number, null);
    }

    public Message(String id, String recipient, String text, int number, String hash) {
        this.id = id;
        this.recipient = recipient;
        this.text = text;
        this.number = number;
        this.hash = (hash == null || hash.trim().isEmpty())
                ? createMessageHash(id, number, text)
                : hash;
    }

    public String getId() { return id; }
    public String getHash() { return hash; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }
    public int getNumber() { return number; }

    public static void clearCollections() {
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
        messageHashes.clear();
        messageIds.clear();
        totalSent = 0;
    }

    public static Message[] getSentMessagesArray() {
        return sentMessages.toArray(new Message[0]);
    }

    public static Message[] getStoredMessagesArray() {
        return storedMessages.toArray(new Message[0]);
    }

    public static Message[] getDisregardedMessagesArray() {
        return disregardedMessages.toArray(new Message[0]);
    }

    public static String[] getMessageHashArray() {
        return messageHashes.toArray(new String[0]);
    }

    public static String[] getMessageIdArray() {
        return messageIds.toArray(new String[0]);
    }

    public static Message[] getSentMessages() { return getSentMessagesArray(); }
    public static Message[] getStoredMessages() { return getStoredMessagesArray(); }
    public static Message[] getDisregardedMessages() { return getDisregardedMessagesArray(); }
    public static String[] getMessageHashes() { return getMessageHashArray(); }
    public static String[] getMessageIds() { return getMessageIdArray(); }

    public static void addSentMessage(Message message) {
        if (message == null) return;
        if (!sentMessages.contains(message)) {
            sentMessages.add(message);
        }
        trackMessageData(message);
    }

    public static void addStoredMessage(Message message) {
        if (message == null) return;
        if (!storedMessages.contains(message)) {
            storedMessages.add(message);
        }
        trackMessageData(message);
    }

    public static void addDisregardedMessage(Message message) {
        if (message == null) return;
        if (!disregardedMessages.contains(message)) {
            disregardedMessages.add(message);
        }
        trackMessageData(message);
    }

    private static void trackMessageData(Message message) {
        if (message == null) return;
        if (message.getId() != null && !messageIds.contains(message.getId())) {
            messageIds.add(message.getId());
        }
        if (message.getHash() != null && !messageHashes.contains(message.getHash())) {
            messageHashes.add(message.getHash());
        }
    }

    public static void populateTestData() {
        clearCollections();

        Message m1 = new Message("001", "+27817123163", "Did you get the cake?", 0, "M1");
        Message m2 = new Message("002", "+27817123163", "Where are you? You are late! I have asked you to be on time.", 1, "M2");
        Message m3 = new Message("003", "+27817123163", "Yohoooo, I am at your gate.", 2, "M3");
        Message m4 = new Message("004", "0817123163", "It is dinner time !", 3, "M4");
        Message m5 = new Message("005", "+27817123163", "Ok, I am leaving without you.", 4, "M5");

        addSentMessage(m1);
        addStoredMessage(m2);
        addDisregardedMessage(m3);
        addSentMessage(m4);
        addStoredMessage(m5);
    }

    public static boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    public static boolean checkRecipientCell(String r) {
        if (r == null) return false;
        r = r.trim();
        if (r.startsWith("+")) {
            String digits = r.substring(1);
            return digits.matches("\\d{9,12}");
        } else if (r.startsWith("0")) {
            return r.matches("0\\d{8,10}");
        }
        return false;
    }

    public static String createMessageHash(String id, int num, String text) {
        String firstTwo = id != null && id.length() >= 2 ? id.substring(0,2) : id;
        String count = String.valueOf(num);
        String cleaned = text == null ? "" : text.trim();
        String[] words = cleaned.split("\\s+");
        String first = "";
        String last = "";
        if (words.length > 0) first = words[0].replaceAll("[^a-zA-Z]", "");
        if (words.length > 0) last = words[words.length-1].replaceAll("[^a-zA-Z]", "");
        return (firstTwo + ":" + count + ":" + (first + last)).toUpperCase();
    }

    public static int returnTotalMessages() {
        return totalSent;
    }

    public void markAsSent() {
        totalSent++;
        addSentMessage(this);
    }

    public static String checkMessageLength(String message) {
        if (message == null) return "Please enter a message of less than 250 characters.";
        int len = message.length();
        if (len <= 250) return "Message ready to send.";
        int excess = len - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    public static Message findLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return null;
        }

        Message longest = storedMessages.get(0);
        for (Message message : storedMessages) {
            if (message.getText() != null && longest.getText() != null && message.getText().length() > longest.getText().length()) {
                longest = message;
            } else if (longest.getText() == null && message.getText() != null) {
                longest = message;
            }
        }
        return longest;
    }

    public static String displayLongestStoredMessage() {
        Message message = findLongestStoredMessage();
        if (message == null) {
            return "No stored messages found.";
        }
        return "Longest stored message: " + message.getText();
    }

    public static Message findMessageById(String id) {
        if (id == null) return null;
        String target = id.trim();
        for (Message message : storedMessages) {
            if (message.getId() != null && message.getId().equalsIgnoreCase(target)) {
                return message;
            }
        }
        for (Message message : sentMessages) {
            if (message.getId() != null && message.getId().equalsIgnoreCase(target)) {
                return message;
            }
        }
        for (Message message : disregardedMessages) {
            if (message.getId() != null && message.getId().equalsIgnoreCase(target)) {
                return message;
            }
        }
        return null;
    }

    public static String displayMessageById(String id) {
        Message message = findMessageById(id);
        if (message == null) {
            return "No message found for ID: " + id;
        }
        return "Message ID: " + message.getId() + "\nRecipient: " + message.getRecipient() + "\nMessage: " + message.getText();
    }

    public static List<Message> findMessagesForRecipient(String recipient) {
        List<Message> matches = new ArrayList<>();
        if (recipient == null) return matches;
        String target = recipient.trim();

        for (Message message : sentMessages) {
            if (message.getRecipient() != null && message.getRecipient().equalsIgnoreCase(target)) {
                matches.add(message);
            }
        }
        for (Message message : storedMessages) {
            if (message.getRecipient() != null && message.getRecipient().equalsIgnoreCase(target)) {
                matches.add(message);
            }
        }
        return matches;
    }

    public static String displayMessagesForRecipient(String recipient) {
        List<Message> matches = findMessagesForRecipient(recipient);
        if (matches.isEmpty()) {
            return "No messages found for recipient: " + recipient;
        }

        StringBuilder sb = new StringBuilder();
        for (Message message : matches) {
            sb.append(message.getText()).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    public static boolean deleteMessageByHash(String hash) {
        if (hash == null) return false;
        String target = hash.trim();
        for (int i = 0; i < storedMessages.size(); i++) {
            Message message = storedMessages.get(i);
            if (message.getHash() != null && message.getHash().equalsIgnoreCase(target)) {
                storedMessages.remove(i);
                if (messageHashes.contains(message.getHash())) {
                    messageHashes.remove(message.getHash());
                }
                if (messageIds.contains(message.getId())) {
                    messageIds.remove(message.getId());
                }
                return true;
            }
        }
        return false;
    }

    public static String displayStoredReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }

        StringBuilder report = new StringBuilder();
        report.append("Stored Messages Report").append(System.lineSeparator());
        for (Message message : storedMessages) {
            report.append("Message ID: ").append(message.getId()).append(System.lineSeparator());
            report.append("Message Hash: ").append(message.getHash()).append(System.lineSeparator());
            report.append("Recipient: ").append(message.getRecipient()).append(System.lineSeparator());
            report.append("Message: ").append(message.getText()).append(System.lineSeparator());
            report.append(System.lineSeparator());
        }
        return report.toString().trim();
    }

    public static String searchMessageID(String id) {
        return displayMessageById(id);
    }

    public static String searchMessagesByRecipient(String recipient) {
        return displayMessagesForRecipient(recipient);
    }

    public static String deleteMessageUsingHash(String hash) {
        boolean deleted = deleteMessageByHash(hash);
        return deleted ? "Message with hash " + hash + " was successfully deleted." : "Message with hash " + hash + " was not found.";
    }

    public static String displayReport() {
        return displayStoredReport();
    }

    public static void loadStoredMessagesFromJson(String filePath) {
        Path file = Paths.get(filePath);
        if (!Files.exists(file)) {
            return;
        }

        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            Pattern pattern = Pattern.compile("\\{\\s*\\\"messageId\\\"\\s*:\\s*\\\"(.*?)\\\"\\s*,\\s*\\\"messageHash\\\"\\s*:\\s*\\\"(.*?)\\\"\\s*,\\s*\\\"recipient\\\"\\s*:\\s*\\\"(.*?)\\\"\\s*,\\s*\\\"message\\\"\\s*:\\s*\\\"(.*?)\\\"\\s*\\}", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String id = unescapeJson(matcher.group(1));
                String hash = unescapeJson(matcher.group(2));
                String recipient = unescapeJson(matcher.group(3));
                String messageText = unescapeJson(matcher.group(4));
                Message storedMessage = new Message(id, recipient, messageText, 0, hash);
                addStoredMessage(storedMessage);
            }
        } catch (IOException ignored) {
            // Ignore file read issues.
        }
    }

    public static synchronized void storeMessage(Message m) {
        if (m != null) {
            addStoredMessage(m);
        }

        Path file = Paths.get("messages.json");
        String obj = String.format("{\n  \"messageId\": \"%s\",\n  \"messageHash\": \"%s\",\n  \"recipient\": \"%s\",\n  \"message\": \"%s\"\n}",
                escape(m == null ? "" : m.getId()), escape(m == null ? "" : m.getHash()),
                escape(m == null ? "" : m.getRecipient()), escape(m == null ? "" : m.getText()));
        try {
            if (!Files.exists(file)) {
                String arr = "[\n" + obj + "\n]";
                Files.write(file, arr.getBytes(StandardCharsets.UTF_8));
            } else {
                List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                String content = String.join("\n", lines).trim();
                if (content.endsWith("]")) {
                    content = content.substring(0, content.length() - 1).trim();
                    content = content + ",\n" + obj + "\n]";
                } else {
                    content = "[\n" + obj + "\n]";
                }
                Files.write(file, content.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
            // Ignore file write issues.
        }
    }

    private static String unescapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
