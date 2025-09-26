
package steamjava;

import javax.swing.*;
import java.awt.*;

public class PlayerListRenderer extends JPanel implements ListCellRenderer<Player> {

    private JLabel iconLabel = new JLabel();
    private JLabel nameLabel = new JLabel();
    private JLabel userInfoLabel = new JLabel();

    public PlayerListRenderer() {
        setLayout(new BorderLayout(15, 5));
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(100, 80));

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(userInfoLabel);

        add(iconLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Player> list, Player player,
                                                   int index, boolean isSelected, boolean cellHasFocus) {
        
        ImageIcon icon = new ImageIcon(player.pathFoto);
        Image image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        iconLabel.setIcon(new ImageIcon(image));

        nameLabel.setText(player.nombre);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        String status = player.estado ? "Activo" : "Inactivo";
        userInfoLabel.setText(String.format("Username: %s | Tipo: %s | Estado: %s", player.username, player.tipoUsuario, status));
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        if (isSelected) {
            setBackground(new Color(42, 71, 94));
            nameLabel.setForeground(Color.WHITE);
            userInfoLabel.setForeground(Color.LIGHT_GRAY);
        } else {
            setBackground(new Color(23, 26, 33));
            nameLabel.setForeground(Color.WHITE);
            userInfoLabel.setForeground(Color.LIGHT_GRAY);
        }
        
        return this;
    }
}