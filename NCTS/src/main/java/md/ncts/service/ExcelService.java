package md.ncts.service;

import md.ncts.model.HouseItem;
import md.ncts.model.ItemTaxes;
import md.ncts.model.Packaging;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelService {

    // ✅ Metoda folosită de MainApp (cu HouseItem)
    public static List<HouseItem> readExcel(File file, String packageType, String shippingMark) {
        Map<String, HouseItem> grouped = new LinkedHashMap<>();
        int sequence = 1;

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String hsCode = sanitizeHsCode(getString(row.getCell(0)));
                String desc = getString(row.getCell(1));
                double qty = getNumeric(row.getCell(3));
                double val = getNumeric(row.getCell(4));
                double nett = getNumeric(row.getCell(5));
                double brutt = getNumeric(row.getCell(6));

                HouseItem item = grouped.get(hsCode);
                if (item == null) {
                    ItemTaxes itemTaxes = new ItemTaxes("EUR", (int) qty, 0, val);
                    item = new HouseItem(
                            sequence++, hsCode, desc, brutt, nett, nett,
                            new ArrayList<>(List.of(new Packaging(packageType, String.valueOf((int) qty), shippingMark, 1))),
                            itemTaxes
                    );
                    grouped.put(hsCode, item);
                } else {
                    item.setGrossMass(item.getGrossMass() + brutt);
                    item.setNetMass(item.getNetMass() + nett);
                    item.setSupplementaryUnits(item.getSupplementaryUnits() + nett);
                    item.getItemTaxes().setQuantity1(item.getItemTaxes().getQuantity1() + (int) qty);
                    item.getItemTaxes().setStatisticalValue(item.getItemTaxes().getStatisticalValue() + val);
                    item.getPackagings().add(new Packaging(packageType, String.valueOf((int) qty), shippingMark, 1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(grouped.values());
    }


    // ✅ Metodă folosită de JsonService (cu ItemRow simplu)
    public static List<JsonService.ItemRow> readRows(File file) {
        List<JsonService.ItemRow> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                JsonService.ItemRow item = new JsonService.ItemRow();
                item.cod_Tarifar = getString(row.getCell(0));
                item.Description = getString(row.getCell(1));
                item.unit_masura = getString(row.getCell(2));
                item.Cantitate = getNumeric(row.getCell(3));
                item.Suma = getNumeric(row.getCell(4));
                item.NETT = getNumeric(row.getCell(5));
                item.BRUTT = getNumeric(row.getCell(6));

                rows.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    // 🧠 Ajutoare interne
    private static String getString(Cell cell) {
        if (cell == null) return "";
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return String.valueOf((long) cell.getNumericCellValue());
            } else {
                return cell.toString().trim().replace(".0", "");
            }
        } catch (Exception e) {
            return "";
        }
    }

    private static double getNumeric(Cell cell) {
        if (cell == null) return 0.0;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else {
                String raw = cell.toString()
                        .trim()
                        .replace(" ", "")          // elimină spațiu non-breaking
                        .replace(".", "")          // elimină separator mii
                        .replace(",", ".");        // înlocuiește virgulă cu punct
                return Double.parseDouble(raw);
            }
        } catch (Exception e) {
            return 0.0;
        }
    }


    private static String sanitizeHsCode(String rawCode) {
        // Elimină orice caracter care nu e cifră
        String digits = rawCode.replaceAll("[^0-9]", "");

        // Păstrează doar primele 6 caractere (dacă sunt mai multe)
        if (digits.length() >= 6) {
            return digits.substring(0, 6);
        }

        // Dacă are mai puțin de 6, completează cu zero-uri la final
        return String.format("%-6s", digits).replace(' ', '0');
    }

}
