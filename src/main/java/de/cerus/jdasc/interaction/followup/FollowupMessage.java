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
    private final int flags;

    public FollowupMessage(final String content,
                           final boolean tts,
                           final List<MessageEmbed> embeds, int flags) {
        this.content = content;
        this.tts = tts;
        this.embeds = embeds;
        this.flags = flags;
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
