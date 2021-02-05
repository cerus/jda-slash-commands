package de.cerus.jdasc.router;

import co.aikar.commands.JDACommandManager;
import de.cerus.jdasc.command.ApplicationCommand;
import de.cerus.jdasc.interaction.Interaction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.DataMessage;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.jetbrains.annotations.NotNull;

public class ACFRouter extends CommandRouter {

    private final JDACommandManager commandManager;

    public ACFRouter(final JDACommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void route(final JDA jda, final ApplicationCommand command, final Interaction interaction) {
        interaction.acknowledge(false);

        final Message message = new DataMessage(false, this.makePath(interaction), null, null,
                null, new String[0], new String[0]) {
            @NotNull
            @Override
            public Guild getGuild() {
                return interaction.getGuild();
            }

            @NotNull
            @Override
            public TextChannel getTextChannel() {
                return (TextChannel) this.getChannel();
            }

            @NotNull
            @Override
            public MessageChannel getChannel() {
                return interaction.getChannel();
            }

            @NotNull
            @Override
            public User getAuthor() {
                return interaction.getMember().getUser();
            }

            @Override
            public Member getMember() {
                return interaction.getMember();
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
        };

        final MessageReceivedEvent event = new MessageReceivedEvent(jda, jda.getResponseTotal(), message);
        try {
            final Method method = this.commandManager.getClass().getDeclaredMethod("dispatchEvent", MessageReceivedEvent.class);
            method.setAccessible(true);
            method.invoke(this.commandManager, event);
        } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.err.println("Failed to route interaction to ACF");
        }
    }

}
