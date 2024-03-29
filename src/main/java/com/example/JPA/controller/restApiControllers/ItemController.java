package com.example.JPA.controller.restApiControllers;

import com.example.JPA.model.Item;
import com.example.JPA.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping()
    public ResponseEntity<List<Item>> getAllItem(){
        return new ResponseEntity<>(itemService.getAllItem(),HttpStatus.OK);
    }

}
