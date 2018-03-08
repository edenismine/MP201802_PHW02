package mx.unam.fciencias.myp.chat.models;

import java.sql.Timestamp;

/**
 * An extendable chat notification.
 *
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class ChatNotification {
    /**
     * Flag that denotes a new message notification.
     */
    public static final int NEW_MESSAGE = 0;
    /**
     * Flag that denotes a notification about a user joining the chat.
     */
    public static final int NEW_USER = 1;
    /**
     * Flag that denotes a notification about a user leaving the chat.
     */
    public static final int USER_LEFT = 2;
    /**
     * A reference to the user that created the notification.
     */
    private AbstractChatUser origin;
    /**
     * Either a descriptive message about this notification, or a custom message sent by a user.
     */
    private String message;
    /**
     * <p>A flag that specifies in which category this notification falls into.</p>
     * <p>The following are valid flags:
     * <ul>
     * <li>{@code 0} ({@link #NEW_MESSAGE}): A notification that carries a custom message sent by a user.</li>
     * <li>{@code 1} ({@link #NEW_USER}): A notification that communicates that its user has joined the chat.</li>
     * <li>{@code 2} ({@link #USER_LEFT}): A notification that communicates that its user has left the chat.</li>
     * </ul>
     */
    private int type;

    /**
     * <p>A chat notification carries a reference to the user that created it, a descriptive message (or
     * a custom message sent by that user) and a flag that specifies why the notification's being built.</p>
     * <p>The following are valid flags:
     * <ul>
     * <li>{@code 0} ({@link #NEW_MESSAGE}): A notification that carries a custom message sent by a user.</li>
     * <li>{@code 1} ({@link #NEW_USER}): A notification that communicates that its user has joined the chat.</li>
     * <li>{@code 2} ({@link #USER_LEFT}): A notification that communicates that its user has left the chat.</li>
     * </ul>
     *
     * @param context The user that created this notification.
     * @param message Either a descriptive message about this notification, or a custom message sent by the user.
     * @param flag    A flag that specifies in which category this notification falls into.
     */
    public ChatNotification(AbstractChatUser context, String message, int flag) {
        if (flag < NEW_MESSAGE || flag > USER_LEFT)
            throw new IllegalArgumentException(String.format("Invalid flag: %d", flag));
        this.origin = context;
        this.message = message;
        this.type = flag;
    }

    /**
     * This method should return a reference to the user that created this notification.
     *
     * @return a reference to the user that created this notification.
     */
    public AbstractChatUser getOrigin() {
        return origin;
    }

    /**
     * This method returns a message if this notification corresponds to a chat message.
     *
     * @return a message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method returns this notification's code/flag.
     *
     * @return this notification's type.
     */
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[@"
                + new Timestamp(System.currentTimeMillis())
                + "] Origin: "
                + origin.getUsername()
                + "  Message: "
                + message
                + "  Type: "
                + type;
    }
}
