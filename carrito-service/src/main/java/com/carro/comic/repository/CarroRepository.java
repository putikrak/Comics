package com.carro.comic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.carro.comic.model.ItemCarro;

import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<ItemCarro, Long> {
    List<ItemCarro> findByUsuarioId(String usuarioId);
    void deleteByUsuarioId(String usuarioId);
}
