package de.cerus.jdasc.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.entities.EntityBuilder;

public class MessageEmbedTypeAdapter extends TypeAdapter<MessageEmbed> {

    private final EntityBuilder entityBuilder;

    public MessageEmbedTypeAdapter(final JDA jda) {
        this.entityBuilder = new EntityBuilder(jda);
    }

    @Override
    public void write(final JsonWriter out, final MessageEmbed value) throws IOException {
        out.jsonValue(value.toData().toString());
    }

    @Override
    public MessageEmbed read(final JsonReader in) throws IOException {
        return this.entityBuilder.createMessageEmbed(DataObject.fromJson(GsonUtil.readAllAsObject(in).toString()));
    }

}
