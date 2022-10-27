package com.lyriqu.monitorservice.repository;

import com.lyriqu.monitorservice.entity.Monitor;
import com.lyriqu.monitorservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, UUID> {
    List<Monitor> findAllByStatus(Status status);
}
