package com.szymon.webclient;

import com.szymon.exceptionHandler.ExampleNotFoundException;
import com.szymon.model.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
public class WebClientExampleController {

    Logger logger = LoggerFactory.getLogger(WebClientExampleController.class);

    private final WebClient webClient;

    @Autowired
    public WebClientExampleController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping(value = "/examples")
    public List<Example> getAllExamples() {
//        WebClient client1 = WebClient.create("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");

        logger.trace("Lipa");

        List<Example> result = webClient.get()
                .uri("http://localhost:8888/examples")
                .retrieve()
                .bodyToFlux(Example.class)
                .collectList()
                .block();

        return result;
    }

    @GetMapping(path = "/examples/{id}")
    public Example getExampleById(@PathVariable int id) {
        Optional<Example> result = Optional.ofNullable(webClient.get()
                .uri("http://localhost:8888/examples/" + id)
                .retrieve()
                .bodyToMono(Example.class)
                .block());

        if(!result.isPresent()) {
            throw new ExampleNotFoundException("Not found Example by id-" + id);
        }

        return result.get();
    }

    @PostMapping(path = "/examples")
    public Example addExample(@RequestBody Example example) {
        Example result = webClient.post()
                .uri("http://localhost:8888/examples")
                .syncBody(example)
                .retrieve()
                .bodyToMono(Example.class)
                .block();

        return result;
    }
}
