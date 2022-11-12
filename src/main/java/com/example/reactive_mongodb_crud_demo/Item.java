package com.example.reactive_mongodb_crud_demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Item {
    private @Id Long id;
    private Integer price;
}
