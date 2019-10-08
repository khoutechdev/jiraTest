package com.samba.sambaDemo.service.impl;

import com.samba.sambaDemo.config.ApplicationProperties;
import com.samba.sambaDemo.domain.Project;
import com.samba.sambaDemo.repository.ProjectRepository;
import com.samba.sambaDemo.service.ProjectService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final WebClient webClient;
    private final ApplicationProperties applicationProperties;

    public ProjectServiceImpl(ProjectRepository projectRepository, ApplicationProperties applicationProperties) {
        this.projectRepository = projectRepository;
        this.applicationProperties = applicationProperties;
        this.webClient = WebClient.builder()
                .baseUrl(this.applicationProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeaders(header -> header.set(HttpHeaders.AUTHORIZATION, this.applicationProperties.getToken()))
                .build();
    }

    @Override
    public Flux<Project> allProjects() {
        return webClient.get()
                .uri("/rest/api/2/project")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Project.class));
    }

    @Override
    public Mono<Project> save(Project project) {
        projectRepository.saveAndFlush(project);
        return Mono.just(project);
    }
}
