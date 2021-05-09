package de.cerus.jdasc.command;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a option. A option could be a sub command, a sub command group or a argument.
 * See https://discord.com/developers/docs/interactions/slash-commands#applicationcommandoption
 */
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
                                    final boolean required) {
        this(type, name, description, required, new ArrayList<>());
    }

    public ApplicationCommandOption(final ApplicationCommandOptionType type,
                                    final String name,
                                    final String description,
                                    final boolean required,
                                    final List<ApplicationCommandOption> options) {
        this(type, name, description, required, null, options);
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
        if (this.choices != null && this.type != ApplicationCommandOptionType.STRING && this.type != ApplicationCommandOptionType.INTEGER) {
            throw new IllegalStateException("Choices are only allowed for STRING and INTEGER arguments");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationCommandOption)) return false;
        ApplicationCommandOption that = (ApplicationCommandOption) o;
        return isRequired() == that.isRequired() &&
                getType() == that.getType() &&
                Objects.equal(getName(), that.getName()) &&
                Objects.equal(getDescription(), that.getDescription()) &&
                Objects.equal(getChoices(), that.getChoices()) &&
                (getOptions() == null || getOptions().isEmpty() || Objects.equal(getOptions(), that.getOptions()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getType(), getName(), getDescription(), isRequired(), getChoices(), getOptions());
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

    @Override
    public String toString() {
        return "ApplicationCommandOption{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", required=" + required +
                ", choices=" + choices +
                ", options=" + options +
                '}';
    }
}
