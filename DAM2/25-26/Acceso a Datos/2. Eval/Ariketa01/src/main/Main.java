package main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	private static final String URL = "jdbc:mysql://localhost:3307/ariketa01julenad";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			System.out.println("Datu-basearekin konektatuta.");

			// 1. zatia: Kontsultak Statement-ekin exekutatu
			System.out.println("\n--- Statement-ekin kontsultak ---");
			exekutatuKontsultakStatement(conn);

			// 2. zatia: 2. kontsulta PreparedStatement-ekin
			System.out.println("\n--- 2. kontsulta PreparedStatement-ekin ---");
			exekutatu2KontsultaPreparedStatement(conn);

			// 3. zatia: Kontsultak CallableStatement-ekin (prozedura gordeta)
			System.out.println("\n--- CallableStatement-ekin kontsultak ---");
			exekutatuKontsultakCallableStatement(conn);

			// Eragiketa gehigarriak CallableStatement-ekin
			System.out.println("\n--- CallableStatement-ekin eragiketa gehigarriak ---");
			eragiketaGehigarriak(conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void exekutatuKontsultakStatement(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();

		// 1. kontsulta
		ResultSet rs1 = stmt.executeQuery("SELECT * FROM DEPARTAMENTOS");
		System.out.println("1. Departamentuak:");
		while (rs1.next()) {
			System.out.println(rs1.getInt("DEPT_NO") + " - " + rs1.getString("DNOMBRE") + " - " + rs1.getString("LOC"));
		}
		rs1.close();

		// 2. kontsulta
		ResultSet rs2 = stmt.executeQuery("SELECT APELLIDO, OFICIO, SALARIO FROM EMPLEADOS WHERE DEPT_NO = 10");
		System.out.println("\n2. 10. departamentuko langileak:");
		while (rs2.next()) {
			System.out.println(
					rs2.getString("APELLIDO") + " - " + rs2.getString("OFICIO") + " - " + rs2.getFloat("SALARIO"));
		}
		rs2.close();

		// 3. kontsulta
		ResultSet rs3 = stmt.executeQuery(
				"SELECT APELLIDO, OFICIO, SALARIO FROM EMPLEADOS WHERE SALARIO = (SELECT MAX(SALARIO) FROM EMPLEADOS)");
		System.out.println("\n3. Soldata altuena duen langilea:");
		while (rs3.next()) {
			System.out.println(
					rs3.getString("APELLIDO") + " - " + rs3.getString("OFICIO") + " - " + rs3.getFloat("SALARIO"));
		}
		rs3.close();

		stmt.close();
	}

	private static void exekutatu2KontsultaPreparedStatement(Connection conn) throws SQLException {
		String sql = "SELECT APELLIDO, OFICIO, SALARIO FROM EMPLEADOS WHERE DEPT_NO = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, 10);
		ResultSet rs = pstmt.executeQuery();
		System.out.println("10. departamentuko langileak:");
		while (rs.next()) {
			System.out.println(
					rs.getString("APELLIDO") + " - " + rs.getString("OFICIO") + " - " + rs.getFloat("SALARIO"));
		}
		rs.close();
		pstmt.close();
	}

	private static void exekutatuKontsultakCallableStatement(Connection conn) throws SQLException {
		// 1. kontsulta
		CallableStatement cstmt1 = conn.prepareCall("{CALL consultar_departamentos()}");
		ResultSet rs1 = cstmt1.executeQuery();
		System.out.println("1. Departamentuak:");
		while (rs1.next()) {
			System.out.println(rs1.getInt("DEPT_NO") + " - " + rs1.getString("DNOMBRE") + " - " + rs1.getString("LOC"));
		}
		rs1.close();
		cstmt1.close();

		// 2. kontsulta
		CallableStatement cstmt2 = conn.prepareCall("{CALL consultar_empleados_dept(?)}");
		cstmt2.setInt(1, 10);
		ResultSet rs2 = cstmt2.executeQuery();
		System.out.println("\n2. 10. departamentuko langileak:");
		while (rs2.next()) {
			System.out.println(
					rs2.getString("APELLIDO") + " - " + rs2.getString("OFICIO") + " - " + rs2.getFloat("SALARIO"));
		}
		rs2.close();
		cstmt2.close();

		// 3. kontsulta
		CallableStatement cstmt3 = conn.prepareCall("{CALL consultar_empleado_max_salario()}");
		ResultSet rs3 = cstmt3.executeQuery();
		System.out.println("\n3. Soldata altuena duen langilea:");
		while (rs3.next()) {
			System.out.println(
					rs3.getString("APELLIDO") + " - " + rs3.getString("OFICIO") + " - " + rs3.getFloat("SALARIO"));
		}
		rs3.close();
		cstmt3.close();
	}

	private static void eragiketaGehigarriak(Connection conn) throws SQLException {
		// Departamentua eta langileak gehitu
		CallableStatement cstmtAdd = conn.prepareCall("{CALL añadir_dept_marketing()}");
		cstmtAdd.execute();
		System.out.println("MARKETING departamentua eta langileak gehitu dira.");
		cstmtAdd.close();

		// Soldatak eguneratu
		CallableStatement cstmtUpdate = conn.prepareCall("{CALL actualizar_salarios_marketing(?)}");
		cstmtUpdate.setFloat(1, 1700);
		cstmtUpdate.execute();
		System.out.println("Soldatak 1700era eguneratu dira.");
		cstmtUpdate.close();

		// Departamentua eta langileak ezabatu
		CallableStatement cstmtDelete = conn.prepareCall("{CALL eliminar_dept_marketing()}");
		cstmtDelete.execute();
		System.out.println("MARKETING departamentua eta langileak ezabatu dira.");
		cstmtDelete.close();
	}
}