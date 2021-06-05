package dev.cerus.jdasc.listener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.cerus.jdasc.JDASlashCommands;
import dev.cerus.jdasc.components.ActionRow;
import dev.cerus.jdasc.components.Button;
import dev.cerus.jdasc.components.Component;
import dev.cerus.jdasc.components.ComponentType;
import dev.cerus.jdasc.event.CommandInteractionEvent;
import dev.cerus.jdasc.interaction.Interaction;
import dev.cerus.jdasc.interaction.InteractionType;
import dev.cerus.jdasc.interaction.response.InteractionResponseOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.RawGatewayEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

public class InteractionListener extends ListenerAdapter {

    @Override
    public void onRawGateway(@NotNull final RawGatewayEvent event) {
        if (event.getType().equals("INTERACTION_CREATE")) {
            final DataObject payload = event.getPayload();
            final JsonObject object = JsonParser.parseString(payload.toString()).getAsJsonObject();
            final InteractionType type = InteractionType.getByVal(object.get("type").getAsInt());

            switch (type) {
                case PING:
                    // Noop
                    break;
                case APPLICATION_COMMAND:
                    this.handleCommandInteraction(event, object);
                    break;
                case MESSAGE_COMPONENT:
                    this.handleMessageComponent(event, object);
                    break;
            }
        }
    }

    private void handleMessageComponent(final RawGatewayEvent event, final JsonObject object) {
        final JsonObject messageObject = object.get("message").getAsJsonObject();
        final JsonObject dataObject = object.get("data").getAsJsonObject();
        final long id = object.get("id").getAsLong();
        final String token = object.get("token").getAsString();

        final JsonArray componentsArray = messageObject.get("components").getAsJsonArray();
        final List<Component> components = this.parseComponents(componentsArray);
        final Component clickedComponent = components.stream()
                .flatMap(component -> {
                    switch (component.getType()) {
                        case ACTION_ROW:
                            return ((ActionRow) component).getComponents().stream();
                        case BUTTON:
                            return Arrays.stream(new Component[] {component});
                        default:
                            return null;
                    }
                })
                .filter(o -> o instanceof Button)
                .map(component -> (Button) component)
                .filter(button -> button.getCustomId() != null)
                .filter(button -> button.getCustomId().equals(dataObject.get("custom_id").getAsString()))
                .findAny()
                .orElse(null);

        this.broadcastInteraction(InteractionType.MESSAGE_COMPONENT,
                event,
                object,
                token,
                id,
                -1,
                null,
                Collections.emptyList(),
                components,
                clickedComponent);
    }

    private void handleCommandInteraction(@NotNull final RawGatewayEvent event, final JsonObject object) {
        final InteractionType type = InteractionType.APPLICATION_COMMAND;
        final JsonObject dataObject = object.get("data").getAsJsonObject();

        final String token = object.get("token").getAsString();
        final String commandName = dataObject.get("name").getAsString();
        final long commandId = dataObject.get("id").getAsLong();
        final long id = object.get("id").getAsLong();

        List<InteractionResponseOption> options = null;
        if (dataObject.has("options")) {
            options = new ArrayList<>();
            for (final JsonElement element : dataObject.get("options").getAsJsonArray()) {
                options.add(new Gson().fromJson(element, InteractionResponseOption.class));
            }
        }

        this.broadcastInteraction(type,
                event,
                object,
                token,
                id,
                commandId,
                commandName,
                options,
                Collections.emptyList(),
                null);
    }

    private void broadcastInteraction(final InteractionType type,
                                      final RawGatewayEvent event,
                                      final JsonObject object,
                                      final String token,
                                      final long id,
                                      final long commandId,
                                      final String commandName,
                                      final List<InteractionResponseOption> options,
                                      final List<Component> components,
                                      final Component clickedComponent) {
        final JDA jda = event.getJDA();
        if (object.has("guild_id")) {
            final Guild guild = jda.getGuildById(object.get("guild_id").getAsString());
            final Member member = guild.retrieveMemberById(object.get("member").getAsJsonObject().get("user").getAsJsonObject().get("id").getAsString()).complete();
            final TextChannel textChannel = guild.getTextChannelById(object.get("channel_id").getAsString());
            final Interaction interaction = new Interaction(token,
                    id,
                    type,
                    commandId,
                    commandName,
                    guild,
                    textChannel,
                    member,
                    options,
                    components,
                    clickedComponent);
            jda.getEventManager().handle(new CommandInteractionEvent(jda, interaction));
        } else {
            //Direct messages
            final PrivateChannel textChannel = jda.getPrivateChannelById(object.get("channel_id").getAsString());
            final Interaction interaction = new Interaction(token,
                    id,
                    type,
                    commandId,
                    commandName,
                    null,
                    textChannel,
                    null,
                    options,
                    components,
                    clickedComponent);
            jda.getEventManager().handle(new CommandInteractionEvent(jda, interaction));
        }
    }

    private List<Component> parseComponents(final JsonArray componentsArray) {
        final List<Component> components = new ArrayList<>();
        componentsArray.forEach(jsonElement -> {
            final JsonObject compObj = jsonElement.getAsJsonObject();
            final ComponentType componentType = ComponentType.getByVal(compObj.get("type").getAsInt());
            switch (componentType) {
                case ACTION_ROW:
                    components.add(new ActionRow(this.parseComponents(compObj.get("components").getAsJsonArray())));
                    break;
                case BUTTON:
                    components.add(new Button(
                            Button.Style.getByVal(compObj.get("style").getAsInt()),
                            compObj.get("label").getAsString(),
                            compObj.has("emoji")
                                    ? new Button.PartialEmoji(compObj.get("emoji").getAsJsonObject())
                                    : null,
                            compObj.has("custom_id")
                                    ? compObj.get("custom_id").getAsString()
                                    : null,
                            compObj.has("url")
                                    ? compObj.get("url").getAsString()
                                    : null,
                            compObj.has("disabled") && compObj.get("disabled").getAsBoolean()
                    ));
                    break;
            }
        });
        return components;
    }

    @Override
    public void onGenericEvent(@NotNull final GenericEvent event) {
        if (!(event instanceof CommandInteractionEvent)) {
            return;
        }

        final CommandInteractionEvent interactionEvent = (CommandInteractionEvent) event;
        final Interaction interaction = interactionEvent.getInteraction();
        JDASlashCommands.handleInteraction(interaction);
    }

}
