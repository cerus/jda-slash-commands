package de.cerus.jdasc.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ApplicationCommandOption {

    private static final Predicate<String> NAME_PREDICATE = s -> s.matches("^[\\w-]{3,32}");
    private static final Predicate<String> DESCRIPTION_PREDICATE = s -> s.length() >= 1 && s.length() <= 100;

    private final ApplicationCommandOptionType type;
    private final String name;
    private final String description;
    private final boolean required;
    private final List<ApplicationCommandOptionChoice> choices;
    private final List<ApplicationCommandOption> options;

    public ApplicationCommandOption(final ApplicationCommandOptionType type, final String name, final String description) {
        this(type, name, description, false, null, type == ApplicationCommandOptionType.SUB_COMMAND
                || type == ApplicationCommandOptionType.SUB_COMMAND_GROUP ? new ArrayList<>() : null);
    }

    public ApplicationCommandOption(final ApplicationCommandOptionType type,
                                    final String name,
                                    final String description,
                                    final boolean required,
                                    final List<ApplicationCommandOptionChoice> choices,
                                    final List<ApplicationCommandOption> options) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.required = required;
        this.choices = choices;
        this.options = options;

        this.validate();
    }

    private void validate() {
        if (!NAME_PREDICATE.test(this.name)) {
            throw new IllegalStateException("Invalid option name");
        }
        if (!DESCRIPTION_PREDICATE.test(this.description)) {
            throw new IllegalStateException("Invalid option description");
        }
        if (this.choices != null && this.choices.size() > 10) {
            throw new IllegalStateException("There are only 10 choices allowed per option");
        }
        if (this.type == ApplicationCommandOptionType.SUB_COMMAND && this.options != null && this.options.size() > 10) {
            throw new IllegalStateException("There are only 10 options allowed per sub command");
        }
    }

    @Override
    public String toString() {
        return "ApplicationCommandOption{" +
                "type=" + this.type +
                ", name='" + this.name + '\'' +
                ", description='" + this.description + '\'' +
                ", required=" + this.required +
                ", choices=" + this.choices +
                ", options=" + this.options +
                '}';
    }

    public ApplicationCommandOptionType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isRequired() {
        return this.required;
    }

    public List<ApplicationCommandOptionChoice> getChoices() {
        return this.choices;
    }

    public List<ApplicationCommandOption> getOptions() {
        return this.options;
    }

}
