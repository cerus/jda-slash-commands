package dev.cerus.jdasc.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A helper class for building commands.
 */
public class CommandBuilder {

    private final List<ApplicationCommandOption> options = new ArrayList<>();
    private String name;
    private String desc;

    public CommandBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public CommandBuilder desc(final String desc) {
        this.desc = desc;
        return this;
    }

    public CommandBuilder option(final ApplicationCommandOption option) {
        if (option != null) {
            this.options.add(option);
        }
        return this;
    }

    public ApplicationCommand build() {
        if (this.name == null || this.desc == null) {
            throw new NullPointerException("name or desc is null");
        }
        return new ApplicationCommand(
                this.name,
                this.desc,
                this.options
        );
    }

    public static class SubCommandGroupBuilder {

        private final List<ApplicationCommandOption> options = new ArrayList<>();
        private String name;
        private String desc;

        public SubCommandGroupBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public SubCommandGroupBuilder desc(final String desc) {
            this.desc = desc;
            return this;
        }

        public SubCommandGroupBuilder option(final ApplicationCommandOption option) {
            if (option != null) {
                this.options.add(option);
            }
            return this;
        }

        public ApplicationCommandOption build() {
            if (this.name == null || this.desc == null) {
                throw new NullPointerException("name or desc is null");
            }
            return new ApplicationCommandOption(ApplicationCommandOptionType.SUB_COMMAND_GROUP,
                    this.name,
                    this.desc,
                    false,
                    null,
                    this.options);
        }

    }

    public static class SubCommandBuilder {

        private final List<ApplicationCommandOption> options = new ArrayList<>();
        private String name;
        private String desc;
        private boolean required;

        public SubCommandBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public SubCommandBuilder desc(final String desc) {
            this.desc = desc;
            return this;
        }

        public SubCommandBuilder required(final boolean b) {
            this.required = b;
            return this;
        }

        public SubCommandBuilder option(final ApplicationCommandOption option) {
            if (option != null) {
                this.options.add(option);
            }
            return this;
        }

        public SubCommandBuilder choices(final ApplicationCommandOptionType type,
                                         final String name,
                                         final String desc,
                                         final ApplicationCommandOptionChoice... choices) {
            return this.option(new ApplicationCommandOption(
                    type,
                    name,
                    desc,
                    true,
                    Arrays.asList(choices).stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()),
                    new ArrayList<>()
            ));
        }

        public ApplicationCommandOption build() {
            if (this.name == null || this.desc == null) {
                throw new NullPointerException("name or desc is null");
            }
            return new ApplicationCommandOption(
                    ApplicationCommandOptionType.SUB_COMMAND,
                    this.name,
                    this.desc,
                    this.required,
                    null,
                    this.options
            );
        }

    }

}
