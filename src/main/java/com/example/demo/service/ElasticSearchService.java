package com.example.demo.service;

import com.example.demo.entity.TextEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchService {

    private final ElasticsearchOperations operations;

    public String search(String query) {
        return Objects.requireNonNull(operations.searchOne(new NativeSearchQueryBuilder()
                .withQuery(matchQuery("query", query)
                        .analyzer("main_analyzer")
                        .fuzziness(Fuzziness.ONE)
                        .minimumShouldMatch("-50%"))
                .build(), TextEntity.class)).getContent().getQuery();
    }

    public void save(String text) {
        operations.save(new TextEntity(UUID.randomUUID(), text));
    }

}
