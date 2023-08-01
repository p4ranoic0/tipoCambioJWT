package com.cambio.ep2.convertidor;

import com.cambio.ep2.configuration.JwtException;
import com.cambio.ep2.configuration.JwtProvider;
import com.cambio.ep2.configuration.JwtProviderChange;
import com.cambio.ep2.response.ResponseTipoCambio;
import com.cambio.ep2.response.ResponseValorCambio;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ConvertidorService {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtProviderChange jwtProviderChange;

    @Autowired
    private RestTemplate restTemplate;

    private static String url = "https://api.exchangerate.host/convert?from={1}&to={2}";
    //https://api.exchangerate.host/convert?from=USD&to=PEN

    private static String url1 = "https://api.exchangerate.host/convert?amount={1}&from={2}&to={3}";

    public String generatedToken(String from, String to) throws JwtException{
        if(!from.equals("") && !to.equals("")){
            return jwtProvider.generatedJWT(from,to);
        }
        else{
            throw new JwtException(Constants.DATA_EMPTY,400);
        }
    }

    public ResponseTipoCambio getDataChange(String token){
        try {
            ConvertidorDTO dto = jwtProvider.readToken(token);
            Object objjson = restTemplate.getForObject(url, Object.class, dto.getFrom(), dto.getTo());

            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = mapper.convertValue(objjson, new TypeReference<Map<String,Object>>(){});
            Double rate = (Double) ((Map<String, Object>) map.get("info")).get("rate");

            return ResponseTipoCambio.builder()
                            .from(dto.getFrom())
                            .to(dto.getTo())
                            .amount(1)
                            .rate(rate)
                            .build();

        }catch (ExpiredJwtException e){
        throw  new JwtException(Constants.ERROR_MESSAGE_WHEN_JWT_EXPIRED,Constants.HTTP_CODE_JWT_EXPIRED);
    }
    }

    public String generatedTokenChange(String from, String to, String amount) throws JwtException{

        try {
            if(amount.equals("")){
                throw new JwtException(Constants.AMOUNT_EMPTY, Constants.AMOUNT_CODE_EMPTY);
            }
            if(!from.equals("") && !to.equals("")){
                return jwtProviderChange.generatedJWTChange(from,to,amount);
            }
            else{
                throw new JwtException(Constants.DATA_EMPTY, Constants.DATA_CODE_EMPTY);
            }
        }catch (JwtException e){
            throw new JwtException(e.getMessage(),e.getCode());
        }
    }

    public ResponseValorCambio getValueChange(String token){
        try {
            ConvertidorDTO dto = jwtProviderChange.readTokenChange(token);
            Object objjson = restTemplate.getForObject(url1, Object.class, dto.getAmount(),dto.getFrom(), dto.getTo());

            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = mapper.convertValue(objjson, new TypeReference<Map<String,Object>>(){});
            System.out.println(map);
            Double rate = (Double) ((Map<String, Object>) map.get("info")).get("rate");
            Double result = (Double) map.get("result");


            return ResponseValorCambio.builder()
                    .from(dto.getFrom())
                    .to(dto.getTo())
                    .amount(dto.getAmount().intValue())
                    .rate(rate)
                    .result(result)
                    .build();

        }catch (ExpiredJwtException e){
            throw  new JwtException(Constants.ERROR_MESSAGE_WHEN_JWT_EXPIRED,Constants.HTTP_CODE_JWT_EXPIRED);
        }
    }
}
