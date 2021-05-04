package de.cerus.jdasc.interaction;

import de.cerus.jdasc.JDASlashCommands;
import de.cerus.jdasc.interaction.followup.FollowupMessage;
import de.cerus.jdasc.interaction.response.InteractionApplicationCommandCallbackData;
import de.cerus.jdasc.interaction.response.InteractionResponse;
import de.cerus.jdasc.interaction.response.InteractionResponseOption;
import de.cerus.jdasc.interaction.response.InteractionResponseType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class Interaction {

    private final long id;
    private final InteractionType type;
    private final long commandId;
    private final String commandName;
    private final Guild guild;
    private final TextChannel channel;
    private final Member member;
    private final String token;
    private final List<InteractionResponseOption> options;

    public Interaction(final String token,
                       final long id,
                       final InteractionType type,
                       final long commandId,
                       final String commandName,
                       final Guild guild,
                       final TextChannel channel,
                       final Member member,
                       final List<InteractionResponseOption> options) {
        this.token = token;
        this.id = id;
        this.type = type;
        this.commandId = commandId;
        this.commandName = commandName;
        this.guild = guild;
        this.channel = channel;
        this.member = member;
        this.options = options;
    }

    /**
     * Acknowledge the interaction but don't send a message.
     *
     * @param eatInput Whether you want to not show (eat) the user's input (message)
     *
     * @return A future
     */
    public CompletableFuture<Void> acknowledge(final boolean eatInput) {
        return this.respond(new InteractionResponse(
                eatInput ? InteractionResponseType.ACKNOWLEDGE : InteractionResponseType.ACKNOWLEDGE_WITH_SOURCE,
                new InteractionApplicationCommandCallbackData(false, "", null, 0)
        ));
    }

    /**
     * Acknowledge the interaction but don't send a message.
     *
     * @param eatInput Whether you want to not show (eat) the user's input (message)
     * @param flags    The flags for the callback
     *
     * @return A future
     */
    public CompletableFuture<Void> acknowledge(final boolean eatInput, final int flags) {
        return this.respond(new InteractionResponse(
                eatInput ? InteractionResponseType.ACKNOWLEDGE : InteractionResponseType.ACKNOWLEDGE_WITH_SOURCE,
                new InteractionApplicationCommandCallbackData(false, "", null, flags)
        ));
    }

    /**
     * Acknowledge the interaction and send a message.
     *
     * @param eatInput Whether you want to not show (eat) the user's input (message)
     * @param embeds   Up to 10 MessageEmbed's
     *
     * @return A future
     *
     * @see MessageEmbed
     * @see net.dv8tion.jda.api.EmbedBuilder
     */
    public CompletableFuture<Void> respond(final boolean eatInput, final MessageEmbed... embeds) {
        return this.respond(eatInput, Arrays.asList(embeds));
    }

    /**
     * Acknowledge the interaction and send a message.
     *
     * @param eatInput Whether you want to not show (eat) the user's input (message)
     * @param flags    The discord flags
     * @param embeds   Up to 10 MessageEmbed's
     *
     * @return A future
     *
     * @see MessageEmbed
     * @see net.dv8tion.jda.api.EmbedBuilder
     */
    public CompletableFuture<Void> respond(final boolean eatInput, final int flags, final MessageEmbed... embeds) {
        return this.respond(eatInput, Arrays.asList(embeds), flags);
    }

    /**
     * Acknowledge the interaction and send a message.
     *
     * @param eatInput Whether you want to not show (eat) the user's input (message)
     * @param embeds   Up to 10 MessageEmbed's
     *
     * @return A future
     *
     * @see MessageEmbed
     * @see net.dv8tion.jda.api.EmbedBuilder
     */
    public CompletableFuture<Void> respond(final boolean eatInput, final List<MessageEmbed> embeds) {
        return this.respond(new InteractionResponse(
                eatInput ? InteractionResponseType.CHANNEL_MESSAGE : InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
                new InteractionApplicationCommandCallbackData(
                        false,
                        "",
                        embeds, 0
                )
        ));
    }

    /**
     * Acknowledge the interaction and send a message.
     *
     * @param eatInput Whether you want to not show (eat) the user's input (message)
     * @param embeds   Up to 10 MessageEmbed's
     * @param flags    Discord flags
     *
     * @return A future
     *
     * @see MessageEmbed
     * @see net.dv8tion.jda.api.EmbedBuilder
     */
    public CompletableFuture<Void> respond(final boolean eatInput, final List<MessageEmbed> embeds, final int flags) {
        return this.respond(new InteractionResponse(
                eatInput ? InteractionResponseType.CHANNEL_MESSAGE : InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
                new InteractionApplicationCommandCallbackData(
                        false,
                        "",
                        embeds, flags
                )
        ));
    }

    /**
     * Acknowledge the interaction and send a message.
     *
     * @param eatInput Whether you want to not show (eat) the user's input (message)
     * @param message  Your message
     *
     * @return A future
     */
    public CompletableFuture<Void> respond(final boolean eatInput, final String message) {
        return this.respond(new InteractionResponse(
                eatInput ? InteractionResponseType.CHANNEL_MESSAGE : InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
                new InteractionApplicationCommandCallbackData(
                        false,
                        message,
                        null, 0
                )
        ));
    }

    /**
     * Acknowledge the interaction and send a message.
     *
     * @param eatInput Whether you want to not show (eat) the user's input (message)
     * @param message  Your message
     * @param flags    The discord flags
     *
     * @return A future
     */
    public CompletableFuture<Void> respond(final boolean eatInput, final String message, final int flags) {
        return this.respond(new InteractionResponse(
                eatInput ? InteractionResponseType.CHANNEL_MESSAGE : InteractionResponseType.CHANNEL_MESSAGE_WITH_SOURCE,
                new InteractionApplicationCommandCallbackData(
                        false,
                        message,
                        null, flags
                )
        ));
    }

    /**
     * Submit a followup message
     * This requires that you have at least acknowledged the interaction
     *
     * @param embeds The message content
     *
     * @return The sent message
     */
    public CompletableFuture<Message> sendFollowup(final MessageEmbed... embeds) {
        return this.sendFollowup(new FollowupMessage("", false, Arrays.asList(embeds), 0));
    }

    /**
     * Submit a followup message
     * This requires that you have at least acknowledged the interaction
     *
     * @param flags  The discord flags
     * @param embeds The message content
     *
     * @return The sent message
     */
    public CompletableFuture<Message> sendFollowup(final int flags, final MessageEmbed... embeds) {
        return this.sendFollowup(new FollowupMessage("", false, Arrays.asList(embeds), flags));
    }

    /**
     * Submit a followup message
     * This requires that you have at least acknowledged the interaction
     *
     * @param message The message content
     *
     * @return The sent message
     */
    public CompletableFuture<Message> sendFollowup(final String message) {
        return this.sendFollowup(new FollowupMessage(message, false, new ArrayList<>(), 0));
    }

    /**
     * Submit a followup message
     * This requires that you have at least acknowledged the interaction
     *
     * @param flags   The message flags
     * @param message The message content
     *
     * @return The sent message
     */
    public CompletableFuture<Message> sendFollowup(final String message, final int flags) {
        return this.sendFollowup(new FollowupMessage(message, false, new ArrayList<>(), flags));
    }

    /**
     * Submit a followup message
     * This requires that you have at least acknowledged the interaction
     *
     * @param message The message content
     *
     * @return The sent message
     */
    public CompletableFuture<Message> sendFollowup(final FollowupMessage message) {
        return JDASlashCommands.submitFollowupMessage(this, message);
    }

    public boolean hasOption(final String name) {
        return this.getOption(name) != null;
    }

    public InteractionResponseOption getGroup(final String name) {
        return this.getOption(name);
    }

    public InteractionResponseOption getSubCommand(final String name) {
        return this.getOption(name);
    }

    public InteractionResponseOption getArgument(final String name) {
        return this.getOption(name);
    }

    public InteractionResponseOption getOption(final String name) {
        return this.options.stream()
                .filter(interactionResponseOption -> interactionResponseOption.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public CompletableFuture<Void> respond(final InteractionResponse response) {
        return JDASlashCommands.submitInteractionResponse(this, response);
    }

    public String getToken() {
        return this.token;
    }

    public long getId() {
        return this.id;
    }

    public InteractionType getType() {
        return this.type;
    }

    public long getCommandId() {
        return this.commandId;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public TextChannel getChannel() {
        return this.channel;
    }

    public Member getMember() {
        return this.member;
    }

    public List<InteractionResponseOption> getOptions() {
        return this.options;
    }

}
