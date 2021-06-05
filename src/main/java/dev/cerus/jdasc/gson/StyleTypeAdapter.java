package dev.cerus.jdasc.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.cerus.jdasc.components.Button;
import java.io.IOException;

public class StyleTypeAdapter extends TypeAdapter<Button.Style> {

    @Override
    public void write(final JsonWriter out, final Button.Style value) throws IOException {
        out.value(value.getVal());
    }

    @Override
    public Button.Style read(final JsonReader in) throws IOException {
        return Button.Style.getByVal(in.nextInt());
    }

}
