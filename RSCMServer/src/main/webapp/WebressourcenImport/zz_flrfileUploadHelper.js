//Entire Script form : https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/

'use strict';

var multipleUploadForm = document.querySelector('#multipleUploadForm');
var multipleFileUploadInput = document
		.querySelector('#multipleFileUploadInput');
var multipleFileUploadError = document
		.querySelector('#multipleFileUploadError');
var multipleFileUploadSuccess = document
		.querySelector('#multipleFileUploadSuccess');
var searchName = document.querySelector('#searchName');

function getFileList(searchString) {
	
	var searchString = document.getElementById("searchName").value;
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/getFileList");
	xhr.onload = function() {
		console.log(xhr.responseText);
		var response = JSON.parse(xhr.responseText);
		if (xhr.status == 200) {
			var tableBuild = "";

			for (var i = 0; i < response.length; i++) {
				tableBuild += "<tr>"
				tableBuild += "<td id='Col1Cell"+i+"'>" + response[i].filename.replace(new RegExp(searchString, 'g'), "<span class='highlight'>"+searchString+"</span>"); + "</td>"
				tableBuild += "<td>" + response[i].size + " Byte</td>"
				tableBuild += "<td>" + response[i].creationDate + "</td>"
				tableBuild += "<td><input type='button' value='display' onclick=\"displayFile('"+response[i].filename+"')\"></td>";
				tableBuild += "<td id='Col2Cell"+i+"'><input type='button' value='delete' onclick=\"showConfirmationDelete('Col2Cell"+i+"','"+ response[i].filename +"')\"></td>"
				tableBuild += "</tr>"
			}

			document.getElementById("tableContent").innerHTML = tableBuild;
			
			
		} else {
			alter("asdasdklj");
			multipleFileUploadSuccess.style.display = "none";
			multipleFileUploadError.innerHTML = (response && response.message)
					|| "Some Error Occurred";
		}
	}
	var formData = new FormData();
	formData.append("search", searchString);
	xhr.send(formData);
	
}

function showConfirmationDelete(htmlID, filename) {
	var newCellContent = "should this file really be deleted? <br>";
	newCellContent += "<input type='button' value='Yes' onclick=\"deleteFile('"+filename+"');\"><input type='button' value='No' onclick=\"getFileList();\">";
	
	document.getElementById(htmlID).innerHTML = newCellContent;
}

function deleteFile(filename) {
	//https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/send
	var xhr = new XMLHttpRequest();
	xhr.open('POST', '/deleteFile', true);
	
	xhr.onload = function () {
		getFileList();
		multipleFileUploadSuccess.innerHTML = "the file " + filename +" was deleted successfully";
		
	};
	var formData = new FormData();
	formData.append("fileName", filename);
	xhr.send(formData);
	// xhr.send('string');
	// xhr.send(new Blob());
	// xhr.send(new Int8Array());
	// xhr.send(document);
}

function displayFile(filename) {
	window.location="ScriptDisplay.jsp?filename=" +filename;
}


function uploadMultipleFiles(files) {
	var formData = new FormData();
	for (var index = 0; index < files.length; index++) {
		formData.append("files", files[index]);
	}

	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/uploadMultipleFiles");

	xhr.onload = function() {
		console.log(xhr.responseText);
		var response = JSON.parse(xhr.responseText);
		if (xhr.status == 200) {
			
			multipleFileUploadError.style.display = "none";
			var content = "<p>All Files Uploaded Successfully</p>";
			content += "<ul>";
			for (var i = 0; i < response.length; i++) {
				if(response[i].size < 0){
					//when file already exists
					content += "<li>Upload " + i + " The File: "+response[i].fileName+" Already exists and the upload of this file is canceled</li>";
				}else{
					content += "<li>Upload " + i + " : <a href='"
					+ response[i].fileDownloadUri + "' target='_blank'>"
					+ response[i].fileName + "</a></li>";
				}
				
				
			}
			content += "</ul>";
			multipleFileUploadSuccess.innerHTML = content;
			multipleFileUploadSuccess.style.display = "block";
		} else {
			multipleFileUploadSuccess.style.display = "none";
			multipleFileUploadError.innerHTML = (response && response.message)
					|| "Some Error Occurred";
		}
	}

	xhr.send(formData);
	getFileList("");
}

multipleUploadForm.addEventListener('submit', function(event) {
	var files = multipleFileUploadInput.files;
	if (files.length === 0) {
		multipleFileUploadError.innerHTML = "Please select at least one file";
		multipleFileUploadError.style.display = "block";
	}
	uploadMultipleFiles(files);
	event.preventDefault();
}, true);



var down = false;
document.getElementById("searchName").addEventListener('keydown', function (){ // https://stackoverflow.com/questions/11991062/keyup-event-listener-fires-when-enter-is-pressed-on-chromes-ominbox
    down = true;
}, false);

document.getElementById("searchName").addEventListener('keyup', function (){ // https://stackoverflow.com/questions/11991062/keyup-event-listener-fires-when-enter-is-pressed-on-chromes-ominbox
    if(down === true){
    	getFileList(document.getElementById("searchName").value);
    }
    else{
        //ignor it there was no key down before
    }
    down = false;
}, false);
