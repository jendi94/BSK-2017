/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;


public class Recenzja {
    private
    int id_rec;
    String nazwa;
    String autor;
    int ocena;
    String data;
    int fk_film;
    
    Recenzja()
    {
        
    }
    Recenzja(int id, String n, String a,int o , String d, int f)
    {
        id_rec = id;
        nazwa = n;
        autor= a;
        ocena= o;
        data = d;
        fk_film = f;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
    
    public void setId(int id)
    {
        this.id_rec = id;
    }
    public void setAutor(String ad)
    {
        this.autor = ad;
    }
    public void setOcena(int ad)
    {
        this.ocena = ad;
    }
    public void setData(String a)
    {
        this.data = a;
    }
    public void setFK(int f)
    {
        this.fk_film = f;
    }
         
    public int getId()
    {
        return this.id_rec;
    }
    public String getAutor()
    {
        return this.autor;
    }
    public int getOcena()
    {
        return this.ocena;
    }
    public String getData()
    {
        return this.data;
    }
    public int getFK(int f)
    {
        return this.fk_film;
    }
}
