package com.mycompany.poepractice;

import java.util.Random;
import java.util.Scanner;

public class MessageApp {

    public static void start(Scanner input) {
        System.out.println("Welcome to QuickChat.");
        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");
            System.out.print("Enter option (1-4): ");
            String opt = input.nextLine();
            switch (opt) {
                case "1":
                    handleSendMessages(input);
                    break;
                case "2":
                    Message[] recent = Message.getSentMessagesArray();
                    if (recent.length == 0) {
                        System.out.println("No recently sent messages available.");
                    } else {
                        System.out.println("Recently sent messages:");
                        for (Message msg : recent) {
                            System.out.println("Recipient: " + msg.getRecipient() + " | Message: " + msg.getText());
                        }
                    }
                    break;
                case "3":
                    handleStoredMessages(input);
                    break;
                case "4":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void handleSendMessages(Scanner input) {
        System.out.print("How many messages would you like to enter? ");
        int num = 0;
        try {
            num = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, returning to menu.");
            return;
        }

        for (int i = 0; i < num; i++) {
            System.out.println("\n--- Message " + (i+1) + " of " + num + " ---");
            String recipient = null;
            while (true) {
                System.out.print("Enter recipient number: ");
                recipient = input.nextLine();
                if (Message.checkRecipientCell(recipient)) {
                    System.out.println("Cell phone number successfully captured.");
                    break;
                } else {
                    System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
                }
            }

            String messageText = null;
            while (true) {
                System.out.print("Enter message text: ");
                messageText = input.nextLine();
                String lengthCheck = Message.checkMessageLength(messageText);
                if ("Message ready to send.".equals(lengthCheck)) {
                    System.out.println("Message ready to send.");
                    break;
                } else {
                    System.out.println(lengthCheck);
                }
            }

            String id = generateRandomId();
            if (!Message.checkMessageID(id)) {
                id = id.substring(0, Math.min(10, id.length()));
            }
            Message m = new Message(id, recipient, messageText, i);

            System.out.println("Message Hash: " + m.getHash());
            System.out.println("Choose action: 1-Send, 2-Disregard (Press 0 to delete), 3-Store");
            System.out.print("Enter choice: ");
            String choice = input.nextLine();
            switch (choice) {
                case "1":
                    m.markAsSent();
                    System.out.println("Message successfully sent.");
                    displayFullMessage(m);
                    break;
                case "2":
                    Message.addDisregardedMessage(m);
                    System.out.println("Press 0 to delete the message");
                    break;
                case "3":
                    Message.storeMessage(m);
                    System.out.println("Message successfully stored");
                    break;
                default:
                    System.out.println("Invalid action. Message discarded.");
            }
        }
        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
    }

    private static void handleStoredMessages(Scanner input) {
        boolean running = true;
        while (running) {
            System.out.println("\nStored Messages Menu");
            System.out.println("1) Display sender and recipient for all stored messages");
            System.out.println("2) Display longest stored message");
            System.out.println("3) Search message by ID");
            System.out.println("4) Search all messages for a recipient");
            System.out.println("5) Delete a message by hash");
            System.out.println("6) Display report");
            System.out.println("7) Back to main menu");
            System.out.print("Enter option (1-7): ");
            String opt = input.nextLine();
            switch (opt) {
                case "1":
                    for (Message msg : Message.getStoredMessagesArray()) {
                        System.out.println("Sender: " + msg.getRecipient() + " -> Recipient: " + msg.getRecipient());
                    }
                    break;
                case "2":
                    System.out.println(Message.displayLongestStoredMessage());
                    break;
                case "3":
                    System.out.print("Enter message ID: ");
                    String id = input.nextLine();
                    System.out.println(Message.searchMessageID(id));
                    break;
                case "4":
                    System.out.print("Enter recipient: ");
                    String recipient = input.nextLine();
                    System.out.println(Message.searchMessagesByRecipient(recipient));
                    break;
                case "5":
                    System.out.print("Enter message hash: ");
                    String hash = input.nextLine();
                    System.out.println(Message.deleteMessageUsingHash(hash));
                    break;
                case "6":
                    System.out.println(Message.displayReport());
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void displayFullMessage(Message m) {
        System.out.println("Full message details:");
        System.out.println("Message ID: " + m.getId());
        System.out.println("Message Hash: " + m.getHash());
        System.out.println("Recipient: " + m.getRecipient());
        System.out.println("Message: " + m.getText());
    }

    private static String generateRandomId() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) sb.append(r.nextInt(10));
        return sb.toString();
    }
}
