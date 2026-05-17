package com.carro.comic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carro.comic.model.ItemCarro;
import com.carro.comic.service.CarroService;

import java.util.List;

@RestController
@RequestMapping("/carro")
public class CarroController {

    @Autowired
    private CarroService service;

    @GetMapping("/usuario/{usuarioId}")
    public List<ItemCarro> obtenerCarro(@PathVariable String usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @PostMapping("/agregar")
    public ItemCarro agregar(@RequestBody ItemCarro item) {
        return service.agregarAlCarro(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vaciar/{usuarioId}")
    public ResponseEntity<Void> vaciarCarro(@PathVariable String usuarioId) {
        service.vaciarCarroUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total/{usuarioId}")
    public double obtenerTotal(@PathVariable String usuarioId) {
        return service.calcularTotal(usuarioId);
    }
}
