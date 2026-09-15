# Ariketa03 - CRUD + Realtime JSON con MySQL

Este proyecto implementa una aplicación Angular con operaciones CRUD completas conectada a una base de datos MySQL mediante PHP.

## Características

- ✅ Crear nuevos registros
- ✅ Leer/Listar registros
- ✅ Actualizar registros existentes
- ✅ Eliminar registros
- ✅ Actualización en tiempo real (cada 5 segundos)
- ✅ Interfaz responsive
- ✅ Backend PHP + MySQL (XAMPP)
- ✅ API REST completa

## Requisitos

- Node.js (para Angular)
- XAMPP (Apache + MySQL)

## Configuración del Backend (XAMPP)

### 1. Instalar XAMPP
Si no lo tienes instalado, descárgalo de https://www.apachefriends.org/

### 2. Copiar archivos del servidor
Copia toda la carpeta `Ariketa03` a:
```
C:\xampp\htdocs\
```

### 3. Crear la base de datos
1. Inicia **Apache** y **MySQL** desde el Panel de Control de XAMPP
2. Abre phpMyAdmin: http://localhost/phpmyadmin
3. Ve a la pestaña "SQL"
4. Copia y pega el contenido del archivo `server/database.sql`
5. Haz clic en "Continuar"

Esto creará:
- Base de datos `tareas_db`
- Tabla `tareas`
- 5 tareas de ejemplo

### 4. Verificar la API
Abre en el navegador: http://localhost/Ariketa03/server/api.php

Deberías ver un JSON con las tareas.

## Instalación del Frontend (Angular)

```bash
cd Ariketa03
npm install
```

## Desarrollo

```bash
npm start
```

La aplicación se abrirá automáticamente en `http://localhost:4200/`

## Estructura del Proyecto

```
Ariketa03/
├── server/                          # Backend PHP
│   ├── api.php                      # API REST endpoints
│   ├── config.php                   # Configuración BD
│   ├── database.sql                 # Script SQL
│   └── README.md                    # Guía del backend
├── src/
│   └── app/
│       ├── components/
│       │   ├── lista-tareas/        # Componente listado
│       │   └── formulario-tarea/    # Componente formulario
│       ├── interfaces/
│       │   └── tarea.ts             # Interfaz TypeScript
│       └── services/
│           └── tarea.service.ts     # Servicio HTTP
└── public/
    └── tareas.json                  # (Ya no se usa)
```

## API Endpoints

- `GET /api.php` - Obtener todas las tareas
- `GET /api.php?id={id}` - Obtener una tarea
- `POST /api.php` - Crear tarea
- `PUT /api.php?id={id}` - Actualizar tarea
- `DELETE /api.php?id={id}` - Eliminar tarea

## Configuración

Si copiaste la carpeta `server` en otra ubicación, edita la URL en:
`src/app/services/tarea.service.ts`

```typescript
private apiUrl = 'http://localhost/TU_RUTA/api.php';
```

## Construir

```bash
npm run build
```
