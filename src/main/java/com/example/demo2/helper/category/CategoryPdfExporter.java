package com.example.demo2.helper.category;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.demo2.common.AbstractExporter;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.User;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

public class CategoryPdfExporter extends AbstractExporter{

	public void export(List<Category> listCategoriess, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "aplication/pdf", ".pdf", "categories_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("List of categories", font);
		paragraph.setAlignment(paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f, 3.5f});
		
		writeTableHeader(table);
		writeTableData(table, listCategoriess);
		
		document.add(table);
		
		document.close();
	}

	private void writeTableData(PdfPTable table, List<Category> liCategories) {
		for (Category category : liCategories) {
			table.addCell(String.valueOf(category.getId()));
			table.addCell(category.getName());
		}
		
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("Category ID", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Name", font));		
		table.addCell(cell);
		
	}

}
