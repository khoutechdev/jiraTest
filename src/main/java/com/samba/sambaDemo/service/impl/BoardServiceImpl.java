package com.samba.sambaDemo.service.impl;

import com.samba.sambaDemo.config.ApplicationProperties;
import com.samba.sambaDemo.domain.Board;
import com.samba.sambaDemo.repository.BoardRepository;
import com.samba.sambaDemo.service.BoardService;
import com.samba.sambaDemo.service.dto.BoardProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final ApplicationProperties applicationProperties;

    private WebClient webClient;

    public BoardServiceImpl(BoardRepository boardRepository, ApplicationProperties applicationProperties) {
        this.boardRepository = boardRepository;
        this.applicationProperties = applicationProperties;
        this.webClient = WebClient.builder()
                .baseUrl(this.applicationProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeaders(header -> header.set(HttpHeaders.AUTHORIZATION, this.applicationProperties.getToken()))
                .build();
    }

    @Override
    public Flux<Board> allBoardByProjects(String id) {
        Mono<BoardProcess> response = webClient.get()
                .uri("/rest/agile/1.0/board?type=scrum&projectKeyOrId="+id)
                .retrieve()
                .bodyToMono(BoardProcess.class);
        response.subscribe(boardProcess -> log.info("BOARD {} ", boardProcess));

        return response.map(BoardProcess::getValues).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Board> save(Board board) {
        boardRepository.saveAndFlush(board);
        return Mono.just(board);
    }
}
