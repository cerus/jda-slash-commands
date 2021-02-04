package de.cerus.jdasc.command;

import de.cerus.jdasc.interaction.Interaction;
import de.cerus.jdasc.interaction.response.InteractionResponseOption;

public interface ApplicationCommandListener {

    void onInteraction(Interaction interaction);

    default void handleArgument(final Interaction interaction, final String argumentName, final InteractionResponseOption option) {
    }

}
