package com.cambio.ep2.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseTipoCambio {
    private String from;
    private Integer amount;
    private String to;

    private Double rate;

}
