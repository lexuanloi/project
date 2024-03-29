// Country
var buttonLoad;
var dropDownCountry;

var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;

var labelCountryName;
var fieldCountryName;

var labelCountryCode;
var fieldCountryCode;

// State
var buttonLoadForStates;
var dropDownCountryForStates;
var dropDownStates;

var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;

var labelStateName;
var fieldStateName;


$(document).ready(function(){
	//Country
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
	
	buttonLoad.click(function(){
		loadCountries();
	});
	
	dropDownCountry.on("change", function() {
		changeFormStateToSelectedCountry();
	});
	
	buttonAddCountry.click(function(){
		if(buttonAddCountry.val() == "Add"){
			addCountry();
		}else{
			changeFormCountryToNew();
		}
	})
	
	buttonUpdateCountry.click(function() {
		updateCountry();
	});
	
	buttonDeleteCountry.click(function() {
		deleteCountry();
	});
	//END COUNTRY
	
	// STATE
	buttonLoadForStates = $("#buttonLoadCountriesForStates");
	dropDownCountryForStates = $("#dropDownCountriesForStates");
	dropDownStates = $("#dropDownStates");
	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");
	
	buttonLoadForStates.click(function(){
		loadCountriesForStates();
	});
	
	dropDownCountryForStates.on("change", function() {
		loadStatesForCountry();
	});
	
	dropDownCountry.on("change", function() {
		changeFormStateToSelectedState();
	});
	
	dropDownStates.on("change", function() {
		changeFormStateToSelectedState();
	});
	
	buttonAddState.click(function(){
		if(buttonAddState.val() == "Add"){
			addState();
		}else{
			changeFormStateToNew();
		}
	})
	
	buttonUpdateState.click(function() {
		updateState();
	});
	
	buttonDeleteState.click(function() {
		deleteState();
	});
});
// country
function deleteCountry() {
	optionValue = dropDownCountry.val();
	countryId = optionValue.split("-")[0];
	
	url = contextPath + "countries/delete/" + countryId;
	
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeFormCountryToNew();
		buttonLoad.val("refresh country list");
		showToastMessage("The country has been deleted");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}

function updateCountry() {
	if(!validateFormCountry()) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryId = dropDownCountry.val().split("-")[0];
	
	jsonData = {id: countryId, name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated");
		
		changeFormStateToSelectedCountry();
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}

function validateFormCountry(){
	formCountry = document.getElementById("formCountry");
	if(!formCountry.checkValidity()){
		formCountry.reportValidity();
		return false;
	}
	return true;
}

function addCountry() {
	if(!validateFormCountry()) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = {name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		changeFormStateToSelectedCountry();
		showToastMessage("The new country has been added");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}

function selectNewlyAddedCountry(countryId, countryCode, countryName) {
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
	
	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
	
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

function changeFormCountryToNew(){
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name :");
	
	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);
	
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

function changeFormStateToSelectedCountry() {
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);
	
	labelCountryName.text("Selected Country");
	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);
	
	countryCode = dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
}

function loadCountries(){
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry.empty();
		
		$.each(responseJSON, function(index, country){
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
	}).done(function() {
		buttonLoad.val("refresh country list");
		showToastMessage("All countries have been loaded");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}

//end country
function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}

// STATE

function deleteState() {
	stateId = dropDownStates.val();
	url = contextPath + "states/delete/" + stateId;
	
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownState option[value='" + stateId + "']").remove();
		loadStatesForCountry();
		changeFormStateToNew();
		showToastMessage("The state has been deleted");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}

function updateState() {
	if(!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	stateId = dropDownStates.val();
	
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	
	jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId){
		$("#dropDownStates option:selected").text(stateName);
		showToastMessage("The state has been updated");
		
		changeFormStateToSelectedState();
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}

function validateFormState(){
	formCountry = document.getElementById("formState");
	if(!formCountry.checkValidity()){
		formCountry.reportValidity();
		return false;
	}
	return true;
}

function addState() {
	if(!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	
	jsonData = {name: stateName, country: {id: countryId, name: countryName}};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId){
		selectNewlyAddedState(stateId, stateName);
		changeFormStateToSelectedState();
		showToastMessage("The new state has been added");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}

function selectNewlyAddedState(stateId, stateName) {
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
	
	$("#dropDownStates option[value='" + stateId + "']").prop("selected", true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToNew(){
	buttonAddState.val("Add");
	labelStateName.text("State/Province Name 1:");
	
	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState() {
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);
	
	labelStateName.text("Selected State/Province");
	
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);
}

function loadStatesForCountry(){
	selectedCountry = $("#dropDownCountriesForStates option:selected")
	countryId = selectedCountry.val();
	url = contextPath + "states/list_by_country/" + countryId;
	$.get(url, function(responseJSON) {
		dropDownStates.empty();
		
		$.each(responseJSON, function(index, state){
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});
	}).done(function() {
		changeFormStateToNew();
		showToastMessage("All states have been loaded for country " + selectedCountry.text());
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}

function loadCountriesForStates() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountryForStates.empty();
		
		$.each(responseJSON, function(index, country){
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountryForStates);
		});
	}).done(function() {
		buttonLoadForStates.val("refresh country list");
		selectNewlyAddedState();
		showToastMessage("All countries have been loaded");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encountered an error")
	});
}
// EN STATE