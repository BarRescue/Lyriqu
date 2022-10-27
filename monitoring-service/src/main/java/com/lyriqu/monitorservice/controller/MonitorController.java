package com.lyriqu.monitorservice.controller;

import com.lyriqu.monitorservice.logic.MonitorLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitoring")
public class MonitorController {

    private MonitorLogic monitorLogic;

    @Autowired
    public MonitorController(MonitorLogic monitorLogic) {
        this.monitorLogic = monitorLogic;
    }

    @GetMapping
    public ResponseEntity getAllWaitingEntries() {
        try {
            return ResponseEntity.ok(this.monitorLogic.getAllWaitingEntries());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getEntryInfo(@PathVariable String id) {
        try {
            return ResponseEntity.ok(this.monitorLogic.getMusicInfo(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity approveEntry(@PathVariable String id) {
        try {
            return ResponseEntity.ok().body(this.monitorLogic.approveEntry(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/deny")
    public ResponseEntity denyEntry(@PathVariable String id, @RequestBody String reason) {
        try {
            return ResponseEntity.ok().body(this.monitorLogic.denyEntry(id, reason));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
