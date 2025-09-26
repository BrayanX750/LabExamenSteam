/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steamjava;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Usuario
 */
public class Steam {

    private RandomAccessFile codeFile;
    private RandomAccessFile gamesFile;
    private RandomAccessFile playerFile;

    public static final int GAME_SIZE = 163;
    public static final int PLAYER_SIZE = 249;

    private void inicializar() {
        try {
            File steamCar = new File("steam");
            if (!steamCar.exists()) {
                steamCar.mkdir();
            }
            File downCar = new File("steam/downloads");
            if (!downCar.exists()) {
                downCar.mkdir();
            }

            codeFile = new RandomAccessFile("steam/codes.stm", "rw");
            gamesFile = new RandomAccessFile("steam/games.stm", "rw");
            playerFile = new RandomAccessFile("steam/player.stm", "rw");

            if (codeFile.length() == 0) {
                codeFile.writeInt(1);
                codeFile.writeInt(1);
                codeFile.writeInt(1);
            }
        } catch (IOException ex) {
            System.out.println("ERROR en archivos base");
        }

    }
    
    
    public Steam(){
        inicializar();
    }

}
