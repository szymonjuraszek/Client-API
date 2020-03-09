package com.szymon.resttemplate;

import com.szymon.model.Example;
import com.szymon.resttemplate.urls.ExampleURL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ExampleRestTemplateControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExampleRestTemplateController controller;

    @Test
    void getExampleCustom() {

    }

    @Test
    void getAllExamplesOther() {
        Example myobjectA = new Example();
        //define the entity you want the exchange to return
        ResponseEntity<List<Example>> myEntity = new ResponseEntity<List<Example>>(HttpStatus.NOT_FOUND);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.eq(ExampleURL.ALL_EXAMPLES),
                ArgumentMatchers.eq(HttpMethod.GET),
                ArgumentMatchers.<HttpEntity<List<Example>>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Example>>>any()))
                .thenReturn(myEntity);

        ResponseEntity<List<Example>> res = controller.getAllExamplesOther();
        Assertions.assertEquals(myobjectA, res.getBody().get(0));
    }
}
