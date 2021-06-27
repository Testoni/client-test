package com.platformbuilders.clients.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.platformbuilders.clients.model.Client;
import com.platformbuilders.clients.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DisplayName("API Test")
@SpringBootTest
class ClientsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ClientsController clientsController;

    @Autowired
    private ClientRepository clientRepository;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientsController).build();
        clientRepository.deleteAll();
        this.mapper = JsonMapper.builder().findAndAddModules().build();
    }

    @Test
    @DisplayName("Should create user - status code 200")
    public void shouldCreateUserStatusCode200() throws Exception {
        Client client = Client.builder()
                .name("Gabriel")
                .email("gabriel@gmail.com")
                .dateOfBirth(LocalDate.of(1992, 10, 27))
                .build();

        String json = this.mapper.writeValueAsString(client);

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(print());

        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.oid").exists());
    }

    @Test
    @DisplayName("Should not create user - status code 400")
    public void shouldNotCreateUserStatusCode400() throws Exception {
        Client client = Client.builder()
                .name("Gabriel")
                .email("invalidemail")
                .dateOfBirth(LocalDate.of(1992, 10, 27))
                .build();

        String json = this.mapper.writeValueAsString(client);

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        perform.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should not find by oid - status code 404")
    public void shouldNotFindByOidStatus404() throws Exception {
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/clients/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON));
        perform.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should find by oid - status code 200")
    public void shouldFindByOidStatus200() throws Exception {
        saveClient();
        List<Client> clients = clientRepository.findAll();

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/clients/" + clients.get(0).getOid())
                .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Should delete by oid - status code 204")
    public void shouldDeleteByOidStatus204() throws Exception {
        saveClient();
        List<Client> clients = clientRepository.findAll();
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/clients/" + clients.get(0).getOid())
                .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Should not delete by oid - status code 404")
    public void shouldNotDeleteByOidStatus404() throws Exception {
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/clients/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON));
        perform.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should not find clients - status code 200")
    public void shouldNotFindClients() throws Exception {
        saveClient();
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/clients?page=0&size=10&name=gabriel&email=gabriel.testoni@gmail.com")
                .param("searchName", "xxx")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Should find all clients - status code 200")
    public void shouldFindAllClients() throws Exception {
        saveClient();
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/clients?page=0&size=10&name=gabriel&email=gabriel.testoni@gmail.com")
                .param("searchName", "gabriel")
                .param("searchFromDate", "1990-01-01")
                .param("searchEndDate", "2000-01-01")
                .param("searchEmail", "@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
    }

    private void saveClient() throws Exception {
        Client client = Client.builder()
                .name("Gabriel")
                .email("gabriel@gmail.com")
                .dateOfBirth(LocalDate.of(1992, 10, 27))
                .build();

        String json = this.mapper.writeValueAsString(client);
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

}