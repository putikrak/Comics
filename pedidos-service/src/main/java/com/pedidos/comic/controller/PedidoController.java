package com.pedidos.comic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pedidos.comic.dto.EnvioDTO;
import com.pedidos.comic.model.Pedido;
import com.pedidos.comic.service.PedidoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping("/checkout/{usuarioId}")
    public ResponseEntity<Pedido> realizarPedido(
            @PathVariable String usuarioId,
            @RequestParam(defaultValue = "Sin direccion") String direccionEnvio) {
        Pedido pedido = service.finalizarCompra(usuarioId, direccionEnvio);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> verPedido(@PathVariable Long id) {
        Pedido pedido = service.buscarPedido(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> pedidosPorUsuario(@PathVariable String usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<Pedido> pedidosPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Pedido pedido = service.actualizarEstado(id, body.get("estado"));
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/envio")
    public ResponseEntity<EnvioDTO> consultarEnvioPedido(@PathVariable Long id) {
        EnvioDTO envio = service.consultarEnvio(id);
        if (envio != null) {
            return ResponseEntity.ok(envio);
        }
        return ResponseEntity.notFound().build();
    }
}
