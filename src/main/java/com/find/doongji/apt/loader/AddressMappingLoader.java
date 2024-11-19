package com.find.doongji.apt.loader;

import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.payload.request.AddressMapping;
import com.find.doongji.apt.repository.AptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddressMappingLoader implements CommandLineRunner {

    private final AptRepository aptRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!(aptRepository.checkIfMappingTableExists() > 0)) {
            processCsvAndInsertMappings();
        } else {
            System.out.println("Table 'address_mapping' already exists. Skipping loader.");
        }
    }

    private void processCsvAndInsertMappings() {
        String filePath = "src/main/resources/review_data_mod.csv";
        List<AddressMapping> mappings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                String oldAddress = columns[2];

                // Extract last two words from the old address
                String[] addressParts = oldAddress.split(" ");
                if (addressParts.length < 2) continue;

                String umdNm = addressParts[addressParts.length - 2];
                String jibun = addressParts[addressParts.length - 1];

                // Fetch matching house info
                AptInfo houseInfo = aptRepository.selectAptInfoByUmdNmAndJibun(umdNm, jibun);
                if (houseInfo != null) {
                    mappings.add(new AddressMapping(oldAddress, umdNm, jibun, houseInfo.getAptSeq()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Bulk insert mappings
        if (!mappings.isEmpty()) {
            aptRepository.bulkInsertAddressMapping(mappings);
            System.out.println("Inserted " + mappings.size() + " records into 'address_mapping'.");
        } else {
            System.out.println("No valid mappings found to insert.");
        }
    }
}
