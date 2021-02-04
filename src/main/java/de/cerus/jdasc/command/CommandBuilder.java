package de.cerus.jdasc.command;

import java.util.ArrayList;
import java.util.List;

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
        this.options.add(option);
        return this;
    }

    public ApplicationCommand build() {
        return new ApplicationCommand(
                this.name,
                this.desc,
                this.options
        );
    }

    public static class SubCommandGroupBuilder {

        private final List<ApplicationCommandOption> options = new ArrayList<>();
        private final String name;
        private String desc;

        public SubCommandGroupBuilder(final String name) {
            this.name = name;
        }

        public SubCommandGroupBuilder desc(final String desc) {
            this.desc = desc;
            return this;
        }

        public SubCommandGroupBuilder option(final ApplicationCommandOption option) {
            this.options.add(option);
            return this;
        }

        public ApplicationCommandOption build() {
            return new ApplicationCommandOption(ApplicationCommandOptionType.SUB_COMMAND_GROUP,
                    this.name,
                    this.desc,
                    false,
                    null,
                    this.options);
        }

    }

    public static class SubCommandBuilder {

        private final List<ApplicationCommandOptionChoice> choices = new ArrayList<>();
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
            this.options.add(option);
            return this;
        }

        public ApplicationCommandOption build() {
            return new ApplicationCommandOption(
                    ApplicationCommandOptionType.SUB_COMMAND,
                    this.name,
                    this.desc,
                    this.required,
                    this.choices.isEmpty() ? null : this.choices,
                    this.options
            );
        }

    }

}
