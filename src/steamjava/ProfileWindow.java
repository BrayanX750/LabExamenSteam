/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steamjava;

// Archivo: ProfileWindow.java


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileWindow extends JFrame {
    private Steam steam;
    private Player currentPlayer;
    private DefaultListModel<Game> listModel;

    public ProfileWindow(Steam steam, Player currentPlayer) {
        this.steam = steam;
        this.currentPlayer = currentPlayer;

        setTitle("Mi Biblioteca - " + currentPlayer.username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        
        Color bgColor = new Color(27, 40, 56);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout(10, 10));

        JPanel profilePanel = new JPanel(new BorderLayout(15, 0));
        profilePanel.setBackground(new Color(23, 26, 33));
        profilePanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        ImageIcon profileIcon = new ImageIcon(currentPlayer.pathFoto);
        Image image = profileIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel profilePicLabel = new JLabel(new ImageIcon(image));
        profilePanel.add(profilePicLabel, BorderLayout.WEST);

        JLabel nameLabel = new JLabel(currentPlayer.nombre);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        profilePanel.add(nameLabel, BorderLayout.CENTER);
        
        add(profilePanel, BorderLayout.NORTH);

       
        listModel = new DefaultListModel<>();
        JList<Game> gameList = new JList<>(listModel);
        gameList.setCellRenderer(new GameListRenderer());
        gameList.setBackground(new Color(23, 26, 33));

        JScrollPane scrollPane = new JScrollPane(gameList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        loadDownloadedGames();
    }

    private void loadDownloadedGames() {
        try {
            ArrayList<Game> downloadedGames = steam.getDownloadsForPlayer(currentPlayer.code);
            listModel.clear();
            if (downloadedGames.isEmpty()) {
                
            } else {
                for (Game game : downloadedGames) {
                    listModel.addElement(game);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tu biblioteca.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}