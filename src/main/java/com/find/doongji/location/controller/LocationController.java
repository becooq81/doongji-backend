package com.find.doongji.location.controller;

import com.find.doongji.location.payload.response.DongCode;
import com.find.doongji.location.payload.response.DongList;
import com.find.doongji.location.payload.response.GugunList;
import com.find.doongji.location.payload.response.SidoList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface LocationController {

    @GetMapping("/gugun")
    ResponseEntity<GugunList> getGugunList(@RequestParam("sidoName") String sidoName);

    @GetMapping("/dong")
    ResponseEntity<DongList> getDongList(@RequestParam("sidoName") String sidoName, @RequestParam("gugunName") String gugunName);


    @GetMapping("/dongcode")
    ResponseEntity<DongCode> getDongCode(@RequestParam("sidoName") String sidoName, @RequestParam("gugunName") String gugunName, @RequestParam("dongName") String dongName);

    @GetMapping("/sido")
    ResponseEntity<SidoList> getSidoList();

}
