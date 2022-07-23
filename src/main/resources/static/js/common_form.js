
// Upload Image
function fn_saveimg() {
	$("#imgupload").trigger("click");
	$("#imgupload").change(function() {
		fileSize = this.files[0].size;
		//alert("File size: "+fileSize);
		if (fileSize > 500768) {
			alert("Bạn phải chọn ảnh ít hơn 500MB !");
			this.setCustomValidity("Bạn phải chọn ảnh ít hơn 500MB !");
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			showImgThumbnail(this);
		}
	});
}

function showImgThumbnail(input) {
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(input.files[0]);
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
