package dev.cerus.jdasc.util;

import dev.cerus.jdasc.interaction.Interaction;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.DataMessage;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.jetbrains.annotations.NotNull;

public class FakeMessage extends DataMessage {

    private final Interaction interaction;

    public FakeMessage(final boolean tts, final String content, final String nonce, final MessageEmbed embed, final EnumSet<MentionType> allowedMentions, final String[] mentionedUsers, final String[] mentionedRoles, final Interaction interaction) {
        super(tts, content, nonce, embed, allowedMentions, mentionedUsers, mentionedRoles);
        this.interaction = interaction;
    }

    public FakeMessage(final boolean tts, final String content, final String nonce, final MessageEmbed embed, final Interaction interaction) {
        super(tts, content, nonce, embed);
        this.interaction = interaction;
    }

    @NotNull
    @Override
    public Guild getGuild() {
        return this.interaction.getGuild();
    }

    @NotNull
    @Override
    public TextChannel getTextChannel() {
        return (TextChannel) this.getChannel();
    }

    @NotNull
    @Override
    public MessageChannel getChannel() {
        return this.interaction.getChannel();
    }

    @NotNull
    @Override
    public User getAuthor() {
        return this.interaction.getMember().getUser();
    }

    @Override
    public Member getMember() {
        return this.interaction.getMember();
    }

    @Override
    public long getIdLong() {
        return 0L;
    }

    @NotNull
    @Override
    public String getId() {
        return String.valueOf(this.getIdLong());
    }

    @NotNull
    @Override
    public ChannelType getChannelType() {
        return this.getChannel().getType();
    }

    @NotNull
    @Override
    public Bag<Emote> getEmotesBag() {
        return new HashBag<>();
    }

    @NotNull
    @Override
    public Bag<Role> getMentionedRolesBag() {
        return new HashBag<>();
    }

    @NotNull
    @Override
    public Bag<TextChannel> getMentionedChannelsBag() {
        return new HashBag<>();
    }

    @NotNull
    @Override
    public Bag<User> getMentionedUsersBag() {
        return new HashBag<>();
    }

    @Override
    public Category getCategory() {
        return this.getGuild().getCategories().get(0);
    }

    @NotNull
    @Override
    public List<IMentionable> getMentions(@NotNull final MentionType... types) {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<Emote> getEmotes() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<Member> getMentionedMembers() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<Member> getMentionedMembers(@NotNull final Guild guild) {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<Role> getMentionedRoles() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<TextChannel> getMentionedChannels() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<User> getMentionedUsers() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<MessageEmbed> getEmbeds() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<MessageReaction> getReactions() {
        return new ArrayList<>();
    }

}
