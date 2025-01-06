package io.camunda.example;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import io.camunda.example.model.request.PostgreSQLRequest;
import io.camunda.example.model.request.PostgreSQLRequestData; 
 
public class PostgreSQLRequestDeserializer implements JsonDeserializer<PostgreSQLRequest<? extends PostgreSQLRequestData>> { 
     
  private String typeElementName; 
    private Gson gson; 
    private Map<String, Class<? extends PostgreSQLRequestData>> typeRegistry; 

    public PostgreSQLRequestDeserializer(String typeElementName) { 
      this.typeElementName = typeElementName; 
      this.gson = new Gson(); 
      this.typeRegistry = new HashMap<>(); 
    } 

    @SuppressWarnings("unchecked") 
    private static TypeToken<PostgreSQLRequest<? extends PostgreSQLRequestData>> getTypeToken( 
        Class<? extends PostgreSQLRequestData> requestDataClass) { 
      return (TypeToken<PostgreSQLRequest<? extends PostgreSQLRequestData>>) 
          TypeToken.getParameterized(PostgreSQLRequest.class, requestDataClass); 
    } 

    public PostgreSQLRequestDeserializer registerType( 
        String typeName, Class<? extends PostgreSQLRequestData> requestType) { 
      typeRegistry.put(typeName, requestType); 
      return this; 
    } 

    @Override 
    public PostgreSQLRequest<? extends PostgreSQLRequestData> deserialize( 
        JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) 
        throws JsonParseException { 
      return getTypeElementValue(jsonElement) 
          .map(typeRegistry::get) 
          .map(PostgreSQLRequestDeserializer::getTypeToken) 
          .map(typeToken -> getPostgreSQLRequest(jsonElement, typeToken)) 
          .orElse(null); 
    } 

    private Optional<String> getTypeElementValue(JsonElement jsonElement) { 
      JsonObject asJsonObject = jsonElement.getAsJsonObject(); 
      JsonElement element = asJsonObject.get(typeElementName); 
      return Optional.ofNullable(element).map(JsonElement::getAsString); 
    } 

    private PostgreSQLRequest<? extends PostgreSQLRequestData> getPostgreSQLRequest( 
        JsonElement jsonElement, 
        TypeToken<PostgreSQLRequest<? extends PostgreSQLRequestData>> typeToken) { 
      return gson.fromJson(jsonElement, typeToken.getType()); 
    } 

} 