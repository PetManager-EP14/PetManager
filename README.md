
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
- Respond
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


## Authors

- [@Juan Jose Monsalve Hernandez](https://github.com/JuanJoM14)
- [@Alejandro Chavarria Mora](https://github.com/AlejandroMora05)
- [@Ana Maria Granada Rodas](https://github.com/anagranada1)
