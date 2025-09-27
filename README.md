
# Pet Manager
### Objetivo General

El objetivo principal de PetManager es optimizar y agilizar los procesos operativos y comerciales de la tienda de mascotas mediante la implementación de un sistema tecnológico especializado en la gestión de inventarios, ventas, compras y atención al cliente. 
Este sistema proporcionará visibilidad en tiempo real de los productos, alertas de 
reposición, y un seguimiento eficiente de las transacciones, mejorando la experiencia del cliente y la rentabilidad de la tienda.

## Feature 4
Permite realizar el seguimiento detallado de las ventas y compras de productos, generando registros de transacciones, controlando el flujo de dinero y facilitando la planificación de compras futuras.
- Autenticación y Autorización: Controla el acceso a las transacciones de ventas y compras según el nivel de permiso del usuario.
- Registro de Venta (CRUD): Facilita la creación de registros de venta, incluyendo los productos adquiridos, cantidades, cliente y monto total.
- Registro de Compra (CRUD): Permite registrar las compras a proveedores, asegurando que los datos de los productos comprados se actualicen correctamente en el inventario.
- Notificación de Venta Especial: Envía notificaciones a los responsables de compras o gerentes cuando se realizan ventas de alto volumen o productos de alta rotación.
- Reporte de Ventas: Genera informes detallados sobre el rendimiento de las ventas, mostrando las tendencias de ventas diarias, semanales y mensuales.
- Accesibilidad: Se debe cumplir con los lineamientos de UI/UX de accesibilidad para discapacidad visual.
- Seguridad: Se debe cumplir con los requisitos de disponibilidad, confidencialidad e integridad de la información.


## EndPoints Implementados 

### Inicio de Sesión

#### POST /api/auth/Login

- **Controlador**: `AuthControler` clase anotada con `@RestController` y `@RequestMapping("/api/auth"))`.

- **Entrada (JSON)**: `{"email": "...", "password": "..."}` (DTO `LoginRequest` con `email` y `password`).

- **Proceso interno**:
    1. Autentica al usuario con `AuthenticationManager` usando `UsernamePasswordAuthenticationToken(email, password)`.

    2. Si las credenciales son válidas, genera un JWT con `JwtService.generateToken(UserDetails)`.

    3. Busca el usuario por email (`UserRepository.findByEmail`) para extraer rol (`u.getRole().getCode()`) y nombre `(u.getName()`).

- **Salida (JSON)**: `{"token":"<jwt>", "role":"<código_rol>", "name":"<nombre>"}` (DTO `LoginResponse`).

#### Ejemplo

- Request
```javascript
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@mail.com",
  "password": "123456"
}

```
- Response
```javascript
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "admin",
  "name": "Alejandro"
}
```

### Cómo se protege el resto de endpoints
**Filtro JWT**

- **Clase:** `JwtAuthenticationFilter` (`OncePerRequestFilter`).
- **Qué hace:** Para cada request:
  - Lee el header `Authorization: Bearer <token>`.
  - Extrae el username (email como `subject` del token).
  - Carga el `UserDetails` con `UsersDetailsService`.
  - Si el token es válido, inyecta una `UsernamePasswordAuthenticationToken` al `SecurityContext`.
  - Si no hay header o es inválido, deja pasar la cadena sin autenticar (el resto de la cadena decidirá si rechaza la petición).

**Configuración de seguridad**

- **Clase:** `SecurityConfig`.
- **Puntos clave:**
  - **Stateless:** `SessionCreationPolicy.STATELESS` (no usa sesión de servidor).
  - **Rutas públicas:** `"/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**"` → `permitAll()`.
  - **Resto:** `anyRequest().authenticated().`
  - **Cadena de filtros:** agrega `JwtAuthenticationFilter` antes de `UsernamePasswordAuthenticationFilter`.


### Estructuras y contratos (DTO, Repos, Services)
**DTOs**

- `LoginRequest`: `email`, `password`. Es la carga de entrada para /login.
- `LoginResponse`: `token`, `role`, `name`. Es la respuesta de `/login`.
- `RoleDTO`: estructura para exponer un rol con `role_id`, `code`, `description`, `created_at`, `updated_at` y `users`.

**Repositorio**

- `UserRepository`: `JpaRepository<user, UUID>` + método `findByEmail(String)`. Se usa tanto en el login (para armar la respuesta) como al cargar detalles de usuario.

**Carga de usuarios para Spring Security**

- `UsersDetailsService`:
  - Busca el usuario por email.
  - Construye un `UserDetails` con `username = email`, `password = password_hash`
  - Asigna autoridades con el prefijo `ROLE_` + `u.getRole().getCode().toUpperCase()`

**Servicio JWT**

- `JwtService`:
  - Llave HMAC se carga desde `jwt.secret` (Base64) y se inicializa en `@PostConstruct`.
  - `generateToken(UserDetails)`: setea `sub = username (email)`, añade claim `"role"` con la primera autoridad, `iat`, `exp` calculado con `jwt.expirationMs`, y firma HS256. 
  - `extractUsername(token)`: devuelve el `subject`.
  - `isTokenValid(token, userDetails)`: compara username y que no esté expirado.

**Beans de seguridad**

- `SecuriyBeans`: `PasswordEncoder` = `BCryptPasswordEncoder()`. Usado para hashear y verificar contraseñas.

### Configuración y ambiente
`application.properties`

- DB por variables de entorno: `DB_URL`, `DB_USER`, `DB_PASS`.
- JWT:
  - `jwt.secret` (Base64) — **debe ser una cadena Base64** de 256 bits para HS256.
  - `jwt.expirationMs=3600000` (1 hora).
- Logging y JPA configurados para Postgres.



**Secret Base64**
- `SecretGenerator`: corre el `main` y imprime una clave de 32 bytes en Base64 (válida para HS256). Pone ese valor en `JWT_SECRET`.

### Flujo completo (happy path)

1. **Login** → `POST /api/auth/login` con `email/password`.
2. **Genera JWT** con `claim role` y `sub=email`.
3. **Frontend** guarda el token.
4. **Llamadas protegidas** → envían `Authorization: Bearer <token>`.
5. **Filtro JWT** valida, arma `Authentication` y autoriza el endpoint.

### Pruebas

- **Curl rápido:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@mail.com","password":"123456"}'
```

- **Luego:**

```bash
curl http://localhost:8080/endpoint-protegido \
  -H "Authorization: Bearer <tu_jwt>"
```

- **Swagger** queda público por la whitelist (`/v3/api-docs/**`, `/swagger-ui/**`).


-----

# Endpoints de Administración de Permisos

Esta sección describe los endpoints disponibles para la gestión de usuarios, roles y permisos.

-----

## Obtener resumen de usuario

### `GET /api/admin/users/{id}`

Este endpoint devuelve un resumen detallado de un usuario específico, incluyendo su rol y permisos efectivos.

  * **Controlador:** `UserAdminController`
  * **Autorización requerida:** `@PreAuthorize("hasAuthority('user.read')")`
  * **Parámetros de entrada:**
      * `id`: UUID del usuario (en la URL/path).
  * **Proceso interno:**
      * `UserAdminServiceImpl.getUserSummary` busca al usuario en la base de datos (`UserRepository.findById`).
      * Extrae el **ID**, **nombre**, **email** y **rol asignado**.
      * Obtiene los permisos del rol y los permisos directos del usuario (`UserPermissionRepository`).
  * **Salida (JSON):**
    ```json
    {
      "userId": "uuid-del-usuario",
      "name": "Carlos Pérez",
      "email": "carlos@mail.com",
      "roleCode": "admin",
      "effectivePermissions": ["purchase.read", "purchase.create"]
    }
    ```

-----

## Asignar rol a usuario

### `PUT /api/admin/users/{id}/role`

Permite asignar un rol específico a un usuario.

  * **Autorización requerida:** `@PreAuthorize("hasAuthority('user.assign_role')")`
  * **Entrada (JSON):**
    ```json
    {
      "roleId": 2
    }
    ```
      * **DTO:** `AssignRoleRequest`
  * **Proceso interno:**
      * `UserAdminServiceImpl.assignRole` busca al usuario por su UUID.
      * Busca el rol correspondiente en `RoleRepository`.
      * Actualiza el rol del usuario y guarda los cambios en la base de datos.
  * **Salida:** `200 OK` (sin cuerpo/body).

-----

## Asignar permisos directos a usuario

### `PUT /api/admin/users/{id}/permissions`

Este endpoint permite asignar un conjunto de permisos directos a un usuario, **reemplazando** los permisos directos existentes.

  * **Autorización requerida:** `@PreAuthorize("hasAuthority('user.assign_permissions')")`
  * **Entrada (JSON):**
    ```json
    {
      "permissions": ["purchase.read", "purchase.update"]
    }
    ```
      * **DTO:** `AssignPermissionsRequest`
  * **Proceso interno:**
      * `UserAdminServiceImpl.assignDirectPermissions`:
          * Borra los permisos directos actuales del usuario.
          * Valida que cada código de permiso exista en `PermissionRepository`.
          * Inserta los nuevos permisos en `UserPermissionRepository`.
  * **Salida:** `200 OK` (sin cuerpo/body).

-----

## Estructuras y Contratos

### DTOs (Data Transfer Objects)

  * **`UserSummary`**: Un objeto que representa el resumen de un usuario con sus permisos efectivos.
  * **`AssignRoleRequest`**: `{ "roleId" }`
  * **`AssignPermissionsRequest`**: `{ "permissions": Set<String> }`

### Repositorios

  * `UserRepository`
  * `RoleRepository`
  * `PermissionRepository`
  * `UserPermissionRepository`

### Servicios

  * **`UserAdminService`** y **`UserAdminServiceImpl`**: Implementan la lógica de negocio para las consultas y asignaciones.

-----

## Ejemplos de flujo (usando cURL)

### Obtener usuario

```bash
curl -H "Authorization: Bearer <jwt>" \
  http://localhost:8080/api/admin/users/6eaf5... 
```

### Asignar rol

```bash
curl -X PUT -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{"roleId":1}' \
  http://localhost:8080/api/admin/users/6eaf5.../role
```

### Asignar permisos directos

```bash
curl -X PUT -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{"permissions":["purchase.read","purchase.update"]}' \
  http://localhost:8080/api/admin/users/6eaf5.../permissions
```


# Endpoints de Compras

Esta sección documenta los endpoints para gestionar **compras**.

---

## Listar compras

### `GET /api/purchases`

* **Controlador:** `PurchaseController` (`@RestController`, `@RequestMapping("/api/purchases")`)
* **Autorización requerida:** `@PreAuthorize("hasAuthority('purchase.read')")`
* **Parámetros de entrada:** *(ninguno)*
* **Query params:** *(no admite filtros ni paginación por ahora)*
* **Proceso interno:**

    1. `PurchaseService.getAllPurchases()` llama a `PurchaseRepository.findAll()`.
    2. Mapea cada entidad `Purchase` a `PurchaseDTO` mediante `PurchaseMapper.toDTO`.
* **Salida (JSON):** Lista de `PurchaseDTO`.

**Ejemplo – Response (200 OK)**

```json
[
  {
    "id": 1,
    "supplierId": 2,
    "date": "2025-09-21T00:00:00Z",
    "status": "DRAFT",
    "total": 20.00,
    "createdAt": "2025-09-21T00:00:00Z",
    "updatedAt": "2025-09-21T00:00:00Z",
    "userId": "fb042c3d-fd39-4dd2-a1ef-a5fa4cb0dbbf",
    "shoppingDetailIds": null
  },
  {
    "id": 2,
    "supplierId": 1,
    "date": "2025-09-21T00:00:00Z",
    "status": "DRAFT",
    "total": 20.00,
    "createdAt": "2025-09-21T00:00:00Z",
    "updatedAt": "2025-09-21T00:00:00Z",
    "userId": "fb042c3d-fd39-4dd2-a1ef-a5fa4cb0dbbf",
    "shoppingDetailIds": null
  }
]
```

**cURL**

```bash
curl -H "Authorization: Bearer <jwt>" \
  http://localhost:8080/api/purchases
```

**Errores comunes**

* `401 Unauthorized` / `403 Forbidden`: token inválido o sin permiso `purchase.read`.
* `500 Internal Server Error`: error no controlado al consultar o mapear resultados.

---

## Obtener compra por ID

### `GET /api/purchases/{id}`

* **Controlador:** `PurchaseController`
* **Autorización requerida:** `@PreAuthorize("hasAuthority('purchase.read')")`
* **Parámetros de entrada:**

    * `id` (path): `Long` (identificador de la compra)
* **Proceso interno:**

    1. `PurchaseService.getPurchaseById(id)` → `PurchaseRepository.findById(id)`.
    2. Si no existe: lanza `RuntimeException("Compra no encontrada")` (gestionado por `GlobalExceptionHandler`).
    3. Si existe: mapea a `PurchaseDTO` con `PurchaseMapper.toDTO`.
* **Salida (JSON):** `PurchaseDTO`.

**Ejemplo – Response (200 OK)**

```json
{
  "id": 1,
  "supplierId": 2,
  "date": "2025-09-21T00:00:00Z",
  "status": "DRAFT",
  "total": 20.00,
  "createdAt": "2025-09-21T00:00:00Z",
  "updatedAt": "2025-09-21T00:00:00Z",
  "userId": "fb042c3d-fd39-4dd2-a1ef-a5fa4cb0dbbf",
  "shoppingDetailIds": null
}
```

**cURL**

```bash
curl -H "Authorization: Bearer <jwt>" \
  http://localhost:8080/api/purchases/1
```

**Errores comunes**

* `401/403`: autenticación/autorización.
* `500 Internal Server Error`: cuando la compra no existe (debido a `RuntimeException("Compra no encontrada")`).

    * *Sugerencia*: cambiar a excepción específica mapeada a `404 Not Found`.

---

## Crear compra

### `POST /api/purchases`

* **Controlador:** `PurchaseController`
* **Autorización requerida:** `@PreAuthorize("hasAuthority('purchase.create')")`
* **Entrada (JSON):** `PurchaseDTO`

**Request:**

```json
{
  "supplierId": 2,
  "date": "2025-09-21T00:00:00Z",
  "status": "DRAFT",
  "total": 20.00,
  "createdAt": "2025-09-21T00:00:00Z",
  "updatedAt": "2025-09-21T00:00:00Z",
  "userId": "fb042c3d-fd39-4dd2-a1ef-a5fa4cb0dbbf",
  "shoppingDetailIds": null
}
```

* **Validaciones/Reglas actuales:**

    * Si `status` es `null` → `400 Bad Request` con cuerpo de texto: `"El estado es obligatorio"`.
    * `supplierId` debe existir (`SupplierRepository.findById`) → si no, `IllegalArgumentException` (mapeada a `400`).
    * `userId` debe existir (`UserRepository.findById`) → si no, `IllegalArgumentException` (mapeada a `400`).
    * `date` y `total` **no deben ser nulos** por restricciones de entidad (`@Column(nullable=false)`). Si llegan nulos podrían provocar error 500 a nivel de persistencia.
    * `createdAt` se fija a `now` si viene `null`; `updatedAt` siempre se actualiza a `now`.

* **Proceso interno:**

    1. `PurchaseMapper.toEntity(purchaseDTO)` convierte el DTO a entidad `Purchase`.
    2. Carga y asigna `Supplier` y `User` por sus IDs.
    3. Normaliza timestamps (`createdAt` si null; `updatedAt` = now).
    4. `PurchaseRepository.save(entity)` y mapeo de vuelta a `PurchaseDTO`.

* **Salida (JSON):** `PurchaseDTO` de la compra creada.

* **Código de respuesta:** `200 OK` *(actual)*.

    * *Sugerencia*: considerar `201 Created` + `Location: /api/purchases/{id}`.

**Request (cURL)**

```bash
curl -X POST http://localhost:8080/api/purchases \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{
        "supplierId": 2,
        "date": "2025-09-21T00:00:00Z",
        "status": "DRAFT",
        "total": 20.00,
        "userId": "fb042c3d-fd39-4dd2-a1ef-a5fa4cb0dbbf",
        "shoppingDetailIds": null
      }'
```

**Response (200 OK)**

```json
{
  "id": 1,
  "supplierId": 2,
  "date": "2025-09-21T00:00:00Z",
  "status": "DRAFT",
  "total": 20.00,
  "createdAt": "2025-09-21T12:34:56Z",
  "updatedAt": "2025-09-21T12:34:56Z",
  "userId": "fb042c3d-fd39-4dd2-a1ef-a5fa4cb0dbbf",
  "shoppingDetailIds": null
}
```

**Errores comunes**

* `400 Bad Request`:

    * `"El estado es obligatorio"` (cuando `status` es `null`).
    * `IllegalArgumentException` por `supplierId` o `userId` inexistentes (ver manejador global).
* `401/403`: autenticación/autorización.
* `500 Internal Server Error`: error al persistir (p. ej., campos nulos que violan `nullable=false`).

---

## Estructuras y contratos

### DTOs

* **`PurchaseDTO`**

    * `id: Long` *(solo salida)*
    * `supplierId: Long` *(requerido)*
    * `date: OffsetDateTime` *(ISO-8601, requerido)*
    * `status: "DRAFT" | "REGISTERED" | "ANNULLED"` *(requerido)*
    * `total: BigDecimal` *(2 decimales, requerido)*
    * `createdAt: OffsetDateTime` *(salida; en entrada se permite null)*
    * `updatedAt: OffsetDateTime` *(salida; se setea a now)*
    * `userId: UUID` *(requerido)*
    * `shoppingDetailIds: List<Long> | null` *(actualmente informativo; no se crean detalles desde este endpoint)*

### Enumeraciones

* **`StatusShopping` / `statusShopping`**: `DRAFT`, `REGISTERED`, `ANNULLED`.

### Repositorios

* `PurchaseRepository`
* `SupplierRepository`
* `UserRepository`

### Servicio

* **`PurchaseService`**

    * `getAllPurchases()`
    * `getPurchaseById(Long id)`
    * `createPurchase(PurchaseDTO purchaseDTO)`

### Mapper

* **`PurchaseMapper`** (MapStruct)

    * Mapea `Purchase <-> PurchaseDTO` (incluye `supplier.supplierId -> supplierId`, `user.userId -> userId`).

---

## Manejo de errores global

Los errores se gestionan con `GlobalExceptionHandler` (`@RestControllerAdvice`).

* **IllegalArgumentException** → `400 Bad Request`:

```json
{
  "timestamp": "2025-09-27T18:10:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Proveedor no encontrado: 2"
}
```

* **Exception (cualquier otra)** → `500 Internal Server Error`:

```json
{
  "timestamp": "2025-09-27T18:10:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Compra no encontrada"
}
```

## Authors

- [@Juan Jose Monsalve Hernandez](https://github.com/JuanJoM14)
- [@Alejandro Chavarria Mora](https://github.com/AlejandroMora05)
- [@Ana Maria Granada Rodas](https://github.com/anagranada1)
