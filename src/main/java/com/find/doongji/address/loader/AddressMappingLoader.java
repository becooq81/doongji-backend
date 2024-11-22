package com.find.doongji.address.loader;

import com.find.doongji.address.payload.request.AddressMapping;
import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.repository.AptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressMappingLoader implements CommandLineRunner {

    private static final int BATCH_SIZE = 100;
    private final AddressRepository addressRepository;
    private final AptRepository aptRepository;

    @Override
    public void run(String... args) throws Exception {
        if (addressRepository.checkIfMappingTableExists() == 0) {
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
                String roadAddress = columns[1];
                String oldAddress = columns[2];
                Long danjiId = Long.parseLong(columns[0]);

                String[] addressParts = oldAddress.split(" ");
                if (addressParts.length < 2) continue;

                String umdNm = addressParts[addressParts.length - 2];
                String jibun = addressParts[addressParts.length - 1];

                List<AptInfo> aptInfoList = aptRepository.selectAptInfoByUmdNmAndJibun(umdNm, jibun);
                for (AptInfo aptInfo : aptInfoList) {
                    AddressMapping addressMapping = AddressMapping.builder()
                            .oldAddress(oldAddress)
                            .umdNm(umdNm)
                            .jibun(jibun)
                            .aptSeq(aptInfo.getAptSeq())
                            .danjiId(danjiId)
                            .bjdCode(aptInfo.getSggCd() + aptInfo.getUmdCd())
                            .roadNm(aptInfo.getRoadNm())
                            .roadNmBonbun(aptInfo.getRoadNmBonbun())
                            .roadNmBubun(aptInfo.getRoadNmBubun())
                            .roadAddress(roadAddress)
                            .aptNm(aptInfo.getAptNm())
                            .build();
                    mappings.add(addressMapping);
                    if (mappings.size() == BATCH_SIZE) {
                        addressRepository.bulkInsertAddressMapping(mappings);
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
