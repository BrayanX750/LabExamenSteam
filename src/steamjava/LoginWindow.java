/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steamjava;

// Archivo: LoginWindow.java
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginWindow extends JFrame {

    private Steam steam; 

    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginWindow(Steam steam) {
        this.steam = steam;

        setTitle("Steam Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgColor = new Color(27, 40, 56);
        Color panelColor = new Color(42, 71, 94);
        Color textColor = Color.WHITE;

        getContentPane().setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(textColor);
        usernameField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(textColor);
        passwordField = new JPasswordField(15);

        loginButton = new JButton("Iniciar Sesión");
        registerButton = new JButton("Registrarse");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(loginButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(registerButton, gbc);

        loginButton.addActionListener(e -> attemptLogin());

        registerButton.addActionListener(e -> {
            this.dispose();
            new RegisterWindow(steam).setVisible(true);
        });
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try {
            Player loggedInPlayer = steam.login(username, password);
            if (loggedInPlayer != null) {
                dispose();

                new MainMenu(steam, loggedInPlayer).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al acceder a los datos de usuario.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }
}
