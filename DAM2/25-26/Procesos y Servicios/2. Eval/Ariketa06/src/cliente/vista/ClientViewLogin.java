package cliente.vista;

import javax.swing.*;
import java.awt.*;

public class ClientViewLogin extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtNickname;
    private JButton btnConectar;
    private String nickname;
    private boolean conectar;
    
    public ClientViewLogin(JFrame parent) {
        super(parent, "Conexión al servidor", true);
        setSize(250, 150);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel central
        JPanel panelCentral = new JPanel(new FlowLayout());
        panelCentral.add(new JLabel("Goitizena:"));
        txtNickname = new JTextField(15);
        panelCentral.add(txtNickname);
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel behekoa
        JPanel panelInferior = new JPanel();
        btnConectar = new JButton("Konektatu");
        panelInferior.add(btnConectar);
        add(panelInferior, BorderLayout.SOUTH);
        
        // Listeners-ak
        btnConectar.addActionListener(e -> conectar());
        txtNickname.addActionListener(e -> conectar());
    }
    
    private void conectar() {
        nickname = txtNickname.getText().trim();
        if (nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Goitizena ezin da hutsik egon", 
                "Errorea", 
                JOptionPane.ERROR_MESSAGE);
        } else {
            conectar = true;
            dispose();
        }
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public boolean isConectar() {
        return conectar;
    }
}
