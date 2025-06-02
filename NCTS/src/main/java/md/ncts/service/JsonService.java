package md.ncts.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.Alert;
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
            boolean onlyFirstHasGrossMass,
            String supportingDocRef,
            String supportingDocType,
            String transportDocRef,
            String transportDocType,
            String dispatchName,
            String dispatchCode
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
                        "quantity1", hi.getItemTaxes().getQuantity1(),
                        "statisticalValue", roundTo2Decimals(hi.getItemTaxes().getStatisticalValue()),
                        "quantity2", 0
                ));


                Map<String, Object> packType = new LinkedHashMap<>();
                packType.put("code", "ZZ");
                packType.put("name", "definitie comuna");
                packType.put("packageKind", "PACKED");

                List<Map<String, Object>> packagingList = new ArrayList<>();
                for (Packaging p : hi.getPackagings()) {
                    Map<String, Object> packaging = new LinkedHashMap<>();
                    packaging.put("packType", packType);
                    packaging.put("packageNumber", p.getPackageNumber()); // ✅ folosește din obiect
                    packaging.put("sequence", p.getSequence());
                    packaging.put("shippingMark", p.getShippingMarks());
                    packagingList.add(packaging);
                }
                item.put("packagings", packagingList);


                item.put("previousDoc", new ArrayList<>());
                item.put("sequence", index);
                item.put("supportingDoc", new ArrayList<>());
                item.put("transportDoc", new ArrayList<>());
                item.put("countryOfDispatch", Map.of(
                        "code", hi.getDispatchCountryCode() != null ? hi.getDispatchCountryCode() : "",
                        "name", hi.getDispatchCountryName() != null ? hi.getDispatchCountryName() : ""
                ));



                houseItems.add(item);
                index++;
            }

            Map<String, Object> root = new LinkedHashMap<>();
            // Supporting Doc
            Map<String, Object> supportingDoc = new LinkedHashMap<>();
            supportingDoc.put("docClass", "SUPPORTING_DOC");
            supportingDoc.put("itmNber", 1);
            supportingDoc.put("sequence", 1);
            supportingDoc.put("reference", supportingDocRef);
            supportingDoc.put("docType", supportingDocType);
            root.put("supportingDoc", List.of(supportingDoc));

            // Transport Doc
            Map<String, Object> transportDoc = new LinkedHashMap<>();
            transportDoc.put("docType", transportDocType);
            transportDoc.put("reference", transportDocRef);
            transportDoc.put("docClass", "TRANSPORT_DOC");
            transportDoc.put("sequence", 1);
            root.put("transportDoc", List.of(transportDoc));

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
            root.put("dispatch", Map.of("code", dispatchCode, "name", dispatchName));
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

            root.put("ucrReference", "002");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(saveFile)) {
                gson.toJson(root, writer);
                System.out.println("✅ JSON salvat în: " + saveFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();

                // Afișează și un dialog vizual
                javafx.application.Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eroare la salvare JSON");
                    alert.setHeaderText("Nu s-a putut salva fișierul!");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
