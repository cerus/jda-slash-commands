package dev.cerus.jdasc.interaction;

import java.util.Arrays;

public enum InteractionType {

    PING(1),
    APPLICATION_COMMAND(2),
    MESSAGE_COMPONENT(3);

    private final int val;

    InteractionType(final int val) {
        this.val = val;
    }

    public static InteractionType getByVal(final int val) {
        return Arrays.stream(values())
                .filter(interactionType -> interactionType.val == val)
                .findAny()
                .orElse(null);
    }

    public int getVal() {
        return this.val;
    }

}
