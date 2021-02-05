package de.cerus.jdasc.command;

import de.cerus.jdasc.interaction.Interaction;
import de.cerus.jdasc.interaction.response.InteractionResponseOption;

/**
 * Represents a basic listener
 */
public interface ApplicationCommandListener {

    /**
     * Gets called for every interaction with the parent command
     *
     * @param interaction The interaction
     */
    void onInteraction(Interaction interaction);

    /**
     * Gets called if a argument was specified
     *
     * @param interaction  The interaction
     * @param argumentName The name of the argument
     * @param option       The actual argument
     */
    default void handleArgument(final Interaction interaction, final String argumentName, final InteractionResponseOption option) {
    }

}
