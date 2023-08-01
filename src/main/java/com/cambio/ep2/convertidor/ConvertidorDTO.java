package com.cambio.ep2.convertidor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConvertidorDTO {
    private String from;
    private String to;

    private Double amount;
}
