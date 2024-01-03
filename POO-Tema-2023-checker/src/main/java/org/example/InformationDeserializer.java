package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class InformationDeserializer extends StdDeserializer<User.Information>{

    public InformationDeserializer(){
        this(null);
    }

    public InformationDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public User.Information deserialize(JsonParser jp,
                                        DeserializationContext context)
            throws IOException{
        JsonNode node = jp.getCodec().readTree(jp);
        User.Information.InformationBuilder builder =
                new User.Information.InformationBuilder(
                new Credentials(node.get("credentials").get("email").asText(),
                        node.get("credentials").get("password").asText()),
                node.get("name").asText())
                .country(node.get("country").asText())
                .age((short) node.get("age").asInt())
                .gender(node.get("gender").asText().charAt(0))
                .birthDate(node.get("birthDate").asText());

        return builder.build();
    }
}
