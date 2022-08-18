package com.example.demo2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo2.dao.CountryDao;
import com.example.demo2.dao.StateDao;
import com.example.demo2.entity.Country;
import com.example.demo2.entity.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {

	@Autowired MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
	@Autowired CountryDao countryDao;
	@Autowired StateDao stateDao;
	
	@Test
	@WithMockUser(username = "admin@gmail.com", password = "something", roles = "ADMIN")
	public void testListCountries() throws Exception {
		String url = "/countries/list";
		MvcResult result = mockMvc.perform(get(url))
		.andExpect(status().isOk())
		.andDo(print())
		.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		Country[]  countries = objectMapper.readValue(jsonResponse, Country[].class);
		
		assertThat(countries).hasSizeGreaterThan(0);
	}
	
	@Test
	@WithMockUser(username = "admin@gmail.com", password = "something", roles = "ADMIN")
	public void testCreateCountries() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		String countryName = "Canada";
		String countryCode = "CA";
		Country country = new Country(countryName, countryCode);
		
		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(country))
				.with(csrf()))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();;
		
		String response = result.getResponse().getContentAsString();
		
		System.out.println("country id :" + response);
	}
	
	@Test
	@WithMockUser(username = "admin@gmail.com", password = "something", roles = "ADMIN")
	public void testCreateStates() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer countryId = 2;
		Country country = countryDao.findById(countryId).get();
		State state = new State("Arizona", country);
		
		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
				.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();;
				
				String response = result.getResponse().getContentAsString();
				Integer stateId = Integer.parseInt(response);
				Optional<State> finById = stateDao.findById(stateId);
				assertThat(finById.isPresent());
	}
}
