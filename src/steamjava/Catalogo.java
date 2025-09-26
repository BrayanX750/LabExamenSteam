/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steamjava;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author unwir
 */
public class Catalogo extends JFrame {
    
    private Steam game;
    private JTextArea display;
    
    
    
    public Catalogo(Steam game){
        this.game = game;
        setTitle("Catalogo de Juegos");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        display = new JTextArea();
        display.setEditable(false);
        add(new JScrollPane(display));
        initAssets();
        
        
    }
    
    
    private void initAssets(){
        try{
            
           ArrayList<Game> jogos = game.getAllGames();
           display.setText("");
        
           if(jogos.isEmpty()){
               display.setText("No hay juegos actualmente");
           }else{
               for (Game j : jogos) {
                   display.append(j.toString() + "\n");
                   
               }
           
           }
           
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "Error al cargar juegos!");
        
        }
    }
    
    
    
    
}

