/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steamjava;

/**
 *
 * @author unwir
 */
public class Player {
    public int code;
    public String username;
    public String password;
    public String nombre;
    public long nacimiento;
    public int contadorDownloads;
    public String pathFoto;
    public String tipoUsuario;
    public boolean estado;

    public Player(int code, String username, String password, String nombre,
            long nacimiento, int contadorDownloads, String pathFoto,
            String tipoUsuario, boolean estado) {
        this.code = code;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.contadorDownloads = contadorDownloads;
        this.pathFoto = pathFoto;
        this.tipoUsuario = tipoUsuario.trim();
        this.estado = estado;
    }
    
    public String getTipoUsuario(){
        return this.tipoUsuario;
    }
    
}

