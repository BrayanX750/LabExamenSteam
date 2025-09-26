package steamjava;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ManagePlayersWindow extends JFrame {

    private Steam steam;
    private JList<Player> playerList;
    private DefaultListModel<Player> listModel;

    public ManagePlayersWindow(Steam steam) {
        this.steam = steam;
        setTitle("Gestionar Jugadores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(27, 40, 56));

        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        playerList.setCellRenderer(new PlayerListRenderer());
        playerList.setBackground(new Color(23, 26, 33));

        JScrollPane scrollPane = new JScrollPane(playerList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        JButton toggleStatusBtn = new JButton("Activar/Desactivar Usuario");
        styleButton(toggleStatusBtn);
        buttonPanel.add(toggleStatusBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        toggleStatusBtn.addActionListener(e -> toggleSelectedPlayerStatus());

        loadPlayers();
    }

    private void loadPlayers() {
        listModel.clear();
        try {
            ArrayList<Player> players = steam.getAllPlayersForAdmin();
            for (Player p : players) {
                listModel.addElement(p);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la lista de jugadores.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleSelectedPlayerStatus() {
        Player selectedPlayer = playerList.getSelectedValue();
        if (selectedPlayer == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un jugador de la lista.", "Ningún Jugador Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            steam.deactivatePlayer(selectedPlayer.code);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cambiar el estado del jugador.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(102, 192, 244));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}
