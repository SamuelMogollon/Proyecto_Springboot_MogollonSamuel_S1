# 📄 Documentación Técnica — LogiTrack

**Autor:** Samuel Mogollón  
**Fecha:** 2026  
**Versión:** 1.0.0  
**Repositorio:** [GitHub](https://github.com/SamuelMogollon/Proyecto_Springboot_MogollonSamuel_S1)

---

## 1. Descripción General

LogiTrack es un sistema backend centralizado desarrollado en Spring Boot para la gestión de bodegas, productos y movimientos de inventario de la empresa LogiTrack S.A. El sistema permite controlar entradas, salidas y transferencias entre bodegas, registrar auditorías automáticas de cada cambio y proteger la información mediante autenticación JWT.

---

## 2. Arquitectura del Sistema

El proyecto sigue una arquitectura en capas basada en el patrón MVC, organizada de la siguiente manera:

```
Cliente (Frontend HTML/CSS/JS)
        ↓
    Controllers (REST API)
        ↓
    Services (Lógica de negocio)
        ↓
    Repositories (Acceso a datos)
        ↓
    Base de datos (MySQL)
```

### Capas del sistema

**Controller** — Recibe las peticiones HTTP, valida los datos de entrada y delega la lógica al servicio correspondiente. Retorna respuestas JSON.

**Service** — Contiene toda la lógica de negocio. Valida reglas, gestiona el stock automáticamente y registra auditorías. Cada entidad tiene su interfaz y su implementación.

**Repository** — Extiende JpaRepository para el acceso a la base de datos. Incluye queries personalizadas con JPQL para consultas avanzadas.

**Model** — Entidades JPA que mapean las tablas de la base de datos con sus relaciones.

**DTO** — Objetos de transferencia de datos que separan la capa de presentación de la capa de persistencia. Hay DTOs de request (entrada) y response (salida).

**Mapper** — Componentes que convierten entre entidades y DTOs.

**Security** — Filtro JWT que intercepta cada petición, valida el token y establece el contexto de seguridad.

---

## 3. Diagrama de Clases

```
┌─────────────────┐       ┌─────────────────┐
│    Usuario      │       │     Bodega      │
├─────────────────┤       ├─────────────────┤
│ id: Long        │       │ id: Long        │
│ nombre: String  │◄──────│ nombre: String  │
│ email: String   │       │ ubicacion:String│
│ password: String│       │ capacidad: Int  │
│ rol: Enum       │       │ encargado:Usuar.│
└────────┬────────┘       └────────┬────────┘
         │                         │
         │                         │
┌────────▼────────┐       ┌────────▼────────┐
│   Movimiento    │       │ BodegaProducto  │
├─────────────────┤       ├─────────────────┤
│ id: Long        │       │ id: Long        │
│ fecha: DateTime │       │ bodega: Bodega  │
│ tipo: Enum      │       │ producto:Produc.│
│ usuario: Usuario│       │ stock: Integer  │
│ bodegaOrigen    │       └─────────────────┘
│ bodegaDestino   │
└────────┬────────┘       ┌─────────────────┐
         │                │    Producto     │
         │                ├─────────────────┤
┌────────▼────────┐       │ id: Long        │
│DetalleMovimiento│       │ nombre: String  │
├─────────────────┤       │ categoria:String│
│ id: Long        │◄──────│ precio: Decimal │
│ movimiento      │       └─────────────────┘
│ producto        │
│ cantidad: Int   │       ┌─────────────────┐
└─────────────────┘       │   Auditoria     │
                          ├─────────────────┤
                          │ id: Long        │
                          │ tipoOperacion   │
                          │ fechaHora       │
                          │ entidadAfectada │
                          │ valoresAnteriores│
                          │ valoresNuevos   │
                          │ usuario: Usuario│
                          └─────────────────┘
```

### Enumeraciones

**Usuario.Rol:** `ADMIN`, `EMPLEADO`

**Movimiento.TipoMovimiento:** `ENTRADA`, `SALIDA`, `TRANSFERENCIA`

**Auditoria.TipoOperacion:** `INSERT`, `UPDATE`, `DELETE`

---

## 4. Modelo de Base de Datos

```sql
usuario            → id, nombre, email, password, rol
bodega             → id, nombre, ubicacion, capacidad, encargado_id
producto           → id, nombre, categoria, precio
bodega_producto    → id, bodega_id, producto_id, stock
movimiento         → id, fecha, tipo, usuario_id, bodega_origen_id, bodega_destino_id
detalle_movimiento → id, movimiento_id, producto_id, cantidad
auditoria          → id, tipo_operacion, fecha_hora, entidad_afectada, valores_anteriores, valores_nuevos, usuario_id
```

---

## 5. Seguridad — JWT

### Flujo de autenticación

```
1. Cliente envía POST /auth/login con email y password
2. El servidor valida las credenciales en la base de datos
3. Si son correctas, genera un token JWT firmado con HS256
4. El cliente almacena el token en localStorage
5. En cada petición protegida el cliente envía el token en el header:
   Authorization: Bearer <token>
6. El JwtFilter intercepta la petición, valida el token y establece el contexto de seguridad
7. Si el token es inválido o expiró, retorna 401 Unauthorized
```

### Estructura del Token JWT

Un token JWT tiene tres partes separadas por puntos:

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW11ZWxAbG9naXRyYWNrLmNvbSIsInJvbCI6IkFETUlOIiwiaWF0IjoxNzA5MDAwMDAwLCJleHAiOjE3MDkwMDM2MDB9.firma
```

**Header:** Algoritmo de firma (HS256)  
**Payload:** Contiene el email del usuario, su rol y la fecha de expiración (1 hora)  
**Signature:** Firma digital generada con la clave secreta del servidor

### Ejemplo de uso

```http
POST /auth/login
Content-Type: application/json

{
  "email": "samuel@logitrack.com",
  "password": "123456"
}

Respuesta:
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}

Petición protegida:
GET /api/productos
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 6. Endpoints Principales

### Autenticación
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/auth/register` | Registrar usuario | No |
| POST | `/auth/login` | Iniciar sesión | No |

### Usuarios
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| GET | `/api/usuarios` | Listar usuarios | Sí |
| POST | `/api/usuarios` | Crear usuario | Sí |
| PUT | `/api/usuarios/{id}` | Actualizar usuario | Sí |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario | Sí |

### Bodegas
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| GET | `/api/bodegas` | Listar bodegas | Sí |
| POST | `/api/bodegas` | Crear bodega | Sí |
| PUT | `/api/bodegas/{id}` | Actualizar bodega | Sí |
| DELETE | `/api/bodegas/{id}` | Eliminar bodega | Sí |

### Productos
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| GET | `/api/productos` | Listar productos | Sí |
| POST | `/api/productos` | Crear producto | Sí |
| PUT | `/api/productos/{id}` | Actualizar producto | Sí |
| DELETE | `/api/productos/{id}` | Eliminar producto | Sí |
| GET | `/api/productos/stock-bajo` | Stock menor a 10 | Sí |

### Movimientos
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| GET | `/api/movimientos` | Listar movimientos | Sí |
| POST | `/api/movimientos` | Crear movimiento | Sí |
| GET | `/api/movimientos/{id}` | Buscar por ID | Sí |
| GET | `/api/movimientos/rango` | Filtrar por fechas | Sí |

### Auditorías
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| GET | `/api/auditorias` | Listar auditorías | Sí |
| GET | `/api/auditorias/usuario/{id}` | Por usuario | Sí |
| GET | `/api/auditorias/tipo/{tipo}` | Por tipo | Sí |

### Reportes
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| GET | `/api/reportes/resumen` | Stock por bodega y productos más movidos | Sí |

---

## 7. Manejo de Errores

El sistema usa un `GlobalExceptionHandler` con `@RestControllerAdvice` que captura todas las excepciones y retorna respuestas JSON estandarizadas:

```json
{
  "timestamp": "2026-03-16T18:00:00",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "El email ya está registrado"
}
```

| Código | Descripción |
|---|---|
| 400 | Datos inválidos o regla de negocio violada |
| 401 | Token inválido o expirado |
| 403 | Sin permisos para el recurso |
| 404 | Recurso no encontrado |
| 500 | Error interno del servidor |

---

## 8. Lógica de Stock

El stock no se almacena directamente en el producto sino en la tabla `bodega_producto`, que representa el stock de cada producto en cada bodega específica.

Cuando se crea un movimiento:

- **ENTRADA** — aumenta el stock en la bodega destino
- **SALIDA** — reduce el stock en la bodega origen
- **TRANSFERENCIA** — reduce en bodega origen y aumenta en bodega destino

El campo `stockTotal` en el ProductoResponseDTO es la suma del stock de todas las bodegas para ese producto.

---

## 9. Auditoría Automática

Cada vez que se registra un movimiento, el sistema crea automáticamente un registro en la tabla `auditoria` con:

- Tipo de operación (INSERT)
- Fecha y hora exacta
- Usuario que realizó la acción
- Entidad afectada
- Valores anteriores y nuevos en formato texto

---

## 10. Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.3.5 | Framework backend |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.3.5 | Acceso a datos |
| Hibernate | 6.x | ORM |
| MySQL | 8.0 | Base de datos |
| JWT (jjwt) | 0.11.5 | Tokens de autenticación |
| Lombok | 1.18.x | Reducción de boilerplate |
| Swagger/OpenAPI | 2.5.0 | Documentación API |
| HTML/CSS/JS | — | Frontend |