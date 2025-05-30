package com.projet.testing.vehicule;
import com.projet.testing.vehicule.controller.VehiculeController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet.testing.vehicule.dto.VehiculeDto;
import com.projet.testing.vehicule.service.VehiculeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehiculeController.class)
public class VehiculeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehiculeService vehiculeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateVehiculeSuccess() throws Exception {
        VehiculeDto vehicule = new VehiculeDto();
        vehicule.setId(UUID.randomUUID());
        vehicule.setRegistrationNumber("ABC123");
        vehicule.setMake("Toyota");
        vehicule.setModel("Corolla");
        vehicule.setYear(2022);
        vehicule.setRentalPrice(300.0);

        Mockito.when(vehiculeService.createVehicule(Mockito.any(VehiculeDto.class)))
                .thenReturn(vehicule);

        mockMvc.perform(post("/vehicule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicule))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationNumber").value("ABC123"));
    }

}

