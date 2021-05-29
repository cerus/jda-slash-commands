package dev.cerus.jdasc.listener;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.cerus.jdasc.JDASlashCommands;
import dev.cerus.jdasc.event.CommandInteractionEvent;
import dev.cerus.jdasc.interaction.Interaction;
import dev.cerus.jdasc.interaction.InteractionType;
import dev.cerus.jdasc.interaction.response.InteractionResponseOption;
import java.util.ArrayList;
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
            final JsonObject dataObject = object.get("data").getAsJsonObject();

            final String token = object.get("token").getAsString();
            final String commandName = dataObject.get("name").getAsString();
            final long commandId = dataObject.get("id").getAsLong();
            final long id = object.get("id").getAsLong();
            final InteractionType type = InteractionType.getByVal(object.get("type").getAsInt());

            List<InteractionResponseOption> options = null;
            if (dataObject.has("options")) {
                options = new ArrayList<>();
                for (final JsonElement element : dataObject.get("options").getAsJsonArray()) {
                    options.add(new Gson().fromJson(element, InteractionResponseOption.class));
                }
            }

            final JDA jda = event.getJDA();
            if (object.has("guild_id")) {
                final Guild guild = jda.getGuildById(object.get("guild_id").getAsString());
                final Member member = guild.retrieveMemberById(object.get("member").getAsJsonObject().get("user").getAsJsonObject().get("id").getAsString()).complete();
                final TextChannel textChannel = guild.getTextChannelById(object.get("channel_id").getAsString());
                final Interaction interaction = new Interaction(token, id, type, commandId, commandName, guild, textChannel, member, options);
                jda.getEventManager().handle(new CommandInteractionEvent(jda, interaction));
            }
            //Direct messages
            else {
                final PrivateChannel textChannel = jda.getPrivateChannelById(object.get("channel_id").getAsString());
                final Interaction interaction = new Interaction(token, id, type, commandId, commandName, null, textChannel, null, options);
                jda.getEventManager().handle(new CommandInteractionEvent(jda, interaction));
            }

        }
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
