package com.pedidos.comic.client;

import com.pedidos.comic.dto.ItemCarroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "carrito-service", url = "${microservicio.carrito.url}")
public interface CarritoClient {

    @GetMapping("/carro/usuario/{usuarioId}")
    List<ItemCarroDTO> obtenerItemsCarrito(@PathVariable("usuarioId") String usuarioId);

    @DeleteMapping("/carro/vaciar/{usuarioId}")
    void vaciarCarrito(@PathVariable("usuarioId") String usuarioId);
}
