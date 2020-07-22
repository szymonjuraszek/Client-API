package com.szymon.webclient;

import com.szymon.exceptionHandler.ExampleNotFoundException;
import com.szymon.model.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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

        if (!result.isPresent()) {
            throw new ExampleNotFoundException("Not found Example by id-" + id);
        }

        return result.get();
    }

    @PostMapping(path = "/examples")
    public void addExample(@RequestBody Example example) {
        logger.info("WebClient before POST request (one object)");

        Mono<ClientResponse> response = webClient.post()
                .uri("http://localhost:8888/examples")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(example)
                .exchange();

        //        System.out.println(response.block().toEntity(Example.class).block().getBody());
        //        return response.block().toEntity(Example.class).block().getBody();
        response.block().headers().asHttpHeaders().entrySet().stream().forEach(System.out::println);
    }

    @PostMapping(path = "/examples/list")
    public void addExample(@RequestBody List<Example> examples) {
        logger.info("WebClient before POST request (many objects)");

        Mono<ClientResponse> response2 = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/examples/list")
                        .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Flux.just(examples), List.class)
                .exchange();

        System.out.println(response2.block().bodyToFlux(Example.class).blockFirst());
    }
}
