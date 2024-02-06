package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.InputItemRequestDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {

  private static final String API_PREFIX = "/requests";

  @Autowired
  public RequestClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
    super(
            builder
                    .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                    .build()
    );
  }

  public ResponseEntity<Object> addRequest(int userId, InputItemRequestDto input) {
    return post("", userId, input);

  }

  public ResponseEntity<Object> getUserRequests(int userId) {
    return get("", userId);
  }

  public ResponseEntity<Object> getRequest(int requestId, int userId) {
    return get("/{requestId}", userId, Map.of("requestId", requestId));
  }

  public ResponseEntity<Object> getOtherUserRequests(int userId, Integer from, Integer size) {
    return get("/all?from={from}&size={size}",userId, Map.of("from", from, "size", size));
  }
}
