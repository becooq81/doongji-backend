package com.find.doongji.danji.loader;

import com.find.doongji.danji.service.DanjiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DanjiLoader implements CommandLineRunner {

    private final DanjiService danjiService;

    @Override
    public void run(String... args) throws Exception {
        danjiService.loadDanji();
    }
}
