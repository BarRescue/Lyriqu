package com.lyriqu.monitorservice.service;

import com.lyriqu.monitorservice.entity.Monitor;
import com.lyriqu.monitorservice.enums.Status;
import com.lyriqu.monitorservice.repository.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MonitorService {

    private final MonitorRepository monitorRepository;

    @Autowired
    public MonitorService(MonitorRepository repository) {
        this.monitorRepository = repository;
    }

    public Monitor createOrUpdate(Monitor monitor) {
        return this.monitorRepository.save(monitor);
    }

    public List<Monitor> findAllByStatus(Status status) {
        return this.monitorRepository.findAllByStatus(status);
    }

    public Optional<Monitor> findById(UUID id) {
        return this.monitorRepository.findById(id);
    }
}
