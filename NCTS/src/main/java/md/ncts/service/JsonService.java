package md.ncts.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import md.ncts.model.*;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JsonService {

    public static class ItemRow {
        public String cod_Tarifar;
        public String Description;
        public String unit_masura;
        public double Cantitate;
        public double Suma;
        public double NETT;
        public double BRUTT;
    }

    public static void generateJson(
            Exporter exporter,
            Contact contact,
            Consignee consignee,
            Representative representative,
            String truckNumber,
            String departureOffice,
            String destinationOffice,
            double grossMass,
            double invoiceValue,
            String guaranteeNumber,
            String guaranteeCode,
            double guaranteeAmount,
            String packageType,
            String shippingMarks,
            File excelFile,
            File saveFile
    ) {
        try {
            List<ItemRow> rows = ExcelService.readRows(excelFile);
            Map<String, List<ItemRow>> grouped = rows.stream()
                    .collect(Collectors.groupingBy(r -> r.cod_Tarifar));

            List<Map<String, Object>> houseItems = new ArrayList<>();
            int index = 1;
            for (String hsCode : grouped.keySet()) {
                List<ItemRow> group = grouped.get(hsCode);
                String desc = group.get(0).Description;
                double qty = group.stream().mapToDouble(r -> r.Cantitate).sum();
                double val = group.stream().mapToDouble(r -> r.Suma).sum();
                double nett = group.stream().mapToDouble(r -> r.NETT).sum();
                double brut = group.stream().mapToDouble(r -> r.BRUTT).sum();

                Map<String, Object> item = new LinkedHashMap<>();
                item.put("additionalInfo", new ArrayList<>());
                item.put("additionalRef", new ArrayList<>());
                item.put("chainAddActors", new ArrayList<>());
                item.put("dangerousGoods", new ArrayList<>());
                item.put("decItmNber", index);
                item.put("goodsDescription", desc);
                item.put("grossMass", brut);
                item.put("hsCode", hsCode);

                Map<String, Object> taxes = new LinkedHashMap<>();
                taxes.put("currency", "EUR");
                taxes.put("quantity1", (int) qty);
                taxes.put("quantity2", 0);
                taxes.put("statisticalValue", val);
                item.put("itemTaxes", taxes);

                Map<String, Object> packType = Map.of(
                        "code", packageType,
                        "name", "definitie comuna",
                        "packageKind", "PACKED"
                );

                Map<String, Object> packaging = new LinkedHashMap<>();
                packaging.put("packType", packType);
                packaging.put("packageNumber", index == 1 ? 1 : 0);
                packaging.put("sequence", 1);
                packaging.put("shippingMark", shippingMarks);
                item.put("packagings", List.of(packaging));

                item.put("previousDoc", new ArrayList<>());
                item.put("sequence", index);
                item.put("supportingDoc", new ArrayList<>());
                item.put("transportDoc", new ArrayList<>());
                houseItems.add(item);
                index++;
            }

            Map<String, Object> root = new LinkedHashMap<>();
            root.put("addDecType", Map.of("code", "A", "name", "Standard customs declaration "));
            root.put("additionalInfo", new ArrayList<>());
            root.put("additionalRef", new ArrayList<>());

            Map<String, Object> applicant = Map.of(
                    "EORINumber", representative.getCui(),
                    "name", representative.getName(),
                    "street", representative.getStreet(),
                    "city", representative.getCity(),
                    "postcode", representative.getPostcode(),
                    "country", representative.getCountry()
            );
            root.put("applicant", applicant);


            root.put("applicantContact", Map.of(
                    "contactFor", "APPLICANT",
                    "name", contact.getName(),
                    "phone", contact.getPhone(),
                    "email", contact.getEmail()
            ));

            root.put("attachments", new ArrayList<>());
            root.put("authorisations", new ArrayList<>());
            root.put("bindingItinerary", Map.of("code", 0, "name", "No"));
            root.put("borderMot", Map.of("code", 3, "mode", "BORDER", "name", "Road transport"));
            root.put("borderTransport", List.of(Map.of(
                    "custOffice", Map.of("code", departureOffice, "name", "LEUSENI (PVFI, rutier)"),
                    "idType", Map.of("code", 30, "description", "Registration Number of the Road Vehicle"),
                    "identificationNumber", truckNumber,
                    "nationality", "MD",
                    "sequence", 1,
                    "transportType", "BORDER"
            )));
            root.put("chainAddActors", new ArrayList<>());
            Map<String, Object> consignor = Map.of(
                    "EORINumber", exporter.getEori(),
                    "name", exporter.getName(),
                    "street", exporter.getStreet(),
                    "city", exporter.getCity(),
                    "postcode", exporter.getPostcode(),
                    "country", exporter.getCountry()
            );
            root.put("consignor", consignor);

            root.put("consignee", Map.of(
                    "name", consignee.getName(),
                    "street", consignee.getStreet(),
                    "city", consignee.getCity(),
                    "postcode", consignee.getPostcode(),
                    "country", consignee.getCountry()
            ));

            root.put("customsOffice", Map.of("code", departureOffice, "name", "LEUSENI (PVFI, rutier)", "riskEnabled", false));
            root.put("declarationType", "T1");
            root.put("departureOffice", Map.of("code", departureOffice, "name", "LEUSENI (PVFI, rutier)", "riskEnabled", false));
            root.put("destinationOffice", Map.of("code", destinationOffice, "name", "CHISINAU2 (PVI, Cricova)"));
            root.put("dispatch", Map.of("code", "RO", "name", "Romania"));
            root.put("goodsLocation", Map.of(
                    "customsOffice", Map.of("code", departureOffice, "name", "LEUSENI (PVFI, rutier)"),
                    "locationType", Map.of("code", "A", "description", "Designated Location"),
                    "qualifier", Map.of("code", "V", "description", "Customs Office Identifier")
            ));
            root.put("grossMass", grossMass);

            root.put("guarantees", List.of(Map.of(
                    "sequence", 1,
                    "guaranteeReferences", List.of(Map.of(
                            "accessCode", guaranteeCode,
                            "grn", guaranteeNumber,
                            "currency", "MDL",
                            "coveredAmount", guaranteeAmount,
                            "sequence", 1
                    )),
                    "type", Map.of(
                            "code", "1",
                            "description", "Comprehensive guarantee",
                            "comprehensive", true,
                            "withGrn", true,
                            "withRfc", true,
                            "monitoringFlag", 1
                    )
            )));

            root.put("houseConsignments", List.of(Map.of(
                    "sequence", 1,
                    "houseItems", houseItems,
                    "grossMass", grossMass
            )));

            root.put("languageAtDeparture", "RO");
            root.put("loadingPlace", Map.of("unCode", "ROCLJ"));

            Map<String, Object> representativeMap = Map.of(
                    "name", representative.getName(),
                    "street", representative.getStreet(),
                    "city", representative.getCity(),
                    "postcode", representative.getPostcode(),
                    "country", representative.getCountry(),
                    "cui", representative.getCui()
            );
            root.put("representative", representativeMap);
            root.put("representativeStatus", 2);

            root.put("supportingDoc", List.of(Map.of(
                    "docClass", "SUPPORTING_DOC",
                    "docType", "N380",
                    "itmNber", 1,
                    "reference", "RO1025002496",
                    "sequence", 1
            )));
            root.put("transportDoc", List.of(Map.of(
                    "docClass", "TRANSPORT_DOC",
                    "docType", "N760",
                    "reference", "MD 0850448",
                    "sequence", 1
            )));
            root.put("ucrReference", "002");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(saveFile)) {
                gson.toJson(root, writer);
            }

            Map<String, String> saved = new HashMap<>();
            saved.put("name", contact.getName());
            saved.put("phone", contact.getPhone());
            saved.put("email", contact.getEmail());
            saved.put("exporter", exporter.getName());
            saved.put("city", exporter.getCity());
            saved.put("street", exporter.getStreet());
            saved.put("postcode", exporter.getPostcode());
            saved.put("guaranteeCode", guaranteeCode);
            saved.put("guaranteeNumber", guaranteeNumber);
            saved.put("guaranteeAmount", String.valueOf(guaranteeAmount));

            Files.write(Paths.get("config.json"), gson.toJson(saved).getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
