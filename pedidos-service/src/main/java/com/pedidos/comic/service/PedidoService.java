package com.pedidos.comic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pedidos.comic.client.CarritoClient;
import com.pedidos.comic.client.EnvioClient;
import com.pedidos.comic.dto.ItemCarroDTO;
import com.pedidos.comic.dto.EnvioDTO;
import com.pedidos.comic.model.Pedido;
import com.pedidos.comic.repository.PedidoRepository;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private CarritoClient carritoClient;

    @Autowired
    private EnvioClient envioClient;

    public Pedido finalizarCompra(String usuarioId, String direccionEnvio) {
        List<ItemCarroDTO> itemsCarro = carritoClient.obtenerItemsCarrito(usuarioId);

        if (itemsCarro == null || itemsCarro.isEmpty()) {
            throw new RuntimeException("El carrito esta vacio para el usuario: " + usuarioId);
        }

        double total = itemsCarro.stream()
                .mapToDouble(ItemCarroDTO::getSubtotal)
                .sum();

        Pedido nuevoPedido = new Pedido(usuarioId, "PROCESANDO");
        nuevoPedido.setTotal(total);
        nuevoPedido.setDireccionEnvio(direccionEnvio);
        Pedido pedidoGuardado = pedidoRepo.save(nuevoPedido);

        EnvioDTO envioRequest = new EnvioDTO();
        envioRequest.setPedidoId(pedidoGuardado.getId());
        envioRequest.setUsuarioId(usuarioId);
        envioRequest.setDireccionDestino(direccionEnvio);
        EnvioDTO envioCreado = envioClient.crearEnvio(envioRequest);

        if (envioCreado != null) {
            pedidoGuardado.setEnvioId(envioCreado.getId());
            pedidoGuardado.setEstado("CONFIRMADO");
            pedidoGuardado = pedidoRepo.save(pedidoGuardado);
        }

        carritoClient.vaciarCarrito(usuarioId);

        return pedidoGuardado;
    }

    public Pedido buscarPedido(Long id) {
        return pedidoRepo.findById(id).orElse(null);
    }

    public List<Pedido> buscarPorUsuario(String usuarioId) {
        return pedidoRepo.findByUsuarioId(usuarioId);
    }

    public List<Pedido> buscarPorEstado(String estado) {
        return pedidoRepo.findByEstado(estado);
    }

    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepo.findById(id).orElse(null);
        if (pedido != null) {
            pedido.setEstado(nuevoEstado);
            return pedidoRepo.save(pedido);
        }
        return null;
    }

    public EnvioDTO consultarEnvio(Long pedidoId) {
        List<EnvioDTO> envios = envioClient.buscarPorPedido(pedidoId);
        if (envios != null && !envios.isEmpty()) {
            return envios.get(0);
        }
        return null;
    }
}
