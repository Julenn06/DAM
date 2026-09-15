package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utils.AstekoOrdutegia;
import utils.Horarios;
import utils.SessionManager;

public class Ordutegiak extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JPanel edukiPanel;
    private final AstekoOrdutegia ordutegiaTaula;
    private final JLabel egoerakoEtiketa;

    public Ordutegiak(String erabiltzailea) {

        setTitle("Ordutegia - EE Software");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        
        edukiPanel = new JPanel();
        edukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        edukiPanel.setLayout(new BorderLayout());
        setContentPane(edukiPanel);

        // Crear la tabla de horarios
        ordutegiaTaula = new AstekoOrdutegia();
        edukiPanel.add(ordutegiaTaula, BorderLayout.CENTER);

        // Panel superior
        JPanel goiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnFreskatu = new JButton("🔄 Eguneratu");
        btnFreskatu.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnFreskatu.setBackground(new Color(70, 130, 180));
        btnFreskatu.setForeground(Color.WHITE);
        btnFreskatu.addActionListener(e -> kargatuHorarioak());

        JButton btnAtzera = new JButton("← Atzera");
        btnAtzera.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnAtzera.setBackground(new Color(108, 117, 125));
        btnAtzera.setForeground(Color.WHITE);
        btnAtzera.addActionListener(e -> {
            dispose();
            new Menua(erabiltzailea).setVisible(true);
        });

        egoerakoEtiketa = new JLabel("Prest");
        egoerakoEtiketa.setFont(new Font("Tahoma", Font.BOLD, 12));

        goiPanel.add(btnFreskatu);
        goiPanel.add(btnAtzera);
        goiPanel.add(egoerakoEtiketa);

        edukiPanel.add(goiPanel, BorderLayout.NORTH);

        // Cargar horarios al iniciar
        kargatuHorarioak();
    }

    private void kargatuHorarioak() {
        egoerakoEtiketa.setText("Horarioak kargatzen...");
        egoerakoEtiketa.setForeground(Color.BLUE);

        new Thread(() -> {
            try {
                Long profeId = SessionManager.getInstance().getErabiltzaileId();
                
                if (profeId == null) {
                    EventQueue.invokeLater(() -> {
                        egoerakoEtiketa.setText("❌ Ez dago saio aktiborik");
                        egoerakoEtiketa.setForeground(Color.RED);
                        JOptionPane.showMessageDialog(this,
                            "Ezin izan dira horarioak kargatu.\nMesedez, hasi saioa berriro.",
                            "Saio Errorea",
                            JOptionPane.ERROR_MESSAGE);
                    });
                    return;
                }

                Horarios horariosBezeroa = new Horarios();
                List<Horarios.HorarioData> horarios = horariosBezeroa.lortuProfeHorarioak(profeId);

                EventQueue.invokeLater(() -> {
                    ordutegiaTaula.garbiOrdutegia();

                    for (Horarios.HorarioData horario : horarios) {
                        int diaIndex = getDiaIndex(horario.getDia());
                        int horaIndex = horario.getHora() - 1; // BD: 1-6, Array: 0-5

                        if (diaIndex > 0 && horaIndex >= 0 && horaIndex < 8) {
                            String texto = horario.getAula();
                            if (horario.getObservaciones() != null && !horario.getObservaciones().isEmpty()) {
                                texto += "\n" + horario.getObservaciones();
                            }
                            
                            ordutegiaTaula.ezarriGelaxkaEdukia(diaIndex, horaIndex, texto);
                            ordutegiaTaula.ezarriGelaxkaKolorea(diaIndex, horaIndex, AstekoOrdutegia.KLASE_ARRUNTA);
                        }
                    }

                    egoerakoEtiketa.setText("✓ " + horarios.size() + " horario kargatuta");
                    egoerakoEtiketa.setForeground(new Color(0, 128, 0));

                    if (horarios.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "Ez duzu horariorik une honetan.",
                            "Horariorik gabe",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                });

            } catch (Exception e) {
                EventQueue.invokeLater(() -> {
                    egoerakoEtiketa.setText("❌ Kargatzeko errorea");
                    egoerakoEtiketa.setForeground(Color.RED);

                    JOptionPane.showMessageDialog(this,
                        "Zerbitzariarekin konektatzeko errorea:\n" + e.getMessage()
                            + "\n\nEgiaztatu zerbitzaria martxan dagoela.",
                        "Konexio Errorea",
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private int getDiaIndex(String dia) {
        return switch (dia.toUpperCase()) {
            case "LUNES" -> 1;
            case "MARTES" -> 2;
            case "MIERCOLES" -> 3;
            case "JUEVES" -> 4;
            case "VIERNES" -> 5;
            default -> 0;
        };
    }
}
