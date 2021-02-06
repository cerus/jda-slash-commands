package de.cerus.jdasc.interaction.followup;

import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * Represents a followup message. Basically the same as a webhook message.
 */
public class FollowupMessage {

    private final String content;
    private final boolean tts;
    private final List<MessageEmbed> embeds;

    public FollowupMessage(final String content,
                           final boolean tts,
                           final List<MessageEmbed> embeds) {
        this.content = content;
        this.tts = tts;
        this.embeds = embeds;
    }

    public String getContent() {
        return this.content;
    }

    public boolean isTts() {
        return this.tts;
    }

    public List<MessageEmbed> getEmbeds() {
        return this.embeds;
    }

}
