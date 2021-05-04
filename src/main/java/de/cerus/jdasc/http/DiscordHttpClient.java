package de.cerus.jdasc.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.cerus.jdasc.command.ApplicationCommand;
import de.cerus.jdasc.command.ApplicationCommandOptionType;
import de.cerus.jdasc.command.permissions.ApplicationCommandPermissionType;
import de.cerus.jdasc.command.permissions.ApplicationCommandPermissions;
import de.cerus.jdasc.gson.ApplicationCommandOptionTypeTypeAdapter;
import de.cerus.jdasc.gson.ApplicationCommandPermissionTypeAdapter;
import de.cerus.jdasc.gson.InteractionResponseTypeAdapter;
import de.cerus.jdasc.gson.InteractionResponseTypeTypeAdapter;
import de.cerus.jdasc.gson.MessageEmbedTypeAdapter;
import de.cerus.jdasc.interaction.Interaction;
import de.cerus.jdasc.interaction.followup.FollowupMessage;
import de.cerus.jdasc.interaction.response.InteractionApplicationCommandCallbackData;
import de.cerus.jdasc.interaction.response.InteractionResponse;
import de.cerus.jdasc.interaction.response.InteractionResponseType;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiscordHttpClient {

    public final String applicationId;
    private final Gson gson;
    private final String botToken;
    private final ExecutorService executorService;
    private final OkHttpClient httpClient;

    public DiscordHttpClient(final String botToken, final String applicationId, final JDA jda) {
        this(Executors.newSingleThreadExecutor(), botToken, applicationId, jda);
    }

    public DiscordHttpClient(final ExecutorService executorService, final String botToken, final String applicationId, final JDA jda) {
        this(jda, applicationId, botToken, executorService, new OkHttpClient());
    }

    public DiscordHttpClient(final OkHttpClient httpClient, final String botToken, final String applicationId, final JDA jda) {
        this(jda, applicationId, botToken, Executors.newSingleThreadExecutor(), httpClient);
    }

    public DiscordHttpClient(final JDA jda, final String applicationId, final String botToken, final ExecutorService executorService, final OkHttpClient httpClient) {
        this.applicationId = applicationId;
        this.botToken = botToken;
        this.executorService = executorService;
        this.httpClient = httpClient;

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(InteractionResponseType.class, new InteractionResponseTypeTypeAdapter())
                .registerTypeAdapter(ApplicationCommandOptionType.class, new ApplicationCommandOptionTypeTypeAdapter())
                .registerTypeAdapter(ApplicationCommandPermissionType.class, new ApplicationCommandPermissionTypeAdapter())
                .registerTypeAdapter(MessageEmbed.class, new MessageEmbedTypeAdapter(jda))
                .registerTypeAdapter(InteractionResponse.class, new InteractionResponseTypeAdapter(jda))
                .create();
    }

    public CompletableFuture<Response> deleteFollowupMessage(final Interaction interaction, final long messageId) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/webhooks/%s/%s/messages/%d", this.applicationId, interaction.getToken(), messageId))
                .delete()
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 204);
    }

    public CompletableFuture<Response> editFollowupMessage(final Interaction interaction, final long messageId, final FollowupMessage message) {
        final JsonObject jsonObject = this.gson.toJsonTree(message).getAsJsonObject();
        jsonObject.remove("tts");
        jsonObject.remove("username");
        jsonObject.remove("avatar_url");
        final String body = jsonObject.toString();

        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/webhooks/%s/%s/messages/%d", this.applicationId, interaction.getToken(), messageId))
                .patch(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body))
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 204);
    }

    public CompletableFuture<Response> submitFollowupMessage(final Interaction interaction, final FollowupMessage message) {
        final String body = this.gson.toJson(message);
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/webhooks/%s/%s", this.applicationId, interaction.getToken()))
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body))
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 204, 201);
    }

    public CompletableFuture<Response> deleteInteractionResponse(final Interaction interaction) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/webhooks/%s/%s/messages/@original", this.applicationId, interaction.getToken()))
                .delete()
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 204);
    }

    public CompletableFuture<Response> editInteractionResponse(final Interaction interaction, final InteractionApplicationCommandCallbackData data) {
        final JsonObject payload = this.gson.toJsonTree(data).getAsJsonObject();
        payload.remove("tts");
        final String body = payload.toString();
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/webhooks/%s/%s/messages/@original", this.applicationId, interaction.getToken()))
                .patch(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body))
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 204);
    }

    public CompletableFuture<Response> submitInteractionResponse(final Interaction interaction, final InteractionResponse response) {
        final String body = this.gson.toJson(response);
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/interactions/%d/%s/callback", interaction.getId(), interaction.getToken()))
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body))
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 204);
    }

    public CompletableFuture<Response> submitGlobalCommand(final ApplicationCommand command) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/applications/%s/commands", this.applicationId))
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), this.gson.toJson(command)))
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 201);
    }

    public CompletableFuture<Response> deleteGlobalCommand(final long commandId) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/applications/%s/commands/%d", this.applicationId, commandId))
                .delete()
                .build(), 204);
    }

    public CompletableFuture<Response> submitGuildCommand(final ApplicationCommand command, final long guildId) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/applications/%s/guilds/%d/commands", this.applicationId, guildId))
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), this.gson.toJson(command)))
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 201);
    }

    public CompletableFuture<Response> deleteGuildCommand(final long commandId, final long guildId) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/applications/%s/guilds/%d/commands/%d", this.applicationId, guildId, commandId))
                .delete()
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 204);
    }

    public CompletableFuture<Response> getGlobalCommand(final long commandId) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/applications/%s/commands/%d", this.applicationId, commandId))
                .get()
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200);
    }

    public CompletableFuture<Response> getGlobalCommands() {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/applications/%s/commands", this.applicationId))
                .get()
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200);
    }

    public CompletableFuture<Response> getGuildCommand(final long guildId, final long commandId) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/applications/%s/guilds/%d/commands/%d", this.applicationId, guildId, commandId))
                .get()
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200);
    }

    public CompletableFuture<Response> getGuildCommands(final long guildId) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v8/applications/%s/guilds/%d/commands", this.applicationId, guildId))
                .get()
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200);
    }

    public CompletableFuture<Response> getApplicationCommandPermissions(final long guildId, final long commandId) {
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v9/applications/%s/guilds/%d/commands/%d/permissions", this.applicationId, guildId, commandId))
                .get()
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 404);
    }

    public CompletableFuture<Response> editApplicationCommandPermissions(final long guildId, final long commandId, final List<ApplicationCommandPermissions> permissions) {
        final JsonArray jsonArray = new JsonArray();
        final JsonObject object = new JsonObject();
        object.addProperty("id", commandId);
        object.add("permissions", this.gson.toJsonTree(permissions));
        jsonArray.add(object);
        final String body = jsonArray.toString();
        return this.execute(new Request.Builder()
                .url(String.format("https://discord.com/api/v9/applications/%s/guilds/%d/commands/permissions", this.applicationId, guildId, commandId))
                .put(RequestBody.create(MediaType.get("application/json; charset=utf-8"), body))
                .addHeader("Authorization", "Bot " + this.botToken)
                .build(), 200, 204, 400);
    }

    private CompletableFuture<Response> execute(final Request request, final int... expectedCodes) {
        final CompletableFuture<Response> future = new CompletableFuture<>();
        this.executorService.submit(() -> {
            try (final Response response = this.httpClient.newCall(request).execute()) {
                final int code = response.code();
                if (Arrays.stream(expectedCodes).noneMatch(i -> i == code)) {
                    future.completeExceptionally(new DiscordApiException(expectedCodes, request, response));
                    return;
                }
                future.complete(response);
            } catch (final IOException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public Gson getGson() {
        return this.gson;
    }

    public void shutdown() {
        this.executorService.shutdown();
    }

}
