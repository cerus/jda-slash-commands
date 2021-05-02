package de.cerus.jdasc.command;

import de.cerus.jdasc.interaction.Interaction;
import de.cerus.jdasc.interaction.response.InteractionResponseOption;

import java.util.List;

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
     * @param options       The list of arguments.
     */
    default void handleArguments(final Interaction interaction, final List<InteractionResponseOption> options) {
    }

}
