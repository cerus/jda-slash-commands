package dev.cerus.jdasc.router;

import dev.cerus.jdasc.JDASlashCommands;
import dev.cerus.jdasc.command.ApplicationCommand;
import dev.cerus.jdasc.command.ApplicationCommandListener;
import dev.cerus.jdasc.interaction.Interaction;
import dev.cerus.jdasc.interaction.response.InteractionResponseOption;
import java.util.List;
import net.dv8tion.jda.api.JDA;

public abstract class CommandRouter implements ApplicationCommandListener {

    protected String prefix = "!";

    public abstract void route(JDA jda, ApplicationCommand command, Interaction interaction);

    @Override
    public void onInteraction(final Interaction interaction) {
        this.route(interaction.getChannel().getJDA(), JDASlashCommands.getCommand(interaction.getCommandId()), interaction);
    }

    protected String makeCommandString(final Interaction interaction) {
        return this.makeCommandString(true, interaction);
    }

    protected String makeCommandString(final boolean withPrefix, final Interaction interaction) {
        final StringBuilder stringBuilder = new StringBuilder(withPrefix ? this.prefix : "").append(interaction.getCommandName()).append(" ");
        this.walk(interaction.getOptions(), stringBuilder);
        return stringBuilder.toString();
    }

    private void walk(final List<InteractionResponseOption> options, final StringBuilder output) {
        for (final InteractionResponseOption option : options) {
            if (option.getOptions() != null && !option.getOptions().isEmpty()) {
                output.append(option.getName()).append(" ");
                this.walk(option.getOptions(), output);
            } else {
                //TODO: Parse value depending on type
                output.append(option.getValue() == null ? option.getName() : option.getValue());
            }
        }
    }

}
