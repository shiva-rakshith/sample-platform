package org.sample.controllers;

import org.sample.dto.Response;
import org.sample.managers.HealthCheckManager;
import org.sample.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping(Constants.VERSION_PREFIX)
public class APIController extends BaseController {

    @PostMapping(Constants.SAMPLE_API)
    public ResponseEntity<Object> api(@RequestBody Map<String, Object> requestBody) {
        try {
            processRequest(requestBody);
            Response response = new Response();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
