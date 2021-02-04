package de.cerus.jdasc.interaction.response;

import java.util.Arrays;

public enum InteractionResponseType {

    PONG(1),
    ACKNOWLEDGE(2),
    CHANNEL_MESSAGE(3),
    CHANNEL_MESSAGE_WITH_SOURCE(4),
    ACKNOWLEDGE_WITH_SOURCE(5);

    private final int val;

    InteractionResponseType(final int val) {
        this.val = val;
    }

    public static InteractionResponseType getByVal(final int val) {
        return Arrays.stream(values())
                .filter(interactionType -> interactionType.val == val)
                .findAny()
                .orElse(null);
    }

    public int getVal() {
        return this.val;
    }

}
