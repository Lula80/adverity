package org.challenge.rest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.challenge.dao.AdDbConnection;
import org.challenge.rest.resp.ResponseWrapper;
import org.challenge.services.AdCalcService;
import org.challenge.services.AdPersistenceService;
import org.challenge.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdControllerTest {
  private final static URI AdApp = URI.create("http://localhost:8080/ads/");
  @Autowired
  private MockMvc mockMvc;

  @Mock
  AdCalcService adCalcService = new AdCalcService(new AdPersistenceService(new AdDbConnection()))
  @Mock
  Rest2DtoMappingService mappingService

  @BeforeEach
  void setUp() {
  }

  @Test
  void testMetricGroupedTwitter() throws Exception{

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("$AdApp${Constants.REST.METRIC_GROUPED}")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("""{
                    \"aggregator\": \"SUM\",
                      \"dimensions\": [
                        \"Datasource\", \"Daily\"
                      ],
                    \"having\": {
                        \"minimum\": 8000
                      },
                      \"metric\": \"CLICKS\"
                    }"""))
            .andExpectAll(status().isOk(),
                      // handler().methodCall(on(AdController.class).getMetricGrouped(same(req))),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath('$.responseObject').isNotEmpty(),
                        jsonPath('$.error').isEmpty(),
                            jsonPath('$.responseObject.groups',
                                    org.hamcrest.Matchers.hasSize(152))).andReturn()
    TypeReference<ResponseWrapper<Object>> typeRef = new TypeReference<ResponseWrapper<Object>>() {};
    ResponseWrapper<Object> wrapper = getWrapper(mvcResult, typeRef);
    assertThat(wrapper.getResponseObject().toString()).doesNotContain(["Google Ads", "Facebook Ads"]);
  }

  @Test
  void testMetricGroupedGoogle() throws Exception{
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("$AdApp${Constants.REST.METRIC_GROUPED}")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("""{
                    \"aggregator\": \"SUM\",
                      \"dimensions\": [
                        \"Datasource\", \"Daily\"
                      ],
                    \"having\": {
                        \"maximum\": 100
                      },
                      \"metric\": \"CLICKS\"
                    }"""))
            .andExpectAll(status().isOk(),
                           // handler().methodCall(on(AdController.class).getMetricGrouped(same(req))),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath('$.responseObject').isNotEmpty(),
                            jsonPath('$.error').isEmpty(),
                            jsonPath('$.responseObject.groups',
                                    org.hamcrest.Matchers.hasSize(208))).andReturn();
    TypeReference<ResponseWrapper<Object>> typeRef = new TypeReference<ResponseWrapper<Object>>() {};
    ResponseWrapper<Object> wrapper = getWrapper(mvcResult, typeRef);
    assertThat(wrapper.getResponseObject().toString()).doesNotContain(["Twitter Ads"]);
  }

  private <T> ResponseWrapper<T> getWrapper(MvcResult mvcResult, TypeReference<ResponseWrapper<T>> type)
            throws java.io.IOException {
        return new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(mvcResult.getResponse().getContentAsString(), type);
    }
}