package com.find.doongji.apt.loader;

import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.payload.request.AddressMapping;
import com.find.doongji.apt.repository.AptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddressMappingLoader implements CommandLineRunner {

    private final AptRepository aptRepository;

    private static final int BATCH_SIZE = 100;

    @Override
    public void run(String... args) throws Exception {
        if (aptRepository.checkIfMappingTableExists() == 0) {
            System.out.println("Table 'address_mapping' does not exist. Loading data...");
            processCsvAndInsertMappings();
        } else {
            System.out.println("Table 'address_mapping' already exists. Skipping loader.");
        }
    }

    private void processCsvAndInsertMappings() {
        String filePath = "src/main/resources/unique_danji_id.csv";
        List<AddressMapping> mappings = new ArrayList<>();

        System.out.println("START Timestamp loading csv: " + System.currentTimeMillis());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("EUC-KR")))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                String oldAddress = columns[1];
                int danjiId = Integer.parseInt(columns[0]);

                String[] addressParts = oldAddress.split(" ");
                if (addressParts.length < 2) continue;

                String umdNm = addressParts[addressParts.length - 2];
                String jibun = addressParts[addressParts.length - 1];

                List<AptInfo> aptInfoList = aptRepository.selectAptInfoByUmdNmAndJibun(umdNm, jibun);
                for (AptInfo aptInfo : aptInfoList) {
                    mappings.add(new AddressMapping(oldAddress, umdNm, jibun, aptInfo.getAptSeq(), danjiId));
                    if (mappings.size() == BATCH_SIZE) {
                        aptRepository.bulkInsertAddressMapping(mappings);
                        System.out.println("Inserted " + mappings.size() + " records into 'address_mapping'.");
                        mappings.clear();
                    }
                }
            }
            System.out.println("END Timestamp loading csv: " + System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
