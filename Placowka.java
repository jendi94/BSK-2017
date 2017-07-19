/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;


public class Placowka {
    private
    int id_plac;
    String adres;
    String kod_pocztowy;
    
    Placowka()
    {
        
    }
    Placowka(int id, String ad, String kod)
    {
        id_plac = id;
        adres= ad;
        kod_pocztowy= kod;
    }
    
    public void setId(int id)
    {
        this.id_plac =id;
    }
    public void setAdres(String ad)
    {
        this.adres =ad;
    }
    public void setKod(String ad)
    {
        this.kod_pocztowy =ad;
    }
         
    public String getKod()
    {
        return this.kod_pocztowy;
    }
    public String getAdres()
    {
        return this.adres;
    }
     public int getId()
    {
        return this.id_plac;
    }
}
