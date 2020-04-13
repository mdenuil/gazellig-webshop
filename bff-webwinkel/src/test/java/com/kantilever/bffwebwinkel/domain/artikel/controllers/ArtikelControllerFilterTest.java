package com.kantilever.bffwebwinkel.domain.artikel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffwebwinkel.domain.artikel.services.ArtikelService;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArtikelControllerFilterTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ArtikelService artikelService;

    @BeforeEach
    void init() {
        DefaultMockMvcBuilder builder = MockMvcBuilders
                .webAppContextSetup(this.context)
                .dispatchOptions(true);

        this.mockMvc = builder.build();
    }

    @Test
    @DisplayName("GET Request on endpoint /filter/{name} returns List of 3 Artikelen")
    void getRequestFilterByNameAndDescription_ReturnsListOfArtikelen() throws Exception {
        var filterName = "f";
        var artikelenToReturn = ObjectBuilders.getDefaultSetofThreeArtikelen();
        var jsonArrayAsString = objectMapper.writeValueAsString(artikelenToReturn);

        when(artikelService.getArtikelenFilteredByNameAndDescription(filterName))
                .thenReturn(artikelenToReturn);

        this.mockMvc.perform(get("/artikel/filter?key=" + filterName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(jsonArrayAsString))
                .andReturn();
    }
}