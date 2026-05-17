package com.envios.comic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.envios.comic.model.Envio;
import com.envios.comic.repository.EnvioRepository;

import java.util.List;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository repository;

    public Envio crearEnvio(Envio envio) {
        return repository.save(envio);
    }

    public Envio buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Envio> buscarPorPedido(Long pedidoId) {
        return repository.findByPedidoId(pedidoId);
    }

    public List<Envio> buscarPorUsuario(String usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Envio> buscarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public Envio actualizarEstado(Long id, String nuevoEstado) {
        Envio envio = repository.findById(id).orElse(null);
        if (envio != null) {
            envio.setEstado(nuevoEstado);
            return repository.save(envio);
        }
        return null;
    }

    public List<Envio> listarTodos() {
        return repository.findAll();
    }
}
