package de.cerus.jdasc.router;

import de.cerus.jdasc.JDASlashCommands;
import de.cerus.jdasc.command.ApplicationCommand;
import de.cerus.jdasc.command.ApplicationCommandListener;
import de.cerus.jdasc.interaction.Interaction;
import de.cerus.jdasc.interaction.response.InteractionResponseOption;
import java.util.List;
import net.dv8tion.jda.api.JDA;

public abstract class CommandRouter implements ApplicationCommandListener {

    protected String prefix = "!";

    public abstract void route(JDA jda, ApplicationCommand command, Interaction interaction);

    @Override
    public void onInteraction(final Interaction interaction) {
        this.route(interaction.getChannel().getJDA(), JDASlashCommands.getCommand(interaction.getCommandId()), interaction);
    }

    protected String makePath(final Interaction interaction) {
        final StringBuilder stringBuilder = new StringBuilder(this.prefix).append(interaction.getCommandName()).append(" ");
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
