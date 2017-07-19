
package bskkk;


public class KontaUzytkownikow {
    private
    int id;

   
    int fkPracownika;
    String login;
    String password;
    public
    Uprawnienia Film;
    Uprawnienia Placowka;
    Uprawnienia Pracownik;
    Uprawnienia Recenzja;
    Uprawnienia Wypozyczenie;
    int przejmij;
    int czy_root;
    
    
    public KontaUzytkownikow(String log, String pwd)
    {
        setLogin(log);
        setPassword(pwd);
    }
    public KontaUzytkownikow(int id, int fk, String log, String pwd, String f, String p, String pr, String r, String w, int przejmijLol, int czy_root)
    {
        setId(id);
        setFkPracownika(fk);
        setLogin(log);
        setPassword(pwd);
        Film = new Uprawnienia(f);
        Placowka = new Uprawnienia(p);
        Pracownik = new Uprawnienia(pr);
        Recenzja = new Uprawnienia(r);
        Wypozyczenie = new Uprawnienia(w);
        przejmij = przejmijLol;
        setCzy_root(czy_root);
    }

    public int getPrzejmij() {
        return przejmij;
    }

    public void setPrzejmij(int przejmij) {
        this.przejmij = przejmij;
    }
    
    public int getCzy_root() {
        return czy_root;
    }

    public void setCzy_root(int czy_root) {
        this.czy_root = czy_root;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    public void setLogin(String log)
    {
        this.login = log;
    }
   public void setPassword(String pswd)
    {
        this.password = pswd;
    }
   public void setPassword(int id)
    {
        this.id = id;
    }
    public String getLogin()
    {
        return this.login;
    }
    public String getPassword()
    {
        return this.password;
    }
    public int getId()
    {
        return this.id;
    }
    
     public int getFkPracownika() {
        return fkPracownika;
    }

    public void setFkPracownika(int fkPracownika) {
        this.fkPracownika = fkPracownika;
    }
}
