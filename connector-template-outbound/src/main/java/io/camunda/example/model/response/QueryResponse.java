package io.camunda.example.model.response;

import java.util.Objects;

public class QueryResponse<T> implements PostgreSQLResponse {
    private final T response;
 
      public QueryResponse(T response) {
        this.response = response;
      }
 
      public T getResponse() {
        return response;
      }
 
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryResponse<?> that = (QueryResponse<?>) o;
        return Objects.equals(response, that.response);
      }
 
      @Override
      public int hashCode() {
        return Objects.hash(response);
      }
 
      @Override
      public String toString() {
        return "QueryResponse{" + "response=" + response + '}';
      }
 
}
