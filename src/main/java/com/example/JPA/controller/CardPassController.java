package com.example.JPA.controller;

import com.example.JPA.dto.FestivalCardpassDto;
import com.example.JPA.service.CardPassService;
import com.example.JPA.dto.FestivalCardPassAddDto;
import com.example.JPA.model.CardPass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cardPass")
public class CardPassController {

    private final CardPassService cardPassService;

    public CardPassController(CardPassService cardPassService){
        this.cardPassService = cardPassService;
    }

    @GetMapping
    public ResponseEntity<List<FestivalCardpassDto>> getAllFestivalCardPass(){
        return new ResponseEntity<>(cardPassService.getAllFestivalCardPass(), HttpStatus.OK);
    }

    @PostMapping
    public void createFestivalCardPass(@RequestBody FestivalCardPassAddDto request){
        cardPassService.createFestivalCardPass(request);
    }

    @PostMapping("/create2")
    public void createFestivalCardPass2(@RequestBody CardPass cardPass){
        cardPassService.create(cardPass);
    }

//    @DeleteMapping("{id}")
//    public ResponseEntity<Void> deletecreateFestivalCardPass(@PathVariable("id") Long id){
//        festivalCardPassService.deleteFestivalCard(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
