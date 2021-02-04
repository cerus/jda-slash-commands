package de.cerus.jdasc.interaction.response;

import java.util.List;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class InteractionResponseOption {

    private final String name;
    private final String value;
    private final List<InteractionResponseOption> options;

    public InteractionResponseOption(final String name, final String value, final List<InteractionResponseOption> options) {
        this.name = name;
        this.value = value;
        this.options = options;
    }

    public boolean hasOption(final String name) {
        return this.getOption(name) != null;
    }

    public InteractionResponseOption getGroup(final String name) {
        return this.getOption(name);
    }

    public InteractionResponseOption getSubCommand(final String name) {
        return this.getOption(name);
    }

    public InteractionResponseOption getArgument(final String name) {
        return this.getOption(name);
    }

    public InteractionResponseOption getOption(final String name) {
        return this.options.stream()
                .filter(interactionResponseOption -> interactionResponseOption.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public int getValueAsInt() {
        return Integer.parseInt(this.value);
    }

    public boolean getValueAsBool() {
        return this.value.equals("true");
    }

    public User getValueAsUser(final JDA jda) {
        return jda.getUserById(this.value);
    }

    public Role getValueAsRole(final JDA jda) {
        return jda.getRoleById(this.value);
    }

    public GuildChannel getValueAsChannel(final JDA jda) {
        return jda.getGuildChannelById(this.value);
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public String getValue() {
        return this.value;
    }

    @Nullable
    public List<InteractionResponseOption> getOptions() {
        return this.options;
    }

}
