// Archivo: Catalogo.java (Versión Corregida)
package steamjava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Catalogo extends JFrame {
    private Steam steam;
    private Player loggedInPlayer;
    private JList<Game> gameList;
    private DefaultListModel<Game> listModel;
    private JComboBox<String> genreFilter;

    public Catalogo(Steam steam, Player loggedInPlayer) {
        this.steam = steam;
        this.loggedInPlayer = loggedInPlayer;

        setTitle("Catálogo de Juegos");
        setSize(800, 600); // Hacemos la ventana más grande
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bgColor = new Color(27, 40, 56);
        getContentPane().setBackground(bgColor);

        listModel = new DefaultListModel<>();
        gameList = new JList<>(listModel);
        gameList.setCellRenderer(new GameListRenderer());
        
       
        gameList.setBackground(new Color(23, 26, 33)); 
        gameList.setForeground(Color.WHITE);
        gameList.setSelectionBackground(new Color(42, 71, 94));
        gameList.setSelectionForeground(Color.WHITE);

        gameList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Game selectedGame = gameList.getSelectedValue();
                    if (selectedGame != null) {
                      new GameDetailsWindow(steam, selectedGame, loggedInPlayer).setVisible(true);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(gameList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        add(scrollPane, BorderLayout.CENTER);

      
        cargarJuegos();
    }
    
    private void cargarJuegos() {
        try {
            ArrayList<Game> juegos = steam.getAllGames(); 
            listModel.clear();
            
            if (juegos.isEmpty()) {
            
            } else {
                for (Game juego : juegos) {
                    listModel.addElement(juego);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}