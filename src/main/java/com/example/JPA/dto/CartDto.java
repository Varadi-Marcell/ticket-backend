package com.example.JPA.dto;

import com.example.JPA.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Double amount;
    private List<ItemDto> itemList;
}
