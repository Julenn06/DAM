package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public final class UserListTable extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public static final Color IKASLE_KOLOREA = new Color(70, 130, 180);
    public static final Color IRAKASLE_KOLOREA = new Color(34, 139, 34);
    public static final Color HAUTATUTAKO_ERRENKADA = new Color(255, 215, 0);
    public static final Color TXANDAKATUTAKO_ERRENKADA = new Color(248, 248, 248);

    private final JTable tabla;
    private final DefaultTableModel taulaModeloa;
    private final List<ErabiltzaileHautatzeEntzulea> hautatzaileEntzuleak;
    private final String[] zutabeIzenak = { "ID", "Izena", "Abizenak", "Emaila", "Mota" };
    
    private int hautatutakoErabiltzaileId = -1;
    private String erabiltzaileMota;

    public UserListTable() {
        this("Erabiltzaileak");
    }

    public UserListTable(String erabiltzaileMota) {
        this.erabiltzaileMota = erabiltzaileMota;
        this.hautatzaileEntzuleak = new ArrayList<>();
        
        setLayout(new BorderLayout());
        
        taulaModeloa = new DefaultTableModel(zutabeIzenak, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
        };

        tabla = new JTable(taulaModeloa);
        konfiguratuTabla();
        
        add(sortuGoiburua(), BorderLayout.NORTH);
        add(sortuTaulaPanela(), BorderLayout.CENTER);
        add(sortuInformazioPanela(), BorderLayout.SOUTH);
    }

    private void konfiguratuTabla() {
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(51, 102, 153));
        tabla.getTableHeader().setForeground(Color.WHITE);

        TableColumnModel zutabeModeloa = tabla.getColumnModel();
        zutabeModeloa.getColumn(0).setPreferredWidth(50);
        zutabeModeloa.getColumn(1).setPreferredWidth(120);
        zutabeModeloa.getColumn(2).setPreferredWidth(150);
        zutabeModeloa.getColumn(3).setPreferredWidth(200);
        zutabeModeloa.getColumn(4).setPreferredWidth(80);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowSelectionAllowed(true);
        tabla.setColumnSelectionAllowed(false);

        tabla.setDefaultRenderer(Object.class, new ErabiltzaileGelaxkaErrendatzailea());
        tabla.setDefaultRenderer(Integer.class, new ErabiltzaileGelaxkaErrendatzailea());

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int hautatutakoErrenkada = tabla.getSelectedRow();
                    if (hautatutakoErrenkada >= 0) {
                        hautatutakoErabiltzaileId = (Integer) taulaModeloa.getValueAt(hautatutakoErrenkada, 0);
                        String erabiltzaileIzena = taulaModeloa.getValueAt(hautatutakoErrenkada, 1) + " " +
                                                   taulaModeloa.getValueAt(hautatutakoErrenkada, 2);
                        String motaHautatua = (String) taulaModeloa.getValueAt(hautatutakoErrenkada, 4);

                        jakinaraziErabiltzaileaHautatua(hautatutakoErabiltzaileId, erabiltzaileIzena, motaHautatua);
                        tabla.repaint();
                    }
                }
            }
        });

        tabla.setAutoCreateRowSorter(true);
    }

    private JLabel sortuGoiburua() {
        JLabel izenburua = new JLabel(erabiltzaileMota + "en Zerrenda", JLabel.CENTER);
        izenburua.setFont(new Font("Tahoma", Font.BOLD, 18));
        izenburua.setForeground(new Color(51, 102, 153));
        izenburua.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return izenburua;
    }

    private JScrollPane sortuTaulaPanela() {
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private JPanel sortuInformazioPanela() {
        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioa"));

        infoPanel.add(sortuInfoElementua("Ikaslea", IKASLE_KOLOREA));
        infoPanel.add(sortuInfoElementua("Irakaslea", IRAKASLE_KOLOREA));

        JLabel argibideak = new JLabel("Egin klik errenkada batean erabiltzaile bat hautatzeko");
        argibideak.setFont(new Font("Tahoma", Font.ITALIC, 11));
        argibideak.setForeground(Color.GRAY);
        infoPanel.add(argibideak);

        return infoPanel;
    }

    private JPanel sortuInfoElementua(String testua, Color kolorea) {
        JPanel elementua = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JPanel koloreKoadroa = new JPanel();
        koloreKoadroa.setBackground(kolorea);
        koloreKoadroa.setPreferredSize(new Dimension(15, 15));
        koloreKoadroa.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel etiketa = new JLabel(testua);
        etiketa.setFont(new Font("Tahoma", Font.PLAIN, 11));

        elementua.add(koloreKoadroa);
        elementua.add(etiketa);

        return elementua;
    }

    public void gehituErabiltzailea(int id, String izena, String abizenak, String emaila, String mota) {
        Object[] errenkada = { id, izena, abizenak, emaila, mota };
        taulaModeloa.addRow(errenkada);
    }

    public void garbituerabiltzaileak() {
        taulaModeloa.setRowCount(0);
        hautatutakoErabiltzaileId = -1;
    }

    public void kargatuErabiltzaileak(List<Object[]> erabiltzaileak) {
        garbituerabiltzaileak();
        erabiltzaileak.forEach(taulaModeloa::addRow);
    }

    public int getHautatutakoErabiltzaileId() {
        return hautatutakoErabiltzaileId;
    }

    public Object[] getHautatutakoErabiltzaileInfo() {
        int hautatutakoErrenkada = tabla.getSelectedRow();
        if (hautatutakoErrenkada >= 0) {
            Object[] erabiltzaileInfo = new Object[5];
            for (int i = 0; i < 5; i++) {
                erabiltzaileInfo[i] = taulaModeloa.getValueAt(hautatutakoErrenkada, i);
            }
            return erabiltzaileInfo;
        }
        return null;
    }

    public void ezarriErabiltzaileMota(String erabiltzaileMota) {
        this.erabiltzaileMota = erabiltzaileMota;
        Component[] osagaiak = getComponents();
        for (Component comp : osagaiak) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setText(erabiltzaileMota + "en Zerrenda");
                break;
            }
        }
    }

    public void gehituErabiltzaileHautatzeEntzulea(ErabiltzaileHautatzeEntzulea entzulea) {
        hautatzaileEntzuleak.add(entzulea);
    }

    public void kenduErabiltzaileHautatzeEntzulea(ErabiltzaileHautatzeEntzulea entzulea) {
        hautatzaileEntzuleak.remove(entzulea);
    }

    private void jakinaraziErabiltzaileaHautatua(int erabiltzaileId, String erabiltzaileIzena, String erabiltzaileMota) {
        hautatzaileEntzuleak.forEach(entzulea -> 
            entzulea.erabiltzaileaHautatuDa(erabiltzaileId, erabiltzaileIzena, erabiltzaileMota));
    }

    public int getErabiltzaileKopurua() {
        return taulaModeloa.getRowCount();
    }

    public void iragaziErabiltzaileak(String bilaketaTerminoa) {
        if (bilaketaTerminoa == null || bilaketaTerminoa.trim().isEmpty()) {
            tabla.setRowSorter(new TableRowSorter<>(taulaModeloa));
            return;
        }

        TableRowSorter<DefaultTableModel> ordenatzailea = new TableRowSorter<>(taulaModeloa);
        ordenatzailea.setRowFilter(RowFilter.regexFilter("(?i)" + bilaketaTerminoa, 1, 2, 3));
        tabla.setRowSorter(ordenatzailea);
    }

    private class ErabiltzaileGelaxkaErrendatzailea extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object balioa, boolean hautatua, 
                boolean fokusDu, int errenkada, int zutabea) {

            Component osagaia = super.getTableCellRendererComponent(tabla, balioa, hautatua, fokusDu, 
                                                                    errenkada, zutabea);

            String erabiltzaileMota = (String) tabla.getModel().getValueAt(errenkada, 4);
            int erabiltzaileId = (Integer) tabla.getModel().getValueAt(errenkada, 0);

            if (erabiltzaileId == hautatutakoErabiltzaileId) {
                osagaia.setBackground(HAUTATUTAKO_ERRENKADA);
                osagaia.setForeground(Color.BLACK);
            } else if (hautatua) {
                osagaia.setBackground(tabla.getSelectionBackground());
                osagaia.setForeground(tabla.getSelectionForeground());
            } else {
                if ("Irakaslea".equalsIgnoreCase(erabiltzaileMota)) {
                    osagaia.setBackground(IRAKASLE_KOLOREA);
                    osagaia.setForeground(Color.WHITE);
                } else if ("Ikaslea".equalsIgnoreCase(erabiltzaileMota)) {
                    osagaia.setBackground(IKASLE_KOLOREA);
                    osagaia.setForeground(Color.WHITE);
                } else {
                    osagaia.setBackground(errenkada % 2 == 0 ? Color.WHITE : TXANDAKATUTAKO_ERRENKADA);
                    osagaia.setForeground(Color.BLACK);
                }
            }

            setHorizontalAlignment(zutabea == 0 ? JLabel.CENTER : JLabel.LEFT);

            return osagaia;
        }
    }

    public interface ErabiltzaileHautatzeEntzulea {
        void erabiltzaileaHautatuDa(int erabiltzaileId, String erabiltzaileIzena, String erabiltzaileMota);
    }
}