package com.example.demo2.controller.settting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dao.StateDao;
import com.example.demo2.dto.StateDTO;
import com.example.demo2.entity.Country;
import com.example.demo2.entity.State;

@RestController
public class StateRestController {
	
	@Autowired private StateDao stateDao;
	
	@RequestMapping("/states/list_by_country/{id}")
	public List<StateDTO> listByContry(@PathVariable("id") Integer countryId){
		List<State> listStates = stateDao.findByCountryOrderByNameAsc(new Country(countryId));
		List<StateDTO> result = new ArrayList<>();
		
		for (State state : listStates) {
			result.add(new StateDTO(state.getId(), state.getName()));
		}
		return result;
	}
	
	@PostMapping("/states/save")
	public String save(@RequestBody State state) {
		State saveState = stateDao.save(state);
		return String.valueOf(saveState.getId());
	}
	
	@DeleteMapping("/states/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		stateDao.deleteById(id);
	}
}
