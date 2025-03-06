package org.example.routeapp.controller;

import org.example.routeapp.dto.RouteListResponseDto;
import org.example.routeapp.model.Location;
import org.example.routeapp.model.TransportationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.example.routeapp.service.TransportationService;


import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = TransportationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TransportationControllerTest {

    @Autowired
    private MockMvc mockMvc;  //inject MockMvc to simulate HTTP requests

    @MockitoBean
    private TransportationService transportationService;

    @Test
    public void testGetTransportationsBetween() throws Exception {
        Long originId = 4L;
        Long destId = 6L;
        String date = "2025-03-05";

        List<List<Location>> mockAllRoutes = new ArrayList<>();
        List<Location> mockRoutes = new ArrayList<>();
        Location mockOriginLocation = new Location();

        mockOriginLocation.setLocationId(4L);
        mockOriginLocation.setName("Tokyo International Airport");
        mockOriginLocation.setCountry("Japan");
        mockOriginLocation.setCity("Tokyo");
        mockOriginLocation.setLocationCode("NRT");

        mockRoutes.add(mockOriginLocation);

        Location mockDestLocation = new Location();
        mockDestLocation.setLocationId(6L);
        mockDestLocation.setName("Paris Charles de Gaulle Airport");
        mockDestLocation.setCountry("France");
        mockDestLocation.setCity("Paris");
        mockDestLocation.setLocationCode("CDG");

        mockRoutes.add(mockDestLocation);

        mockAllRoutes.add(mockRoutes);

        List<List<TransportationType>> mockAllTransportationTypes = new ArrayList<>();
        List<TransportationType> mockTransportationTypes = new ArrayList<>();

        mockTransportationTypes.add(TransportationType.FLIGHT);
        mockAllTransportationTypes.add(mockTransportationTypes);

        RouteListResponseDto res = new RouteListResponseDto(mockAllRoutes, mockAllTransportationTypes);


        when(transportationService.getTransportationsBetween(originId, destId, date))
                .thenReturn(ResponseEntity.ok(res));


        mockMvc.perform(get("/transportations/origin/{originId}/dest/{destId}/date/{date}", originId, destId, date)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  //expect HTTP 200 OK
                .andExpect(jsonPath("$.routes[0][0].locationId").value(4))
                .andExpect(jsonPath("$.routes[0][0].name").value("Tokyo International Airport"))
                .andExpect(jsonPath("$.routes[0][0].country").value("Japan"))
                .andExpect(jsonPath("$.routes[0][0].city").value("Tokyo"))
                .andExpect(jsonPath("$.routes[0][0].locationCode").value("NRT"))
                .andExpect(jsonPath("$.routes[0][1].locationId").value(6))
                .andExpect(jsonPath("$.routes[0][1].name").value("Paris Charles de Gaulle Airport"))
                .andExpect(jsonPath("$.routes[0][1].country").value("France"))
                .andExpect(jsonPath("$.routes[0][1].city").value("Paris"))
                .andExpect(jsonPath("$.routes[0][1].locationCode").value("CDG"))
                .andExpect(jsonPath("$.transportationTypes[0][0]").value("FLIGHT"));
    }
}
