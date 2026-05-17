package com.envios.comic.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.envios.comic.model.Envio;
import com.envios.comic.service.EnvioService;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService service;

    @PostMapping
    public Envio crearEnvio(@Valid @RequestBody Envio envio) {
        return service.crearEnvio(envio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> buscarPorId(@PathVariable Long id) {
        Envio envio = service.buscarPorId(id);
        if (envio != null) {
            return ResponseEntity.ok(envio);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<Envio> buscarPorPedido(@PathVariable Long pedidoId) {
        return service.buscarPorPedido(pedidoId);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Envio> buscarPorUsuario(@PathVariable String usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<Envio> buscarPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Envio> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Envio envio = service.actualizarEstado(id, estado);
        if (envio != null) {
            return ResponseEntity.ok(envio);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Envio> listarTodos() {
        return service.listarTodos();
    }
}
