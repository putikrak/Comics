package com.pedidos.comic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
    private RestTemplate restTemplate;

    @Value("${microservicio.carrito.url}")
    private String carritoServiceUrl;

    @Value("${microservicio.envios.url}")
    private String enviosServiceUrl;

    public Pedido finalizarCompra(String usuarioId, String direccionEnvio) {
        // 1. Obtener items del carrito desde el microservicio de carrito
        List<ItemCarroDTO> itemsCarro = obtenerItemsCarrito(usuarioId);

        if (itemsCarro == null || itemsCarro.isEmpty()) {
            throw new RuntimeException("El carrito esta vacio para el usuario: " + usuarioId);
        }

        // 2. Calcular el total del pedido
        double total = itemsCarro.stream()
                .mapToDouble(ItemCarroDTO::getSubtotal)
                .sum();

        // 3. Crear el pedido
        Pedido nuevoPedido = new Pedido(usuarioId, "PROCESANDO");
        nuevoPedido.setTotal(total);
        nuevoPedido.setDireccionEnvio(direccionEnvio);
        Pedido pedidoGuardado = pedidoRepo.save(nuevoPedido);

        // 4. Crear envio en el microservicio de envios
        EnvioDTO envioCreado = crearEnvio(pedidoGuardado.getId(), usuarioId, direccionEnvio);
        if (envioCreado != null) {
            pedidoGuardado.setEnvioId(envioCreado.getId());
            pedidoGuardado.setEstado("CONFIRMADO");
            pedidoGuardado = pedidoRepo.save(pedidoGuardado);
        }

        // 5. Vaciar el carrito del usuario
        vaciarCarrito(usuarioId);

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

    // --- Comunicacion con microservicio de Carrito ---

    private List<ItemCarroDTO> obtenerItemsCarrito(String usuarioId) {
        String url = carritoServiceUrl + "/carro/usuario/" + usuarioId;
        ResponseEntity<List<ItemCarroDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ItemCarroDTO>>() {}
        );
        return response.getBody();
    }

    private void vaciarCarrito(String usuarioId) {
        String url = carritoServiceUrl + "/carro/vaciar/" + usuarioId;
        restTemplate.delete(url);
    }

    // --- Comunicacion con microservicio de Envios ---

    private EnvioDTO crearEnvio(Long pedidoId, String usuarioId, String direccionEnvio) {
        String url = enviosServiceUrl + "/api/envios";
        EnvioDTO envioRequest = new EnvioDTO();
        envioRequest.setPedidoId(pedidoId);
        envioRequest.setUsuarioId(usuarioId);
        envioRequest.setDireccionDestino(direccionEnvio);
        return restTemplate.postForObject(url, envioRequest, EnvioDTO.class);
    }

    public EnvioDTO consultarEnvio(Long pedidoId) {
        String url = enviosServiceUrl + "/api/envios/pedido/" + pedidoId;
        ResponseEntity<List<EnvioDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EnvioDTO>>() {}
        );
        List<EnvioDTO> envios = response.getBody();
        if (envios != null && !envios.isEmpty()) {
            return envios.get(0);
        }
        return null;
    }
}
