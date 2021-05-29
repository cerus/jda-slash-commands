package dev.cerus.jdasc.event;

import dev.cerus.jdasc.interaction.Interaction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;

/**
 * A simple event for interactions
 */
public class CommandInteractionEvent extends Event {

    private final Interaction interaction;

    public CommandInteractionEvent(final JDA jda, final Interaction interaction) {
        super(jda, jda.getResponseTotal());
        this.interaction = interaction;
    }

    public Interaction getInteraction() {
        return this.interaction;
    }

}
