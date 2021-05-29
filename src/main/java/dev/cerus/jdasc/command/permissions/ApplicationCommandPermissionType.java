package dev.cerus.jdasc.command.permissions;

import java.util.Arrays;

public enum ApplicationCommandPermissionType {

    ROLE(1),
    USER(2);

    private final int val;

    ApplicationCommandPermissionType(final int val) {
        this.val = val;
    }

    public static ApplicationCommandPermissionType getByVal(final int val) {
        return Arrays.stream(values())
                .filter(applicationCommandOptionType -> applicationCommandOptionType.val == val)
                .findAny()
                .orElse(null);
    }

    public int getVal() {
        return this.val;
    }
}
