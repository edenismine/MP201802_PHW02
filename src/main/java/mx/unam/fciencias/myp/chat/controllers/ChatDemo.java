package mx.unam.fciencias.myp.chat.controllers;

import mx.unam.fciencias.myp.chat.models.AbstractChatUser;
import mx.unam.fciencias.myp.chat.services.MultilingualChat;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Main controller.
 *
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class ChatDemo {
    /**
     * Runs the {@link MultilingualChat} service and demos users going online, conversing and leaving the chat.
     * It attempts to write three files that make-do for the user's individual displays.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Get chat
        MultilingualChat chat = MultilingualChat.getInstance();

        // Get user output devices
        String[] usernames = {"Juan", "Raul", "John"};
        PrintStream[] userOutput = new PrintStream[usernames.length];
        for (int i = 0; i < usernames.length; i++) {
            File outputFile = new File(String.format("%s.txt", usernames[i]));
            try {
                userOutput[i] = new PrintStream(outputFile);
            } catch (IOException e) {
                userOutput[i] = System.out;
            }
        }

        // Create users
        AbstractChatUser juan = new MultilingualChat.Mexican(usernames[0], userOutput[0]);
        AbstractChatUser raul = new MultilingualChat.Spanish(usernames[1], userOutput[1]);
        AbstractChatUser john = new MultilingualChat.British(usernames[2], userOutput[2]);

        // Users join
        chat.register(juan);
        chat.register(raul);
        chat.register(john);

        // Users send a message
        juan.sendMessage(chat, "Hola, raza.");
        raul.sendMessage(chat, "Hola, tíos.");
        john.sendMessage(chat, "Heyy, guys.");

        // Users leave the chat
        chat.unregister(juan);
        chat.unregister(raul);
        chat.unregister(john);

        // Print chat events
        System.out.println("Chat history: ");
        chat.getLogs().forEach(System.out::println);
        System.out.println();

        // Print chat history for each user:
        AbstractChatUser[] users = {juan, raul, john};
        for (AbstractChatUser user : users) {
            System.out.println(user.getUsername() + " saw:");
            user.getChatHistory().forEach(System.out::println);
            System.out.println();
        }

    }
}
