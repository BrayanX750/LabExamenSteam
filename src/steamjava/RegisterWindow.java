// Archivo: RegisterWindow.java (con JDateChooser)
package steamjava;

import com.toedter.calendar.JDateChooser; // ¡Importante!
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date; // Usaremos Date para el JDateChooser

public class RegisterWindow extends JFrame {
    private Steam steam;
    private String profilePicPath;

    private JTextField usernameField, nameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JDateChooser birthDateChooser; // Reemplazamos los JComboBox
    private JComboBox<String> userTypeBox;
    private JLabel selectedImageLabel;
    private JLabel imagePreviewLabel;

    public RegisterWindow(Steam steam) {
        this.steam = steam;
        this.profilePicPath = null;

        setTitle("Crear una Cuenta Nueva");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
   
        Color bgColor = new Color(27, 40, 56);
        Color textColor = Color.WHITE;
        getContentPane().setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // ... (Campos de username, nombre y contraseña se mantienen igual) ...
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Username:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 0; usernameField = new JTextField(20); add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Nombre Completo:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 1; nameField = new JTextField(20); add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Contraseña:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 2; passwordField = new JPasswordField(20); add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Confirmar Contraseña:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 3; confirmPasswordField = new JPasswordField(20); add(confirmPasswordField, gbc);

        // --- Fila 4: Fecha de Nacimiento con JDateChooser ---
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Fecha de Nacimiento:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        birthDateChooser = new JDateChooser();
        birthDateChooser.setPreferredSize(new Dimension(200, 25)); // Ajustar tamaño
        add(birthDateChooser, gbc);

        // ... (El resto de los campos y botones se mantienen igual) ...
        gbc.gridx = 0; gbc.gridy = 5; JButton selectImageButton = new JButton("Elegir Foto..."); styleButton(selectImageButton); add(selectImageButton, gbc);
        JPanel imageInfoPanel = new JPanel(new BorderLayout()); imageInfoPanel.setBackground(bgColor); selectedImageLabel = new JLabel("Ningún archivo seleccionado.") {{ setForeground(textColor); }}; imagePreviewLabel = new JLabel(); imagePreviewLabel.setPreferredSize(new Dimension(50, 50)); imageInfoPanel.add(selectedImageLabel, BorderLayout.CENTER); imageInfoPanel.add(imagePreviewLabel, BorderLayout.EAST); gbc.gridx = 1; gbc.gridy = 5; add(imageInfoPanel, gbc);
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Tipo de Usuario:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 6; userTypeBox = new JComboBox<>(new String[]{"NORMAL", "ADMIN"}); add(userTypeBox, gbc);
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.CENTER; JButton registerButton = new JButton("Crear Cuenta"); styleButton(registerButton); add(registerButton, gbc);

        selectImageButton.addActionListener(e -> chooseImage());
        registerButton.addActionListener(e -> performRegistration());
    }

    private void performRegistration() {
        // ... (Validaciones de campos vacíos, contraseñas y foto se mantienen igual) ...
        if (usernameField.getText().isBlank() || nameField.getText().isBlank() || passwordField.getPassword().length == 0) { JOptionPane.showMessageDialog(this, "Por favor, llena todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE); return; }
        if (!Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) { JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error de Contraseña", JOptionPane.WARNING_MESSAGE); return; }
        if (profilePicPath == null) { JOptionPane.showMessageDialog(this, "Por favor, selecciona una foto de perfil.", "Foto Faltante", JOptionPane.WARNING_MESSAGE); return; }

        // Nueva validación para la fecha
        Date selectedDate = birthDateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fecha de nacimiento.", "Fecha Faltante", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String nombre = nameField.getText();
        String userType = (String) userTypeBox.getSelectedItem();
        
        long nacimientoMs = selectedDate.getTime(); // ¡Mucho más simple!

        try {
            steam.addPlayer(username, password, nombre, nacimientoMs, profilePicPath, userType);
            JOptionPane.showMessageDialog(this, "¡Cuenta creada exitosamente!", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginWindow(steam).setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los datos.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // El resto de los métodos (chooseImage, styleButton) no necesitan cambios
    private void chooseImage() { JFileChooser chooser = new JFileChooser(); FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"); chooser.setFileFilter(filter); int returnVal = chooser.showOpenDialog(this); if (returnVal == JFileChooser.APPROVE_OPTION) { File file = chooser.getSelectedFile(); profilePicPath = file.getAbsolutePath(); selectedImageLabel.setText(file.getName()); ImageIcon icon = new ImageIcon(profilePicPath); Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); imagePreviewLabel.setIcon(new ImageIcon(scaledImage)); } }
    private void styleButton(JButton button) { button.setBackground(new Color(102, 192, 244)); button.setForeground(Color.BLACK); button.setFocusPainted(false); button.setFont(new Font("Arial", Font.BOLD, 12)); button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); }
}