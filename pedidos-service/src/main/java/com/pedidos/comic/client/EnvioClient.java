package com.pedidos.comic.client;

import com.pedidos.comic.dto.EnvioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "envios-service", url = "${microservicio.envios.url}")
public interface EnvioClient {

    @PostMapping("/api/envios")
    EnvioDTO crearEnvio(@RequestBody EnvioDTO envio);

    @GetMapping("/api/envios/pedido/{pedidoId}")
    List<EnvioDTO> buscarPorPedido(@PathVariable("pedidoId") Long pedidoId);
}
