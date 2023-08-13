package org.sample.controllers;

import org.sample.managers.HealthCheckManager;
import org.sample.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(Constants.VERSION_PREFIX)
public class HealthController {

    @Autowired
    private HealthCheckManager healthCheckManager;

    @GetMapping(Constants.HEALTH)
    public ResponseEntity<Object> health() {
        return new ResponseEntity<>(healthCheckManager.checkAllSystemHealth(), HttpStatus.OK);
    }

}
