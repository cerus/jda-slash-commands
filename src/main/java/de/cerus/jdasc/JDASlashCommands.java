package de.cerus.jdasc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.cerus.jdasc.command.ApplicationCommand;
import de.cerus.jdasc.command.ApplicationCommandListener;
import de.cerus.jdasc.command.ApplicationCommandOption;
import de.cerus.jdasc.command.ApplicationCommandOptionType;
import de.cerus.jdasc.http.DiscordHttpClient;
import de.cerus.jdasc.interaction.Interaction;
import de.cerus.jdasc.interaction.followup.FollowupMessage;
import de.cerus.jdasc.interaction.response.InteractionApplicationCommandCallbackData;
import de.cerus.jdasc.interaction.response.InteractionResponse;
import de.cerus.jdasc.interaction.response.InteractionResponseOption;
import de.cerus.jdasc.listener.InteractionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.EntityBuilder;
import okhttp3.Response;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public class JDASlashCommands {

    private static final Map<Long, ApplicationCommand> commandMap = new HashMap<>();
    private static final Map<ApplicationCommand, ApplicationCommandListener> commandListenerMap = new HashMap<>();

    private static DiscordHttpClient discordHttpClient;
    private static EntityBuilder entityBuilder;

    private JDASlashCommands() {
    }

    /**
     * Delete a followup message
     *
     * @param interaction The interaction
     * @param messageId   The id of your followup message
     *
     * @return A future
     */
    public static CompletableFuture<Void> deleteFollowupMessage(final Interaction interaction, final long messageId) {
        return discordHttpClient.deleteFollowupMessage(interaction, messageId).thenApply(response -> null);
    }

    /**
     * Edit a followup message
     *
     * @param interaction The interaction
     * @param embeds      The message content
     * @param messageId   The id of your followup message
     *
     * @return A future
     */
    public static CompletableFuture<Void> editFollowupMessage(final Interaction interaction, final long messageId, final MessageEmbed... embeds) {
        return editFollowupMessage(interaction, messageId, new FollowupMessage("", false, Arrays.asList(embeds)));
    }

    /**
     * Edit a followup message
     *
     * @param interaction The interaction
     * @param message     The message content
     * @param messageId   The id of your followup message
     *
     * @return A future
     */
    public static CompletableFuture<Void> editFollowupMessage(final Interaction interaction, final long messageId, final String message) {
        return editFollowupMessage(interaction, messageId, new FollowupMessage(message, false, new ArrayList<>()));
    }

    /**
     * Edit a followup message
     *
     * @param interaction The interaction
     * @param message     The message content
     * @param messageId   The id of your followup message
     *
     * @return A future
     */
    public static CompletableFuture<Void> editFollowupMessage(final Interaction interaction, final long messageId, final FollowupMessage message) {
        return discordHttpClient.editFollowupMessage(interaction, messageId, message).thenApply(response -> null);
    }

    /**
     * Submit a followup message
     * This requires that you have at least acknowledged the interaction
     *
     * @param interaction The interaction
     * @param embeds      The message content
     *
     * @return The sent message
     */
    public static CompletableFuture<Message> submitFollowupMessage(final Interaction interaction, final MessageEmbed... embeds) {
        return submitFollowupMessage(interaction, new FollowupMessage("", false, Arrays.asList(embeds)));
    }

    /**
     * Submit a followup message
     * This requires that you have at least acknowledged the interaction
     *
     * @param interaction The interaction
     * @param message     The message content
     *
     * @return The sent message
     */
    public static CompletableFuture<Message> submitFollowupMessage(final Interaction interaction, final String message) {
        return submitFollowupMessage(interaction, new FollowupMessage(message, false, new ArrayList<>()));
    }

    /**
     * Submit a followup message
     * This requires that you have at least acknowledged the interaction
     *
     * @param interaction The interaction
     * @param message     The message content
     *
     * @return The sent message
     */
    public static CompletableFuture<Message> submitFollowupMessage(final Interaction interaction, final FollowupMessage message) {
        return discordHttpClient.submitFollowupMessage(interaction, message).thenApply(response -> {
            try {
                return entityBuilder.createMessage(DataObject.fromJson(response.body().string()));
            } catch (final IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Create or update a global command.
     *
     * @param command  The command you'd like to create / update
     * @param listener The interaction listener
     *
     * @return The command id
     */
    public static CompletableFuture<Long> submitGlobalCommand(final ApplicationCommand command, final ApplicationCommandListener listener) {
        return discordHttpClient.submitGlobalCommand(command).thenApply(response -> {
            final JsonObject object;
            try {
                object = JsonParser.parseString(response.body().string()).getAsJsonObject();
            } catch (final IOException e) {
                throw new CompletionException(e);
            }

            return object.get("id").getAsLong();
        }).thenApply(commandId -> {
            commandMap.put(commandId, command);
            commandListenerMap.put(command, listener);
            return commandId;
        });
    }

    /**
     * Create or update a guild command.
     *
     * @param command  The command you'd like to create / update
     * @param guild    The guild that should have access to this command
     * @param listener The interaction listener
     *
     * @return The command id
     */
    public static CompletableFuture<Long> submitGuildCommand(final ApplicationCommand command,
                                                             final Guild guild,
                                                             final ApplicationCommandListener listener) {
        return submitGuildCommand(command, guild.getIdLong(), listener);
    }

    /**
     * Create or update a guild command.
     *
     * @param command  The command you'd like to create / update
     * @param guildId  The id of the guild that should have access to this command
     * @param listener The interaction listener
     *
     * @return The command id
     */
    public static CompletableFuture<Long> submitGuildCommand(final ApplicationCommand command,
                                                             final long guildId,
                                                             final ApplicationCommandListener listener) {
        return discordHttpClient.submitGuildCommand(command, guildId).thenApply(response -> {
            final JsonObject object;
            try {
                object = JsonParser.parseString(response.body().string()).getAsJsonObject();
            } catch (final IOException e) {
                throw new CompletionException(e);
            }

            return object.get("id").getAsLong();
        }).thenApply(commandId -> {
            commandMap.put(commandId, command);
            commandListenerMap.put(command, listener);
            return commandId;
        });
    }


    /**
     * Delete your interaction response
     *
     * @param interaction The interaction
     *
     * @return A future
     */
    public static CompletableFuture<Void> deleteInteractionResponse(final Interaction interaction) {
        return discordHttpClient.deleteInteractionResponse(interaction).thenApply(response -> null);
    }

    /**
     * Edit your interaction response
     * Will produce a 404 if you did not send a response
     *
     * @param interaction The interaction
     * @param embeds      The new message content (up to 10 embeds)
     *
     * @return A future
     *
     * @see JDASlashCommands#editInteractionResponse(Interaction, InteractionApplicationCommandCallbackData)
     */
    public static CompletableFuture<Void> editInteractionResponse(final Interaction interaction, final MessageEmbed... embeds) {
        return editInteractionResponse(interaction, new InteractionApplicationCommandCallbackData(
                false, "", Arrays.asList(embeds)
        ));
    }

    /**
     * Edit your interaction response
     * Will produce a 404 if you did not send a response
     *
     * @param interaction The interaction
     * @param message     The new message content
     *
     * @return A future
     *
     * @see JDASlashCommands#editInteractionResponse(Interaction, InteractionApplicationCommandCallbackData)
     */
    public static CompletableFuture<Void> editInteractionResponse(final Interaction interaction, final String message) {
        return editInteractionResponse(interaction, new InteractionApplicationCommandCallbackData(
                false, message, new ArrayList<>()
        ));
    }

    /**
     * Edit your interaction response
     * Will produce a 404 if you did not send a response
     *
     * @param interaction The interaction
     * @param data        The message data
     *
     * @return A future
     */
    public static CompletableFuture<Void> editInteractionResponse(final Interaction interaction,
                                                                  final InteractionApplicationCommandCallbackData data) {
        return discordHttpClient.editInteractionResponse(interaction, data).thenApply(response -> null);
    }

    /**
     * Submit a response to a interaction
     *
     * @param interaction The interaction that you want to respond to
     * @param response    Your response
     *
     * @return Nothing
     */
    public static CompletableFuture<Void> submitInteractionResponse(final Interaction interaction, final InteractionResponse response) {
        return discordHttpClient.submitInteractionResponse(interaction, response).thenApply(r -> null);
    }

    /**
     * Retrieve a global command
     *
     * @param commandId The id of the command
     *
     * @return The command
     */
    public static CompletableFuture<ApplicationCommand> getGlobalCommand(final long commandId) {
        return discordHttpClient.getGlobalCommand(commandId).thenApply(response -> {
            try {
                return discordHttpClient.getGson().fromJson(JsonParser
                        .parseString(response.body().string()), ApplicationCommand.class);
            } catch (final IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Retrieve a guild command
     *
     * @param guildId   The id of the guild
     * @param commandId The id of the command
     *
     * @return The command
     */
    public static CompletableFuture<ApplicationCommand> getGuildCommand(final long guildId, final long commandId) {
        return discordHttpClient.getGuildCommand(guildId, commandId).thenApply(response -> {
            try {
                return discordHttpClient.getGson().fromJson(JsonParser
                        .parseString(response.body().string()), ApplicationCommand.class);
            } catch (final IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Retrieve all global commands
     *
     * @return All global commands
     */
    public static CompletableFuture<List<ApplicationCommand>> getGlobalCommands() {
        return discordHttpClient.getGlobalCommands().thenApply(JDASlashCommands::parseCommands);
    }

    /**
     * Retrieve all guild commands of a specific guild
     *
     * @param guild The guild
     *
     * @return The guild commands
     */
    public static CompletableFuture<List<ApplicationCommand>> getGuildCommands(final Guild guild) {
        return getGuildCommands(guild.getIdLong());
    }

    public static CompletableFuture<List<ApplicationCommand>> getGuildCommands(final long guildId) {
        return discordHttpClient.getGuildCommands(guildId).thenApply(JDASlashCommands::parseCommands);
    }

    public static CompletableFuture<Void> deleteGlobalCommand(final long commandId) {
        return discordHttpClient.deleteGlobalCommand(commandId).thenApply(response -> null);
    }

    public static CompletableFuture<Void> deleteGuildCommand(final long guildId, final long commandId) {
        return discordHttpClient.deleteGuildCommand(commandId, guildId).thenApply(response -> null);
    }

    /**
     * Parse a response into commands
     *
     * @param response
     *
     * @return
     */
    private static List<ApplicationCommand> parseCommands(final Response response) {
        final List<ApplicationCommand> list = new ArrayList<>();
        try {
            final JsonArray jsonArray = JsonParser.parseString(response.body().string()).getAsJsonArray();
            for (final JsonElement element : jsonArray) {
                list.add(discordHttpClient.getGson().fromJson(element, ApplicationCommand.class));
            }
        } catch (final IOException e) {
            throw new CompletionException(e);
        }
        return list;
    }

    /**
     * Initialize this library
     *
     * @param jda           The JDA instance
     * @param botToken      The bot token
     * @param applicationId The bot application id
     */
    public static void initialize(final JDA jda, final String botToken, final String applicationId) {
        if (jda instanceof JDAImpl && !((JDAImpl) jda).isRawEvents()) {
            throw new IllegalStateException("Slash commands will not work without raw events! See JDABuilder#setRawEventsEnabled()");
        }

        discordHttpClient = new DiscordHttpClient(botToken, applicationId, jda);
        entityBuilder = new EntityBuilder(jda);

        jda.addEventListener(new InteractionListener());
        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onShutdown(@NotNull final ShutdownEvent event) {
                discordHttpClient.shutdown();
            }
        });
    }

    /**
     * Notify the corresponding command about a interaction
     *
     * @param interaction The interaction
     */
    public static void handleInteraction(final Interaction interaction) {
        final ApplicationCommand command = commandMap.get(interaction.getCommandId());
        final ApplicationCommandListener listener = commandListenerMap.get(command);
        listener.onInteraction(interaction);

        final InteractionResponseOption argument = findArgument(command, interaction);
        if (argument != null) {
            listener.handleArgument(interaction, argument.getName(), argument);
        }
    }

    /**
     * Walk the provided interaction options and find a command argument
     *
     * @param command     The command
     * @param interaction The interaction
     *
     * @return A argument or null
     */
    private static InteractionResponseOption findArgument(final ApplicationCommand command, final Interaction interaction) {
        final List<ApplicationCommandOption> cmdOptions = new ArrayList<>();
        walkList(cmdOptions, command.getOptions(), option -> option.getOptions() != null && option.getOptions().size() > 0, ApplicationCommandOption::getOptions);
        final Set<String> argNames = cmdOptions.stream()
                .filter(option -> option.getType() != ApplicationCommandOptionType.SUB_COMMAND
                        && option.getType() != ApplicationCommandOptionType.SUB_COMMAND_GROUP)
                .map(ApplicationCommandOption::getName)
                .collect(Collectors.toSet());

        final List<InteractionResponseOption> rspOptions = new ArrayList<>();
        walkList(rspOptions, interaction.getOptions(), option -> option.getOptions() != null && option.getOptions().size() > 0, InteractionResponseOption::getOptions);
        return rspOptions.stream()
                .filter(option -> argNames.contains(option.getName()))
                .findAny()
                .orElse(null);
    }

    private static <T> void walkList(final List<T> output, final List<T> list, final Predicate<T> predicate, final Function<T, List<T>> function) {
        for (final T item : list) {
            if (predicate.test(item)) {
                walkList(output, function.apply(item), predicate, function);
            } else {
                output.add(item);
            }
        }
    }

    /**
     * Get a command by id
     *
     * @param id The command id
     *
     * @return The command
     */
    @ApiStatus.Internal
    public static ApplicationCommand getCommand(final long id) {
        return commandMap.get(id);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @ApiStatus.Experimental
    @TestOnly
    public static DiscordHttpClient getDiscordHttpClient() {
        return discordHttpClient;
    }

}
