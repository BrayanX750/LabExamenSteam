package steamjava;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ManageGameWindow extends JFrame {
    private Steam steam;
    private JTable gamesTable;
    private DefaultTableModel tableModel;

    public ManageGameWindow(Steam steam) {
        this.steam = steam;
        setTitle("Gestionar Catálogo de Juegos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(27, 40, 56));

        String[] columnNames = {"Código", "Título", "Género", "Precio", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gamesTable = new JTable(tableModel);

        styleTable(gamesTable);
        
        JScrollPane scrollPane = new JScrollPane(gamesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(42, 71, 94)));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        JButton updatePriceBtn = new JButton("Modificar Precio");
        JButton toggleStatusBtn = new JButton("Activar/Desactivar");
        
        styleButton(updatePriceBtn);
        styleButton(toggleStatusBtn);
        
        buttonPanel.add(updatePriceBtn);
        buttonPanel.add(toggleStatusBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        updatePriceBtn.addActionListener(e -> updateSelectedGamePrice());
        toggleStatusBtn.addActionListener(e -> toggleSelectedGameStatus());
        
        loadGames();
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(23, 26, 33));
        table.setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(42, 71, 94));
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(102, 192, 244));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(42, 71, 94));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setReorderingAllowed(false);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(102, 192, 244));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void loadGames() {
        tableModel.setRowCount(0);
        try {
            ArrayList<Game> games = steam.getAllGamesForAdmin();
            for (Game game : games) {
                Object[] row = {
                    game.code,
                    game.titulo,
                    game.genero,
                    String.format("%.2f", game.precio),
                    game.activo ? "Activo" : "Inactivo"
                };
                tableModel.addRow(row);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedGamePrice() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un juego de la tabla.", "Ningún Juego Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int gameCode = (int) tableModel.getValueAt(selectedRow, 0);
        String newPriceStr = JOptionPane.showInputDialog(this, "Introduce el nuevo precio para el juego:", "Modificar Precio", JOptionPane.PLAIN_MESSAGE);

        if (newPriceStr != null && !newPriceStr.isBlank()) {
            try {
                double newPrice = Double.parseDouble(newPriceStr);
                if(steam.updatePriceFor(gameCode, newPrice)) {
                    JOptionPane.showMessageDialog(this, "Precio actualizado correctamente.");
                    loadGames();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo encontrar el juego para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce un número válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el precio.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleSelectedGameStatus() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un juego de la tabla.", "Ningún Juego Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int gameCode = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            if(steam.toggleGameStatus(gameCode)) {
                JOptionPane.showMessageDialog(this, "Estado del juego cambiado correctamente.");
                loadGames();
            } else {
                 JOptionPane.showMessageDialog(this, "No se pudo encontrar el juego para cambiar su estado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cambiar el estado del juego.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }
}