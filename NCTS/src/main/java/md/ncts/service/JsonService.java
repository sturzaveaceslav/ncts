// ✅ JsonService.java - generează JSON complet, fără erori
package md.ncts.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import md.ncts.model.*;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class JsonService {
    public static void generateJson(
            Applicant applicant,
            ApplicantContact contact,
            Consignee consignee,
            Representative representative,
            List<Guarantee> guarantees,
            List<HouseConsignment> houseConsignments,
            BorderTransport borderTransport,
            String declarationType,
            String grossMass,
            String totalValue,
            String customsOfficeDeparture,
            String customsOfficeDestination,
            File saveFile
    ) {
        Map<String, Object> root = new LinkedHashMap<>();

        root.put("declarationType", declarationType);
        root.put("languageAtDeparture", "RO");
        root.put("declarationAcceptanceDateTime", "2024-01-01T10:00:00Z");
        root.put("departureCountry", "MD");
        root.put("customsOfficeOfDeparture", customsOfficeDeparture);
        root.put("customsOfficeOfDestination", customsOfficeDestination);
        root.put("departureOffice", Map.of("code", customsOfficeDeparture));
        root.put("destinationOffice", Map.of("code", customsOfficeDestination));
        root.put("customsOffice", Map.of("code", customsOfficeDeparture));

        root.put("applicant", Map.of(
                "name", applicant.getName(),
                "eoriNumber", applicant.getEoriNumber(),
                "address", Map.of(
                        "street", applicant.getStreet(),
                        "city", applicant.getCity(),
                        "country", applicant.getCountry()
                )
        ));

        root.put("applicantContact", Map.of(
                "contactFor", "APPLICANT",
                "name", contact.getName(),
                "phoneNumber", contact.getPhone(),
                "emailAddress", contact.getEmail()
        ));

        root.put("representative", Map.of(
                "name", representative.getName(),
                "eoriNumber", representative.getEoriNumber(),
                "address", Map.of(
                        "street", representative.getStreet(),
                        "city", representative.getCity(),
                        "country", representative.getCountry()
                )
        ));

        root.put("borderTransport", List.of(Map.of(
                "transportType", borderTransport.getTransportType(),
                "identificationNumber", borderTransport.getIdentificationNumber(),
                "identificationType", Map.of(
                        "code", borderTransport.getIdType().getCode(),
                        "description", borderTransport.getIdType().getDescription(),
                        "active", borderTransport.getIdType().isActive()
                ),

                "nationality", borderTransport.getNationality(),
                "sequence", 1
        )));

        root.put("consignee", Map.of(
                "name", consignee.getName(),
                "eoriNumber", consignee.getEoriNumber(),
                "address", Map.of(
                        "street", consignee.getStreet(),
                        "city", consignee.getCity(),
                        "country", consignee.getCountry()
                )
        ));

        root.put("consignor", Map.of(
                "name", applicant.getName(),
                "address", Map.of(
                        "street", applicant.getStreet(),
                        "city", applicant.getCity(),
                        "country", applicant.getCountry()
                )
        ));

        root.put("grossMass", Double.parseDouble(grossMass));
        root.put("statisticalValue", Map.of("value", Double.parseDouble(totalValue), "currency", "EUR"));

        // Guarantees
        List<Object> guaranteeList = new ArrayList<>();
        for (Guarantee g : guarantees) {
            Map<String, Object> gMap = new LinkedHashMap<>();
            gMap.put("sequenceNumber", g.getSequence());
            gMap.put("guaranteeTypeCode", g.getTypeCode());
            gMap.put("type", Map.of(
                    "code", g.getTypeCode(),
                    "description", g.getTypeCode().equals("1") ? "Comprehensive guarantee" : g.getTypeCode().equals("0") ? "None" : "Individual guarantee",
                    "comprehensive", g.getTypeCode().equals("1"),
                    "withGrn", true,
                    "withRfc", true,
                    "monitoringFlag", 1
            ));

            List<Object> refs = new ArrayList<>();
            for (GuaranteeReference ref : g.getReferences()) {
                refs.add(Map.of(
                        "sequenceNumber", ref.getSequence(),
                        "grn", ref.getGrn(),
                        "accessCode", ref.getAccessCode(),
                        "amount", Map.of("value", ref.getCoveredAmount(), "currency", ref.getCurrency())
                ));
            }
            gMap.put("guaranteeReferences", refs);
            guaranteeList.add(gMap);
        }
        root.put("guarantees", guaranteeList);

        // House Consignments
        List<Object> consList = new ArrayList<>();
        for (HouseConsignment c : houseConsignments) {
            Map<String, Object> cMap = new LinkedHashMap<>();
            cMap.put("sequenceNumber", c.getSequenceNumber());
            cMap.put("grossMass", c.getGrossMass());

            List<Object> items = new ArrayList<>();
            for (HouseItem hi : c.getHouseItems()) {
                Map<String, Object> iMap = new LinkedHashMap<>();
                iMap.put("sequenceNumber", hi.getSequenceNumber());
                iMap.put("commodity", Map.of("hsCode", hi.getHsCode(), "description", hi.getDescription()));
                iMap.put("goodsMeasure", Map.of(
                        "grossMass", hi.getGrossMass(),
                        "netMass", hi.getGrossMass() * 0.95,
                        "supplementaryUnits", hi.getQuantity()
                ));
                iMap.put("statisticalValue", Map.of("value", hi.getStatisticalValue(), "currency", "EUR"));

                List<Object> packaging = new ArrayList<>();
                for (Packaging p : hi.getPackagings()) {
                    packaging.add(Map.of(
                            "sequenceNumber", p.getSequence(),
                            "typeCode", p.getTypeCode(),
                            "numberOfPackages", p.getNumberOfPackages(),
                            "shippingMarks", p.getShippingMarks()
                    ));
                }
                iMap.put("packagings", packaging);
                items.add(iMap);
            }
            cMap.put("houseItems", items);
            consList.add(cMap);
        }
        root.put("houseConsignments", consList);

        try (FileWriter writer = new FileWriter(saveFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(root, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
