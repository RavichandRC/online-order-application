package com.techrc.productservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
}
