package com.btgpactual.btg_investment_api.controller;
import com.btgpactual.btg_investment_api.model.Fund;
import com.btgpactual.btg_investment_api.service.FundService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/funds")
public class FundController {
    @Autowired
    private FundService fundService;

    @GetMapping
    @Operation(summary = "Consulta todos los Fondos creados. (Enviar Bearer token en cabecera)")
    public ResponseEntity<List<Fund>> getAllFunds() {
        return ResponseEntity.ok(fundService.getAllFunds());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener fondo por ID.")
    public ResponseEntity<Fund> getFundById(@PathVariable String id) {
        return fundService.getFundById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear Fondo por ID.(Enviar Bearer token en cabecera)")
    public ResponseEntity<Fund> createFund(@RequestBody Fund fund) {
        return ResponseEntity.ok(fundService.createFund(fund));
    }
}
