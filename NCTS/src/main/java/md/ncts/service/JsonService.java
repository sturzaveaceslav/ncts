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
    private static double roundTo2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


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
            List<HouseItem> items,
            File saveFile,
            boolean onlyFirstHasGrossMass
    ) {
        try {
            List<Map<String, Object>> houseItems = new ArrayList<>();
            int index = 1;
            for (HouseItem hi : items) {
                Map<String, Object> item = new LinkedHashMap<>();
                // Setăm greutatea brută corect doar pentru prima poziție
                double itemGrossMass;
                if (onlyFirstHasGrossMass) {
                    itemGrossMass = (index == 1) ? hi.getGrossMass() : 0;
                } else {
                    itemGrossMass = hi.getGrossMass();
                }

                item.put("additionalInfo", new ArrayList<>());
                item.put("additionalRef", new ArrayList<>());
                item.put("chainAddActors", new ArrayList<>());
                item.put("dangerousGoods", new ArrayList<>());
                item.put("decItmNber", index);
                item.put("goodsDescription", hi.getDescription());
                item.put("grossMass", roundTo2Decimals(itemGrossMass));
                item.put("hsCode", hi.getHsCode());
                item.put("itemTaxes", Map.of(
                        "currency", "EUR",
                        "quantity1", (int) hi.getQuantity(),
                        "quantity2", 0,
                        "statisticalValue", roundTo2Decimals(hi.getStatisticalValue())
                ));

                Map<String, Object> packType = new LinkedHashMap<>();
                packType.put("code", "ZZ");
                packType.put("name", "definitie comuna");
                packType.put("packageKind", "PACKED");

                Map<String, Object> packaging = new LinkedHashMap<>();
                packaging.put("packType", packType);
                packaging.put("packageNumber", (index == 1) ? 1 : 0);
                packaging.put("sequence", 1);
                packaging.put("shippingMark", "F/M");

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

            root.put("applicant", Map.of(
                    "EORINumber", representative.getCui(),
                    "name", representative.getName(),
                    "street", representative.getStreet(),
                    "city", representative.getCity(),
                    "postcode", representative.getPostcode(),
                    "country", representative.getCountry()
            ));

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

            root.put("consignor", Map.of(
                    "EORINumber", exporter.getEori(),
                    "name", exporter.getName(),
                    "street", exporter.getStreet(),
                    "city", exporter.getCity(),
                    "postcode", exporter.getPostcode(),
                    "country", exporter.getCountry()
            ));

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

            root.put("grossMass", roundTo2Decimals(grossMass));
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

            root.put("representative", Map.of(
                    "name", representative.getName(),
                    "street", representative.getStreet(),
                    "city", representative.getCity(),
                    "postcode", representative.getPostcode(),
                    "country", representative.getCountry(),
                    "cui", representative.getCui()
            ));
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
