package com.szymon.resttemplate;

import com.szymon.exceptionHandler.ExampleNotFoundException;
import com.szymon.model.Example;
import com.szymon.resttemplate.urls.ExampleURL;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

@RestController
@RequestMapping(path = "template")
public class ExampleRestTemplateController {

    private final RestTemplate restTemplate;

    public ExampleRestTemplateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(path = "/examples")
    public List<Example> getAllExamples() {

        System.out.println("Dupa zbita");
        ResponseEntity<Example[]> result = restTemplate.getForEntity(ExampleURL.ALL_EXAMPLES, Example[].class);

//        Wyswietla id dla sesji http 'JSESSIONID'
        System.out.println(RequestContextHolder.currentRequestAttributes().getSessionId());

        if (result.getBody() == null) {
            return null;
        }

        return Arrays.asList(result.getBody());
    }

    @PostMapping(path = "example")
    public ResponseEntity<String> addExample(@RequestBody Example example) {
        ResponseEntity<?> response = restTemplate.postForEntity(ExampleURL.ADD_EXAMPLE, example, Example.class);

        return Optional
                .ofNullable(response.getBody())
                .map((result) -> ResponseEntity.ok().body("Added object: " + result))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    // Jak nie ma za duzo custom exception mozna tak robic. Ale przy wiekszej liczbie uzyc "@CustomAdvice"


    @GetMapping(path = "/examples/{id}")
    public ResponseEntity<Example> getExample(@RequestParam(required = false) String name, @PathVariable Long id) {
//        Zwraca ResponseEntity
        ResponseEntity<Example> response = restTemplate.getForEntity(ExampleURL.MAIN_URL + "examples/" + id, Example.class);
//        Zwraca tylko obiekt, 3 argument podaje sie w przyapdku pathVariable
//        Example example = restTemplate.getForObject(ExampleURL.MAIN_URL + "examples/{id}", Example.class, id);

        return Optional
                .ofNullable(response.getBody())
                .map(example -> ResponseEntity.ok().body(example))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/custom/examples/{id}")
    public ResponseEntity<Example> getExampleCustom(@PathVariable Long id) {
        ResponseEntity<Example> response = restTemplate.getForEntity(ExampleURL.MAIN_URL + "examples/" + id, Example.class);

        if (!response.hasBody()) {
            throw new ExampleNotFoundException("Not found Example by id: " + id);
        }

        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/other/examples", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Example>> getAllExamplesOther() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

//        Dzieki temu mozna przeslac params :)
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("msisdn", msisdn)
//                .queryParam("email", email)
//                .queryParam("clientVersion", clientVersion)
//                .queryParam("clientType", clientType)
//                .queryParam("issuerName", issuerName)
//                .queryParam("applicationName", applicationName);

        ResponseEntity<List<Example>> response =
                restTemplate.exchange(ExampleURL.ALL_EXAMPLES,
                        HttpMethod.GET, entity, new ParameterizedTypeReference<List<Example>>() {});


        if (!response.hasBody()) {
            throw new ExampleNotFoundException("Nie znaleziono zadnych rekordow");
        }

        Collections.sort(response.getBody(), Comparator.comparing(Example::getName)
                .thenComparing(Example::getValue));

        return ResponseEntity.ok(response.getBody());
    }

}
