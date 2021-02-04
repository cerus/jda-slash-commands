package de.cerus.jdasc.command;

import java.util.function.Predicate;

public class ApplicationCommandOptionChoice {

    private static final Predicate<String> NAME_PREDICATE = s -> s.length() >= 1 && s.length() <= 100;

    private final String name;
    private final String value;

    public ApplicationCommandOptionChoice(final String name, final String value) {
        this.name = name;
        this.value = value;

        validate();
    }

    private void validate() {
        if (!NAME_PREDICATE.test(name)) {
            throw new IllegalStateException("Invalid choice name");
        }
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
