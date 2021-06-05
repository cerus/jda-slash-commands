package dev.cerus.jdasc.interaction.response;

import dev.cerus.jdasc.components.Component;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class InteractionApplicationCommandCallbackData {

    private final boolean tts;
    private final String content;
    private final List<MessageEmbed> embeds;
    private final int flags;
    private final List<Component> components;

    public InteractionApplicationCommandCallbackData(final boolean tts,
                                                     final String content,
                                                     final List<MessageEmbed> embeds,
                                                     final int flags) {
        this(tts, content, embeds, flags, Collections.emptyList());
    }

    public InteractionApplicationCommandCallbackData(final boolean tts,
                                                     final String content,
                                                     final List<MessageEmbed> embeds,
                                                     final int flags,
                                                     final List<Component> components) {
        this.tts = tts;
        this.content = content;
        this.embeds = embeds;
        this.flags = flags;
        this.components = components;

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

    public int getFlags() {
        return this.flags;
    }

    public List<Component> getComponents() {
        return this.components;
    }

}
