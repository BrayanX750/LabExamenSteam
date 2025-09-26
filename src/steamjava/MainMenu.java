// Archivo: MainMenu.java (con Diseño de Botones en Columnas)
package steamjava;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenu extends JFrame {

    private Steam steam;
    private Player loggedInPlayer;

    public MainMenu(Steam steam, Player loggedInPlayer) {
        this.steam = steam;
        this.loggedInPlayer = loggedInPlayer;

        // --- Configuración de la Estética Steam ---
        setTitle("Steam Main Menu");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgColor = new Color(27, 40, 56);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Superior con Perfil de Usuario (sin cambios) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(23, 26, 33));
        // ... (código para mostrar el username y la foto de perfil) ...
        add(topPanel, BorderLayout.NORTH);

        // --- Panel Principal para los Botones ---
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bgColor);

        // Comprobamos el tipo de usuario para construir el panel principal
        if (loggedInPlayer.getTipoUsuario().equals("ADMIN")) {
            buildAdminPanel(mainPanel);
        } else {
            buildNormalUserPanel(mainPanel);
        }

        add(mainPanel, BorderLayout.CENTER);
    }

    private void buildAdminPanel(JPanel mainPanel) {
        // Usamos GridLayout para crear dos columnas principales
        mainPanel.setLayout(new GridLayout(1, 2, 30, 0)); // 1 fila, 2 columnas, 30px de espacio horizontal
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margen

        // --- Columna 1: Gestión de Juegos ---
        JPanel gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS)); // Layout vertical
        gamesPanel.setOpaque(false); // Hacemos el panel transparente

        JLabel gamesTitle = new JLabel("Gestión de Juegos");
        styleTitle(gamesTitle);
        gamesPanel.add(gamesTitle);
        gamesPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espaciador

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
        JButton reportUserBtn = new JButton("Generar Reporte");

        styleButton(viewUsersBtn);
        styleButton(reportUserBtn);

        usersPanel.add(viewUsersBtn);
        usersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        usersPanel.add(reportUserBtn);

        // Añadir las dos columnas al panel principal
        mainPanel.add(gamesPanel);
        mainPanel.add(usersPanel);

        // --- Action Listeners para Admin ---
        addGameBtn.addActionListener(e -> {
            this.dispose();
            new AddGameWindow(steam).setVisible(true);
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
