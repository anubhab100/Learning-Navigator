package com.crio.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EasterEggService {

    private final String NUMBERS_API_URL = "http://numbersapi.com/";

    public String getNumberFact(int number) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(NUMBERS_API_URL + number, String.class);
    }
}
