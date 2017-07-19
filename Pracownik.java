/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;


public class Pracownik {
    private
    int id_prac;
    String Imie;
    String Nazwisko;
    String PESEL;
    String Stanowisko;
    int FK_Plac;
    
    Pracownik()
    {
        
    }

    public Pracownik(int id_prac, String Imie, String Nazwisko, String Stanowisko) {
        this.id_prac = id_prac;
        this.Imie = Imie;
        this.Nazwisko = Nazwisko;
        this.Stanowisko = Stanowisko;
    }

    public int getId_prac() {
        return id_prac;
    }

    public void setId_prac(int id_prac) {
        this.id_prac = id_prac;
    }
    

    public String getImie() {
        return Imie;
    }

    public void setImie(String Imie) {
        this.Imie = Imie;
    }

    public String getNazwisko() {
        return Nazwisko;
    }

    public void setNazwisko(String Nazwisko) {
        this.Nazwisko = Nazwisko;
    }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public String getStanowisko() {
        return Stanowisko;
    }

    public void setStanowisko(String Stanowisko) {
        this.Stanowisko = Stanowisko;
    }

    public int getFK_Plac() {
        return FK_Plac;
    }

    public void setFK_Plac(int FK_Plac) {
        this.FK_Plac = FK_Plac;
    }
    
    
}
