package com.envios.comic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.envios.comic.model.Envio;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByPedidoId(Long pedidoId);
    List<Envio> findByUsuarioId(String usuarioId);
    List<Envio> findByEstado(String estado);
}
