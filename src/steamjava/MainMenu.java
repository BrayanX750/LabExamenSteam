
package steamjava;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class MainMenu extends JFrame {

    private Steam steam;
    private Player loggedInPlayer;

    public MainMenu(Steam steam, Player loggedInPlayer) {
        this.steam = steam;
        this.loggedInPlayer = loggedInPlayer;

      
        setTitle("Steam Main Menu");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgColor = new Color(27, 40, 56);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout(10, 10));

       
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(23, 26, 33));
     
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bgColor);

        if (loggedInPlayer.getTipoUsuario().equals("ADMIN")) {
            buildAdminPanel(mainPanel);
        } else {
            buildNormalUserPanel(mainPanel);
        }

        add(mainPanel, BorderLayout.CENTER);
    }

    private void buildAdminPanel(JPanel mainPanel) {
       
        mainPanel.setLayout(new GridLayout(1, 2, 30, 0)); 
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        
        JPanel gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS)); 
        gamesPanel.setOpaque(false); 
        JLabel gamesTitle = new JLabel("Gestión de Juegos");
        styleTitle(gamesTitle);
        gamesPanel.add(gamesTitle);
        gamesPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton addGameBtn = new JButton("Agregar Juego");
        JButton modifyGameBtn = new JButton("Modificar Juego");
        JButton deactivateGameBtn = new JButton("Desactivar Juego");

        styleButton(addGameBtn);
        styleButton(modifyGameBtn);
        styleButton(deactivateGameBtn);

        gamesPanel.add(addGameBtn);
        gamesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gamesPanel.add(modifyGameBtn);
        gamesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gamesPanel.add(deactivateGameBtn);

        // --- Columna 2: Gestión de Usuarios ---
        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        usersPanel.setOpaque(false);

        JLabel usersTitle = new JLabel("Gestión de Usuarios");
        styleTitle(usersTitle);
        usersPanel.add(usersTitle);
        usersPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton viewUsersBtn = new JButton("Ver Jugadores");
        JButton reportUserBtn = new JButton("Generar Reporte"); // Este es el botón que cambiamos

        styleButton(viewUsersBtn);
        styleButton(reportUserBtn);

        usersPanel.add(viewUsersBtn);
        usersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        usersPanel.add(reportUserBtn);

        mainPanel.add(gamesPanel); 
        mainPanel.add(usersPanel);

       
        addGameBtn.addActionListener(e -> new AddGameWindow(steam).setVisible(true));
        modifyGameBtn.addActionListener(e -> new ManageGameWindow(steam).setVisible(true));
        deactivateGameBtn.addActionListener(e -> new ManageGameWindow(steam).setVisible(true));
        viewUsersBtn.addActionListener(e -> new ManagePlayersWindow(steam).setVisible(true));

       
        reportUserBtn.addActionListener(e -> {
            String playerCodeStr = JOptionPane.showInputDialog(
                    this,
                    "Introduce el código del jugador para generar el reporte:",
                    "Generar Reporte",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (playerCodeStr != null && !playerCodeStr.isBlank()) {
                try {
                    int playerCode = Integer.parseInt(playerCodeStr);
                  
                    steam.reportForClient(playerCode);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, introduce un código numérico válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error de archivo al generar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private void buildNormalUserPanel(JPanel mainPanel) {
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JButton catalogBtn = new JButton("Ver Catálogo de Juegos");
        JButton profileBtn = new JButton("Ver mi Perfil");

        styleButton(catalogBtn);
        styleButton(profileBtn);

        mainPanel.add(catalogBtn);
        mainPanel.add(profileBtn);

        catalogBtn.addActionListener(e -> {
            new Catalogo(steam, loggedInPlayer).setVisible(true);
        });

        profileBtn.addActionListener(e -> {
            new ProfileWindow(steam, loggedInPlayer).setVisible(true);
        });
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(42, 71, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(102, 192, 244)),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Para BoxLayout
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height)); // Para que ocupen todo el ancho
    }

    private void styleTitle(JLabel label) {
        label.setForeground(new Color(102, 192, 244));
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
