package de.cerus.jdasc.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ApplicationCommand {

    private static final Predicate<String> NAME_PREDICATE = s -> s.matches("^[\\w-]{3,32}");
    private static final Predicate<String> DESCRIPTION_PREDICATE = s -> s.length() >= 1 && s.length() <= 100;

    private final String name;
    private final String description;
    private final List<ApplicationCommandOption> options;

    public ApplicationCommand(final String name, final String description) {
        this(name, description, new ArrayList<>());
    }

    public ApplicationCommand(final String name, final String description, final List<ApplicationCommandOption> options) {
        this.name = name;
        this.description = description;
        this.options = options;
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
    public String toString() {
        return "ApplicationCommand{" +
                "name='" + this.name + '\'' +
                ", description='" + this.description + '\'' +
                ", options=" + this.options +
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

}
