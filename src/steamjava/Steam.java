/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steamjava;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Steam {

    private RandomAccessFile codeFile;
    private RandomAccessFile gamesFile;
    private RandomAccessFile playerFile;

    public static final int GAME_SIZE = 163;
    public static final int PLAYER_SIZE = 249;

    public Steam() {
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
        writeFixedString(gamesFile, titulo, 50);
        writeFixedString(gamesFile, genero, 20);
        gamesFile.writeChar(so);
        gamesFile.writeInt(edadMinima);
        gamesFile.writeDouble(precio);
        gamesFile.writeInt(0);
        writeFixedString(gamesFile, foto, 100);
        gamesFile.writeBoolean(true);
    }

    private void writeFixedString(RandomAccessFile raf, String text, int size) throws IOException {
        StringBuilder sb = new StringBuilder(text != null ? text : "");
        sb.setLength(size);
        raf.writeChars(sb.toString());
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

    private String readFixedString(RandomAccessFile raf, int size) throws IOException {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(raf.readChar());
        }
        return sb.toString().trim();
    }

    public boolean updatePriceFor(int codeGame, double newPrice) throws IOException {
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            int code = gamesFile.readInt();
            readFixedString(gamesFile, 50);
            readFixedString(gamesFile, 20);
            gamesFile.readChar();
            gamesFile.readInt();
            if (code == codeGame) {
                gamesFile.writeDouble(newPrice);
                return true;
            } else {
                gamesFile.readDouble();
                gamesFile.readInt();
                readFixedString(gamesFile, 100);
                gamesFile.readBoolean();
            }
        }
        return false;
    }

    public boolean reportForClient(int codeClient, String txtFile) throws IOException {
        playerFile.seek(0);
        int p_code = -1;
        String p_username = null;
        String p_nombre = null;
        long p_nac = 0L;
        int p_totalDls = 0;
        boolean p_estado = false;

        while (playerFile.getFilePointer() < playerFile.length()) {
            int code = playerFile.readInt();
            String user = playerFile.readUTF();
            String pass = playerFile.readUTF();
            String nombre = playerFile.readUTF();
            long nac = playerFile.readLong();
            int dls = playerFile.readInt();
            String foto = playerFile.readUTF();
            String tipo = playerFile.readUTF();
            boolean estado = playerFile.readBoolean();
            if (code == codeClient) {
                p_code = code;
                p_username = user;
                p_nombre = nombre;
                p_nac = nac;
                p_totalDls = dls;
                p_estado = estado;
                break;
            }
        }

        if (p_code == -1) {
            javax.swing.JOptionPane.showMessageDialog(null, "NO SE PUEDE CREAR REPORTE");
            return false;
        }

        int edad = 0;
        if (p_nac > 0) {
            java.util.Calendar n = java.util.Calendar.getInstance();
            n.setTimeInMillis(p_nac);
            java.util.Calendar h = java.util.Calendar.getInstance();
            edad = h.get(java.util.Calendar.YEAR) - n.get(java.util.Calendar.YEAR);
            if (h.get(java.util.Calendar.DAY_OF_YEAR) < n.get(java.util.Calendar.DAY_OF_YEAR)) {
                edad--;
            }
        }

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String nacTxt = (p_nac > 0) ? f.format(new java.util.Date(p_nac)) : "";
        String estadoTxt = p_estado ? "ACTIVO" : "DESACTIVO";

        FileWriter fw = new FileWriter(txtFile, false);
        PrintWriter pw = new PrintWriter(fw);

        pw.println("REPORTE CLIENTE: " + p_nombre + " (username: " + p_username + ")");
        pw.println("Código cliente: " + p_code);
        pw.println("Fecha de nacimiento: " + nacTxt + " (" + edad + " años)");
        pw.println("Estado: " + estadoTxt);
        pw.println("Total downloads: " + p_totalDls);
        pw.println("HISTORIAL DE DESCARGAS:");
        pw.println("FECHA(YYYY-MM-DD) | DOWNLOAD ID | GAME CODE | GAME NAME | PRICE | GENRE");

        File dir = new File("steam/downloads");
        File[] files = dir.listFiles((d, name) -> name.startsWith("download_") && name.endsWith(".stm"));
        if (files != null) {
            for (File fdl : files) {
                RandomAccessFile draf = new RandomAccessFile(fdl, "r");
                int d_code = draf.readInt();
                int d_player = draf.readInt();
                String d_playerName = draf.readUTF();
                int d_gameCode = draf.readInt();
                String d_gameName = draf.readUTF();
                int imgLen = draf.readInt();
                if (imgLen > 0) {
                    draf.seek(draf.getFilePointer() + imgLen);
                }
                double d_price = draf.readDouble();
                long d_fecha = draf.readLong();
                draf.close();

                if (d_player == p_code) {
                    String d_fechaTxt = f.format(new java.util.Date(d_fecha));
                    String genero = getGeneroByGameCode(d_gameCode);
                    pw.println(d_fechaTxt + " | " + d_code + " | " + d_gameCode + " | " + d_gameName + " | " + String.format("%.2f", d_price) + " | " + genero);
                }
            }
        }

        pw.flush();
        pw.close();

        javax.swing.JOptionPane.showMessageDialog(null, "REPORTE CREADO");
        return true;
    }

    private String getGeneroByGameCode(int codeGame) throws IOException {
        long keep = gamesFile.getFilePointer();
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            int code = gamesFile.readInt();
            String titulo = readFixedString(gamesFile, 50);
            String genero = readFixedString(gamesFile, 20);
            gamesFile.readChar();
            gamesFile.readInt();
            gamesFile.readDouble();
            gamesFile.readInt();
            readFixedString(gamesFile, 100);
            gamesFile.readBoolean();
            if (code == codeGame) {
                gamesFile.seek(keep);
                return genero;
            }
        }
        gamesFile.seek(keep);
        return "";
    }

}
