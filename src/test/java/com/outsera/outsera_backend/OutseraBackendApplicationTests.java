package com.outsera.outsera_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsera.outsera_backend.dto.IntervaloDTO;
import com.outsera.outsera_backend.dto.IntervaloRetornoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OutseraBackendApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void deveRetornarIntervalosCorretosParaCsvPadrao() throws IOException {

        IntervaloRetornoDTO esperado = carregarJsonEsperado("esperado/retorno_esperado.json");

        ResponseEntity<IntervaloRetornoDTO> resposta = restTemplate.getForEntity("/produtor",
                IntervaloRetornoDTO.class);

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        IntervaloRetornoDTO resultado = resposta.getBody();
        assertNotNull(resultado);

        assertNotNull(resultado.getMin());
        assertNotNull(esperado.getMin());
        assertEquals(esperado.getMin().size(), resultado.getMin().size());

        for (int i = 0; i < esperado.getMin().size(); i++) {

            assertEquals(esperado.getMin().get(i).getProducer(), resultado.getMin().get(i).getProducer());
            assertEquals(esperado.getMin().get(i).getInterval(), resultado.getMin().get(i).getInterval());
            assertEquals(esperado.getMin().get(i).getPreviousWin(), resultado.getMin().get(i).getPreviousWin());
            assertEquals(esperado.getMin().get(i).getFollowingWin(), resultado.getMin().get(i).getFollowingWin());
        }

        assertNotNull(resultado.getMax());
        assertNotNull(esperado.getMax());
        assertEquals(esperado.getMax().size(), resultado.getMax().size());

        for (int i = 0; i < esperado.getMax().size(); i++) {

            assertEquals(esperado.getMax().get(i).getProducer(), resultado.getMax().get(i).getProducer());
            assertEquals(esperado.getMax().get(i).getInterval(), resultado.getMax().get(i).getInterval());
            assertEquals(esperado.getMax().get(i).getPreviousWin(), resultado.getMax().get(i).getPreviousWin());
            assertEquals(esperado.getMax().get(i).getFollowingWin(), resultado.getMax().get(i).getFollowingWin());
        }
    }

    private IntervaloRetornoDTO carregarJsonEsperado(String caminhoRecurso) throws IOException {

        ClassPathResource recurso = new ClassPathResource(caminhoRecurso);
        try (InputStream inputStream = recurso.getInputStream()) {

            return objectMapper.readValue(inputStream, IntervaloRetornoDTO.class);
        }
    }
}
