package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import utils.AstekoOrdutegia;
import utils.GetAllUsers;
import utils.Horarios;

public class Kontsultak extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JPanel edukiPanel;
    private final AstekoOrdutegia ordutegiaTabla;
    private final String erabiltzailea;
    private final JList<String> profesoreLista;
    private final DefaultListModel<String> listaModeloa;
    private final JLabel egoerakoEtiketa;
    private List<GetAllUsers.ErabiltzaileData> profesoreak;

    public Kontsultak(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
        this.listaModeloa = new DefaultListModel<>();
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(
            Kontsultak.class.getResource("/img/elorrieta.png")));
        setTitle("Kontsultak - EE Software");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 700);
        
        edukiPanel = new JPanel();
        edukiPanel.setBackground(new Color(240, 240, 240));
        edukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        edukiPanel.setLayout(new BorderLayout());
        setContentPane(edukiPanel);

        // Panel superior
        edukiPanel.add(sortuGoiburua(), BorderLayout.NORTH);

        // Panel central con división
        JPanel erdigunePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Panel izquierdo - Lista de profesores
        JPanel ezkerPanel = new JPanel(new BorderLayout());
        ezkerPanel.setBorder(new TitledBorder("Irakasleen Zerrenda"));
        
        profesoreLista = new JList<>(listaModeloa);
        profesoreLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profesoreLista.setFont(new Font("Tahoma", Font.PLAIN, 13));
        profesoreLista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                kargatuHautatutakoProfeHorarioa();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(profesoreLista);
        ezkerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel botoiPanel = new JPanel(new FlowLayout());
        JButton btnFreskatu = new JButton("🔄 Eguneratu");
        btnFreskatu.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnFreskatu.setBackground(new Color(70, 130, 180));
        btnFreskatu.setForeground(Color.WHITE);
        btnFreskatu.addActionListener(e -> kargatuProfesoreakZerbitzaritik());
        botoiPanel.add(btnFreskatu);
        
        ezkerPanel.add(botoiPanel, BorderLayout.SOUTH);
        
        // Panel derecho - Horario
        JPanel eskuinPanel = new JPanel(new BorderLayout());
        eskuinPanel.setBorder(new TitledBorder("Ordutegia"));
        
        ordutegiaTabla = new AstekoOrdutegia();
        ordutegiaTabla.ezarriOrdutegiIzenburua("Hautatu irakasle bat");
        eskuinPanel.add(ordutegiaTabla, BorderLayout.CENTER);
        
        erdigunePanel.add(ezkerPanel);
        erdigunePanel.add(eskuinPanel);
        
        edukiPanel.add(erdigunePanel, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel azpiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        egoerakoEtiketa = new JLabel("Prest");
        egoerakoEtiketa.setFont(new Font("Tahoma", Font.BOLD, 12));
        azpiPanel.add(egoerakoEtiketa);
        edukiPanel.add(azpiPanel, BorderLayout.SOUTH);
        
        // Cargar profesores al iniciar
        kargatuProfesoreakZerbitzaritik();
    }

    private JPanel sortuGoiburua() {
        JPanel goiburuPanel = new JPanel();
        goiburuPanel.setBackground(new Color(240, 240, 240));
        goiburuPanel.setLayout(null);
        goiburuPanel.setPreferredSize(new java.awt.Dimension(0, 80));

        JLabel izenburua = new JLabel("Irakasleen Ordutegiak - Kontsulta");
        izenburua.setFont(new Font("Tahoma", Font.BOLD, 24));
        izenburua.setForeground(new Color(51, 102, 153));
        izenburua.setBounds(50, 20, 600, 40);
        goiburuPanel.add(izenburua);

        JButton btnAtzera = new JButton("← Atzera");
        btnAtzera.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAtzera.setBackground(new Color(108, 117, 125));
        btnAtzera.setForeground(Color.WHITE);
        btnAtzera.setBounds(980, 25, 100, 30);
        btnAtzera.addActionListener(e -> {
            dispose();
            new Menua(erabiltzailea).setVisible(true);
        });
        goiburuPanel.add(btnAtzera);

        return goiburuPanel;
    }

    private void kargatuProfesoreakZerbitzaritik() {
        egoerakoEtiketa.setText("Irakasleak kargatzen...");
        egoerakoEtiketa.setForeground(Color.BLUE);

        new Thread(() -> {
            try {
                GetAllUsers bezeroa = new GetAllUsers();
                profesoreak = bezeroa.lortuErabiltzaileakMotarenArabera(3); // tipo_id=3 (profesores)

                EventQueue.invokeLater(() -> {
                    listaModeloa.clear();

                    for (GetAllUsers.ErabiltzaileData profesorea : profesoreak) {
                        String izena = profesorea.getIzena() + " " + profesorea.getAbizenak();
                        listaModeloa.addElement(izena + " (" + profesorea.getEmail() + ")");
                    }

                    egoerakoEtiketa.setText("✓ " + profesoreak.size() + " irakasle kargatuta");
                    egoerakoEtiketa.setForeground(new Color(0, 128, 0));

                    if (profesoreak.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "Ez da irakaslerik aurkitu sisteman.",
                            "Daturik gabe",
                            JOptionPane.WARNING_MESSAGE);
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

    private void kargatuHautatutakoProfeHorarioa() {
        int hautatutakoIndizea = profesoreLista.getSelectedIndex();
        
        if (hautatutakoIndizea == -1 || profesoreak == null || hautatutakoIndizea >= profesoreak.size()) {
            return;
        }

        GetAllUsers.ErabiltzaileData hautatutakoProfe = profesoreak.get(hautatutakoIndizea);
        String profeIzena = hautatutakoProfe.getIzena() + " " + hautatutakoProfe.getAbizenak();
        
        egoerakoEtiketa.setText(profeIzena + " - Ordutegia kargatzen...");
        egoerakoEtiketa.setForeground(Color.BLUE);

        new Thread(() -> {
            try {
                Horarios horariosBezeroa = new Horarios();
                List<Horarios.HorarioData> horarios = horariosBezeroa.lortuProfeHorarioak(hautatutakoProfe.getId());

                EventQueue.invokeLater(() -> {
                    ordutegiaTabla.garbiOrdutegia();
                    ordutegiaTabla.ezarriOrdutegiIzenburua(profeIzena + "ren Ordutegia");

                    for (Horarios.HorarioData horario : horarios) {
                        int diaIndex = getDiaIndex(horario.getDia());
                        int horaIndex = horario.getHora() - 1;

                        if (diaIndex > 0 && horaIndex >= 0 && horaIndex < 8) {
                            String texto = horario.getAula();
                            if (horario.getObservaciones() != null && !horario.getObservaciones().isEmpty()) {
                                texto += "\n" + horario.getObservaciones();
                            }
                            
                            ordutegiaTabla.ezarriGelaxkaEdukia(diaIndex, horaIndex, texto);
                            ordutegiaTabla.ezarriGelaxkaKolorea(diaIndex, horaIndex, AstekoOrdutegia.KLASE_ARRUNTA);
                        }
                    }

                    egoerakoEtiketa.setText("✓ " + profeIzena + " - " + horarios.size() + " ordu kargatuta");
                    egoerakoEtiketa.setForeground(new Color(0, 128, 0));

                    if (horarios.isEmpty()) {
                        ordutegiaTabla.ezarriOrdutegiIzenburua(profeIzena + " - Ez du horariorik");
                    }
                });

            } catch (Exception e) {
                EventQueue.invokeLater(() -> {
                    egoerakoEtiketa.setText("❌ Errorea horarioa kargatzean");
                    egoerakoEtiketa.setForeground(Color.RED);

                    JOptionPane.showMessageDialog(this,
                        "Errorea " + profeIzena + "ren horarioa kargatzean:\n" + e.getMessage(),
                        "Errorea",
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
