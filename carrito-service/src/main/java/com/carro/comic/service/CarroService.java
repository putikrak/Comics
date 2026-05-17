package com.carro.comic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.carro.comic.model.ItemCarro;
import com.carro.comic.repository.CarroRepository;

import java.util.List;

@Service
public class CarroService {

    @Autowired
    private CarroRepository repository;

    public List<ItemCarro> listarPorUsuario(String usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public ItemCarro agregarAlCarro(ItemCarro item) {
        item.setSubtotal(item.getPrecioUnitario() * item.getCantidad());
        return repository.save(item);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void vaciarCarroUsuario(String usuarioId) {
        repository.deleteByUsuarioId(usuarioId);
    }

    public double calcularTotal(String usuarioId) {
        List<ItemCarro> items = repository.findByUsuarioId(usuarioId);
        return items.stream().mapToDouble(ItemCarro::getSubtotal).sum();
    }
}
