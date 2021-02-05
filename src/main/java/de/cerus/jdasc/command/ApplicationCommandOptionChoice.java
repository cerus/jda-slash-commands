package de.cerus.jdasc.command;

import java.util.function.Predicate;

/**
 * Represents a choice. The name of the choice is visible to the user and the value of the choice will be used by the api.
 * See https://discord.com/developers/docs/interactions/slash-commands#applicationcommandoptionchoice
 */
public class ApplicationCommandOptionChoice {

    private static final Predicate<String> NAME_PREDICATE = s -> s.length() >= 1 && s.length() <= 100;

    // The name will be visible for the user
    private final String name;
    // The value can be used internally to check what the user chose
    private final String value;

    public ApplicationCommandOptionChoice(final String name, final String value) {
        this.name = name;
        this.value = value;

        this.validate();
    }

    private void validate() {
        if (!NAME_PREDICATE.test(this.name)) {
            throw new IllegalStateException("Invalid choice name");
        }
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

}
