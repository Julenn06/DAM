# 📋 RESUMEN DEL PROYECTO LRLL GYM

## 📁 ESTRUCTURA DEL PROYECTO

| Carpeta                | Archivos | Descripción                                                  |
| ---------------------- | -------- | ------------------------------------------------------------ |
| **📂 src/controller/** | 3        | Flujo de control y conexión Firebase                         |
| **📂 src/model/**      | 4        | Entidades del dominio (User, Exercise, Workout, RoutineData) |
| **📂 src/view/**       | 11       | Interfaces gráficas de la aplicación                         |
| **📂 src/service/**    | 9        | Lógica de negocio y operaciones                              |
| **📂 src/util/**       | 9        | Funciones auxiliares reutilizables                           |
| **📂 lib/**            | 40+      | JARs (Firebase, Google Cloud, gRPC, Gson, jBcrypt)           |

### 📄 Archivos Raíz

- `pom.xml` → Configuración Maven
- `serviceAccountKey.json` → Credenciales Firebase
- `backup.dat` → Backup cifrado de BD
- `historic.xml` / `offlineHistoric.xml` → Históricos de entrenamientos
- `user.dat` → Email usuario actual (cifrado)

---

## 🔍 COMPONENTES PRINCIPALES

---

## 📂 CONTROLLER

### 1️⃣ **MainApp.java** - Punto de Entrada

**Función**: Inicializa aplicación  
**Flujo**: Aplica tema → Conecta Firebase → Muestra FirstView → Ejecuta backup daemon

### 2️⃣ **Controller.java** - Singleton Global

**Función**: Gestiona estado global (conexión, DB, vistas)  
**Patrón**: Thread-safe con double-checked locking

**Métodos clave**:

- `getInstance()` → Patrón Singleton
- `getDb()` → Obtiene instancia Firestore
- `isOnline()` → Verifica estado conexión
- `setOnline(boolean)` → Actualiza modo online/offline

### 3️⃣ **DBConnection.java** - Gestor Conexión Firebase

**Función**: Inicializa Firebase con `serviceAccountKey.json`  
**Validación**: Prueba socket a `firestore.googleapis.com:443` (timeout 1000ms)

---

## 📂 MODEL

### 1️⃣ **User.java**

**Atributos**: `id`, `name`, `surname`, `surname2`, `email`, `password` (hash), `birthDate`, `trainer`, `level`

**Métodos**:

- `getFullSurname()` → Concatena apellidos
- `getDobString()` → Fecha formato "dd/MM/yyyy"

### 2️⃣ **Exercise.java**

**Atributos**: `name`, `description`, `img`, `videoURL`, `sets`, `reps`, `serieTime`, `restTime`

**Características**:

- Setters usan `ParseUtils.parseInt()` para conversión segura
- `toString()` → "nombre — X sets × Y reps"
- `equals()` → Compara todos los campos

### 3️⃣ **Workout.java**

**Atributos**: `name`, `duration`, `level`, `videoURL`, `exercises[]`

### 4️⃣ **RoutineData.java**

**Tipo**: DTO inmutable  
**Atributos**: `List<Exercise>`, `description`, `totalSets`

---

## 📂 SERVICE

### 1️⃣ **AuthenticationService.java** 🔐

**Función**: Registro y login de usuarios  
**Modos**:

- **Online**: Firebase Auth API + sincroniza histórico
- **Offline**: Valida contra `backup.dat` con PBKDF2

**Métodos**:

- `eskaeraRegistratu()` → Valida, crea usuario en Auth y Firestore
- `handleLogin()` → Login online/offline, guarda email cifrado
- `checkLogin()` → POST a Firebase Auth API, sincroniza histórico

### 2️⃣ **BackupService.java** 💾

**Función**: Genera backups cifrados automáticos  
**Proceso**: Lee Firebase Auth + Firestore recursivo → Cifra XOR → Serializa a `backup.dat`  
**Históricos**: Separados en `historic.xml`

### 3️⃣ **BackupReaderService.java** 📖

**Función**: Lee/descifra backups para modo offline  
**Formatos**: Deserializa binario o parsea XML legacy  
**Estructura**: `BackupData` (users + collections anidadas)  
**Clases internas**: `UserData`, `DocumentData`, `BackupData`

### 4️⃣ **RoutineService.java** 🏋️

**Función**: Gestión de rutinas de entrenamiento  
**Modos**:

- **Online**: Query Firestore por nivel
- **Offline**: Filtra desde backup

**Métodos**:

- `levels()` → Array niveles disponibles según user level
- `getRoutines()` → Nombres rutinas del nivel seleccionado
- `getAriketak()` → Ejercicios de la rutina

### 5️⃣ **WorkoutExecutionService.java** ⏱️

**Función**: Ejecuta workouts con 3 threads paralelos  
**Threads**: Tiempo total | Series | Descansos  
**Controles**: Pausar | Saltar | Detener

**Métodos**:

- `loadRoutine()` → Carga ejercicios online/offline
- `executeWorkout()` → Thread principal, countdown 5→1
- `startExerciseThreads()` → Gestiona ejecución, popup estadísticas
- `historyLog()` → Guarda en Firestore o `offlineHistoric.xml`
- `sumLevel()` → Incrementa nivel si completa workout actual

### 6️⃣ **ProfileService.java** 👤

**Función**: Actualiza perfil usuario

**Métodos**:

- `updateUserDocument()` → Modifica campos en Firestore
- `updatePasswordAuthAndSaveHash()` → Actualiza Auth + hashea password
- `loadProfileFromDb()` → Carga datos asíncrono

### 7️⃣ **OfflineHistoricService.java** 🔄

**Función**: Sincroniza histórico offline → online  
**Proceso**: Lee `offlineHistoric.xml` → Sube a Firestore → Mueve a `historic.xml` → Limpia temporal

### 8️⃣ **HistoricReaderService.java** 📊

**Función**: Lee histórico de entrenamientos  
**Fuentes**: Firestore (`users/{uid}/historic`) + XML local  
**Filtros**: Nivel y rutina  
**Formato**: "Data: DD/MM | Bukatuta: Bai/Ez (XX%) | Serieak: X/Y | Denbora: ZZZ seg"

### 9️⃣ **UserBackupService.java** 💼

**Función**: Gestión de sesión local  
**Persistencia**: `user.dat` cifrado XOR  
**Métodos**: `saveEmail()`, `getCurrentUserEmail()`

---

## 📂 UTIL

### 1️⃣ **CryptoUtils.java** 🔐

**Función**: Cifrado XOR simétrico (clave `0x5A`)  
**Métodos**:

- `xorEncrypt()` → Base64
- `xorDecrypt()` → String
- `xorBytes()` → Archivos binarios

### 2️⃣ **DateUtils.java** 📅

**Formato**: "dd/MM/yyyy"  
**Métodos**: `getCurrentFormattedDate()`, `formatDate()`, `parseDate()`, `parseDateSafe()`

### 3️⃣ **PasswordUtils.java** 🔑

**Algoritmo**: PBKDF2WithHmacSHA256  
**Configuración**: 65536 iteraciones, salt 16 bytes  
**Métodos**:

- `hashPasahitza()` → Retorna "salt$hash" Base64
- `egiaztaturPasahitza()` → Comparación tiempo constante (anti timing attacks)

### 4️⃣ **ValidationUtils.java** ✅

**Función**: Validaciones centralizadas en euskera  
**Validaciones**:

- Email (regex completo)
- Password (>= 6 caracteres)
- Fecha (dd/MM/yyyy)
- `balidatuErregistroa()` → Valida todos campos de registro

### 5️⃣ **ParseUtils.java** 🔄

**Función**: Parsing seguro sin excepciones  
**Métodos**:

- `parseInt()` → Retorna 0 si falla
- `parseBoolean()` → Acepta "true/bai/yes/1"
- `booleanToEuskera()` → "Bai"/"Ez"

### 6️⃣ **FirestoreUtils.java** 🔍

**Función**: Queries comunes Firestore  
**Métodos**:

- `getUserDocumentByEmail()` → Query por email
- `getUserIdByEmail()` → Extrae UID
- `getUserLevelFromBackup()` → Obtiene nivel

### 7️⃣ **XMLUtils.java** 📄

**Función**: Gestión de archivos XML  
**Métodos**:

- `parseXmlDocument()` → Parsea XML existente
- `parseOrCreateXmlDocument()` → Crea si no existe
- `saveXmlDocument()` → Guarda con indent UTF-8
- `getTagValue()` → Extrae contenido tag

### 8️⃣ **ExceptionHandler.java** ⚠️

**Función**: Gestión centralizada de errores (euskera)  
**ErrorMota**: KONEXIO, AUTENTIFIKAZIO, DATU, VALIDAZIO, SISTEMA, SINKRONIZAZIO  
**Métodos**:

- `erakutsiErrorea()` → JOptionPane ERROR
- `erakutsiInfo()` → INFO
- `erakutsiAbisua()` → WARNING
- `eskaBaieztapena()` → YES/NO

### 9️⃣ **DateFormater.java** 📆

**Función**: Formateador personalizado JDatePicker  
**Formato**: SimpleDateFormat "dd-MM-yyyy"  
**Método**: `valueToString()` → Formatea Date/Calendar

---

## 📂 VIEW

### 1️⃣ **Theme.java** 🎨

**Función**: Aplica tema Nimbus personalizado  
**Patrón**: Double-checked locking thread-safe  
**Customización**: UIManager con PRIMARY, BACKGROUND, fuentes

### 2️⃣ **UIStyle.java** 🖌️

**Colores**:

- `PRIMARY` → #2196F3 (azul)
- `ACCENT` → #FFC107 (amarillo)
- `BUTTON_BG` → #197682 (verde azulado)

**Fuentes**: Segoe UI  
**Métodos**:

- `styleButton()` → Bordes redondeados custom UI
- `styleLabel()` → Estilo labels
- `addHoverEffect()` → Cambia a ACCENT en hover

### 3️⃣ **FirstView.java** 🏠

**Dimensiones**: 560×380  
**Contenido**: Logo centrado + botón "Sartu" → Abre `LoginFrame`

### 4️⃣ **LoginFrame.java** 🔐

**Dimensiones**: 480×600  
**Componentes**: Logo + campos email/password + botones Login/Register  
**Acción**: Llama `AuthenticationService.handleLogin()`

### 5️⃣ **Inter.java** 🎯

**Dimensiones**: 600×450  
**Función**: Hub principal  
**Componentes**: Título + 2 botones con iconos (Profile, Workouts)  
**Layout**: GridBagLayout con efectos hover

### 6️⃣ **RegisterDialog.java** 📝

**Dimensiones**: 520×420  
**Tipo**: Formulario modal  
**Campos**:

- Email, password, nombre, 2 apellidos
- Fecha (JDatePicker)
- Checkbox trainer
- Botones: Registratu/Utzi

### 7️⃣ **Workouts.java** 🏋️

**Dimensiones**: 700×500  
**Componentes**:

- Header (Atzera/Logout)
- Filtros (nivel, rutinas)
- JList ejercicios (`CardListRenderer`)
- Botones: "Ikusi historia", "Hasi Workout-a"

**Service**: `RoutineService`

### 8️⃣ **ThreadFrame.java** ⏱️

**Dimensiones**: 693×490  
**Función**: Ejecución workout en tiempo real  
**Componentes**:

- Info rutina
- 3 labels cronómetros (Total/Serieak/Atsedenak)
- Countdown 5→1
- 3 botones control (Pausatu/Saltar/Amaitu)

**Thread-safe**: Flags con Suppliers, `pauseLock` para sincronización

### 9️⃣ **ViewHistoric.java** 📊

**Dimensiones**: 700×480  
**Función**: Visualiza histórico de entrenamientos  
**Formato**: "Data | Bukatuta: XX% | Serieak: X/Y | Denbora: ZZZ seg"  
**Service**: `HistoricReaderService`

### 🔟 **Profile.java** 👤

**Función**: Edición de perfil usuario  
**Campos**:

- 4 campos texto (nombre, apellidos, fecha)
- 2 campos password
- Botones: Gorde/Atzera

**Service**: `ProfileService.loadProfileFromDb()`

### 1️⃣1️⃣ **CardListRenderer.java** 🎴

**Función**: Renderer personalizado para JList  
**Características**: Paneles con bordes, padding, antialiasing  
**Display**: Texto multilínea formateado

### 1️⃣2️⃣ **LoadLogo.java** 🖼️

**Función**: Carga y escala logo desde resources  
**Optimización**: Caché de imágenes con double-checked locking

---

## 🗂️ ARQUITECTURA Y FLUJOS DE DATOS

### 📥 **Flujo de Lectura (Query)**

```
Usuario → View → Service → Controller → Firebase/Backup → Model → Service → View → Usuario
```

**Ejemplo: Cargar rutinas**

1. Usuario selecciona nivel en `Workouts`
2. View llama `RoutineService.getRoutines(level)`
3. Service verifica online/offline en `Controller`
4. **Online**: Query Firestore directamente
5. **Offline**: Lee `BackupReaderService.loadBackupSafe()`
6. Service convierte a `List<Exercise>`
7. View renderiza con `CardListRenderer`

---

### 📤 **Flujo de Escritura (Command)**

```
Usuario → View → Service → Validación (Utils) → Controller → Firebase → Backup
```

**Ejemplo: Actualizar perfil**

1. Usuario edita datos en `Profile`
2. View llama `ProfileService.updateProfileInDb()`
3. Service valida con `ValidationUtils`
4. Service hashea password con `PasswordUtils` (si cambió)
5. Service actualiza Firestore vía `Controller.getDb()`
6. Service actualiza Firebase Auth (si cambió email/password)
7. `BackupService` sincroniza cambios
8. View muestra confirmación

---

### 🧵 **Flujo de Ejecución de Entrenamientos**

```
ThreadFrame → WorkoutExecutionService.executeWorkout()
    ↓
├─ RoutineService.loadRoutine() → Obtiene ejercicios
├─ Thread 1: Temporizador total
├─ Thread 2: Sets actuales
└─ Thread 3: Descansos
    ↓
├─ Pausar: pauseLock.wait()
├─ Reanudar: pauseLock.notifyAll()
└─ Saltar: skipRestRequested = true
    ↓
WorkoutExecutionService.historyLog()
    ↓
├─ Online: Firestore users/{uid}/historic.add()
└─ Offline: OfflineHistoricService.gehituSarrera()
```

**Pasos detallados**:

1. `Inter` → "Workouts" → `Workouts`
2. Usuario selecciona nivel y rutina
3. `RoutineService` carga ejercicios (Firestore u offline)
4. "Hasi Workout-a" → `ThreadFrame`
5. `WorkoutExecutionService.executeWorkout()`:
   - Countdown 5→1
   - 3 threads paralelos (total, series, descansos)
   - Control: Pausar/Saltar/Detener con flags thread-safe
6. Al finalizar: popup estadísticas, guarda histórico
7. Si completa nivel actual: incrementa nivel con `sumLevel()`

---

### 🔐 **Flujo de Autenticación**

```
LoginFrame → AuthenticationService.handleLogin(email, password)
    ↓
├─ Online:
│   ├─ Firebase REST API: signInWithPassword
│   ├─ Obtiene UID
│   ├─ Firestore: users/{uid}.get()
│   └─ OfflineHistoricService.sinkronizatuLineazKanpoDBra()
│
└─ Offline:
    ├─ BackupReaderService.loadBackupSafe()
    ├─ Busca usuario por email
    └─ PasswordUtils.egiaztaturPasahitza() → PBKDF2
    ↓
UserBackupService.saveEmail(email) → Guarda sesión local
    ↓
Inter → Hub principal
```

**Pasos detallados**:

1. `FirstView` → "Sartu" → `LoginFrame`
2. Usuario introduce email/password
3. `AuthenticationService.handleLogin()`:
   - **ONLINE**: Valida Firebase Auth, sincroniza histórico offline
   - **OFFLINE**: Valida contra backup.dat descifrado
4. Si éxito: guarda email en user.dat cifrado, abre `Inter`

---

### 📊 **Flujo de Inicio de Aplicación**

```
MainApp.main()
    ↓
Theme.apply() → Nimbus
    ↓
Controller.getInstance() → Singleton
    ↓
DBConnection.initialize()
    ├─ Firebase disponible? → ONLINE (Firestore access completo)
    └─ Firebase no disponible? → OFFLINE (Load backup.dat)
    ↓
FirstView.show()
    ↓
BackupService (Thread daemon) → Backup automático en background
```

---

### 📖 **Flujo de Histórico**

1. `Workouts` → "Ikusi historia" → `ViewHistoric`
2. `HistoricReaderService.getHistoric()`:
   - **ONLINE**: Firestore users/{uid}/historic
   - **OFFLINE**: historic.xml + offlineHistoric.xml
3. Formatea registros con %, tiempo, fecha

---

### 👤 **Flujo de Perfil**

1. `Inter` → "Profila" → `Profile`
2. Carga datos con `ProfileService.loadProfileFromDb()`
3. Usuario edita → "Gorde" → valida y actualiza
4. Actualiza Firestore + hashea nueva password si existe

---

### 🔄 **Flujo de Sincronización Offline**

1. Login ONLINE → `OfflineHistoricService.sinkronizatuLineazKanpoDBra()`
2. Lee offlineHistoric.xml
3. Sube registros a Firestore
4. Mueve a historic.xml (permanente)
5. Limpia offlineHistoric.xml

---

### 💾 **Flujo de Sistema de Backups**

1. Thread daemon ejecuta `BackupService.saveBackup()` al inicio
2. Lee Firebase Auth + Firestore recursivamente
3. Cifra con XOR, serializa a backup.dat
4. Históricos separados en historic.xml
5. Se usa en modo offline

---

## 🔒 SEGURIDAD

### 🔑 **Contraseñas**

| Característica  | Detalle                                             |
| --------------- | --------------------------------------------------- |
| **Algoritmo**   | PBKDF2WithHmacSHA256                                |
| **Iteraciones** | 65,536                                              |
| **Salt**        | 16 bytes (SecureRandom)                             |
| **Formato**     | "salt$hash" en Base64                               |
| **Protección**  | Verificación tiempo constante (anti timing attacks) |

### 🔐 **Cifrado de Datos**

| Elemento          | Método                                   |
| ----------------- | ---------------------------------------- |
| **Algoritmo**     | XOR simétrico                            |
| **Clave**         | 0x5A                                     |
| **Archivos**      | `backup.dat`, `user.dat`                 |
| **⚠️ Limitación** | XOR simple, no criptográficamente seguro |

### ✅ **Validaciones**

- **Email**: Regex completo
- **Password**: Mínimo 6 caracteres
- **Campos obligatorios**: Validados
- **Fecha**: Formato dd/MM/yyyy con regex

---

## 📡 MODO ONLINE vs OFFLINE

| Característica         | 🌐 Online (Firebase)     | 💾 Offline (Local)       |
| ---------------------- | ------------------------ | ------------------------ |
| **Autenticación**      | ✅ Firebase Auth API     | ✅ `backup.dat`          |
| **Datos**              | ✅ Firestore tiempo real | ✅ Backup local          |
| **Sincronización**     | ✅ Automática            | ⏳ Al reconectar         |
| **Backup**             | ✅ Automático            | ✅ Lectura local         |
| **Histórico**          | ✅ Firestore             | ✅ XML local             |
| **Registro histórico** | ✅ Firestore             | ✅ `offlineHistoric.xml` |
| **Incremento nivel**   | ✅ Sí                    | ❌ No                    |
| **Crear usuarios**     | ✅ Sí                    | ❌ No                    |
| **Editar perfil**      | ✅ Sí                    | ❌ No                    |

---
