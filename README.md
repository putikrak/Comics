# Comics - Microservicios

Proyecto de microservicios para gestión de comics con Spring Boot.

## Arquitectura

El proyecto está compuesto por 3 microservicios independientes que se comunican entre sí mediante REST (RestTemplate):

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  carrito-service │◄────│ pedidos-service  │────►│  envios-service  │
│    (port 8081)   │     │   (port 8082)    │     │   (port 8083)    │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

### carrito-service (Puerto 8081)
Gestiona el carrito de compras de cada usuario.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/carro/usuario/{usuarioId}` | Obtener items del carrito |
| POST | `/carro/agregar` | Agregar item al carrito |
| DELETE | `/carro/{id}` | Eliminar item del carrito |
| DELETE | `/carro/vaciar/{usuarioId}` | Vaciar carrito del usuario |
| GET | `/carro/total/{usuarioId}` | Obtener total del carrito |

### pedidos-service (Puerto 8082)
Gestiona los pedidos. Se conecta con carrito-service y envios-service.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/pedidos/checkout/{usuarioId}?direccionEnvio=...` | Realizar pedido (obtiene items del carrito, crea envío, vacía carrito) |
| GET | `/api/pedidos/{id}` | Ver pedido por ID |
| GET | `/api/pedidos/usuario/{usuarioId}` | Pedidos por usuario |
| GET | `/api/pedidos/estado/{estado}` | Pedidos por estado |
| PUT | `/api/pedidos/{id}/estado` | Actualizar estado del pedido |
| GET | `/api/pedidos/{id}/envio` | Consultar envío del pedido |

### envios-service (Puerto 8083)
Gestiona los envíos asociados a los pedidos.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/envios` | Crear envío |
| GET | `/api/envios/{id}` | Buscar envío por ID |
| GET | `/api/envios/pedido/{pedidoId}` | Buscar envíos por pedido |
| GET | `/api/envios/usuario/{usuarioId}` | Buscar envíos por usuario |
| GET | `/api/envios/estado/{estado}` | Buscar envíos por estado |
| PUT | `/api/envios/{id}/estado?estado=...` | Actualizar estado del envío |
| GET | `/api/envios` | Listar todos los envíos |

## Flujo de Checkout

1. El usuario agrega items al carrito (`carrito-service`)
2. El usuario realiza checkout (`pedidos-service`)
3. `pedidos-service` consulta los items del carrito desde `carrito-service`
4. `pedidos-service` crea el pedido con el total calculado
5. `pedidos-service` solicita la creación de un envío a `envios-service`
6. `pedidos-service` vacía el carrito del usuario en `carrito-service`

## Requisitos

- Java 17
- Maven 3.8+

## Ejecución

Iniciar cada microservicio en una terminal separada:

```bash
# Terminal 1 - Carrito
cd carrito-service && ./mvnw spring-boot:run

# Terminal 2 - Envíos
cd envios-service && ./mvnw spring-boot:run

# Terminal 3 - Pedidos
cd pedidos-service && ./mvnw spring-boot:run
```

## Base de Datos

Por defecto usa H2 en memoria para desarrollo. Para producción con SQL Server, descomentar la configuración en cada `application.properties`.
