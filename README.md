# BTG-PACTUAL-API

## Descripción

**BTG-PACTUAL-API** es una API RESTful desarrollada en Java con Spring Boot para la gestión de inversiones, fondos y clientes, simulando la operatividad de una plataforma de inversión. El sistema permite registrar usuarios, autenticarse mediante JWT, gestionar clientes, suscribirse a fondos, consultar transacciones y recibir notificaciones según preferencias.

### Características principales

- **Autenticación y autorización** con JWT.
- **Gestión de usuarios**: registro, login, actualización de perfil, cambio y reseteo de contraseña.
- **Gestión de clientes**: consulta de perfil, historial de transacciones, suscripción/cancelación a fondos.
- **Gestión de fondos**: creación y consulta de fondos de inversión.
- **Notificaciones**: envío simulado por email o SMS según preferencia.
- **Persistencia** en MongoDB.
- **Validaciones** robustas con mensajes personalizados.
- **Swagger/OpenAPI** para documentación interactiva.
- **Pruebas unitarias** con JUnit y Mockito.

---

## Documentación del Proyecto OPEN-API

```
.
<img width="1352" height="564" alt="image" src="https://github.com/user-attachments/assets/4cf7a238-e7b7-4ba0-8629-c14ea4339097" />

```

---

## Requisitos Previos

- **Java 17** o superior
- **Maven 3.9.x** (o usar el Maven Wrapper incluido)
- **MongoDB** (local o Atlas, la conexión por defecto es a MongoDB Atlas)
- **Git** (opcional, para clonar el repositorio)

---

## Instalación y Ejecución Local

### 1. Clonar el repositorio

```sh
git clone https://github.com/Alegria2016/BTG-PACTUAL-API
cd BTG-PACTUAL-API
```

### 2. Configurar variables de entorno (opcional)

Por defecto, la API se conecta a una base de datos MongoDB Atlas. Si deseas usar una instancia local, edita `src/main/resources/application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/btg
```

Asegúrate de que la base de datos `btg` exista o será creada automáticamente.

### 3. Compilar el proyecto

Usa Maven Wrapper (recomendado):

```sh
./mvnw clean install
```

O con Maven instalado:

```sh
mvn clean install
```

### 4. Ejecutar la aplicación

```sh
./mvnw spring-boot:run
```
o
```sh
mvn spring-boot:run
```

La API estará disponible en: [http://localhost:8080](http://localhost:8080)

### 5. Acceder a la documentación Swagger

Una vez levantada la API, accede a la documentación interactiva en:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Endpoints principales

- **Autenticación y usuarios**
  - `POST /api/v1/auth/register` — Registro de usuario
  - `POST /api/v1/auth/login` — Login y obtención de JWT
  - `GET /api/v1/auth/users` — Listado de usuarios (requiere rol ADMIN)

- **Clientes**
  - `GET /api/v1/clients/me` — Perfil del cliente autenticado
  - `POST /api/v1/clients/subscribe/{fundId}` — Suscribirse a un fondo
  - `POST /api/v1/clients/cancel/{fundId}` — Cancelar suscripción a fondo
  - `GET /api/v1/clients/transactions` — Historial de transacciones

- **Fondos**
  - `GET /api/v1/funds` — Listar fondos disponibles
  - `GET /api/v1/funds/{id}` — Consultar fondo por ID
  - `POST /api/v1/funds` — Crear fondo (requiere autenticación)

---

## Pruebas

Para ejecutar las pruebas unitarias:

```sh
./mvnw test
```
o
```sh
mvn test
```

---

## Notas de Seguridad

- El secreto JWT y la URI de MongoDB están en `application.properties`. Para producción, usa variables de entorno o un gestor de secretos.
- El endpoint `/api/v1/auth/users` requiere el rol `ADMIN`.
- Todos los endpoints (excepto login y registro) requieren autenticación JWT.

---

## Despliegue en Producción

- Edita `src/main/resources/application-prod.properties` para la configuración de producción.
- Puedes usar el archivo [infrastructure/cloudformation-template.yml](infrastructure/cloudformation-template.yml) para desplegar en AWS usando CloudFormation.

---

## Licencia

Este proyecto está bajo la Licencia Apache 2.0.

---

## Autor

BTG Pactual - Prueba de Desarrollo Felix Alegria L.
