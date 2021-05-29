package dev.cerus.jdasc.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.IOException;

public class GsonUtil {

    private GsonUtil() {
    }

    public static JsonObject readAllAsObject(final JsonReader reader) throws IOException {
        final JsonObject jsonObject = new JsonObject();
        String name = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.peek()) {
                case BEGIN_ARRAY:
                    jsonObject.add(name, readAllAsArray(reader));
                    break;
                case END_ARRAY:
                    reader.endArray();
                    break;
                case BEGIN_OBJECT:
                    jsonObject.add(name, readAllAsObject(reader));
                    break;
                case END_OBJECT:
                    reader.endObject();
                    break;
                case NAME:
                    name = reader.nextName();
                    break;
                case STRING:
                    jsonObject.addProperty(name, reader.nextString());
                    break;
                case NUMBER:
                    try {
                        jsonObject.addProperty(name, reader.nextInt());
                    } catch (final NumberFormatException e1) {
                        try {
                            jsonObject.addProperty(name, reader.nextLong());
                        } catch (final NumberFormatException e2) {
                            try {
                                jsonObject.addProperty(name, reader.nextDouble());
                            } catch (final NumberFormatException e3) {
                                reader.skipValue();
                            }
                        }
                    }
                    break;
                case BOOLEAN:
                    jsonObject.addProperty(name, reader.nextBoolean());
                    break;
                case NULL:
                    reader.nextNull();
                    jsonObject.addProperty(name, (String) null);
                    break;
                default:
                case END_DOCUMENT:
                    return jsonObject;
            }
        }
        return jsonObject;
    }

    private static JsonArray readAllAsArray(final JsonReader reader) throws IOException {
        final JsonArray array = new JsonArray();

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.peek()) {
                case BEGIN_ARRAY:
                    array.add(readAllAsArray(reader));
                    break;
                case END_ARRAY:
                    reader.endArray();
                    break;
                case BEGIN_OBJECT:
                    array.add(readAllAsObject(reader));
                    break;
                case END_OBJECT:
                    reader.endObject();
                    break;
                case NAME:
                    reader.nextName();
                    break;
                case STRING:
                    array.add(reader.nextString());
                    break;
                case NUMBER:
                    try {
                        array.add(reader.nextInt());
                    } catch (final NumberFormatException e1) {
                        try {
                            array.add(reader.nextLong());
                        } catch (final NumberFormatException e2) {
                            try {
                                array.add(reader.nextDouble());
                            } catch (final NumberFormatException e3) {
                                reader.skipValue();
                            }
                        }
                    }
                    break;
                case BOOLEAN:
                    array.add(reader.nextBoolean());
                    break;
                case NULL:
                    reader.nextNull();
                    break;
                default:
                case END_DOCUMENT:
                    return array;
            }
        }
        return array;
    }

}
