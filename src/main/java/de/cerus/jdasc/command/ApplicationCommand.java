package de.cerus.jdasc.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a basic application command.
 * See https://discord.com/developers/docs/interactions/slash-commands#applicationcommand
 */
public class ApplicationCommand {

    private static final Predicate<String> NAME_PREDICATE = s -> s.matches("^[\\w-]{3,32}");
    private static final Predicate<String> DESCRIPTION_PREDICATE = s -> s.length() >= 1 && s.length() <= 100;
    private final String name;
    private final String description;
    private final List<ApplicationCommandOption> options;
    private long id;

    public ApplicationCommand(final String name, final String description) {
        this(name, description, new ArrayList<>());
    }

    public ApplicationCommand(final String name, final String description, final List<ApplicationCommandOption> options) {
        this.name = name;
        this.description = description;
        this.options = options;

        this.validate();
    }

    private void validate() {
        if (!NAME_PREDICATE.test(this.name)) {
            throw new IllegalStateException("Invalid command name");
        }
        if (!DESCRIPTION_PREDICATE.test(this.description)) {
            throw new IllegalStateException("Invalid command description");
        }
        if (this.options != null && this.options.size() > 10) {
            throw new IllegalStateException("There are only 10 options allowed per command");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationCommand)) {
            return false;
        }
        final ApplicationCommand that = (ApplicationCommand) o;
        return com.google.common.base.Objects.equal(this.getName(), that.getName()) && com.google.common.base.Objects.equal(this.getDescription(), that.getDescription()) && com.google.common.base.Objects.equal(this.getOptions(), that.getOptions());
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(this.getName(), this.getDescription(), this.getOptions());
    }


    @Override
    public String toString() {
        return "ApplicationCommand{" +
                "name='" + this.name + '\'' +
                ", description='" + this.description + '\'' +
                ", options=" + this.options +
                ", id=" + this.id +
                '}';
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<ApplicationCommandOption> getOptions() {
        return this.options;
    }

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
