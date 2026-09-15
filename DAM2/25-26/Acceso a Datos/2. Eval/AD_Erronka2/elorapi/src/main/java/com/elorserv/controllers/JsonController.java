package com.elorserv.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/getCenterList")
public class JsonController {

    private List<String> centerList = new ArrayList<>();

    private final String JSON_URL = "http://localhost/ikastetxeak.json";

    @GetMapping
    public String getCenterList(){
		RestTemplate restTemplate = new RestTemplate();
    	return restTemplate.getForObject(JSON_URL, String.class);
    }
}