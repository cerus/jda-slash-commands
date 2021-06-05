package dev.cerus.jdasc.command;

import dev.cerus.jdasc.interaction.Interaction;
import dev.cerus.jdasc.interaction.response.InteractionResponseOption;
import java.util.Map;

/**
 * Represents a basic listener
 */
public interface ApplicationCommandListener {

    /**
     * Gets called for every interaction with the parent command
     *
     * @param interaction The interaction
     */
    void onInteraction(final Interaction interaction);

    /**
     * Gets called if a argument was specified
     *
     * @param interaction The interaction
     * @param options     The list of arguments.
     */
    default void handleArguments(final Interaction interaction, final Map<String, InteractionResponseOption> options) {
    }

}
