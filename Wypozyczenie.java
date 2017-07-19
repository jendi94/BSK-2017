/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;

/**
 *
 * @author Jendi94
 */
public class Wypozyczenie {
    private
    int id_wyp;
    String data_wyp;
    String data_zwr;
    int cena;
    int kara;
    int FK;

    public int getId_wyp() {
        return id_wyp;
    }

    public void setId_wyp(int id_wyp) {
        this.id_wyp = id_wyp;
    }

    public String getData_wyp() {
        return data_wyp;
    }

    public void setData_wyp(String data_wyp) {
        this.data_wyp = data_wyp;
    }

    public String getData_zwr() {
        return data_zwr;
    }

    public void setData_zwr(String data_zwr) {
        this.data_zwr = data_zwr;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public int getKara() {
        return kara;
    }

    public void setKara(int kara) {
        this.kara = kara;
    }

    public int getFK() {
        return FK;
    }

    public void setFK(int FK) {
        this.FK = FK;
    }

    public Wypozyczenie(int id_wyp, String data_wyp, String data_zwr, int cena, int kara, int FK) {
        this.id_wyp = id_wyp;
        this.data_wyp = data_wyp;
        this.data_zwr = data_zwr;
        this.cena = cena;
        this.kara = kara;
        this.FK = FK;
    }
    
    
    
    
    Wypozyczenie()
    {
        
    }
}



