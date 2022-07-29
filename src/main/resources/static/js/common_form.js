
$(window).on('load', function() {
	$(".modal").modal({
		backdrop: 'static',
		keyboard: false,
		show: true
	});

	$('.modal').on('click', 'button.close', function(eventObject) {
		$('.modal').modal('hide');
	});
});
		
// Upload Image
function fn_saveimg() {
	$("#imgupload").trigger("click");
	$("#imgupload").change(function() {
		if(!checkFileSize(this)) {
			return;
		}
			showImgThumbnail(this);
	});
}

function showImgThumbnail(input) {
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(input.files[0]);
}

function checkFileSize(fileInput) {
	fileSize = fileInput.files[0].size;
	//alert("File size: "+fileSize);
	if (fileSize > MAX_FILE_SIZE) {
		alert("Bạn phải chọn ảnh ít hơn " + MAX_FILE_SIZE + " byte !");
		fileInput.setCustomValidity("Bạn phải chọn ảnh ít hơn " + MAX_FILE_SIZE + " byte !");
		fileInput.reportValidity();
		return false;
	} else {
		fileInput.setCustomValidity("");
		return true;
	}
}

// message
function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal("show");
}

function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}

//delete
function showDeleteConfirmModal(link, entityName) {
	$("#comfirmText").text("Bạn có chắc chắn muốn xoá " + entityName + " id " + link.attr("entityId"))
	$("#yes-btn").attr("href", link.attr("href"));
	$("#comfirmModal").modal("show");
}
