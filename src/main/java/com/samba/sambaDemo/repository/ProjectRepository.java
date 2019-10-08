package com.samba.sambaDemo.repository;

import com.samba.sambaDemo.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
