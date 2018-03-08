package mx.unam.fciencias.myp.chat.models;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic Chat user template.
 *
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public abstract class AbstractChatUser {
    /**
     * Holds the user's chat history.
     */
    private final ArrayList<String> chatHistory;

    /**
     * Holds the user's username.
     */
    private final String username;

    /**
     * Holds a reference to the user's output print stream.
     */
    private final PrintStream screen;

    /**
     * When constructing a user without specifying an output print stream, {@link System#out} is chosen by default.
     *
     * @param username The username chosen by the user to identify him/herself with.
     */
    public AbstractChatUser(String username) {
        this(username, System.out);
    }

    /**
     * When constructing a user without specifying an output print stream, {@link System#out} is chosen by default.
     *
     * @param username The username chosen by the user to identify him/herself with.
     * @param screen   The user's display as a PrintStream.
     */
    public AbstractChatUser(String username, PrintStream screen) {
        this.chatHistory = new ArrayList<>(100);
        this.username = username;
        this.screen = screen;
    }

    /**
     * Retrieves a copy of the user's chat history.
     *
     * @return a copy of the user's chat history.
     */
    public List<String> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }

    /**
     * Retrieves the user's username.
     *
     * @return the user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Attempts to broadcast a message to the specified chat.
     *
     * @param chat    chat the user is trying to broadcast to.
     * @param message the message the user is trying to broadcast.
     */
    public void sendMessage(Chat chat, String message) {
        if (chat.isOnline(this)) {
            chat.updateUsers(new ChatNotification(
                    this, message, ChatNotification.NEW_MESSAGE
            ));
        } else {
            throw new IllegalStateException("Offline user trying to send a message.");
        }
    }

    /**
     * <p>This method receives a chat notification and dynamically updates the user's screen depending on what kind of
     * notification is passed as a parameter. This method should work seamlessly among extending subclasses that
     * implement this class' abstract methods which provide custom strings or possibly translations to fill in some
     * basic templates:
     * <p>
     * Defaults:
     * <ul>
     * <li>When a {@link ChatNotification#NEW_MESSAGE} notification is passed, the following template is used:
     * "%s %s: %s", the first string should reference the subject (can be this user if it matches the notification's)
     * the second string should reference the appropriate conjugation/translation of the verb to say, and lastly, the
     * third string should be the message referenced by the notification.
     * </li>
     * <li>When a {@link ChatNotification#NEW_USER} notification is passed, the following template is used:
     * "%s %s", the first string should reference the subject (can be this user if it matches the notification's)
     * the second string should reference the appropriate phrase that indicates that the user that generated the
     * notification has joined.
     * </li>
     * <li>When a {@link ChatNotification#USER_LEFT} notification is passed, the following template is used:
     * "%s %s", the first string should reference the subject (can be this user if it matches the notification's)
     * the second string should reference the appropriate phrase that indicates that the user that generated the
     * notification is leaving.
     * </li>
     * </ul>
     *
     * @param notification the notification.
     * @see #getHaveJoinedSecond()
     * @see #getHaveJoinedThird()
     * @see #getHaveLeftSecond()
     * @see #getHaveLeftThird()
     * @see #getVerbSaySecond()
     * @see #getVerbSayThird()
     * @see #getPronounYou()
     */
    public final void update(ChatNotification notification) {
        // Call the appropriate method:
        switch (notification.getType()) {
            case ChatNotification.NEW_MESSAGE:
                gotNewMessage(notification.getOrigin(), notification.getMessage());
                break;
            case ChatNotification.NEW_USER:
                userJoined(notification.getOrigin());
                break;
            case ChatNotification.USER_LEFT:
                userLeft(notification.getOrigin());
                break;
            default:
                throw new IllegalArgumentException("Unsupported update.");
        }
    }

    /**
     * Prints a message about the specified user leaving the chat to the user's screen and adds it to the chat history.
     * By default its functionality depends on the {@link #getHaveLeftSecond()} and {@link #getHaveLeftThird()} methods.
     * The resulting components are put into the default template "%s %s" as needed. This method can be overridden but
     * it's not recommended.
     *
     * @param user the user that left the chat.
     */
    protected void userLeft(AbstractChatUser user) {
        String goodbye = user.equals(this) ? getHaveLeftSecond() : getHaveLeftThird();
        String string = String.format(
                "%s %s",
                user.getUsername(),
                goodbye
        );
        chatHistory.add(string);
        screen.println(string);
    }

    /**
     * Prints a message about the specified user joining the chat to the user's screen and adds it to the chat history.
     * By default its functionality depends on the {@link #getHaveJoinedSecond()}, {@link #getHaveJoinedThird()} and
     * {@link #getPronounYou()} methods. The resulting components are put into the default template "%s %s" as needed.
     * This method can be overridden but it's not recommended.
     *
     * @param user the user that joined the chat.
     */
    protected void userJoined(AbstractChatUser user) {
        String subject = user.getUsername();
        String predicate = getHaveJoinedThird();
        if (user.equals(this)) {
            subject = getPronounYou();
            predicate = getHaveJoinedSecond();
        }
        String string = String.format(
                "%s %s",
                subject,
                predicate
        );
        chatHistory.add(string);
        screen.println(string);
    }

    /**
     * Prints a message sent by another user to the user's screen and adds it to the chat history. By default its
     * functionality depends on the {@link #getVerbSaySecond()}, {@link #getVerbSayThird()} and {@link #getPronounYou()}
     * methods that are put into the default template "%s %s: %s" as needed. This method can be overridden but it's not
     * recommended.
     *
     * @param user    the user that sent the message.
     * @param message the message sent by {@code user}.
     */
    protected void gotNewMessage(AbstractChatUser user, String message) {
        String username = user.getUsername();
        String verb = getVerbSayThird();
        if (user.equals(this)) {
            username = getPronounYou();
            verb = getVerbSaySecond();
        }
        String string = String.format(
                "%s %s: %s",
                username,
                verb,
                message
        );
        chatHistory.add(string);
        screen.println(string);
    }

    /**
     * @return a string that corresponds to an equivalent predicate in the phrase "you have joined."
     */
    protected abstract String getHaveJoinedSecond();

    /**
     * @return a string that corresponds to an equivalent predicate in the phrase "he/she has joined."
     */
    protected abstract String getHaveJoinedThird();

    /**
     * @return a string that corresponds to an equivalent predicate in the phrase "you have left."
     */
    protected abstract String getHaveLeftSecond();

    /**
     * @return a string that corresponds to an equivalent predicate in the phrase "he/she has left."
     */
    protected abstract String getHaveLeftThird();

    /**
     * @return a string that corresponds to the second-person singular indicative simple-past form of the verb to say.
     */
    protected abstract String getVerbSaySecond();

    /**
     * @return a string that corresponds to the third-person singular indicative simple-past form of the verb to say.
     */
    protected abstract String getVerbSayThird();

    /**
     * @return a string that is equivalent to the pronoun "you"
     */
    protected abstract String getPronounYou();
}
