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
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;

public class Steam {

    private RandomAccessFile codeFile;
    private RandomAccessFile gamesFile;
    private RandomAccessFile playerFile;

    public static final int GAME_SIZE = 163;
    public static final int PLAYER_SIZE = 457;

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

    public void addPlayer(String username, String password, String nombre, long nacimientoMs, String foto, String tipoUsuario) throws IOException {
        int code = generarCodigoUser();
        playerFile.seek(playerFile.length());

        playerFile.writeInt(code);
        writeFixedString(playerFile, username, 30);
        writeFixedString(playerFile, password, 30);
        writeFixedString(playerFile, nombre, 50);
        playerFile.writeLong(nacimientoMs);
        playerFile.writeInt(0);
        writeFixedString(playerFile, foto, 100);
        writeFixedString(playerFile, tipoUsuario, 10);
        playerFile.writeBoolean(true);
    }

    public ArrayList<Game> getAllGames() throws IOException {
        ArrayList<Game> lista = new ArrayList<>();
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            int code = gamesFile.readInt();
            String titulo = readFixedString(gamesFile, 50);
            String genero = readFixedString(gamesFile, 20);
            char so = gamesFile.readChar();
            int edadMinima = gamesFile.readInt();
            double precio = gamesFile.readDouble();
            int downloads = gamesFile.readInt();
            String foto = readFixedString(gamesFile, 100);
            boolean activo = gamesFile.readBoolean();

            if (activo) {
                Game g = new Game(code, titulo, genero, so, edadMinima, precio, downloads, foto, activo);
                lista.add(g);
            }
        }
        return lista;
    }

    public boolean updatePriceFor(int codeGame, double newPrice) throws IOException {
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            long startPosition = gamesFile.getFilePointer();
            int code = gamesFile.readInt();

            if (code == codeGame) {

                gamesFile.seek(startPosition + 150);
                gamesFile.writeDouble(newPrice);
                return true;
            }

            gamesFile.seek(startPosition + GAME_SIZE);
        }
        return false;
    }

    public void printGames() throws IOException {
        gamesFile.seek(0);
        boolean hay = false;
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            int code = gamesFile.readInt();
            String titulo = readFixedString(gamesFile, 50);
            String genero = readFixedString(gamesFile, 20);
            char so = gamesFile.readChar();
            int edadMinima = gamesFile.readInt();
            double precio = gamesFile.readDouble();
            int downloads = gamesFile.readInt();
            String foto = readFixedString(gamesFile, 100);
            boolean activo = gamesFile.readBoolean();
            if (activo) {
                hay = true;
                String soTxt = (so == 'W' || so == 'w') ? "Windows" : (so == 'M' || so == 'm') ? "Mac" : (so == 'L' || so == 'l') ? "Linux" : "Otro";
                System.out.println(String.format(
                        "#%d | %s | %s | %s | %d+ | $%.2f | Downloads: %d",
                        code, titulo, genero, soTxt, edadMinima, precio, downloads
                ));
            }
        }
        if (!hay) {
            System.out.println("No hay juegos disponibles.");
        }
    }

    private String readFixedString(RandomAccessFile raf, int size) throws IOException {
        char[] chars = new char[size];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars).trim();
    }
    
    
    public boolean reportForClient(int codeClient) throws IOException {
    Player player = findPlayerByCode(codeClient);

    if (player == null) {
        JOptionPane.showMessageDialog(null, "NO SE PUEDE CREAR REPORTE: Jugador con código " + codeClient + " no existe o está inactivo.");
        return false;
    }

    // El nombre del archivo ahora es fijo y está dentro de la carpeta /steam
    String reportPath = "steam/cliente.txt";
    
    int edad = 0;
    if (player.nacimiento > 0) {
        java.util.Calendar n = java.util.Calendar.getInstance();
        n.setTimeInMillis(player.nacimiento);
        java.util.Calendar h = java.util.Calendar.getInstance();
        edad = h.get(java.util.Calendar.YEAR) - n.get(java.util.Calendar.YEAR);
        if (h.get(java.util.Calendar.DAY_OF_YEAR) < n.get(java.util.Calendar.DAY_OF_YEAR)) {
            edad--;
        }
    }

    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    String nacTxt = (player.nacimiento > 0) ? f.format(new java.util.Date(player.nacimiento)) : "N/A";
    String estadoTxt = player.estado ? "ACTIVO" : "DESACTIVO";

    try (PrintWriter pw = new PrintWriter(new FileWriter(reportPath))) { // Usamos la nueva ruta fija
        pw.println("REPORTE CLIENTE: " + player.nombre + " (username: " + player.username + ")");
        pw.println("Código cliente: " + player.code);
        pw.println("Fecha de nacimiento: " + nacTxt + " (" + edad + " años)");
        pw.println("Estado: " + estadoTxt);
        pw.println("Total downloads: " + player.contadorDownloads);
        pw.println();
        pw.println("HISTORIAL DE DESCARGAS:");
        pw.println("FECHA(YYYY-MM-DD) | DOWNLOAD ID | GAME CODE | GAME NAME | PRECIO | GÉNERO");
        pw.println("---------------------------------------------------------------------------------");

        File dir = new File("steam/downloads");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".stm"));

        if (files != null) {
            for (File fdl : files) {
                try (RandomAccessFile draf = new RandomAccessFile(fdl, "r")) {
                    int d_code = draf.readInt();
                    int d_player_code = draf.readInt();

                    if (d_player_code == player.code) {
                        draf.readUTF();
                        int d_gameCode = draf.readInt();
                        String d_gameName = draf.readUTF();
                        draf.readInt();
                        double d_price = draf.readDouble();
                        long d_fecha = draf.readLong();
                        
                        String d_fechaTxt = f.format(new java.util.Date(d_fecha));
                        String genero = getGeneroByGameCode(d_gameCode);
                        
                        pw.printf("%-18s | %-11d | %-9d | %-20s | $%-9.2f | %s%n",
                                  d_fechaTxt, d_code, d_gameCode, d_gameName, d_price, genero);
                    }
                }
            }
        }
    }

    JOptionPane.showMessageDialog(null, "Reporte generado/actualizado en: " + reportPath);
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

    public Player login(String username, String password) throws IOException {
        playerFile.seek(0);
        while (playerFile.getFilePointer() < playerFile.length()) {

            long startPosition = playerFile.getFilePointer();

            int code = playerFile.readInt();
            String currentUsername = readFixedString(playerFile, 30);
            String currentPassword = readFixedString(playerFile, 30);

            if (currentUsername.equals(username) && currentPassword.equals(password)) {
                String nombre = readFixedString(playerFile, 50);
                long nacimiento = playerFile.readLong();
                int downloads = playerFile.readInt();
                String foto = readFixedString(playerFile, 100);
                String tipo = readFixedString(playerFile, 10);
                boolean activo = playerFile.readBoolean();

                if (activo) {
                    return new Player(code, currentUsername, currentPassword, nombre, nacimiento, downloads, foto, tipo, activo);
                }
            }

            playerFile.seek(startPosition + PLAYER_SIZE);
        }
        return null;
    }

    public boolean deactivatePlayer(int playerCode) throws IOException {
        playerFile.seek(0);
        while (playerFile.getFilePointer() < playerFile.length()) {
            long startPosition = playerFile.getFilePointer();
            int code = playerFile.readInt();

            if (code == playerCode) {

                playerFile.seek(startPosition + PLAYER_SIZE - 1);
                playerFile.writeBoolean(false);
                return true;
            }

            playerFile.seek(startPosition + PLAYER_SIZE);
        }
        return false;
    }

    public Game findGameByCode(int gameCode) throws IOException {
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            long startPosition = gamesFile.getFilePointer();
            int code = gamesFile.readInt();
            if (code == gameCode) {

                String titulo = readFixedString(gamesFile, 50);
                String genero = readFixedString(gamesFile, 20);
                char so = gamesFile.readChar();
                int edadMinima = gamesFile.readInt();
                double precio = gamesFile.readDouble();
                int downloads = gamesFile.readInt();
                String foto = readFixedString(gamesFile, 100);
                boolean activo = gamesFile.readBoolean();
                if (activo) {
                    return new Game(code, titulo, genero, so, edadMinima, precio, downloads, foto, activo);
                }
            }

            gamesFile.seek(startPosition + GAME_SIZE);
        }
        return null;
    }

    public Player findPlayerByCode(int playerCode) throws IOException {
        playerFile.seek(0);
        while (playerFile.getFilePointer() < playerFile.length()) {
            long startPosition = playerFile.getFilePointer();
            int code = playerFile.readInt();
            if (code == playerCode) {
                String username = readFixedString(playerFile, 30);
                String password = readFixedString(playerFile, 30);
                String nombre = readFixedString(playerFile, 50);
                long nacimiento = playerFile.readLong();
                int downloads = playerFile.readInt();
                String foto = readFixedString(playerFile, 100);
                String tipo = readFixedString(playerFile, 10);
                boolean activo = playerFile.readBoolean();
                if (activo) {
                    return new Player(code, username, password, nombre, nacimiento, downloads, foto, tipo, activo);
                }
            }
            playerFile.seek(startPosition + PLAYER_SIZE);
        }
        return null;
    }

    public boolean downloadGame(int gameCode, int playerCode, char sistemaOperativo) throws IOException {
        // --- 1. VALIDACIONES ---
        Game game = findGameByCode(gameCode);
        if (game == null) {
            return false;
        }

        Player player = findPlayerByCode(playerCode);
        if (player == null) {
            return false;
        }

        if (game.sistemaOperativo != sistemaOperativo) {
            return false;
        }

        Calendar birth = new GregorianCalendar();
        birth.setTimeInMillis(player.nacimiento);
        int playerAge = Calendar.getInstance().get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            playerAge--;
        }
        if (playerAge < game.edadMinima) {
            return false;
        }

        int downloadCode = generarCodigoDownload();
        try (RandomAccessFile downloadFile = new RandomAccessFile("steam/downloads/download_" + downloadCode + ".stm", "rw")) {
            downloadFile.writeInt(downloadCode);
            downloadFile.writeInt(player.code);
            downloadFile.writeUTF(player.nombre);
            downloadFile.writeInt(game.code);
            downloadFile.writeUTF(game.titulo);
            downloadFile.writeUTF(game.pathFoto);
            downloadFile.writeDouble(game.precio);
            downloadFile.writeLong(System.currentTimeMillis());
        }

        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            long startPos = gamesFile.getFilePointer();
            if (gamesFile.readInt() == gameCode) {

                gamesFile.seek(startPos + 158);
                int currentDownloads = gamesFile.readInt();
                gamesFile.seek(startPos + 158);
                gamesFile.writeInt(currentDownloads + 1);
                break;
            }
            gamesFile.seek(startPos + GAME_SIZE);
        }

        playerFile.seek(0);
        while (playerFile.getFilePointer() < playerFile.length()) {
            long startPos = playerFile.getFilePointer();
            if (playerFile.readInt() == playerCode) {

                playerFile.seek(startPos + 232);
                int currentDownloads = playerFile.readInt();
                playerFile.seek(startPos + 232);
                playerFile.writeInt(currentDownloads + 1);
                break;
            }
            playerFile.seek(startPos + PLAYER_SIZE);
        }

        return true;
    }

    public ArrayList<Game> getDownloadsForPlayer(int playerCode) throws IOException {
        ArrayList<Game> downloadedGames = new ArrayList<>();
        File downloadsDir = new File("steam/downloads");
        File[] downloadFiles = downloadsDir.listFiles();

        if (downloadFiles == null) {
            return downloadedGames;
        }

        for (File file : downloadFiles) {
            if (file.isFile() && file.getName().endsWith(".stm")) {
                try (RandomAccessFile downloadFile = new RandomAccessFile(file, "r")) {
                    downloadFile.readInt();
                    int filePlayerCode = downloadFile.readInt();

                    if (filePlayerCode == playerCode) {
                        downloadFile.readUTF();
                        int gameCode = downloadFile.readInt();
                        Game game = findGameByCode(gameCode);
                        if (game != null) {
                            downloadedGames.add(game);
                        }
                    }
                }
            }
        }
        return downloadedGames;
    }

  
    public ArrayList<Game> getAllGamesForAdmin() throws IOException {
        ArrayList<Game> lista = new ArrayList<>();
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            int code = gamesFile.readInt();
            String titulo = readFixedString(gamesFile, 50);
            String genero = readFixedString(gamesFile, 20);
            char so = gamesFile.readChar();
            int edadMinima = gamesFile.readInt();
            double precio = gamesFile.readDouble();
            int downloads = gamesFile.readInt();
            String foto = readFixedString(gamesFile, 100);
            boolean activo = gamesFile.readBoolean();

            
            Game g = new Game(code, titulo, genero, so, edadMinima, precio, downloads, foto, activo);
            lista.add(g);
        }
        return lista;
    }

    public boolean toggleGameStatus(int gameCode) throws IOException {
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            long startPosition = gamesFile.getFilePointer();
            int code = gamesFile.readInt();
            if (code == gameCode) {
            
                long statusPosition = startPosition + GAME_SIZE - 1;
                gamesFile.seek(statusPosition);
                boolean currentStatus = gamesFile.readBoolean();
                gamesFile.seek(statusPosition);
                gamesFile.writeBoolean(!currentStatus); 
                return true;
            }
            gamesFile.seek(startPosition + GAME_SIZE);
        }
        return false;
    }
    
    public ArrayList<Player> getAllPlayersForAdmin() throws IOException {
    ArrayList<Player> players = new ArrayList<>();
    playerFile.seek(0);
    while (playerFile.getFilePointer() < playerFile.length()) {
        int code = playerFile.readInt();
        String username = readFixedString(playerFile, 30);
        String password = readFixedString(playerFile, 30);
        String nombre = readFixedString(playerFile, 50);
        long nacimiento = playerFile.readLong();
        int downloads = playerFile.readInt();
        String foto = readFixedString(playerFile, 100);
        String tipo = readFixedString(playerFile, 10);
        boolean activo = playerFile.readBoolean();
        
        players.add(new Player(code, username, password, nombre, nacimiento, downloads, foto, tipo, activo));
    }
    return players;
}
    
}
