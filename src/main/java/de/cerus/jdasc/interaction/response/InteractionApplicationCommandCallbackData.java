package de.cerus.jdasc.interaction.response;

import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class InteractionApplicationCommandCallbackData {

    private final boolean tts;
    private final String content;
    private final List<MessageEmbed> embeds;

    public InteractionApplicationCommandCallbackData(final boolean tts, final String content, final List<MessageEmbed> embeds) {
        this.tts = tts;
        this.content = content;
        this.embeds = embeds;

        this.validate();
    }

    private void validate() {
        if (this.embeds != null && this.embeds.size() > 10) {
            throw new IllegalStateException("Only 10 interaction response embeds are allowed");
        }
    }

    public boolean isTts() {
        return this.tts;
    }

    public String getContent() {
        return this.content;
    }

    public List<MessageEmbed> getEmbeds() {
        return this.embeds;
    }

}
