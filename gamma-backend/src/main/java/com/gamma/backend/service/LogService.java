package com.gamma.backend.service;

import com.gamma.backend.model.Log;
import com.gamma.backend.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void addLog(String level, String message) {
        Log log = new Log();
        log.setLevel(level);
        log.setMessage(message);
        logRepository.save(log);
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }
}