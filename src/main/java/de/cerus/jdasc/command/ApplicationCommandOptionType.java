package de.cerus.jdasc.command;

import java.util.Arrays;

public enum ApplicationCommandOptionType {

    SUB_COMMAND(1),
    SUB_COMMAND_GROUP(2),
    STRING(3),
    INTEGER(4),
    BOOLEAN(5),
    USER(6),
    CHANNEL(7),
    ROLE(8);

    private final int val;

    ApplicationCommandOptionType(final int val) {
        this.val = val;
    }

    public static ApplicationCommandOptionType getByVal(final int val) {
        return Arrays.stream(values())
                .filter(applicationCommandOptionType -> applicationCommandOptionType.val == val)
                .findAny()
                .orElse(null);
    }

    public int getVal() {
        return this.val;
    }

}
