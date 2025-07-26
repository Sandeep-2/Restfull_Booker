package Restfull_Booker.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.nio.file.*;
import java.util.List;

public class ExcelWriterUtil {

	public static void writeExcel(List<List<Object>> data, List<String> headers, String filePath) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Bookings");

		// Write header
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}

		// Write data rows
		for (int i = 0; i < data.size(); i++) {
			Row row = sheet.createRow(i + 1);
			List<Object> rowData = data.get(i);
			for (int j = 0; j < rowData.size(); j++) {
				Object value = rowData.get(j);
				if (value instanceof Number) {
					row.createCell(j).setCellValue(((Number) value).doubleValue());
				} else if (value instanceof Boolean) {
					row.createCell(j).setCellValue((Boolean) value);
				} else {
					row.createCell(j).setCellValue(value != null ? value.toString() : "");
				}
			}
		}

		// Auto-size columns
		for (int i = 0; i < headers.size(); i++) {
			sheet.autoSizeColumn(i);
		}

		// ✅ Add summary sheet
		writeSummarySheet(workbook, data);

		try {
			Path path = Paths.get(filePath);
			Files.createDirectories(path.getParent());

			try (FileOutputStream out = new FileOutputStream(path.toFile())) {
				workbook.write(out);
				System.out.println("✅ Excel written successfully to " + path.toAbsolutePath());
			}
		} catch (Exception e) {
			System.err.println("❌ Failed to write Excel: " + e.getMessage());
		} finally {
			try {
				workbook.close();
			} catch (Exception ignored) {
			}
		}
	}

	private static void writeSummarySheet(Workbook workbook, List<List<Object>> data) {
		Sheet summarySheet = workbook.createSheet("Summary");

		int totalBookings = data.size();
		long depositsPaid = data.stream().filter(row -> row.get(4) instanceof Boolean && (Boolean) row.get(4)).count();
		double avgPrice = data.stream().mapToDouble(row -> ((Number) row.get(3)).doubleValue()).average().orElse(0);

		String[][] summaryData = { { "Metric", "Value" }, { "Total Bookings", String.valueOf(totalBookings) },
				{ "Bookings with Deposit Paid", String.valueOf(depositsPaid) },
				{ "Average Total Price", String.format("%.2f", avgPrice) } };

		for (int i = 0; i < summaryData.length; i++) {
			Row row = summarySheet.createRow(i);
			for (int j = 0; j < summaryData[i].length; j++) {
				row.createCell(j).setCellValue(summaryData[i][j]);
			}
		}

		summarySheet.autoSizeColumn(0);
		summarySheet.autoSizeColumn(1);
	}
}