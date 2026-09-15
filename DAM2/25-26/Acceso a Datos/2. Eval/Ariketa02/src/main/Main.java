package main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// RowSet imports
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

public class Main {

	private static final String URL = "jdbc:mysql://localhost:3307/repasoporlacara";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	public static void main(String[] args) {

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

			System.out.println("╔════════════════════════════════════════════════════════════════╗");
			System.out.println("║   CONEXIÓN ESTABLECIDA A LA BASE DE DATOS       ║");
			System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

			// Crear tabla y procedimiento almacenado
			crearTablaUsuarios(conn);
			crearProcedimientoAlmacenado(conn);

			// ═══════════════════════════════════════
			// READ
			// ═══════════════════════════════════════

			System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
			System.out.println("║                    📖 TODAS LAS FORMAS DE LEER                ║");
			System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

			leerConStatement(conn);
			leerConPreparedStatement(conn, 1);
			leerConCallableStatement(conn);
			leerConJdbcRowSet(conn);
			leerConCachedRowSet(conn);

			// ═══════════════════════════════════════
			// CREATE
			// ═══════════════════════════════════════

			System.out.println("\n\n╔════════════════════════════════════════════════════════════════╗");
			System.out.println("║                   ➕ TODAS LAS FORMAS DE CREAR                 ║");
			System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

			crearConStatement(conn, "Carlos", 35, true);
			crearConPreparedStatement(conn, "Ana", 28, true);
			crearConBatch(conn);

			// ═══════════════════════════════════════
			// UPDATE
			// ═══════════════════════════════════════

			System.out.println("\n\n╔════════════════════════════════════════════════════════════════╗");
			System.out.println("║                 ✏️  TODAS LAS FORMAS DE ACTUALIZAR             ║");
			System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

			actualizarConStatement(conn, 1, "CarlosEditado", 36, false);
			actualizarConPreparedStatement(conn, 2, "AnaEditado", 29, false);
			actualizarConBatch(conn);
			actualizarConCachedRowSet(conn, 1);

			// ═══════════════════════════════════════
			// DELETE
			// ═══════════════════════════════════════

			System.out.println("\n\n╔════════════════════════════════════════════════════════════════╗");
			System.out.println("║                 🗑️  TODAS LAS FORMAS DE ELIMINAR               ║");
			System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

			eliminarConStatement(conn, 3);
			eliminarConPreparedStatement(conn, 4);
			eliminarConBatch(conn);
			eliminarConCachedRowSet(conn, 5);

			System.out.println("\n\n╔══════════════════════════════════════════════════════╗");
			System.out.println("║          ✅ TODOS LOS EJEMPLOS COMPLETADOS 🎉        ║");
			System.out.println("╚══════════════════════════════════════════════════════╝\n");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ═══════════════════════════════════════
	// SETUP
	// ═══════════════════════════════════════

	/**
	 * CREA LA TABLA usuarios SI NO EXISTE
	 */
	private static void crearTablaUsuarios(Connection conn) throws SQLException {
		Statement st = conn.createStatement();

		String crearTabla = "CREATE TABLE IF NOT EXISTS usuarios (" + "    id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "    nombre VARCHAR(100) NOT NULL, " + "    edad INT NOT NULL, " + "    matriculado BOOLEAN NOT NULL"
				+ ")";

		st.execute(crearTabla);
		System.out.println("✓ Tabla 'usuarios' verificada/creada correctamente.\n");
	}

	/**
	 * CREA EL PROCEDIMIENTO ALMACENADO si no existe
	 */
	private static void crearProcedimientoAlmacenado(Connection conn) throws SQLException {
		Statement st = conn.createStatement();

		// Primero borrar si existe (para evitar errores)
		try {
			st.execute("DROP PROCEDURE IF EXISTS obtenerUsuarios");
		} catch (SQLException e) {
			// Ignorar si no existe
		}

		// Crear el procedimiento
		String crearProcedure = "CREATE PROCEDURE obtenerUsuarios() " + "BEGIN " + "    SELECT * FROM usuarios; "
				+ "END";

		st.execute(crearProcedure);
		System.out.println("✓ Procedimiento almacenado 'obtenerUsuarios' creado correctamente.\n");
	}

	// ═══════════════════════════════════════
	// READ
	// ═══════════════════════════════════════

	/**
	 * 1️⃣ LECTURA CON STATEMENT (básico)
	 */
	private static void leerConStatement(Connection conn) throws SQLException {
		System.out.println("━━━ 1️⃣  MÉTODO: Statement (básico) ━━━");

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM usuarios");

		int count = 0;
		while (rs.next()) {
			imprimirUsuario(rs);
			count++;
		}
		System.out.println("📊 Total registros leídos: " + count + "\n");
	}

	/**
	 * 2️⃣ LECTURA CON PREPAREDSTATEMENT (parametrizado)
	 */
	private static void leerConPreparedStatement(Connection conn, int id) throws SQLException {
		System.out.println("━━━ 2️⃣  MÉTODO: PreparedStatement (parametrizado) ━━━");

		PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
		ps.setInt(1, id);

		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			imprimirUsuario(rs);
		} else {
			System.out.println("⚠️  Ese ID no existe, Chumacho.");
		}
		System.out.println();
	}

	/**
	 * 3️⃣ LECTURA CON CALLABLESTATEMENT (Stored Procedure)
	 */
	private static void leerConCallableStatement(Connection conn) throws SQLException {
		System.out.println("━━━ 3️⃣  MÉTODO: CallableStatement (Stored Procedure) ━━━");

		CallableStatement cs = conn.prepareCall("{CALL obtenerUsuarios()}");
		ResultSet rs = cs.executeQuery();

		int count = 0;
		while (rs.next()) {
			imprimirUsuario(rs);
			count++;
		}
		System.out.println("📊 Total registros leídos: " + count + "\n");
	}

	/**
	 * 4️⃣ LECTURA CON JDBCROWSET (scrollable + conectado)
	 */
	private static void leerConJdbcRowSet(Connection conn) throws SQLException {
		System.out.println("━━━ 4️⃣  MÉTODO: JdbcRowSet (scrollable) ━━━");

		JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
		rowSet.setUrl(URL);
		rowSet.setUsername(USER);
		rowSet.setPassword(PASSWORD);

		rowSet.setCommand("SELECT * FROM usuarios");
		rowSet.execute();

		int count = 0;
		while (rowSet.next()) {
			System.out.println("   " + rowSet.getInt("id") + " - " + rowSet.getString("nombre") + " - "
					+ rowSet.getInt("edad") + " - " + rowSet.getBoolean("matriculado"));
			count++;
		}
		System.out.println("📊 Total registros leídos: " + count + "\n");
	}

	/**
	 * 5️⃣ LECTURA CON CACHEDROWSET (desconectado)
	 */
	private static void leerConCachedRowSet(Connection conn) throws SQLException {
		System.out.println("━━━ 5️⃣  MÉTODO: CachedRowSet (desconectado) ━━━");

		CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
		crs.setCommand("SELECT * FROM usuarios");
		crs.execute(conn);

		int count = 0;
		while (crs.next()) {
			System.out.println("   " + crs.getInt("id") + " - " + crs.getString("nombre") + " - " + crs.getInt("edad")
					+ " - " + crs.getBoolean("matriculado"));
			count++;
		}
		System.out.println("📊 Total registros leídos: " + count + "\n");
	}

	// ═══════════════════════════════════════
	// CREATE
	// ═══════════════════════════════════════

	/**
	 * 1️⃣ CREATE CON STATEMENT (básico, sin parámetros) ⚠️ NO USAR EN PRODUCCIÓN:
	 * Vulnerable a SQL Injection
	 */
	private static void crearConStatement(Connection conn, String nombre, int edad, boolean matriculado)
			throws SQLException {
		System.out.println("━━━ 1️⃣  MÉTODO: Statement (básico) ━━━");

		Statement st = conn.createStatement();
		String sql = "INSERT INTO usuarios (nombre, edad, matriculado) VALUES ('" + nombre + "', " + edad + ", "
				+ matriculado + ")";

		st.executeUpdate(sql);
		System.out.println("✓ Usuario insertado: " + nombre + "\n");
	}

	/**
	 * 2️⃣ CREATE CON PREPAREDSTATEMENT (parametrizado y seguro)
	 */
	private static void crearConPreparedStatement(Connection conn, String nombre, int edad, boolean matriculado)
			throws SQLException {
		System.out.println("━━━ 2️⃣  MÉTODO: PreparedStatement (parametrizado) ━━━");

		PreparedStatement ps = conn
				.prepareStatement("INSERT INTO usuarios (nombre, edad, matriculado) VALUES (?, ?, ?)");

		ps.setString(1, nombre);
		ps.setInt(2, edad);
		ps.setBoolean(3, matriculado);

		ps.executeUpdate();
		System.out.println("✓ Usuario insertado: " + nombre + "\n");
	}

	/**
	 * 3️⃣ CREATE CON BATCH PROCESSING (múltiples inserciones)
	 */
	private static void crearConBatch(Connection conn) throws SQLException {
		System.out.println("━━━ 3️⃣  MÉTODO: Batch Processing (múltiples operaciones) ━━━");

		PreparedStatement ps = conn
				.prepareStatement("INSERT INTO usuarios (nombre, edad, matriculado) VALUES (?, ?, ?)");

		// Usuario 1
		ps.setString(1, "María");
		ps.setInt(2, 30);
		ps.setBoolean(3, true);
		ps.addBatch();

		// Usuario 2
		ps.setString(1, "Luis");
		ps.setInt(2, 35);
		ps.setBoolean(3, false);
		ps.addBatch();

		// Usuario 3
		ps.setString(1, "Pedro");
		ps.setInt(2, 32);
		ps.setBoolean(3, true);
		ps.addBatch();

		// Ejecutar todo de golpe
		int[] resultados = ps.executeBatch();
		System.out.println("✓ " + resultados.length + " usuarios insertados con Batch\n");
	}

	// ═══════════════════════════════════════
	// UPDATE
	// ═══════════════════════════════════════

	/**
	 * 1️⃣ UPDATE CON STATEMENT (básico, sin parámetros) ⚠️ NO USAR EN PRODUCCIÓN:
	 * Vulnerable a SQL Injection
	 */
	private static void actualizarConStatement(Connection conn, int id, String nuevoNombre, int nuevaEdad,
			boolean nuevoMatriculado) throws SQLException {
		System.out.println("━━━ 1️⃣  MÉTODO: Statement (básico) ━━━");

		Statement st = conn.createStatement();
		String sql = "UPDATE usuarios SET nombre = '" + nuevoNombre + "', edad = " + nuevaEdad + ", matriculado = "
				+ nuevoMatriculado + " WHERE id = " + id;

		st.executeUpdate(sql);
		System.out.println("✓ Usuario actualizado, ID: " + id + "\n");
	}

	/**
	 * 2️⃣ UPDATE CON PREPAREDSTATEMENT (parametrizado y seguro)
	 */
	private static void actualizarConPreparedStatement(Connection conn, int id, String nuevoNombre, int nuevaEdad,
			boolean nuevoMatriculado) throws SQLException {
		System.out.println("━━━ 2️⃣  MÉTODO: PreparedStatement (parametrizado) ━━━");

		PreparedStatement ps = conn
				.prepareStatement("UPDATE usuarios SET nombre = ?, edad = ?, matriculado = ? WHERE id = ?");

		ps.setString(1, nuevoNombre);
		ps.setInt(2, nuevaEdad);
		ps.setBoolean(3, nuevoMatriculado);
		ps.setInt(4, id);

		ps.executeUpdate();
		System.out.println("✓ Usuario actualizado, ID: " + id + "\n");
	}

	/**
	 * 3️⃣ UPDATE CON BATCH PROCESSING (múltiples actualizaciones)
	 */
	private static void actualizarConBatch(Connection conn) throws SQLException {
		System.out.println("━━━ 3️⃣  MÉTODO: Batch Processing (múltiples operaciones) ━━━");

		PreparedStatement ps = conn.prepareStatement("UPDATE usuarios SET edad = edad + 1 WHERE nombre = ?");

		ps.setString(1, "Carlos");
		ps.addBatch();

		ps.setString(1, "Ana");
		ps.addBatch();

		int[] resultados = ps.executeBatch();
		System.out.println("✓ " + resultados.length + " usuarios actualizados con Batch\n");
	}

	/**
	 * 4️⃣ UPDATE CON CACHEDROWSET (modo desconectado)
	 */
	private static void actualizarConCachedRowSet(Connection conn, int id) throws SQLException {
		System.out.println("━━━ 4️⃣  MÉTODO: CachedRowSet (desconectado) ━━━");

		// CachedRowSet necesita su propia conexión con control de transacciones
		Connection crsConn = DriverManager.getConnection(URL, USER, PASSWORD);
		crsConn.setAutoCommit(false); // Desactivar autocommit para CachedRowSet

		try {
			CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
			crs.setCommand("SELECT * FROM usuarios WHERE id = " + id);
			crs.setTableName("usuarios");

			int[] keys = { 1 };
			crs.setKeyColumns(keys);

			crs.execute(crsConn);

			if (crs.next()) {
				crs.updateString("nombre", crs.getString("nombre") + "_Editado");
				crs.updateInt("edad", crs.getInt("edad") + 1);
				crs.updateRow();

				crs.acceptChanges(crsConn);
				crsConn.commit();
				System.out.println("✓ Usuario actualizado con CachedRowSet, ID: " + id + "\n");
			} else {
				System.out.println("⚠️  No se encontró el usuario con ID: " + id + "\n");
			}
		} catch (SQLException e) {
			crsConn.rollback();
			throw e;
		} finally {
			crsConn.close();
		}
	}

	// ═══════════════════════════════════════
	// DELETE
	// ═══════════════════════════════════════

	/**
	 * 1️⃣ DELETE CON STATEMENT (básico, sin parámetros) ⚠️ NO USAR EN PRODUCCIÓN:
	 * Vulnerable a SQL Injection
	 */
	private static void eliminarConStatement(Connection conn, int id) throws SQLException {
		System.out.println("━━━ 1️⃣  MÉTODO: Statement (básico) ━━━");

		Statement st = conn.createStatement();
		String sql = "DELETE FROM usuarios WHERE id = " + id;

		st.executeUpdate(sql);
		System.out.println("✓ Usuario eliminado, ID: " + id + "\n");
	}

	/**
	 * 2️⃣ DELETE CON PREPAREDSTATEMENT (parametrizado y seguro)
	 */
	private static void eliminarConPreparedStatement(Connection conn, int id) throws SQLException {
		System.out.println("━━━ 2️⃣  MÉTODO: PreparedStatement (parametrizado) ━━━");

		PreparedStatement ps = conn.prepareStatement("DELETE FROM usuarios WHERE id = ?");
		ps.setInt(1, id);

		ps.executeUpdate();
		System.out.println("✓ Usuario eliminado, ID: " + id + "\n");
	}

	/**
	 * 3️⃣ DELETE CON BATCH PROCESSING (múltiples eliminaciones)
	 */
	private static void eliminarConBatch(Connection conn) throws SQLException {
		System.out.println("━━━ 3️⃣  MÉTODO: Batch Processing (múltiples operaciones) ━━━");

		PreparedStatement ps = conn.prepareStatement("DELETE FROM usuarios WHERE nombre = ?");

		ps.setString(1, "María");
		ps.addBatch();

		ps.setString(1, "Luis");
		ps.addBatch();

		ps.setString(1, "Pedro");
		ps.addBatch();

		int[] resultados = ps.executeBatch();
		System.out.println("✓ " + resultados.length + " usuarios eliminados con Batch\n");
	}

	/**
	 * 4️⃣ DELETE CON CACHEDROWSET (modo desconectado)
	 */
	private static void eliminarConCachedRowSet(Connection conn, int id) throws SQLException {
		System.out.println("━━━ 4️⃣  MÉTODO: CachedRowSet (desconectado) ━━━");

		// CachedRowSet necesita su propia conexión con control de transacciones
		Connection crsConn = DriverManager.getConnection(URL, USER, PASSWORD);
		crsConn.setAutoCommit(false); // Desactivar autocommit para CachedRowSet

		try {
			CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
			crs.setCommand("SELECT * FROM usuarios WHERE id = " + id);
			crs.setTableName("usuarios");

			int[] keys = { 1 };
			crs.setKeyColumns(keys);

			crs.execute(crsConn);

			if (crs.next()) {
				crs.deleteRow();

				crs.acceptChanges(crsConn);
				crsConn.commit();
				System.out.println("✓ Usuario eliminado con CachedRowSet, ID: " + id + "\n");
			} else {
				System.out.println("⚠️  No se encontró el usuario con ID: " + id + "\n");
			}
		} catch (SQLException e) {
			crsConn.rollback();
			throw e;
		} finally {
			crsConn.close();
		}
	}

	// ===============================================================
	// ====================== FUNCIÓN AUXILIAR =======================
	// ===============================================================

	private static void imprimirUsuario(ResultSet rs) throws SQLException {
		System.out.println(rs.getInt("id") + " - " + rs.getString("nombre") + " - " + rs.getInt("edad") + " - "
				+ rs.getBoolean("matriculado"));
	}
}
