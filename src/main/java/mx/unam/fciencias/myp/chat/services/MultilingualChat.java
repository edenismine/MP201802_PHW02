package mx.unam.fciencias.myp.chat.services;

import mx.unam.fciencias.myp.chat.models.AbstractChatUser;
import mx.unam.fciencias.myp.chat.models.Chat;
import mx.unam.fciencias.myp.chat.models.ChatNotification;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Multilingual chat service.
 *
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class MultilingualChat implements Chat {
    /**
     * Single instance.
     */
    private static MultilingualChat instance = null;
    /**
     * Chat users.
     */
    private ArrayList<AbstractChatUser> users = new ArrayList<>(50);
    /**
     * Chat logs.
     */
    private ArrayList<String> logs = new ArrayList<>(50);

    /**
     * Private constructor.
     */
    private MultilingualChat() {
    }

    /**
     * Retrieves the single instance of {@link MultilingualChat}.
     *
     * @return the single instance of {@link MultilingualChat}.
     */
    public static MultilingualChat getInstance() {
        if (instance == null) {
            synchronized (MultilingualChat.class) {
                if (instance == null) {
                    instance = new MultilingualChat();
                }
            }
        }
        return instance;
    }

    @Override
    public void register(AbstractChatUser user) {
        users.add(user);
        updateUsers(new ChatNotification(
                user, String.format("%s joined", user.getUsername()),
                ChatNotification.NEW_USER
        ));
    }

    @Override
    public void unregister(AbstractChatUser user) {
        if (users.remove(user)) {
            ChatNotification notification = new ChatNotification(
                    user, String.format("%s left", user.getUsername()),
                    ChatNotification.USER_LEFT);
            // since user is no longer in the observer list, we have to update it manually.
            user.update(notification);
            // we can then update everyone else as usual.
            updateUsers(notification);
        }
    }

    @Override
    public void updateUsers(ChatNotification notification) {
        if (notification.getType() == ChatNotification.NEW_MESSAGE && !isOnline(notification.getOrigin())) {
            logs.add("Unregistered user "
                    + notification.getOrigin().getUsername()
                    + " tried to broadcast a message.");
            return;
        }
        logs.add(notification.toString());
        users.forEach(it -> it.update(notification));
    }

    @Override
    public boolean isOnline(AbstractChatUser user) {
        return users.contains(user);
    }

    /**
     * Retrieves a copy of the chat's logs.
     *
     * @return a copy of the chat's logs.
     */
    public ArrayList<String> getLogs() {
        return new ArrayList<>(logs);
    }

    /**
     * Mexican chat user. Extends abstract template. All chat notifications are displayed in Spanish (Mexico).
     */
    public static class Mexican extends AbstractChatUser {

        public Mexican(String username, PrintStream screen) {
            super(username, screen);
        }

        @Override
        protected String getHaveJoinedSecond() {
            return "le caiste al chat.";
        }

        @Override
        protected String getHaveJoinedThird() {
            return "le cayó al chat.";
        }

        @Override
        protected String getHaveLeftSecond() {
            return "te pelaste del chat, mijo.";
        }

        @Override
        protected String getHaveLeftThird() {
            return "se peló del chat.";
        }

        @Override
        protected String getVerbSayThird() {
            return "dijo";
        }

        @Override
        protected String getVerbSaySecond() {
            return "dijiste";
        }

        @Override
        protected String getPronounYou() {
            return "Tú";
        }
    }

    /**
     * Spanish chat user. Extends abstract template. All chat notifications are displayed in Spanish (Spain).
     */
    public static class Spanish extends AbstractChatUser {

        public Spanish(String username, PrintStream screen) {
            super(username, screen);
        }

        @Override
        protected String getHaveJoinedSecond() {
            return "vos habéis unido al chat.";
        }

        @Override
        protected String getHaveJoinedThird() {
            return "ha arrivado al chat.";
        }

        @Override
        protected String getHaveLeftSecond() {
            return "habéis abandonado el chat, tio.";
        }

        @Override
        protected String getHaveLeftThird() {
            return "ha abandonado el chat.";
        }

        @Override
        protected String getVerbSayThird() {
            return "ha dicho";
        }

        @Override
        protected String getVerbSaySecond() {
            return "habéis dicho";
        }

        @Override
        protected String getPronounYou() {
            return "Vos";
        }
    }

    /**
     * British chat user. Extends abstract template. All chat notifications are displayed in English (UK).
     */
    public static class British extends AbstractChatUser {

        public British(String username, PrintStream screen) {
            super(username, screen);
        }

        @Override
        protected String getHaveJoinedSecond() {
            return "have joined the chat.";
        }

        @Override
        protected String getHaveJoinedThird() {
            return "has joined the chat.";
        }

        @Override
        protected String getHaveLeftSecond() {
            return "you have left the chat.";
        }

        @Override
        protected String getHaveLeftThird() {
            return "has left the chat.";
        }

        @Override
        protected String getVerbSayThird() {
            return "said";
        }

        @Override
        protected String getVerbSaySecond() {
            return "said";
        }

        @Override
        protected String getPronounYou() {
            return "You";
        }
    }
}