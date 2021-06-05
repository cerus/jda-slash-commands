package dev.cerus.jdasc.components;

import dev.cerus.jdasc.interaction.Interaction;

public interface ComponentListener {

    /**
     * Gets called when a user interacts with a message component
     *
     * @param interaction The interaction
     */
    default void onInteraction(final Interaction interaction) {
    }

}
