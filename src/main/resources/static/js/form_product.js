dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function() {

	$("#shortDescription").richText();
	$("#fullDescription").richText();

	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});
	getCategories();
	
});

function fn_saveExtraImg() {
	$("#extraImage1").trigger("click");
	$("#extraImage1").change(function() {
		if(!checkFileSize(this)) {
			return;
		}
			showExtraImgThumbnail(this);
	});
}

function showExtraImgThumbnail(input) {
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#extraThumbnail1").attr("src", e.target.result);
	};
	reader.readAsDataURL(input.files[0]);
}

function getCategories() {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categoties";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
}

function checkUnique(form) {
	productId = $("#id").val();
	productName = $("#name").val();
	csrfValue = $("input[name='_csrf']").val();

	url = "[[@{/products/check_unique}]]";

	params = { id: productId, name: productName, _csrf: csrfValue };

	$.post(url, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicate") {
			showWarningModal("Product đã được đăng kí: " + productName)
		} else {
			showErrorModal("Unknown response from  server");
		}
	}).fail(function() {
		alert('could not connect to the server');
	});
	return false;
}