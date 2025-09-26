package steamjava;

import javax.swing.SwingUtilities;

public class Main {

  
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
          
            Steam steam = new Steam();

            LoginWindow loginWindow = new LoginWindow(steam);
            loginWindow.setVisible(true);
        });
    }
    
}
