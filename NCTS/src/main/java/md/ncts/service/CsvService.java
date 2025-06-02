package md.ncts.service;

import md.ncts.model.HouseItem;
import md.ncts.model.ItemTaxes;
import md.ncts.model.Packaging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class CsvService {

    // ✅ Versiune cu grupare după cod tarifar
    public static List<HouseItem> readCsv(File file, String packageType, String shippingMark) {
        Map<String, HouseItem> grouped = new LinkedHashMap<>();
        int sequence = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                row++;
                if (row == 1) continue; // skip header

                String[] cols = line.split(";", -1); // sau ",", în funcție de formatul tău

                String hsCode = sanitizeHsCode(cols[0]);
                String desc = cols[1];
                double qty = parseDouble(cols[3]);
                double val = parseDouble(cols[4]);
                double nett = parseDouble(cols[5]);
                double brutt = parseDouble(cols[6]);

                HouseItem item = grouped.get(hsCode);
                if (item == null) {
                    ItemTaxes itemTaxes = new ItemTaxes("EUR", (int) qty, 0, val);
                    item = new HouseItem(
                            sequence++, hsCode, desc, brutt, nett, nett,
                            new ArrayList<>(List.of(
                                    new Packaging(packageType, (int) qty, shippingMark, 1)
                            )),
                            itemTaxes
                    );
                    grouped.put(hsCode, item);
                } else {
                    item.setGrossMass(item.getGrossMass() + brutt);
                    item.setNetMass(item.getNetMass() + nett);
                    item.setSupplementaryUnits(item.getSupplementaryUnits() + nett);
                    item.getItemTaxes().setQuantity1(item.getItemTaxes().getQuantity1() + (int) qty);
                    item.getItemTaxes().setStatisticalValue(item.getItemTaxes().getStatisticalValue() + val);
                    item.getPackagings().add(new Packaging(packageType, (int) qty, shippingMark, 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(grouped.values());
    }

    // ✅ Versiune simplă cu ItemRow
    public static List<JsonService.ItemRow> readCsvRows(File file) {
        List<JsonService.ItemRow> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                row++;
                if (row == 1) continue; // skip header

                String[] cols = line.split(";", -1);

                JsonService.ItemRow item = new JsonService.ItemRow();
                item.cod_Tarifar = cols[0];
                item.Description = cols[1];
                item.unit_masura = cols[2];
                item.Cantitate = parseDouble(cols[3]);
                item.Suma = parseDouble(cols[4]);
                item.NETT = parseDouble(cols[5]);
                item.BRUTT = parseDouble(cols[6]);

                rows.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    private static double parseDouble(String raw) {
        try {
            return Double.parseDouble(
                    raw.trim()
                            .replace(" ", "") // non-breaking space
                            .replace(".", "") // thousand separator
                            .replace(",", ".")
            );
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static String sanitizeHsCode(String rawCode) {
        String digits = rawCode.replaceAll("[^0-9]", "");
        if (digits.length() >= 6) return digits.substring(0, 6);
        return String.format("%-6s", digits).replace(' ', '0');
    }
}
