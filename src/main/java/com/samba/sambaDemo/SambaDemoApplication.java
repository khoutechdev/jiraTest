package com.samba.sambaDemo;

import com.samba.sambaDemo.config.ApplicationProperties;
import com.samba.sambaDemo.service.BoardService;
import com.samba.sambaDemo.service.ProjectService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class SambaDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SambaDemoApplication.class, args);
	}

}

@Component
class InitData implements CommandLineRunner {

	private final ProjectService projectsService;
	private final BoardService boardService;

	InitData(ProjectService projectsService, BoardService boardService) {
		this.projectsService = projectsService;
		this.boardService = boardService;
	}

	@Override
	public void run(String... args) {
		projectsService
				.allProjects()
				.flatMap(projectsService::save)
				.flatMap(projects -> boardService.allBoardByProjects(projects.getId()).flatMap(board -> boardService.save(board.toBuilder().project(projects).build())))
				.then(Mono.just(true))
				.subscribe(System.out::println);
	}

}
