package org.challenge.rest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.challenge.dao.AdConnection;
import org.challenge.rest.req.GetGroupedMetricRq;
import org.challenge.rest.req.GetMetricsRq;
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

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdControllerTest {
  private final static URI AdApp = URI.create("http://localhost:8080/ads/");
  @Autowired
  private MockMvc mockMvc;

  @Mock
  AdCalcService adCalcService = new AdCalcService(new AdPersistenceService(new AdConnection()));
  @Mock
  Rest2DtoMappingService mappingService;

  @BeforeEach
  void setUp() {
  }

  @Test
  void testMetricGroupedTwitter() throws Exception{

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(AdApp+ Constants.REST.METRIC_GROUPED)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("{\n" +
                    "  \"aggregator\": \"SUM\",\n" +
                    "  \"dimensions\": [\n" +
                    "    \"Datasource\", \"Daily\"\n" +
                    "  ],\n" +
                    "  \"having\": {\n" +
                    "\n" +
                    "    \"minimum\": 8000\n" +
                    "  },\n" +
                    "  \"metric\": \"CLICKS\"\n" +
                    "}"))
            .andExpectAll(status().isOk(),
                      // handler().methodCall(on(AdController.class).getMetricGrouped(same(req))),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.responseObject").isNotEmpty(),
                        jsonPath("$.error").isEmpty(),
                            jsonPath("$.responseObject.groups",
                                    org.hamcrest.Matchers.hasSize(329))).andReturn();
    TypeReference<ResponseWrapper<Object>> typeRef = new TypeReference<ResponseWrapper<Object>>() {};
    ResponseWrapper<Object> wrapper = getWrapper(mvcResult, typeRef);
    assertThat(wrapper.getResponseObject().toString()).doesNotContain(List.of("Google Ads", "Facebook Ads"));
  }

  @Test
  void testMetricGroupedGoogle() throws Exception{
    String clicksMax = "\"100\"";
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(AdApp+ Constants.REST.METRIC_GROUPED)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("{\n" +
                    "  \"aggregator\": \"SUM\",\n" +
                    "  \"dimensions\": [\n" +
                    "    \"Datasource\", \"Daily\"\n" +
                    "  ],\n" +
                    "  \"having\": {\n" +
                    "\n" +
                    "    \"maximum\": 100\n" +
                    "  },\n" +
                    "  \"metric\": \"CLICKS\"\n" +
                    "}"))
            .andExpectAll(status().isOk(),
                           // handler().methodCall(on(AdController.class).getMetricGrouped(same(req))),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.responseObject").isNotEmpty(),
                            jsonPath("$.error").isEmpty(),
                            jsonPath("$.responseObject.groups",
                                    org.hamcrest.Matchers.hasSize(79))).andReturn();
    TypeReference<ResponseWrapper<Object>> typeRef = new TypeReference<ResponseWrapper<Object>>() {};
    ResponseWrapper<Object> wrapper = getWrapper(mvcResult, typeRef);
    assertThat(wrapper.getResponseObject().toString()).doesNotContain(List.of("Twitter Ads"));
  }

  private <T> ResponseWrapper<T> getWrapper(MvcResult mvcResult, TypeReference<ResponseWrapper<T>> type)
            throws java.io.IOException {
        return new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(mvcResult.getResponse().getContentAsString(), type);
    }
}