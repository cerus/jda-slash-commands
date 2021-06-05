package dev.cerus.jdasc.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.cerus.jdasc.components.ComponentType;
import java.io.IOException;

public class ComponentTypeTypeAdapter extends TypeAdapter<ComponentType> {

    @Override
    public void write(final JsonWriter out, final ComponentType value) throws IOException {
        out.value(value.getVal());
    }

    @Override
    public ComponentType read(final JsonReader in) throws IOException {
        return ComponentType.getByVal(in.nextInt());
    }

}
