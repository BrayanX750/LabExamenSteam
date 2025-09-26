/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steamjava;


import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Steam {

    private RandomAccessFile codeFile;
    private RandomAccessFile gamesFile;
    private RandomAccessFile playerFile;

    public static final int GAME_SIZE = 163;
    public static final int PLAYER_SIZE = 249;

    public Steam(){
        inicializar();
    }

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

    private int nextCodeAt(long pos) throws IOException {
        codeFile.seek(pos);
        int val = codeFile.readInt();
        codeFile.seek(pos);
        codeFile.writeInt(val + 1);
        return val;
    }

    public int generarCodigoGame() throws IOException {
        return nextCodeAt(0);
    }

    public int generarCodigoUser() throws IOException {
        return nextCodeAt(4);
    }

    public int generarCodigoDownload() throws IOException {
        return nextCodeAt(8);
    }

    public void addGame(String titulo, String genero, char so, int edadMinima, double precio, String foto) throws IOException {
        int code = generarCodigoGame();
        gamesFile.seek(gamesFile.length());
        gamesFile.writeInt(code);
        gamesFile.writeUTF(titulo == null ? "" : titulo);
        gamesFile.writeUTF(genero == null ? "" : genero);
        gamesFile.writeChar(so);
        gamesFile.writeInt(edadMinima);
        gamesFile.writeDouble(precio);
        gamesFile.writeInt(0);
        gamesFile.writeUTF(foto == null ? "" : foto);
        gamesFile.writeBoolean(true); // activo por defecto
    }

    public void addPlayer(String username, String password, String nombre, long nacimientoMs, String foto, String tipoUsuario, boolean estado) throws IOException {
        int code = generarCodigoUser();
        playerFile.seek(playerFile.length());
        playerFile.writeInt(code);
        playerFile.writeUTF(username == null ? "" : username);
        playerFile.writeUTF(password == null ? "" : password);
        playerFile.writeUTF(nombre == null ? "" : nombre);
        playerFile.writeLong(nacimientoMs);
        playerFile.writeInt(0);
        playerFile.writeUTF(foto == null ? "" : foto);
        playerFile.writeUTF(tipoUsuario == null ? "" : tipoUsuario);
        playerFile.writeBoolean(estado);
    }

    public ArrayList<Game> getAllGames() throws IOException {
        ArrayList<Game> lista = new ArrayList<>();
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            int code = gamesFile.readInt();
            String titulo = gamesFile.readUTF();
            String genero = gamesFile.readUTF();
            char so = gamesFile.readChar();
            int edadMinima = gamesFile.readInt();
            double precio = gamesFile.readDouble();
            int downloads = gamesFile.readInt();
            String foto = gamesFile.readUTF();
            boolean activo = gamesFile.readBoolean();
            Game g = new Game(code, titulo, genero, so, edadMinima, precio, downloads, foto, activo);
            lista.add(g);
        }
        return lista;
    }
}
