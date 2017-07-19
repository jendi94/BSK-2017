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
public class Film {
    private
    int id_fil;
    String tytul;
    String gatunek;
    String rezyser;
    String rok;
    int fk_plac;
    
    Film()
    {
        
    }
    
    Film(int id, String a, String b, String c, String d, int fk)
    {
        id_fil = id;
        tytul = a;
        gatunek = b;
        rezyser = c;
        rok = d;
        fk_plac = fk;
    }

    public int getId_fil() {
        return id_fil;
    }

    public void setId_fil(int id_fil) {
        this.id_fil = id_fil;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getGatunek() {
        return gatunek;
    }

    public void setGatunek(String gatunek) {
        this.gatunek = gatunek;
    }

    public String getRezyser() {
        return rezyser;
    }

    public void setRezyser(String rezyser) {
        this.rezyser = rezyser;
    }

    public String getRok() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }

    public int getFk_plac() {
        return fk_plac;
    }

    public void setFk_plac(int fk_plac) {
        this.fk_plac = fk_plac;
    }
    
    
}
