package com.outsera.outsera_backend.controller;

import com.outsera.outsera_backend.dto.IntervaloRetornoDTO;
import com.outsera.outsera_backend.service.ProdutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtor")
@RequiredArgsConstructor
public class ProdutorController {

    private final ProdutorService produtorService;

    @GetMapping
    public ResponseEntity<IntervaloRetornoDTO> obterProdutores() {

        return ResponseEntity.ok(produtorService.obterIntervalosProdutor());
    }
}
