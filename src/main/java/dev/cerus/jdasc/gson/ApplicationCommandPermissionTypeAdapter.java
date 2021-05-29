package dev.cerus.jdasc.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.cerus.jdasc.command.permissions.ApplicationCommandPermissionType;
import java.io.IOException;

public class ApplicationCommandPermissionTypeAdapter extends TypeAdapter<ApplicationCommandPermissionType> {

    @Override
    public void write(final JsonWriter out, final ApplicationCommandPermissionType value) throws IOException {
        out.value(value.getVal());
    }

    @Override
    public ApplicationCommandPermissionType read(final JsonReader in) throws IOException {
        return ApplicationCommandPermissionType.getByVal(in.nextInt());
    }

}
