package dev.cerus.jdasc.interaction.followup;

import dev.cerus.jdasc.components.Component;
import java.util.Collections;
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
    private final List<Component> components;

    public FollowupMessage(final String content,
                           final boolean tts,
                           final List<MessageEmbed> embeds,
                           final int flags,
                           final List<Component> components) {
        this.content = content;
        this.tts = tts;
        this.embeds = embeds;
        this.flags = flags;
        this.components = components;
    }

    public static FollowupMessage create(final String content) {
        return new FollowupMessage(content, false, Collections.emptyList(), 0, Collections.emptyList());
    }

    public static FollowupMessage create(final String content, final int flags) {
        return new FollowupMessage(content, false, Collections.emptyList(), flags, Collections.emptyList());
    }

    public static FollowupMessage create(final List<MessageEmbed> embeds) {
        return new FollowupMessage("", false, embeds, 0, Collections.emptyList());
    }

    public static FollowupMessage create(final List<MessageEmbed> embeds, final int flags) {
        return new FollowupMessage("", false, embeds, flags, Collections.emptyList());
    }

    public static FollowupMessage create(final String content, final List<Component> components) {
        return new FollowupMessage(content, false, Collections.emptyList(), 0, components);
    }

    public static FollowupMessage create(final String content, final int flags, final List<Component> components) {
        return new FollowupMessage(content, false, Collections.emptyList(), flags, components);
    }

    public static FollowupMessage create(final List<MessageEmbed> embeds, final List<Component> components) {
        return new FollowupMessage("", false, embeds, 0, components);
    }

    public static FollowupMessage create(final List<MessageEmbed> embeds, final int flags, final List<Component> components) {
        return new FollowupMessage("", false, embeds, flags, components);
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

    public int getFlags() {
        return this.flags;
    }

    public List<Component> getComponents() {
        return this.components;
    }

}
