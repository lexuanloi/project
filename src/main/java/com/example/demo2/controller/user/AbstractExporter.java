package com.example.demo2.controller.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.demo2.entity.User;

public class AbstractExporter {
	public void setResponseHeader(HttpServletResponse response , String contentType, String extension) throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormat.format(new Date());
		String fileName = "users_" +timestamp + extension;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename = "+fileName;
		response.setHeader(headerKey, headerValue);
	}
}
