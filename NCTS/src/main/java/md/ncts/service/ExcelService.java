package md.ncts.service;

import md.ncts.model.HouseItem;
import md.ncts.model.Packaging;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import md.ncts.model.ItemTaxes;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelService {

    public static List<HouseItem> readExcel(File file, String packageType, String shippingMark) {
        Map<String, HouseItem> grouped = new LinkedHashMap<>();
        int sequence = 1;

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String hsCode = getString(row.getCell(0));
                String desc = getString(row.getCell(1));
                double qty = getNumeric(row.getCell(3));
                double val = getNumeric(row.getCell(4));
                double nett = getNumeric(row.getCell(5));
                double brutt = getNumeric(row.getCell(6));

                HouseItem item = grouped.get(hsCode);
                if (item == null) {
                    ItemTaxes taxes = new ItemTaxes("EUR", 0, 0, val);
                    item = new HouseItem(
                            sequence++, hsCode, desc, brutt, qty, val,
                            List.of(new Packaging(packageType, String.valueOf((int) qty), shippingMark, 1)),
                            taxes
                    );
                    grouped.put(hsCode, item);
                } else {
                    item.addToTotals(qty, val, brutt);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(grouped.values());
    }

    private static String getString(Cell cell) {
        if (cell == null) return "";

        String raw;
        if (cell.getCellType() == CellType.NUMERIC) {
            raw = String.valueOf((long) cell.getNumericCellValue());
        } else {
            raw = cell.toString().trim().replace(".0", "");
        }

        return raw.length() >= 6 ? raw.substring(0, 6) : raw;
    }

    private static double getNumeric(Cell cell) {
        if (cell == null) return 0.0;
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            try {
                return Double.parseDouble(cell.toString().trim());
            } catch (Exception ex) {
                return 0.0;
            }
        }
    }
}
