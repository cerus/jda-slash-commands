package dev.cerus.jdasc.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.cerus.jdasc.interaction.response.InteractionResponseType;
import java.io.IOException;

public class InteractionResponseTypeTypeAdapter extends TypeAdapter<InteractionResponseType> {

    @Override
    public void write(final JsonWriter out, final InteractionResponseType value) throws IOException {
        out.value(value.getVal());
    }

    @Override
    public InteractionResponseType read(final JsonReader in) throws IOException {
        return InteractionResponseType.getByVal(in.nextInt());
    }

}
