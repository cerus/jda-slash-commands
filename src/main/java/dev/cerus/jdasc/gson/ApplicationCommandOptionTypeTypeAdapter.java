package dev.cerus.jdasc.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.cerus.jdasc.command.ApplicationCommandOptionType;
import java.io.IOException;

public class ApplicationCommandOptionTypeTypeAdapter extends TypeAdapter<ApplicationCommandOptionType> {

    @Override
    public void write(final JsonWriter out, final ApplicationCommandOptionType value) throws IOException {
        out.value(value.getVal());
    }

    @Override
    public ApplicationCommandOptionType read(final JsonReader in) throws IOException {
        return ApplicationCommandOptionType.getByVal(in.nextInt());
    }

}
