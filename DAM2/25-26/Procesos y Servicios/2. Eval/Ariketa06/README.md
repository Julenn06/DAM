# Txat Aurreratua - Aplicación de Chat Cliente-Servidor en Java

## Descripción
Aplicación completa de chat en Java que implementa una arquitectura cliente-servidor con interfaz gráfica Swing. Basada en el patrón MVC (Modelo-Vista-Controlador) y usando sockets para la comunicación en red.

## Estructura del Proyecto

```
src/
├── servidor/
│   ├── ServidorMain.java
│   ├── controlador/
│   │   └── ServerController.java
│   ├── modelo/
│   │   └── ServerModel.java
│   └── vista/
│       └── ServerView.java
├── cliente/
│   ├── ClienteMain.java
│   ├── controlador/
│   │   └── ClientController.java
│   ├── modelo/
│   │   └── ClientModel.java
│   └── vista/
│       ├── ClientViewLogin.java
│       └── ClientViewChat.java
└── module-info.java
```

## Características

### Servidor
- **Interfaz gráfica** que muestra:
  - Número de clientes conectados
  - Registro de todos los eventos (conexiones, desconexiones, mensajes)
  - Botón para cerrar el servidor
- **Gestión de múltiples clientes** (hasta 10 simultáneos)
- **Multihilo**: Un hilo por cada cliente conectado para recibir mensajes
- **Puerto**: 5000
- La ventana no se puede maximizar y el botón 'X' no cierra la aplicación (usar botón "Irten")

### Cliente
- **Ventana de login** para introducir nickname
- **Ventana de chat** con:
  - Campo de texto para escribir mensajes
  - Área de chat para ver todos los mensajes
  - Botón "Bidali" para enviar
  - Botón "Irten" para salir
- **Funcionalidad**:
  - Enviar con Enter o botón Bidali
  - Recepción en tiempo real de todos los mensajes
  - Notificaciones cuando alguien se conecta/desconecta
- La ventana no se puede maximizar y el botón 'X' no cierra la aplicación (usar botón "Irten")

## Protocolo de Comunicación

1. **Conexión**: El cliente envía su nickname al servidor
2. **Mensajes**: 
   - Texto normal: se reenvía a todos los clientes con formato "nickname: mensaje"
   - Asterisco (*): señal de desconexión
3. **Desconexión**:
   - Cliente envía "*" al salir
   - Servidor envía "*" a todos los clientes al cerrar

## Compilación y Ejecución

### Opción 1: Desde línea de comandos

```bash
# Compilar todas las clases
cd src
javac servidor/*.java servidor/controlador/*.java servidor/modelo/*.java servidor/vista/*.java
javac cliente/*.java cliente/controlador/*.java cliente/modelo/*.java cliente/vista/*.java

# Ejecutar servidor
java servidor.ServidorMain

# Ejecutar cliente (en otra terminal, se pueden ejecutar múltiples)
java cliente.ClienteMain
```

### Opción 2: Desde un IDE (Eclipse, IntelliJ IDEA, VS Code)

1. Importar el proyecto
2. Ejecutar `ServidorMain.java` primero
3. Ejecutar `ClienteMain.java` (puedes ejecutar varias instancias)

## Instrucciones de Uso

### 1. Iniciar el Servidor
- Ejecutar `ServidorMain`
- El servidor se iniciará en el puerto 5000
- Esperará conexiones de clientes

### 2. Conectar Clientes
- Ejecutar `ClienteMain`
- Introducir un nickname (goitizena)
- Click en "Konektatu"
- Si la conexión es exitosa, se abrirá la ventana de chat

### 3. Chatear
- Escribir mensaje en el campo superior
- Presionar Enter o click en "Bidali"
- Los mensajes se verán en todos los clientes conectados y en el servidor

### 4. Salir
- **Cliente**: Click en "Irten" → Confirmar
- **Servidor**: Click en "Irten" → Todos los clientes serán notificados y desconectados

## Detalles Técnicos Importantes

### Orden de creación de streams
**MUY IMPORTANTE**: Los streams deben crearse en este orden:
1. Primero `ObjectOutputStream`
2. Luego `ObjectInputStream`

Esto evita deadlocks durante el handshake de los streams.

### Manejo de hilos
- El servidor crea un hilo (`HiloRecepcion`) por cada cliente conectado
- Cada cliente tiene su propio hilo de recepción de mensajes
- Se usa `SwingUtilities.invokeLater()` para actualizar la interfaz desde otros hilos

### Sincronización
- La lista de clientes conectados está sincronizada para evitar condiciones de carrera
- Los mensajes se envían de forma thread-safe

## Posibles Mejoras Futuras

- ✅ Validación de nicknames únicos
- ✅ Lista de usuarios conectados en el servidor
- ✅ Opción para expulsar usuarios desde el servidor
- ⬜ Mensajes privados entre usuarios
- ⬜ Guardar historial de chat
- ⬜ Cifrado de mensajes
- ⬜ Autenticación de usuarios

## Requisitos del Sistema

- Java 11 o superior
- Conexión de red (localhost para pruebas)
- Sistema operativo: Windows, Linux o macOS

## Solución de Problemas

### El cliente no puede conectar
- Verificar que el servidor esté ejecutándose
- Comprobar que el puerto 5000 no esté bloqueado por firewall
- Verificar la dirección del servidor (localhost por defecto)

### Los mensajes no se reciben
- Verificar la conexión de red
- Revisar que no haya excepciones en la consola
- Comprobar que los streams se hayan creado correctamente

### Error al cerrar la aplicación
- Siempre usar el botón "Irten", no el 'X' de la ventana
- Si hay problemas, terminar el proceso manualmente

## Autor
Aplicación desarrollada para DAM2 - Procesos y Servicios
Basada en las especificaciones de "3.6 - Txat aurreratua"
