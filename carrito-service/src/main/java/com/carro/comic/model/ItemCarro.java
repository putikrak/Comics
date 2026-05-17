package com.carro.comic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "items_carro")
public class ItemCarro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @NotNull(message = "El comicId es obligatorio")
    private Long comicId;

    @NotBlank(message = "El nombre del comic es obligatorio")
    private String nombreComic;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    @Positive(message = "El precio unitario debe ser mayor a 0")
    private double precioUnitario;

    private double subtotal;

    public ItemCarro() {
    }

    public ItemCarro(String usuarioId, Long comicId, String nombreComic, int cantidad, double precioUnitario) {
        this.usuarioId = usuarioId;
        this.comicId = comicId;
        this.nombreComic = nombreComic;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario * cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getComicId() {
        return comicId;
    }

    public void setComicId(Long comicId) {
        this.comicId = comicId;
    }

    public String getNombreComic() {
        return nombreComic;
    }

    public void setNombreComic(String nombreComic) {
        this.nombreComic = nombreComic;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
