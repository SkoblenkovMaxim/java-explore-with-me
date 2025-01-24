package ru.practicum.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import ru.practicum.StatsClient;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HitsEventViewUtil {

    private ObjectMapper objectMapper;

    public Long getHitsEvent(Long eventId, LocalDateTime start, LocalDateTime end, Boolean unique, StatsClient statsClient) {

        List<String> uris = new ArrayList<>();
        uris.add("/events/" + eventId);

        ResponseEntity<Object> response = statsClient.getStats(start, end, uris, unique);

        Object responseBody = response.getBody();

        List<ViewStatsDto> output = null;

        if (responseBody != null) {
            output = objectMapper.convertValue(responseBody, new TypeReference<List<ViewStatsDto>>() {});
        }

        Long view = 0L;

        if (!output.isEmpty()) {
            view = output.get(0).getHits();
        }
        return view;

    }
}
