package com.example.demo2.controller.category;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.example.demo2.conf.AbstractExporter;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.User;

public class CategoryCsvExporter extends AbstractExporter{
	
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv; charset=UTF-8", ".csv", "categories_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = {"Category_ID", "Category_Name"};
		String[] fileMapping = {"id", "name"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (Category category : listCategories) {
			category.setName(category.getName().replace("--", "  "));
			csvWriter.write(category, fileMapping);
		}
		
		csvWriter.close();
	}

}
