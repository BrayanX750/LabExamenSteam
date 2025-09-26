// Archivo: GameDetailsWindow.java
package steamjava;

import javax.swing.*;
import java.awt.*;

public class GameDetailsWindow extends JFrame {
    private Steam steam;
    private Game game;
    private Player currentPlayer;

    public GameDetailsWindow(Steam steam, Game game, Player currentPlayer) {
        this.steam = steam;
        this.game = game;
        this.currentPlayer = currentPlayer;

        setTitle("Detalles de: " + game.titulo);
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Estética Steam ---
        Color bgColor = new Color(27, 40, 56);
        Color textColor = Color.WHITE;
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));

        // --- Título en la parte SUPERIOR ---
        JLabel titleLabel = new JLabel(game.titulo, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Imagen a la IZQUIERDA ---
        ImageIcon icon = new ImageIcon(game.pathFoto);
        Image scaledImage = icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        add(imageLabel, BorderLayout.WEST);

        // --- Panel de Detalles en el CENTRO ---
        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.add(createDetailLabel("Género: " + game.genero));
        detailsPanel.add(createDetailLabel(String.format("Precio: Lps. %.2f", game.precio)));
        detailsPanel.add(createDetailLabel("Edad Mínima: " + game.edadMinima + " años"));
        detailsPanel.add(createDetailLabel("Sistema Operativo: " + getOsName(game.sistemaOperativo)));
        add(detailsPanel, BorderLayout.CENTER);

        // --- Botón de Descarga en la parte INFERIOR ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        JButton downloadButton = new JButton("📥 DESCARGAR");
        styleDownloadButton(downloadButton);
        bottomPanel.add(downloadButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // --- ActionListener para el botón ---
        downloadButton.addActionListener(e -> performDownload());
    }

    private void performDownload() {
        // Por simplicidad, asumimos que se descarga para Windows ('W')
        char targetOS = 'W'; 
        
        try {
            boolean success = steam.downloadGame(game.code, currentPlayer.code, targetOS);
            if (success) {
                // Simulación de descarga con SwingWorker para no congelar la UI
                final JDialog progressDialog = new JDialog(this, "Descargando...", true);
                JProgressBar progressBar = new JProgressBar(0, 100);
                progressDialog.add(progressBar);
                progressDialog.pack();
                progressDialog.setLocationRelativeTo(this);
                
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        for (int i = 0; i <= 100; i++) {
                            Thread.sleep(20); // Simular trabajo
                            progressBar.setValue(i);
                        }
                        return null;
                    }
                    @Override
                    protected void done() {
                        progressDialog.dispose();
                        JOptionPane.showMessageDialog(GameDetailsWindow.this, "¡Descarga completada!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                };
                worker.execute();
                progressDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No cumples los requisitos para descargar este juego (Edad, SO, etc.)", "Descarga Fallida", JOptionPane.ERROR_MESSAGE);
            }
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(this, "Error de archivo durante la descarga.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Métodos de ayuda para estilo y formato
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return label;
    }
    
    private String getOsName(char os) {
        return switch (os) {
            case 'W' -> "Windows";
            case 'M' -> "Mac";
            case 'L' -> "Linux";
            default -> "Desconocido";
        };
    }

    private void styleDownloadButton(JButton button) {
        button.setBackground(new Color(98, 154, 43)); // Verde Steam
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
    }
}