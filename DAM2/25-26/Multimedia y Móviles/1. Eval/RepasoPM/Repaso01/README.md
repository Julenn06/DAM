# 📱 Guía Básica de Android con Kotlin

## 📚 Índice
1. [Conceptos Fundamentales](#-conceptos-fundamentales)
2. [ViewBinding - Acceder a las Vistas](#-viewbinding---acceder-a-las-vistas)
3. [Listeners - Botones y Eventos](#-listeners---botones-y-eventos)
4. [Leer Input del Usuario](#-leer-input-del-usuario)
5. [RecyclerView - Listas](#-recyclerview---listas)
6. [Firestore - Base de Datos](#-firestore---base-de-datos)
7. [DataStore - Guardar Preferencias](#-datastore---guardar-preferencias)
8. [Corrutinas - Operaciones Asíncronas](#-corrutinas---operaciones-asíncronas)
9. [Lambdas - Funciones como Parámetros](#-lambdas---funciones-como-parámetros)
10. [Intent - Navegar entre Activities](#-intent---navegar-entre-activities)

---

## 🎯 Conceptos Fundamentales

### ¿Qué es una Activity?
Una **Activity** es una pantalla de tu app. Cada pantalla = 1 Activity.

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aquí inicializas tu pantalla
    }
}
```

### ¿Qué es un Layout XML?
Un archivo XML que define cómo se ve tu pantalla (botones, textos, etc.).

```xml
<!-- activity_main.xml -->
<Button
    android:id="@+id/btnSave"
    android:text="Guardar" />
```

---

## 🔗 ViewBinding - Acceder a las Vistas

### ¿Qué es ViewBinding?
Es la forma moderna de acceder a las vistas del XML desde Kotlin. **NO uses findViewById**.

### Paso 1: Habilitar ViewBinding
En `build.gradle.kts` (del módulo app):

```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
```

### Paso 2: Usar ViewBinding en una Activity

```kotlin
class AddUserActivity : AppCompatActivity() {
    // 1. Declarar variable de binding (lateinit = se inicializa después)
    private lateinit var binding: ActivityAddUserBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 2. Inflar el binding (conectar con el XML)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        
        // 3. Setear como contenido de la Activity
        setContentView(binding.root)
        
        // 4. Ahora puedes acceder a las vistas:
        binding.btnSave.text = "Guardar Usuario"
    }
}
```

**Regla de Oro**: Si tu XML se llama `activity_add_user.xml`, el binding será `ActivityAddUserBinding`.

---

## 👆 Listeners - Botones y Eventos

### Clic en un Botón

```kotlin
// Forma 1: Lambda (moderna)
binding.btnSave.setOnClickListener {
    // Código cuando se hace clic
    Toast.makeText(this, "¡Botón presionado!", Toast.LENGTH_SHORT).show()
}

// Forma 2: Tradicional (no recomendada)
binding.btnSave.setOnClickListener(object : View.OnClickListener {
    override fun onClick(v: View?) {
        // Código aquí
    }
})
```

**Ejemplo del Proyecto** (MainActivity.kt):
```kotlin
// Botón para ir a añadir usuario
binding.btnAddUser.setOnClickListener {
    // INTENT: Navegar a AddUserActivity
    startActivity(Intent(this, AddUserActivity::class.java))
}
```

### Botón de Retroceso

```kotlin
binding.btnBack.setOnClickListener {
    finish() // Cierra la Activity actual
}
```

**Ejemplo del Proyecto** (AddUserActivity.kt):
```kotlin
binding.btnBack.setOnClickListener {
    finish() // Vuelve a la pantalla anterior
}
```

---

## 📝 Leer Input del Usuario

### TextInputEditText (Material Design)

```kotlin
// Leer texto
val nombre = binding.etNombre.text.toString()

// Leer número
val edadTexto = binding.etEdad.text.toString()
val edad = edadTexto.toIntOrNull() // Convierte a Int o null si no es válido

// Escribir texto
binding.etNombre.setText("Juan")
```

### Validar Input

```kotlin
fun validarFormulario(): Boolean {
    val nombre = binding.etNombre.text.toString()
    val edadTexto = binding.etEdad.text.toString()
    
    // Validar que no esté vacío
    if (nombre.isBlank()) {
        binding.tilNombre.error = "El nombre es obligatorio"
        return false
    }
    
    // Validar número
    val edad = edadTexto.toIntOrNull()
    if (edad == null || edad <= 0) {
        binding.tilEdad.error = "Edad inválida"
        return false
    }
    
    // Limpiar errores
    binding.tilNombre.error = null
    binding.tilEdad.error = null
    
    return true
}
```

**Ejemplo del Proyecto** (AddUserActivity.kt):
```kotlin
binding.btnSave.setOnClickListener {
    // Leer valores
    val nombre = binding.etNombre.text.toString().trim()
    val edadStr = binding.etEdad.text.toString().trim()
    
    // Validar
    if (nombre.isBlank()) {
        Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }
    
    val edad = edadStr.toIntOrNull()
    if (edad == null || edad <= 0) {
        Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }
    
    // Crear usuario
    val user = User(nombre, edad)
}
```

---

## 📋 RecyclerView - Listas

### ¿Qué es RecyclerView?
Es una lista eficiente que **reutiliza** las vistas. Ideal para listas largas.

### Componentes Necesarios
1. **RecyclerView** en el XML
2. **Adapter** (gestiona los datos)
3. **ViewHolder** (cachea las vistas)
4. **Layout del Item** (cómo se ve cada elemento)

### Paso 1: RecyclerView en XML

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### Paso 2: Layout del Item (item_user.xml)

```xml
<TextView
    android:id="@+id/tvNombre"
    android:text="Nombre Usuario" />

<TextView
    android:id="@+id/tvEdad"
    android:text="Edad: 25" />
```

### Paso 3: Crear el Adapter

```kotlin
class UserAdapter(
    private val onUserClick: (User, String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    
    // Lista de datos
    private val users = mutableListOf<Pair<String, User>>()
    
    // ViewHolder: cachea las vistas
    inner class UserViewHolder(private val binding: ItemUserBinding) 
        : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(userId: String, user: User) {
            binding.tvNombre.text = user.nombre
            binding.tvEdad.text = "Edad: ${user.edad}"
            
            // Click en el item
            binding.root.setOnClickListener {
                onUserClick(user, userId)
            }
        }
    }
    
    // Crear ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }
    
    // Bindear datos
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val (userId, user) = users[position]
        holder.bind(userId, user)
    }
    
    // Tamaño de la lista
    override fun getItemCount(): Int = users.size
    
    // Actualizar datos
    fun submitList(newUsers: List<Pair<String, User>>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
}
```

### Paso 4: Usar el Adapter en la Activity

```kotlin
// 1. Declarar adapter
private lateinit var userAdapter: UserAdapter

// 2. Configurar en onCreate
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Crear adapter con lambda para clics
    userAdapter = UserAdapter { user, userId ->
        // Qué hacer cuando se hace clic
        Toast.makeText(this, "Clic en ${user.nombre}", Toast.LENGTH_SHORT).show()
    }
    
    // Configurar RecyclerView
    binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = userAdapter
    }
    
    // Cargar datos
    cargarUsuarios()
}

// 3. Cargar datos
fun cargarUsuarios() {
    val usuarios = listOf(
        Pair("id1", User("Juan", 25)),
        Pair("id2", User("María", 30))
    )
    userAdapter.submitList(usuarios)
}
```

**Ejemplo del Proyecto** (UserListActivity.kt):
```kotlin
// Configurar adapter
userAdapter = UserAdapter { user, userId ->
    // Registrar en historial
    lifecycleScope.launch {
        historyManager.addViewAction(user.nombre, userId)
    }
    Toast.makeText(this, "Usuario: ${user.nombre}", Toast.LENGTH_SHORT).show()
}

// Configurar RecyclerView
binding.recyclerView.apply {
    layoutManager = LinearLayoutManager(this@UserListActivity)
    adapter = userAdapter
}
```

---

## 🔥 Firestore - Base de Datos

### ¿Qué es Firestore?
Base de datos en la nube de Firebase. Estructura: **Colecciones → Documentos → Campos**.

```
usuarios (colección)
  ├─ abc123 (documento)
  │   ├─ nombre: "Juan"
  │   └─ edad: 25
  └─ def456 (documento)
      ├─ nombre: "María"
      └─ edad: 30
```

### Configurar Firestore

```kotlin
class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()
    
    // ...métodos aquí
}
```

### 1. **CREAR** (Create) - Añadir Documento

```kotlin
fun addDocument(collection: String, data: Map<String, Any>, onResult: (String?) -> Unit) {
    db.collection(collection)
        .add(data) // Genera ID automático
        .addOnSuccessListener { documentReference ->
            // Éxito: devuelve el ID
            onResult(documentReference.id)
        }
        .addOnFailureListener { e ->
            // Error
            onResult(null)
        }
}
```

**Uso en la Activity**:
```kotlin
val user = User("Juan", 25)
val data = mapOf(
    "nombre" to user.nombre,
    "edad" to user.edad
)

firestore.addDocument("usuarios", data) { userId ->
    if (userId != null) {
        Toast.makeText(this, "Usuario creado: $userId", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show()
    }
}
```

**Ejemplo del Proyecto** (AddUserActivity.kt):
```kotlin
binding.btnSave.setOnClickListener {
    val nombre = binding.etNombre.text.toString().trim()
    val edad = binding.etEdad.text.toString().toIntOrNull() ?: 0
    
    val user = User(nombre, edad)
    val userData = mapOf(
        "nombre" to user.nombre,
        "edad" to user.edad
    )
    
    // FIRESTORE: Guardar en Firestore
    firestore.addDocument("usuarios", userData) { userId ->
        if (userId != null) {
            // HISTORIAL: Registrar creación
            lifecycleScope.launch {
                historyManager.addCreateAction(nombre, userId)
            }
            Toast.makeText(this, "Usuario añadido", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
```

### 2. **LEER** (Read) - Obtener Documentos

```kotlin
fun getCollection(collection: String, onResult: (List<Map<String, Any>>) -> Unit) {
    db.collection(collection)
        .get()
        .addOnSuccessListener { querySnapshot ->
            val documents = querySnapshot.documents.map { doc ->
                val data = doc.data ?: emptyMap()
                data.toMutableMap().apply {
                    put("__id__", doc.id) // Añadir ID al mapa
                }
            }
            onResult(documents)
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}
```

**Uso en la Activity**:
```kotlin
fun cargarUsuarios() {
    binding.progressBar.visibility = View.VISIBLE
    
    firestore.getCollection("usuarios") { documents ->
        binding.progressBar.visibility = View.GONE
        
        // Transformar a lista de usuarios
        val usuarios = documents.mapNotNull { doc ->
            val userId = doc["__id__"] as? String ?: return@mapNotNull null
            val nombre = doc["nombre"] as? String ?: return@mapNotNull null
            val edad = (doc["edad"] as? Long)?.toInt() ?: return@mapNotNull null
            
            Pair(userId, User(nombre, edad))
        }
        
        // Mostrar en RecyclerView
        userAdapter.submitList(usuarios)
    }
}
```

**Ejemplo del Proyecto** (UserListActivity.kt):
```kotlin
private fun loadUsers() {
    binding.progressBar.visibility = View.VISIBLE
    
    // FIRESTORE: Obtener todos los usuarios
    firestore.getCollection("usuarios") { documents ->
        binding.progressBar.visibility = View.GONE
        
        if (documents.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.tvEmpty.visibility = View.GONE
            
            // LISTOF: Transformar mapas a objetos User
            val users = documents.mapNotNull { doc ->
                val userId = doc["__id__"] as? String ?: return@mapNotNull null
                val nombre = doc["nombre"] as? String ?: return@mapNotNull null
                val edad = (doc["edad"] as? Long)?.toInt() ?: return@mapNotNull null
                
                Pair(userId, User(nombre, edad))
            }
            
            userAdapter.submitList(users)
        }
    }
}
```

### 3. **ACTUALIZAR** (Update) - Modificar Documento

```kotlin
fun updateDocument(collection: String, docId: String, data: Map<String, Any>, onResult: (Boolean) -> Unit) {
    db.collection(collection)
        .document(docId)
        .update(data)
        .addOnSuccessListener {
            onResult(true)
        }
        .addOnFailureListener {
            onResult(false)
        }
}
```

**Uso en la Activity**:
```kotlin
binding.btnUpdate.setOnClickListener {
    val userId = currentUserId ?: return@setOnClickListener
    val nombre = binding.etNombre.text.toString()
    val edad = binding.etEdad.text.toString().toIntOrNull() ?: 0
    
    val updates = mapOf(
        "nombre" to nombre,
        "edad" to edad
    )
    
    firestore.updateDocument("usuarios", userId, updates) { success ->
        if (success) {
            Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }
}
```

### 4. **ELIMINAR** (Delete) - Borrar Documento

```kotlin
fun deleteDocument(collection: String, docId: String, onResult: (Boolean) -> Unit) {
    db.collection(collection)
        .document(docId)
        .delete()
        .addOnSuccessListener {
            onResult(true)
        }
        .addOnFailureListener {
            onResult(false)
        }
}
```

**Uso en la Activity**:
```kotlin
binding.btnDelete.setOnClickListener {
    val userId = currentUserId ?: return@setOnClickListener
    
    // Mostrar confirmación
    AlertDialog.Builder(this)
        .setTitle("Confirmar")
        .setMessage("¿Eliminar este usuario?")
        .setPositiveButton("Eliminar") { _, _ ->
            firestore.deleteDocument("usuarios", userId) { success ->
                if (success) {
                    Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        .setNegativeButton("Cancelar", null)
        .show()
}
```

---

## 💾 DataStore - Guardar Preferencias

### ¿Qué es DataStore?
Reemplazo moderno de SharedPreferences. Guarda datos clave-valor localmente.

### Configurar DataStore

```kotlin
// Extensión del Context para crear DataStore
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class PreferencesManager(private val context: Context) {
    // Keys para las preferencias
    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
    
    // Guardar valor
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }
    
    // Leer valor como Flow (observable)
    fun getUserName(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_NAME] ?: ""
        }
    }
}
```

### Usar DataStore en Activity

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var preferencesManager: PreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        preferencesManager = PreferencesManager(this)
        
        // Guardar dato
        lifecycleScope.launch {
            preferencesManager.saveUserName("Juan")
        }
        
        // Observar cambios
        lifecycleScope.launch {
            preferencesManager.getUserName().collect { name ->
                binding.tvWelcome.text = "Bienvenido $name"
            }
        }
    }
}
```

**Ejemplo del Proyecto** (FavoritesManager.kt):
```kotlin
// DataStore para favoritos
private val Context.favoritesDataStore by preferencesDataStore(name = "favorites_preferences")

class FavoritesManager(private val context: Context) {
    private object Keys {
        val FAVORITE_IDS = stringSetPreferencesKey("favorite_ids")
    }
    
    // Añadir favorito
    suspend fun addFavorite(userId: String) {
        context.favoritesDataStore.edit { preferences ->
            val currentFavorites = preferences[Keys.FAVORITE_IDS] ?: emptySet()
            preferences[Keys.FAVORITE_IDS] = currentFavorites + userId
        }
    }
    
    // Remover favorito
    suspend fun removeFavorite(userId: String) {
        context.favoritesDataStore.edit { preferences ->
            val currentFavorites = preferences[Keys.FAVORITE_IDS] ?: emptySet()
            preferences[Keys.FAVORITE_IDS] = currentFavorites - userId
        }
    }
    
    // Observar favoritos
    fun getFavorites(): Flow<Set<String>> {
        return context.favoritesDataStore.data.map { preferences ->
            preferences[Keys.FAVORITE_IDS] ?: emptySet()
        }
    }
}
```

---

## ⚡ Corrutinas - Operaciones Asíncronas

### ¿Qué son las Corrutinas?
Forma moderna de hacer tareas en segundo plano (red, base de datos, etc.) **sin bloquear la UI**.

### Conceptos Básicos

```kotlin
// lifecycleScope: corrutina atada al ciclo de vida de la Activity
lifecycleScope.launch {
    // Código asíncrono aquí
    val result = fetchDataFromInternet()
    
    // Actualizar UI (automáticamente en el hilo principal)
    binding.tvResult.text = result
}
```

### Suspend Functions
Funciones que pueden suspenderse (pausarse) sin bloquear.

```kotlin
suspend fun fetchDataFromInternet(): String {
    delay(2000) // Simula espera de 2 segundos
    return "Datos obtenidos"
}
```

### Ejemplo Práctico

```kotlin
binding.btnLoad.setOnClickListener {
    // Mostrar loading
    binding.progressBar.visibility = View.VISIBLE
    
    lifecycleScope.launch {
        // Operación en segundo plano
        val data = fetchDataFromInternet()
        
        // Ocultar loading y mostrar resultado
        binding.progressBar.visibility = View.GONE
        binding.tvData.text = data
    }
}
```

**Ejemplo del Proyecto** (EditUserActivity.kt):
```kotlin
// Click en botón de favorito
binding.btnFavorite.setOnClickListener {
    lifecycleScope.launch {
        // Operación asíncrona de DataStore
        favoritesManager.toggleFavorite(userId)
        
        // Verificar nuevo estado
        val isFav = favoritesManager.isFavorite(userId)
        
        // Actualizar UI
        Toast.makeText(
            this@EditUserActivity,
            if (isFav) "Añadido a favoritos" else "Removido de favoritos",
            Toast.LENGTH_SHORT
        ).show()
    }
}
```

### Observar Flow con Collect

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Observar cambios en favoritos
    lifecycleScope.launch {
        favoritesManager.getFavorites().collect { favoriteIds ->
            // Se ejecuta cada vez que cambian los favoritos
            updateFavoriteButtons(favoriteIds)
        }
    }
}
```

---

## 🔗 Lambdas - Funciones como Parámetros

### ¿Qué es una Lambda?
Una **función anónima** que puedes pasar como parámetro.

### Sintaxis Básica

```kotlin
// Lambda sin parámetros
val saludar = { println("Hola") }
saludar() // Ejecutar

// Lambda con parámetros
val sumar = { a: Int, b: Int -> a + b }
val resultado = sumar(5, 3) // 8

// Lambda con un parámetro (puede usar 'it')
val doble = { numero: Int -> numero * 2 }
val dobleConIt: (Int) -> Int = { it * 2 }
```

### Lambda como Parámetro de Función

```kotlin
// Función que recibe lambda
fun ejecutarOperacion(a: Int, b: Int, operacion: (Int, Int) -> Int): Int {
    return operacion(a, b)
}

// Uso
val suma = ejecutarOperacion(5, 3) { x, y -> x + y } // 8
val resta = ejecutarOperacion(5, 3) { x, y -> x - y } // 2
```

### Lambda en Click Listeners

```kotlin
// Forma tradicional (sin lambda)
binding.btnSave.setOnClickListener(object : View.OnClickListener {
    override fun onClick(v: View?) {
        println("Clic")
    }
})

// Con lambda (moderno y limpio)
binding.btnSave.setOnClickListener {
    println("Clic")
}
```

**Ejemplo del Proyecto** (UserAdapter.kt):
```kotlin
// Adapter que recibe lambdas
class UserAdapter(
    // Lambda para clic en usuario
    private val onUserClick: (User, String) -> Unit,
    // Lambda opcional para favoritos
    private val onFavoriteClick: ((String) -> Unit)? = null
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    
    inner class UserViewHolder(private val binding: ItemUserBinding) 
        : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(userId: String, user: User) {
            // Ejecutar lambda cuando se hace clic
            binding.root.setOnClickListener {
                onUserClick(user, userId) // Invocar lambda
            }
            
            // Lambda de favorito (si existe)
            binding.btnFavorite.setOnClickListener {
                onFavoriteClick?.invoke(userId) // ?. = solo si no es null
            }
        }
    }
}
```

**Uso del Adapter**:
```kotlin
userAdapter = UserAdapter(
    // Lambda 1: Qué hacer al clic en usuario
    onUserClick = { user, userId ->
        Toast.makeText(this, "Seleccionaste ${user.nombre}", Toast.LENGTH_SHORT).show()
    },
    // Lambda 2: Qué hacer al clic en favorito
    onFavoriteClick = { userId ->
        lifecycleScope.launch {
            favoritesManager.toggleFavorite(userId)
        }
    }
)
```

### Trailing Lambda
Si el último parámetro es una lambda, puedes sacarla de los paréntesis.

```kotlin
// Forma 1: Lambda dentro
lifecycleScope.launch({ 
    println("Hola") 
})

// Forma 2: Trailing lambda (más común)
lifecycleScope.launch {
    println("Hola")
}
```

---

## 🚀 Intent - Navegar entre Activities

### ¿Qué es un Intent?
Un **mensaje** para navegar entre Activities o enviar datos.

### 1. Intent Simple (sin datos)

```kotlin
// Ir a otra Activity
binding.btnNext.setOnClickListener {
    val intent = Intent(this, OtraActivity::class.java)
    startActivity(intent)
}
```

**Ejemplo del Proyecto** (MainActivity.kt):
```kotlin
binding.btnAddUser.setOnClickListener {
    // INTENT: Navegar a AddUserActivity
    startActivity(Intent(this, AddUserActivity::class.java))
}

binding.btnViewUsers.setOnClickListener {
    startActivity(Intent(this, UserListActivity::class.java))
}

binding.btnEditUser.setOnClickListener {
    startActivity(Intent(this, EditUserActivity::class.java))
}

binding.btnFavorites.setOnClickListener {
    startActivity(Intent(this, FavoritesActivity::class.java))
}

binding.btnHistory.setOnClickListener {
    startActivity(Intent(this, HistoryActivity::class.java))
}
```

### 2. Intent con Datos (Extras)

**Enviar datos**:
```kotlin
binding.btnSend.setOnClickListener {
    val intent = Intent(this, DetalleActivity::class.java)
    intent.putExtra("NOMBRE", "Juan")
    intent.putExtra("EDAD", 25)
    intent.putExtra("ES_ADMIN", true)
    startActivity(intent)
}
```

**Recibir datos**:
```kotlin
class DetalleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Obtener extras
        val nombre = intent.getStringExtra("NOMBRE") ?: ""
        val edad = intent.getIntExtra("EDAD", 0)
        val esAdmin = intent.getBooleanExtra("ES_ADMIN", false)
        
        binding.tvNombre.text = nombre
        binding.tvEdad.text = "Edad: $edad"
    }
}
```

### 3. Intent Implícito (Compartir, Abrir URL, etc.)

**Compartir texto**:
```kotlin
binding.btnShare.setOnClickListener {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Hola, mira esto!")
        putExtra(Intent.EXTRA_SUBJECT, "Asunto")
    }
    startActivity(Intent.createChooser(shareIntent, "Compartir vía"))
}
```

**Ejemplo del Proyecto** (FavoritesActivity.kt):
```kotlin
// Click en item para compartir
userAdapter = UserAdapter { user, userId ->
    // INTENT: Compartir información del usuario
    val shareText = getString(
        R.string.share_user_text, 
        user.nombre, 
        user.edad, 
        userId
    )
    
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_user_subject))
    }
    
    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
}
```

**Abrir URL**:
```kotlin
binding.btnOpenWeb.setOnClickListener {
    val url = "https://www.google.com"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}
```

---

## 📦 Data Classes

### ¿Qué es una Data Class?
Clase especial para almacenar datos. Kotlin genera automáticamente `equals()`, `hashCode()`, `toString()`, `copy()`.

```kotlin
// Data class simple
data class User(
    val nombre: String,
    val edad: Int
)

// Uso
val user = User("Juan", 25)
println(user) // User(nombre=Juan, edad=25)

// Copiar con cambios
val user2 = user.copy(edad = 30)
println(user2) // User(nombre=Juan, edad=30)
```

**Ejemplo del Proyecto** (User.kt):
```kotlin
/**
 * DATA CLASS: Clase de datos para usuario
 * ===========================================
 * - data class genera automáticamente equals, hashCode, toString, copy
 * - Propiedades inmutables (val)
 * - Constructor primario con parámetros
 */
data class User(
    val nombre: String,
    val edad: Int
)
```

### Data Class con Valores por Defecto

```kotlin
data class Config(
    val darkMode: Boolean = false,
    val notificaciones: Boolean = true,
    val idioma: String = "es"
)

// Uso
val config1 = Config() // Todos los valores por defecto
val config2 = Config(darkMode = true) // Solo cambiar darkMode
```

**Ejemplo del Proyecto** (HistoryAction.kt):
```kotlin
data class HistoryAction(
    val userName: String,
    val userId: String,
    val actionType: ActionType,
    val timestamp: Long = System.currentTimeMillis() // Valor por defecto
) {
    enum class ActionType {
        CREATE, UPDATE, DELETE, VIEW
    }
    
    fun getActionText(): String {
        return when (actionType) {
            ActionType.CREATE -> "Usuario creado"
            ActionType.UPDATE -> "Usuario actualizado"
            ActionType.DELETE -> "Usuario eliminado"
            ActionType.VIEW -> "Usuario visualizado"
        }
    }
}
```

---

## 🎨 Strings y Colors en XML

### Strings.xml
Externalizar textos para facilitar traducción y mantenimiento.

```xml
<!-- res/values/strings.xml -->
<resources>
    <string name="app_name">Mi App</string>
    <string name="btn_save">Guardar</string>
    <string name="hint_name">Introduce tu nombre</string>
    
    <!-- Con parámetros -->
    <string name="welcome_message">Bienvenido, %1$s</string>
    <string name="user_age">Edad: %1$d años</string>
</resources>
```

**Uso en Kotlin**:
```kotlin
// String simple
binding.tvTitle.text = getString(R.string.app_name)

// String con parámetros
val nombre = "Juan"
binding.tvWelcome.text = getString(R.string.welcome_message, nombre)

val edad = 25
binding.tvAge.text = getString(R.string.user_age, edad)
```

**Uso en XML**:
```xml
<Button
    android:text="@string/btn_save" />

<TextView
    android:text="@string/app_name" />
```

### Colors.xml
Definir paleta de colores reutilizable.

```xml
<!-- res/values/colors.xml -->
<resources>
    <color name="primary">#6200EE</color>
    <color name="primary_dark">#3700B3</color>
    <color name="accent">#03DAC5</color>
    <color name="text_primary">#212121</color>
    <color name="text_secondary">#757575</color>
</resources>
```

**Uso en Kotlin**:
```kotlin
// Obtener color
val color = getColor(R.color.primary)
binding.tvTitle.setTextColor(color)
```

**Uso en XML**:
```xml
<TextView
    android:textColor="@color/text_primary"
    android:background="@color/primary" />
```

---

## 🔧 Tips y Trucos Adicionales

### 1. Visibilidad de Vistas

```kotlin
// Visible
binding.progressBar.visibility = View.VISIBLE

// Invisible (ocupa espacio)
binding.progressBar.visibility = View.INVISIBLE

// Gone (no ocupa espacio)
binding.progressBar.visibility = View.GONE
```

### 2. Toast (Mensajes Cortos)

```kotlin
Toast.makeText(
    this,              // Context
    "Mensaje aquí",    // Texto
    Toast.LENGTH_SHORT // Duración: SHORT o LONG
).show()
```

### 3. AlertDialog (Diálogos)

```kotlin
AlertDialog.Builder(this)
    .setTitle("Título")
    .setMessage("¿Estás seguro?")
    .setPositiveButton("Sí") { dialog, which ->
        // Acción al presionar Sí
    }
    .setNegativeButton("No") { dialog, which ->
        // Acción al presionar No
    }
    .setNeutralButton("Cancelar", null)
    .show()
```

**Ejemplo del Proyecto** (EditUserActivity.kt):
```kotlin
binding.btnDelete.setOnClickListener {
    // Confirmación antes de eliminar
    AlertDialog.Builder(this)
        .setTitle(getString(R.string.confirm_delete_title))
        .setMessage(getString(R.string.confirm_delete_message))
        .setPositiveButton(getString(R.string.btn_delete)) { _, _ ->
            deleteUser()
        }
        .setNegativeButton(getString(R.string.btn_cancel), null)
        .show()
}
```

### 4. ProgressBar

```kotlin
// Mostrar loading
binding.progressBar.visibility = View.VISIBLE

// Operación asíncrona
lifecycleScope.launch {
    val data = fetchData()
    
    // Ocultar loading
    binding.progressBar.visibility = View.GONE
    
    // Mostrar datos
    binding.tvData.text = data
}
```

### 5. ScrollView (Scroll en Layouts)

```xml
<ScrollView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true">
    
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">
        
        <!-- Contenido aquí -->
        
    </LinearLayout>
</ScrollView>
```

### 6. apply, let, with, run (Scope Functions)

```kotlin
// apply: configurar objeto (retorna el objeto)
binding.tvTitle.apply {
    text = "Título"
    textSize = 24f
    setTextColor(Color.BLACK)
}

// let: operaciones con 'it' (retorna resultado)
val length = binding.etNombre.text?.let {
    it.toString().trim().length
} ?: 0

// with: múltiples operaciones en objeto
with(binding.recyclerView) {
    layoutManager = LinearLayoutManager(context)
    adapter = myAdapter
    setHasFixedSize(true)
}
```

---

## 📱 Estructura del Proyecto

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/repaso01/
│   │   │   ├── MainActivity.kt
│   │   │   ├── AddUserActivity.kt
│   │   │   ├── UserListActivity.kt
│   │   │   ├── EditUserActivity.kt
│   │   │   ├── FavoritesActivity.kt
│   │   │   ├── HistoryActivity.kt
│   │   │   ├── User.kt (data class)
│   │   │   ├── HistoryAction.kt (data class)
│   │   │   ├── UserAdapter.kt
│   │   │   ├── HistoryAdapter.kt
│   │   │   ├── FirestoreManager.kt
│   │   │   ├── PreferencesManager.kt
│   │   │   ├── FavoritesManager.kt
│   │   │   └── HistoryManager.kt
│   │   │
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_add_user.xml
│   │   │   │   ├── activity_user_list.xml
│   │   │   │   ├── activity_edit_user.xml
│   │   │   │   ├── activity_favorites.xml
│   │   │   │   ├── activity_history.xml
│   │   │   │   ├── item_user.xml
│   │   │   │   └── item_history.xml
│   │   │   │
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   │
│   │   │   └── drawable/ (iconos, imágenes)
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── test/ (tests unitarios)
│
├── build.gradle.kts (configuración del módulo)
└── google-services.json (config de Firebase)
```

---

## 🎓 Conceptos Clave para el Examen

### 1. **ViewBinding**
- Reemplaza `findViewById`
- Binding = `NombreLayoutBinding` (PascalCase)
- Inflar con `.inflate(layoutInflater)`
- Acceder a vistas: `binding.nombreVista`

### 2. **RecyclerView**
- Lista eficiente que reutiliza vistas
- Necesita: Adapter, ViewHolder, LayoutManager
- `notifyDataSetChanged()` para actualizar

### 3. **Firestore**
- Base de datos NoSQL en la nube
- CRUD: `add()`, `get()`, `update()`, `delete()`
- Callbacks: `addOnSuccessListener` / `addOnFailureListener`

### 4. **DataStore**
- Reemplazo de SharedPreferences
- Operaciones con `suspend` (corrutinas)
- `Flow` para observar cambios

### 5. **Corrutinas**
- `lifecycleScope.launch { }` para tareas asíncronas
- `suspend` para funciones que se pueden pausar
- `collect` para observar Flows

### 6. **Lambdas**
- Funciones anónimas: `{ parámetros -> código }`
- Como parámetros: `(TipoEntrada) -> TipoSalida`
- Click listener: `setOnClickListener { código }`

### 7. **Intent**
- Explícito: navegar entre Activities
- Implícito: compartir, abrir URLs
- Extras: `putExtra()` / `getExtra()`

### 8. **Data Classes**
- `data class` genera automáticamente métodos
- Inmutables con `val`
- `copy()` para crear variaciones

---

## ✅ Checklist de Conceptos

- ✅ **INTENT**: MainActivity navega a otras Activities
- ✅ **DATA CLASS**: User y HistoryAction
- ✅ **LISTOF**: Listas en adapters y transformaciones
- ✅ **BINDING**: ViewBinding en todas las Activities
- ✅ **CONSTRAINTLAYOUT**: Layouts con constraints
- ✅ **RECYCLERVIEW**: UserAdapter y HistoryAdapter
- ✅ **LAMBDA**: Callbacks en adapters y click listeners
- ✅ **DATASTORE**: FavoritesManager, HistoryManager, PreferencesManager
- ✅ **COROUTINE**: lifecycleScope.launch y Flow.collect
- ✅ **CRASHLYTICS**: Logging de eventos
- ✅ **AUTH**: Autenticación de Firebase
- ✅ **FIRESTORE**: CRUD completo de usuarios

---

## 🚀 ¡Éxito en el Examen!

**Recuerda**:
1. ViewBinding para acceder a vistas
2. Lambdas para callbacks y eventos
3. Corrutinas para operaciones asíncronas
4. RecyclerView para listas eficientes
5. Firestore para base de datos en la nube

**Patrón común**:
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // 1. Inflar binding
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    // 2. Configurar vistas
    setupViews()
    
    // 3. Cargar datos
    loadData()
}
```

¡Todo lo que necesitas está en este proyecto! 💪
