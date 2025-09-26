// Archivo: AddGameWindow.java
package steamjava;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AddGameWindow extends JFrame {
    private Steam steam;
    private String coverImagePath;

  
    private JTextField titleField, genreField;
    private JSpinner ageSpinner, priceSpinner;
    private JComboBox<String> osComboBox;
    private JLabel imagePreviewLabel;
    
    
    public AddGameWindow(Steam steam) {
        this.steam = steam;
        this.coverImagePath = null;

        setTitle("Agregar Nuevo Juego al Catálogo");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgColor = new Color(27, 40, 56);
        Color textColor = Color.WHITE;
        getContentPane().setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Título:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        titleField = new JTextField(25);
        add(titleField, gbc);

       
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Género:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        genreField = new JTextField(25);
        add(genreField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Sistema Operativo:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        osComboBox = new JComboBox<>(new String[]{"Windows", "Mac", "Linux"});
        add(osComboBox, gbc);

     
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Edad Mínima:") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        ageSpinner = new JSpinner(new SpinnerNumberModel(8, 0, 100, 1));
        add(ageSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Precio (LPS):") {{ setForeground(textColor); }}, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        priceSpinner = new JSpinner(new SpinnerNumberModel(59.99, 0.0, 10000.0, 0.01));
        add(priceSpinner, gbc);

        // --- Fila 5: Selector de Carátula ---
        gbc.gridx = 0; gbc.gridy = 5;
        JButton selectImageButton = new JButton("Elegir Carátula...");
        styleButton(selectImageButton);
        add(selectImageButton, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        imagePreviewLabel = new JLabel("Sin carátula seleccionada");
        imagePreviewLabel.setPreferredSize(new Dimension(120, 60));
        imagePreviewLabel.setForeground(textColor);
        add(imagePreviewLabel, gbc);
        
        // --- Fila 6: Botón para Agregar ---
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addButton = new JButton("Agregar Juego");
        styleButton(addButton);
        add(addButton, gbc);

        // --- Action Listeners ---
        selectImageButton.addActionListener(e -> chooseImage());
        addButton.addActionListener(e -> performAddGame());
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            coverImagePath = file.getAbsolutePath();
            ImageIcon icon = new ImageIcon(coverImagePath);
            Image scaledImage = icon.getImage().getScaledInstance(120, 60, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
            imagePreviewLabel.setText("");
        }
    }

    private void performAddGame() {
        if (titleField.getText().isBlank() || genreField.getText().isBlank() || coverImagePath == null) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos y elige una carátula.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = titleField.getText();
        String genero = genreField.getText();
        int edadMinima = (Integer) ageSpinner.getValue();
        double precio = (Double) priceSpinner.getValue();
        char so = switch ((String) osComboBox.getSelectedItem()) {
            case "Mac" -> 'M';
            case "Linux" -> 'L';
            default -> 'W';
        };
        
        try {
            steam.addGame(titulo, genero, so, edadMinima, precio, coverImagePath);
            JOptionPane.showMessageDialog(this, "¡Juego agregado al catálogo exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el juego en el archivo.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(102, 192, 244));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}