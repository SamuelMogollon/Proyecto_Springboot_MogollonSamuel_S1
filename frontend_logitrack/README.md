# 🏭 LogiTrack — Sistema de Gestión de Bodegas

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)
![JWT](https://img.shields.io/badge/Auth-JWT-orange?logo=jsonwebtokens)
![Swagger](https://img.shields.io/badge/Docs-Swagger-green?logo=swagger)
![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk)

> Sistema backend centralizado para la gestión de bodegas, productos, movimientos de inventario y auditoría automática de cambios.

---

## 📖 Descripción

**LogiTrack S.A.** administra varias bodegas distribuidas en distintas ciudades. Este sistema permite:

- 📦 Controlar movimientos de inventario entre bodegas (entradas, salidas, transferencias)
- 🔍 Registrar automáticamente auditorías de cada cambio
- 🔐 Proteger la información con autenticación JWT
- 📊 Generar reportes de stock y productos más movidos
- 📝 Documentar toda la API con Swagger/OpenAPI 3

---

## 🛠️ Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.5 |
| Spring Security | 6.x |
| MySQL | 8.0 |
| Hibernate / JPA | 6.x |
| JWT (jjwt) | 0.11.5 |
| Swagger / OpenAPI | 2.5.0 |
| Lombok | 1.18.x |

---

## ✅ Requisitos

- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Git

---

## 📥 Instalación
```bash
# Clonar el repositorio
git clone https://github.com/SamuelMogollon/Proyecto_Springboot_MogollonSamuel_S1.git

# Entrar al directorio
cd Proyecto_Springboot_MogollonSamuel_S1
```

---

## ⚙️ Configuración

Crea la base de datos en MySQL:
```sql
CREATE DATABASE logitrack;
```

Configura `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/logitrack
spring.datasource.username=root
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

---

## ▶️ Ejecución
```bash
# Compilar y ejecutar
mvn spring-boot:run
```

El servidor inicia en: `http://localhost:8080`

Para el frontend abre `frontend_logitrack/index.html` con Live Server en VSCode.

---

## 🔐 Autenticación JWT

### Registrar usuario
```http
POST /auth/register
Content-Type: application/json

{
  "nombre": "Samuel Mogollón",
  "email": "samuel@logitrack.com",
  "password": "123456",
  "rol": "ADMIN"
}
```

### Iniciar sesión
```http
POST /auth/login
Content-Type: application/json

{
  "email": "samuel@logitrack.com",
  "password": "123456"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Usa el token en cada petición protegida:
```
Authorization: Bearer <token>
```

---

## 📡 Endpoints

### 👤 Usuarios
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/usuarios` | Crear usuario |
| GET | `/api/usuarios` | Listar usuarios |
| GET | `/api/usuarios/{id}` | Buscar por ID |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |

### 🏭 Bodegas
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/bodegas` | Crear bodega |
| GET | `/api/bodegas` | Listar bodegas |
| GET | `/api/bodegas/{id}` | Buscar por ID |
| PUT | `/api/bodegas/{id}` | Actualizar bodega |
| DELETE | `/api/bodegas/{id}` | Eliminar bodega |

### 📦 Productos
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/productos` | Crear producto |
| GET | `/api/productos` | Listar productos |
| GET | `/api/productos/{id}` | Buscar por ID |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |
| GET | `/api/productos/stock-bajo` | Productos con stock < 10 |

### 🔄 Movimientos
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/movimientos` | Crear movimiento |
| GET | `/api/movimientos` | Listar movimientos |
| GET | `/api/movimientos/{id}` | Buscar por ID |
| GET | `/api/movimientos/rango` | Filtrar por fechas |

### 🔍 Auditorías
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/auditorias` | Listar auditorías |
| GET | `/api/auditorias/usuario/{id}` | Por usuario |
| GET | `/api/auditorias/tipo/{tipo}` | Por tipo (INSERT/UPDATE/DELETE) |

### 📊 Reportes
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/reportes/resumen` | Stock por bodega y productos más movidos |

---

## 📚 Swagger

Accede a la documentación interactiva en:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📁 Estructura del Proyecto
```
src/main/java/com/logitrack/logitrack/
├── auth/           # Login y registro
├── config/         # Seguridad y Swagger
├── controller/     # Controladores REST
├── dto/
│   ├── request/    # DTOs de entrada
│   └── response/   # DTOs de salida
├── exception/      # Manejo global de errores
├── mapper/         # Conversión entidad ↔ DTO
├── model/          # Entidades JPA
├── repository/     # Repositorios Spring Data
└── service/
    ├── (interfaces)
    └── impl/       # Implementaciones

frontend_logitrack/
├── index.html          # Login
├── pages/              # Páginas del sistema
├── styles/             # Archivos CSS
└── js/                 # Archivos JavaScript
```

---

## 👨‍💻 Autor

**Samuel Mogollón**
Proyecto Spring Boot — 2026