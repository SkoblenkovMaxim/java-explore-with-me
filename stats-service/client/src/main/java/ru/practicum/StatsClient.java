package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.exception.DateBadRequestException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsClient extends BaseClient {

    private static final String API_PREFIX = "/";

    @Autowired
    public StatsClient(@Value("${stats.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(LocalDateTime start,
                                           LocalDateTime end,
                                           List<String> uris,
                                           Boolean unique
    ) {
        if (start.isAfter(end)) {
            throw new DateBadRequestException("Start date cannot be after end date");
        }

        String urisStr = uris.stream().map(uri -> "&uris=" + uri).collect(Collectors.joining());
        Map<String, Object> params = new HashMap<>();
        params.put("start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.put("end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.put("unique", unique);
        return get("/stats?start={start}&end={end}" + urisStr + "&unique={unique}", params);
    }

    public ResponseEntity<Object> hit(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

}
