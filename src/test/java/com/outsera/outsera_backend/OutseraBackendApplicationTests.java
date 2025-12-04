package com.outsera.outsera_backend;

import com.outsera.outsera_backend.dto.IntervaloDTO;
import com.outsera.outsera_backend.dto.IntervaloRetornoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OutseraBackendApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveCarregarContexto() {
    }

    @Test
    void deveRetornarEstruturaBasicaDoEndpoint() {

        ResponseEntity<IntervaloRetornoDTO> resposta =
                restTemplate.getForEntity("/produtor", IntervaloRetornoDTO.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());

        IntervaloRetornoDTO resultado = resposta.getBody();

        validarEstruturaMinima(resultado);
    }

    private void validarEstruturaMinima(IntervaloRetornoDTO resultado) {

        assertNotNull(resultado.getMin(), "Lista de intervalos mínimos não deve ser nula");
        assertNotNull(resultado.getMax(), "Lista de intervalos máximos não deve ser nula");

        assertFalse(resultado.getMin().isEmpty(), "Lista de intervalos mínimos deve conter pelo menos um item");
        assertFalse(resultado.getMax().isEmpty(), "Lista de intervalos máximos deve conter pelo menos um item");

        validarCamposIntervalo(resultado.getMin().get(0), "mínimo");
        validarCamposIntervalo(resultado.getMax().get(0), "máximo");
    }

    private void validarCamposIntervalo(IntervaloDTO intervalo, String tipo) {

        assertNotNull(intervalo.getProducer(), String.format("Campo 'producer' do intervalo %s não deve ser nulo", tipo));
        assertNotNull(intervalo.getInterval(), String.format("Campo 'interval' do intervalo %s não deve ser nulo", tipo));
        assertNotNull(intervalo.getPreviousWin(), String.format("Campo 'previousWin' do intervalo %s não deve ser nulo", tipo));
        assertNotNull(intervalo.getFollowingWin(), String.format("Campo 'followingWin' do intervalo %s não deve ser nulo", tipo));
    }
}
