package com.szymon.resttemplate;

import com.szymon.exceptionHandler.ExampleNotFoundException;
import com.szymon.model.Example;
import com.szymon.resttemplate.urls.ExampleURL;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/template")
public class ExampleRestTemplateController {

    private final RestTemplate restTemplate;

    public ExampleRestTemplateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(path = "/examples")
    public List<Example> getAllExamples() {

        ResponseEntity<Example[]> result = restTemplate.getForEntity(ExampleURL.ALL_EXAMPLES, Example[].class);

        if(result.getBody() == null) {
            return null;
        }

        return Arrays.asList(result.getBody());
    }

    @GetMapping(path = "/other/examples")
    public ResponseEntity<List<Example>> getAllExamplesOther() {

        ResponseEntity<List<Example>> response =
                restTemplate.exchange(ExampleURL.ALL_EXAMPLES,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Example>>() {
                        });

        if(response.hasBody()) {
            throw new ExampleNotFoundException("Nie znaleziono zadnych rekordow");
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping(path = "example")
    public ResponseEntity<String> addExample(@RequestBody Example example) {
        ResponseEntity<?> response = restTemplate.postForEntity(ExampleURL.ADD_EXAMPLE, example, Example.class);

        return Optional
                .ofNullable( response.getBody() )
                .map( (result) -> ResponseEntity.ok().body("Added object: " + result) )
                .orElseGet( () -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() );
    }

    // Jak nie ma za duzo custom exception mozna tak robic. Ale przy wiekszej liczbie uzyc "@CustomAdvice"

    @GetMapping(path = "/examples/{id}")
    public ResponseEntity<Example> getExample(@PathVariable Long id) {
        ResponseEntity<Example> response = restTemplate.getForEntity(ExampleURL.MAIN_URL + "examples/" + id, Example.class);

        return Optional
                .ofNullable( response.getBody() )
                .map( example -> ResponseEntity.ok().body(example) )
                .orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping(path = "/custom/examples/{id}")
    public ResponseEntity<Example> getExampleCustom(@PathVariable Long id) {
        ResponseEntity<Example> response = restTemplate.getForEntity(ExampleURL.MAIN_URL + "examples/" + id, Example.class);

        if(!response.hasBody()) {
            throw new ExampleNotFoundException("Not found Example by id: " + id);
        }

        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

}
