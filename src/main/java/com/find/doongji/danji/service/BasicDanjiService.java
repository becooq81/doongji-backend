package com.find.doongji.danji.service;

import com.find.doongji.address.util.AddressUtil;
import com.find.doongji.danji.client.DanjiClient;
import com.find.doongji.danji.payload.request.DanjiEntity;
import com.find.doongji.danji.repository.DanjiRepository;
import com.find.doongji.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicDanjiService implements DanjiService {

    private final DanjiRepository danjiRepository;
    private final LocationRepository locationRepository;
    private final DanjiClient danjiClient;

    @Override
    @Transactional
    public void loadDanji() throws Exception {
        if (danjiRepository.checkIfTableExists() != 0) {
            System.out.println("Danji table already exists");
            return;
        }

        System.out.println("Start loading danji table: " + System.currentTimeMillis());

        List<String> bjdCodes = locationRepository.selectAllDongCode();
        List<DanjiEntity> responses = danjiClient.fetchItemsForBjdCodes(bjdCodes);

        List<DanjiEntity> entities = responses.stream()
                .map(item -> DanjiEntity.builder()
                        .as1(AddressUtil.cleanAddress(item.getAs1()))
                        .as2(item.getAs2())
                        .as3(item.getAs3())
                        .bjdCode(item.getBjdCode())
                        .kaptCode(item.getKaptCode())
                        .kaptName(item.getKaptName())
                        .build())
                .toList();

        danjiRepository.bulkInsertDanji(entities);

        System.out.println("End loading danji table: " + System.currentTimeMillis());
    }
}
