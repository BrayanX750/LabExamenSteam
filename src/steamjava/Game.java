/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steamjava;

/**
 *
 * @author unwir
 */
public class Game {

    public int code;
    public String titulo;
    public String genero;
    public char sistemaOperativo;
    public int edadMinima;
    public double precio;
    public int contadorDownloads;
    public String pathFoto;
    public boolean activo;

    public Game(int code, String titulo, String genero, char sistemaOperativo,
            int edadMinima, double precio, int contadorDownloads,
            String pathFoto, boolean activo) {
        this.code = code;
        this.titulo = titulo;
        this.genero = genero;
        this.sistemaOperativo = sistemaOperativo;
        this.edadMinima = edadMinima;
        this.precio = precio;
        this.contadorDownloads = contadorDownloads;
        this.pathFoto = pathFoto;
        this.activo = activo;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - Lps. %.2f", this.genero, this.titulo, this.precio);
    }
    
    
    
    

}
