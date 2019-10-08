package com.samba.sambaDemo.service;

import com.samba.sambaDemo.domain.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {

    Flux<Project> allProjects();

    Mono<Project> save(Project project);
}
