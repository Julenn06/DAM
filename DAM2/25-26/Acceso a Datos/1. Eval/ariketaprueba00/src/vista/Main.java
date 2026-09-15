package vista;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controlador.ReadFile;
import controlador.GetId;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton AddUsers = new JButton("Add User");
		AddUsers.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        JPanel panelDatos = new JPanel();
		        panelDatos.setLayout(new GridLayout(3, 2));

		        panelDatos.add(new JLabel("Name:"));
		        JTextField txtNombre = new JTextField(10);
		        panelDatos.add(txtNombre);

		        panelDatos.add(new JLabel("Age:"));
		        JTextField txtEdad = new JTextField(10);
		        panelDatos.add(txtEdad);

		        panelDatos.add(new JLabel("Salary:"));
		        JTextField txtSalary = new JTextField(10);
		        panelDatos.add(txtSalary);

		        int result = JOptionPane.showConfirmDialog(null, panelDatos, "Introduce tus datos",
		                JOptionPane.OK_CANCEL_OPTION);

		        if (result == JOptionPane.OK_OPTION) {
		            String nombre = txtNombre.getText();
		            try {
		                int age = Integer.parseInt(txtEdad.getText());
		                double salary = Double.parseDouble(txtSalary.getText());

		                GetId idGetter = new GetId();
		                int lastId = idGetter.getLastId("users.txt");
		                int newId = lastId + 1;

		                try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
		                    writer.write(newId + "\t" + nombre + "\t" + age + "\t" + salary);
		                    writer.newLine();
		                    JOptionPane.showMessageDialog(null, "User added successfully with ID: " + newId);
		                } catch (Exception ex) {
		                    JOptionPane.showMessageDialog(null, "Error saving user: " + ex.getMessage());
		                }
		            } catch (NumberFormatException ex) {
		                JOptionPane.showMessageDialog(null, "Invalid age or salary format.");
		            }
		        }
		    }
		});


		AddUsers.setBounds(109, 68, 110, 93);
		contentPane.add(AddUsers);

		JButton ShowUsers = new JButton("Show Users");
		ShowUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReadFile reader = new ReadFile();
				reader.MostrarFichero();
			}
		});
		ShowUsers.setBounds(557, 82, 110, 93);
		contentPane.add(ShowUsers);
		
		JButton DeleteUser = new JButton("Delete User");
		DeleteUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReadFile reader = new ReadFile();
				reader.MostrarFichero();
				
			}
		});
		DeleteUser.setBounds(109, 280, 110, 93);
		contentPane.add(DeleteUser);
	}
}