package dev.cerus.jdasc.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.cerus.jdasc.components.Button;
import dev.cerus.jdasc.components.ComponentType;
import dev.cerus.jdasc.interaction.response.InteractionResponse;
import java.io.IOException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class InteractionResponseTypeAdapter extends TypeAdapter<InteractionResponse> {

    private final JDA jda;

    public InteractionResponseTypeAdapter(final JDA jda) {
        this.jda = jda;
    }

    @Override
    public void write(final JsonWriter out, final InteractionResponse value) throws IOException {
        out.beginObject()
                .name("type")
                .value(value.getType().getVal());

        if (!value.getType().name().startsWith("ACK")) {
            out.name("data")
                    .jsonValue(new GsonBuilder()
                            .registerTypeAdapter(MessageEmbed.class, new MessageEmbedTypeAdapter(this.jda))
                            .registerTypeAdapter(ComponentType.class, new ComponentTypeTypeAdapter())
                            .registerTypeAdapter(Button.Style.class, new StyleTypeAdapter())
                            .create().toJson(value.getData()));
        }
        out.endObject();
    }

    @Override
    public InteractionResponse read(final JsonReader in) throws IOException {
        return null;
    }

}
