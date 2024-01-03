package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GenresDeserializer extends StdDeserializer<ArrayList<Genre>> {

    public GenresDeserializer() {
        this(null);
    }

    public GenresDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ArrayList<Genre> deserialize(JsonParser jp,
                                        DeserializationContext context)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        Iterator<JsonNode> elements = node.elements();
        ArrayList<Genre> genres = new ArrayList<>();

        while (elements.hasNext()) {
            JsonNode genreNode = elements.next();
            genres.add(Genre.valueOf(genreNode.asText().toUpperCase()));
        }

        return genres;
    }
}
