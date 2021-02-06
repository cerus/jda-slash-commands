package de.cerus.jdasc.interaction.followup;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * Represents a followup message. Basically the same as a webhook message.
 */
public class FollowupMessage {

    private final String content;
    private final String username;
    @SerializedName("avatar_url")
    private final String avatarUrl;
    private final boolean tts;
    private final List<MessageEmbed> embeds;

    public FollowupMessage(final String content,
                           final String username,
                           final String avatarUrl,
                           final boolean tts,
                           final List<MessageEmbed> embeds) {
        this.content = content;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.tts = tts;
        this.embeds = embeds;
    }

    public String getContent() {
        return this.content;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public boolean isTts() {
        return this.tts;
    }

    public List<MessageEmbed> getEmbeds() {
        return this.embeds;
    }

}
