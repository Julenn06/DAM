package com.example.repaso01

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repaso01.databinding.ItemUserBinding

/**
 * UserAdapter - Adapter personalizado para RecyclerView
 * 
 * PATRÓN ADAPTER:
 * ===============
 * El adapter act como puente entre los datos y el RecyclerView:
 *   1. Mantiene la lista de datos (users)
 *   2. Crea ViewHolders cuando es necesario (onCreateViewHolder)
 *   3. Reutiliza ViewHolders existentes para eficiencia
 *   4. Bindea datos a las vistas (onBindViewHolder)
 *   5. Informa el tamaño de la lista (getItemCount)
 * 
 * VENTAJAS DE RECYCLERVIEW sobre ListView:
 * ========================================
 *   - Reutilización automática de vistas (ViewHolder pattern obligatorio)
 *   - Mejor rendimiento con listas grandes
 *   - Animaciones de items incluidas
 *   - LayoutManagers flexibles (linear, grid, staggered)
 *   - Separación clara de responsabilidades
 * 
 * CONCEPTOS DEMOSTRADOS:
 * ======================
 * 1. RECYCLERVIEW: Implementación completa de adapter
 * 2. LAMBDA: Callback para eventos de click
 * 3. BINDING: ViewBinding para items del RecyclerView
 * 4. LISTOF: Manejo de listas mutables
 * 5. Inner class para ViewHolder
 * 6. Patrón ViewHolder para eficiencia
 */
// RECYCLERVIEW: Adapter que extiende RecyclerView.Adapter
// LAMBDA: Acepta función lambda como parámetro del constructor
class UserAdapter(
    // LAMBDA: Parámetro de tipo función (Higher-Order Function)
    // Sintaxis: (parámetros) -> TipoRetorno
    // (User, String) -> Unit significa:
    //   - Recibe User y String
    //   - No retorna nada (Unit es como void en Java)
    // Este parámetro permite al llamador definir qué hacer cuando se hace click
    private val onUserClick: (User, String) -> Unit,
    // LAMBDA: Callback para botón de favoritos (opcional con default)
    private val onFavoriteClick: ((String) -> Unit)? = null,
    // LAMBDA: Función para verificar si un ID es favorito
    private val isFavorite: ((String) -> Boolean)? = null
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // ============================================================================
    // LISTOF: Lista mutable de datos
    // ============================================================================
    // mutableListOf() crea ArrayList<Pair<String, User>>
    // Pair es una data class que almacena dos valores:
    //   - first: String (userId)
    //   - second: User (objeto usuario)
    // 
    // ¿Por qué Pair y no solo User?
    //   - Necesitamos el ID del documento de Firestore
    //   - User solo tiene nombre y edad
    //   - Pair asocia cada User con su ID
    private val users = mutableListOf<Pair<String, User>>()

    // ============================================================================
    // RECYCLERVIEW: ViewHolder - Patrón para eficiencia
    // ============================================================================
    /**
     * ViewHolder contiene referencias a las vistas de un item
     * 
     * PROPÓSITO:
     * ==========
     * - findViewById es costoso (traversa todo el árbol de vistas)
     * - ViewHolder guarda las referencias en memoria
     * - RecyclerView reutiliza ViewHolders en lugar de crearlos siempre
     * - Resultado: scroll suave incluso con miles de items
     * 
     * CICLO DE VIDA:
     * ==============
     * 1. onCreateViewHolder() crea el ViewHolder (pocas veces)
     * 2. onBindViewHolder() actualiza datos del ViewHolder (muchas veces)
     * 3. RecyclerView reutiliza ViewHolders al hacer scroll
     */
    // inner class: puede acceder a miembros de la clase externa (onUserClick)
    inner class UserViewHolder(
        // BINDING: ItemUserBinding generado desde item_user.xml
        // Contiene: tvNombre, tvEdad, tvId, root
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Bindea (asocia) datos a las vistas
         * Llamado cada vez que un item entra en pantalla
         */
        fun bind(userId: String, user: User) {
            // BINDING: apply {} para múltiples operaciones en el mismo objeto
            // Evita repetir "binding." en cada línea
            binding.apply {
                // Actualizar TextViews con datos del usuario
                tvNombre.text = user.nombre
                tvEdad.text = root.context.getString(R.string.user_item_edad, user.edad)
                tvId.text = "ID: $userId"

                // ============================================================
                // LAMBDA: Configurar botón de favoritos
                // ============================================================
                // Mostrar/ocultar botón según si onFavoriteClick está presente
                if (onFavoriteClick != null && isFavorite != null) {
                    btnFavorite.visibility = android.view.View.VISIBLE
                    
                    // Verificar si es favorito usando la lambda isFavorite
                    val favorite = isFavorite.invoke(userId)
                    
                    // Cambiar icono según estado de favorito
                    btnFavorite.setImageResource(
                        if (favorite) android.R.drawable.star_big_on
                        else android.R.drawable.star_big_off
                    )
                    
                    // LAMBDA: Click en botón de favoritos
                    btnFavorite.setOnClickListener {
                        // Invocar callback
                        onFavoriteClick.invoke(userId)
                    }
                } else {
                    // Ocultar botón si no hay callback de favoritos
                    btnFavorite.visibility = android.view.View.GONE
                }

                // ============================================================
                // LAMBDA: Invocar función lambda en evento click
                // ============================================================
                // root es la vista raíz del item (CardView en este caso)
                // setOnClickListener acepta una lambda
                root.setOnClickListener {
                    // Invocar la lambda recibida en el constructor
                    // Esto ejecuta el código definido en UserListActivity
                    onUserClick(user, userId)
                }
            }
        }
    }

    // ============================================================================
    // RECYCLERVIEW: Métodos obligatorios del adapter
    // ============================================================================
    
    /**
     * onCreateViewHolder - Crea un nuevo ViewHolder
     * 
     * CUÁNDO SE LLAMA:
     * ================
     * - Al inicio para crear ViewHolders iniciales
     * - Cuando se necesita uno nuevo (scroll a nuevos items)
     * - Se llama POCAS veces (RecyclerView reutiliza)
     * 
     * @param parent: ViewGroup contenedor (el RecyclerView)
     * @param viewType: tipo de vista (útil para listas heterogéneas)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // BINDING: Inflar layout del item usando ViewBinding
        // LayoutInflater convierte XML en objetos View
        // attachToRoot = false porque RecyclerView lo añade automáticamente
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false // No adjuntar a parent todavía
        )
        return UserViewHolder(binding)
    }

    /**
     * onBindViewHolder - Bindea datos a un ViewHolder existente
     * 
     * CUÁNDO SE LLAMA:
     * ================
     * - Cada vez que un item entra en pantalla
     * - Al hacer scroll (reutiliza ViewHolders)
     * - Se llama MUCHAS veces
     * 
     * @param holder: ViewHolder a actualizar
     * @param position: posición en la lista
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // Obtener usuario en esta posición
        // Destructuring: val (a, b) = pair extrae pair.first y pair.second
        val (userId, user) = users[position]
        // Delegar al ViewHolder para bindear datos
        holder.bind(userId, user)
    }

    /**
     * getItemCount - Retorna cantidad de items
     * 
     * RecyclerView usa esto para:
     * - Saber cuántos items debe mostrar
     * - Detectar cambios en el tamaño de la lista
     * - Optimizar el reciclaje de ViewHolders
     */
    override fun getItemCount(): Int = users.size

    // ============================================================================
    // LISTOF: Métodos para actualizar la lista
    // ============================================================================
    
    /**
     * submitList - Reemplaza toda la lista de usuarios
     * 
     * PASOS:
     * ======
     * 1. Limpia la lista actual
     * 2. Añade todos los nuevos elementos
     * 3. Notifica al RecyclerView para que redibuje
     * 
     * notifyDataSetChanged():
     * - Indica que todos los datos cambiaron
     * - RecyclerView redibuja toda la lista
     * - Para listas pequeñas está bien
     * - Para grandes, usar DiffUtil es más eficiente
     */
    fun submitList(newUsers: List<Pair<String, User>>) {
        users.clear() // Vaciar lista actual
        users.addAll(newUsers) // Añadir nuevos
        notifyDataSetChanged() // Notificar cambios
    }

    /**
     * addUser - Añade un usuario individual
     * 
     * notifyItemInserted():
     * - Más eficiente que notifyDataSetChanged()
     * - Solo redibuja el item añadido
     * - Incluye animación de inserción
     */
    fun addUser(userId: String, user: User) {
        users.add(Pair(userId, user))
        // notifyItemInserted en la última posición
        notifyItemInserted(users.size - 1)
    }
}
