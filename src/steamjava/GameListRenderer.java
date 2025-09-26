// Archivo: GameListRenderer.java (Versión Corregida)
package steamjava;

import javax.swing.*;
import java.awt.*;

public class GameListRenderer extends JPanel implements ListCellRenderer<Game> {

    // SOLUCIÓN: Inicializamos las etiquetas aquí mismo.
    private JLabel iconLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private JLabel genrePriceLabel = new JLabel();

    public GameListRenderer() {
        setLayout(new BorderLayout(10, 5));
        setOpaque(true); // Es importante para que los colores de fondo funcionen bien
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.setOpaque(false); // Hacerlo transparente para heredar el fondo
        textPanel.add(titleLabel);
        textPanel.add(genrePriceLabel);

        add(iconLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Game> list, Game game,
                                                   int index, boolean isSelected, boolean cellHasFocus) {

        // Cargar y escalar la imagen
        ImageIcon icon = new ImageIcon(game.pathFoto);
        Image image = icon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);
        iconLabel.setIcon(new ImageIcon(image));

        // Poner el texto
        titleLabel.setText(game.titulo);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        genrePriceLabel.setText(String.format("%s - Lps. %.2f", game.genero, game.precio));
        genrePriceLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Cambiar colores si el item está seleccionado
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            titleLabel.setForeground(list.getSelectionForeground());
            genrePriceLabel.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            titleLabel.setForeground(list.getForeground());
            genrePriceLabel.setForeground(list.getForeground());
        }

        return this;
    }
}