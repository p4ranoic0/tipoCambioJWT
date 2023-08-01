package com.cambio.ep2.convertidor;

import com.cambio.ep2.configuration.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1")
public class ConvertidorController {


    @Autowired
    private ConvertidorService convertidorService;

    @GetMapping("tipo-cambio/token")
    public ResponseEntity<String> generatedToken (@RequestParam String from, @RequestParam String to)throws JwtException {
        try {
            return ResponseEntity.ok().body(convertidorService.generatedToken(from,to));
        } catch (JwtException e) {
            return ResponseEntity.status(e.getCode()).body(e.getMessage());
        }
    }

    @GetMapping("/tipo-cambio")
    public ResponseEntity<HashMap<String, Object>> getValue(@RequestHeader("Authorization") String authorization) throws JwtException{
        HashMap<String,Object> response= new HashMap<>();
        try {
            response.put("response",(Object) convertidorService.getDataChange(authorization));
            response.put("tipo-cambio",(Object) convertidorService.getDataChange(authorization).getRate());
            return ResponseEntity.ok().body(response);
        }catch (JwtException e) {
            response.put("Error",e.getMessage());
            return ResponseEntity.status(e.getCode()).body(response);
        }
    }
    @GetMapping("valor-cambio/token")
    public ResponseEntity<String> generatedTokenChange (@RequestParam String from, @RequestParam String to,@RequestParam String amount) throws JwtException{
        try {
            return ResponseEntity.ok().body(convertidorService.generatedTokenChange(from,to,amount));
        } catch (JwtException e) {
            return ResponseEntity.status(e.getCode()).body(e.getMessage());
        }
    }
    @GetMapping("/valor-cambio")
    public ResponseEntity<HashMap<String, Object>> getValueChange(@RequestHeader("Authorization") String authorization) throws JwtException{
        HashMap<String,Object> response= new HashMap<>();
        try {
            response.put("response",(Object) convertidorService.getValueChange(authorization));
            response.put("valor-cambio",(Object) convertidorService.getValueChange(authorization).getResult());
            return ResponseEntity.ok().body(response);
        }catch (JwtException e) {
            response.put("error",e.getMessage());
            return ResponseEntity.status(e.getCode()).body(response);
        }
    }


}
