/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Statement;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;



public class ProgramController implements Initializable {

    Connection con;
    private Integer zalogowanyUzytkownik;
    private Integer idWLiscie;
    private List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
    private List<Pracownik> pracownicyKtorychNieMaWBazie = new ArrayList<Pracownik>();
    private List<Historia> historia = new ArrayList<Historia>();
    private List<String> rodzice = new ArrayList<String>();
    //String[] upFilm = {"----", "----"};
    //String[] upPlacowka = {"----", "----"};
   // String[] upPracownik = {"----", "----"};
   // String[] upRecenzja = {"----", "----"};
   // String[] upWypozyczenie = {"----", "----"};
    
    String ktoTraci="";
    
    @FXML
    private Label idUzytkownika;

    @FXML
    private ChoiceBox<String> encje;

    @FXML
    private Button zatwierdz;
    
    @FXML
    private Button wybierzPrzejmij;

    @FXML
    private Tab uzytkownicyTab;
    
    @FXML
    private ChoiceBox<String> tabela;
    
    @FXML
    private Button dodajTabele;
    
    @FXML
    private ChoiceBox<String> choiceUzytkownik;

    @FXML
    private ChoiceBox<String> choiceTabela;

    @FXML
    private Button buttonWybierz;

    @FXML
    private CheckBox checkMS;

    @FXML
    private CheckBox checkMU;

    @FXML
    private CheckBox checkMI;

    @FXML
    private CheckBox checkMD;

    @FXML
    private CheckBox checkDS;

    @FXML
    private CheckBox checkDU;

    @FXML
    private CheckBox checkDI;

    @FXML
    private CheckBox checkDD;

    @FXML
    private Button buttonPrzekaż;

    @FXML 
    private AnchorPane przejmijPane;
    
    @FXML
    private TableView tabelaUprawnien;
    
    private ObservableList<ObservableList> data;

    @FXML
    private CheckBox checkCS;

    @FXML
    private CheckBox checkCU;

    @FXML
    private CheckBox checkCI;

    @FXML
    private CheckBox checkCD;

    @FXML
    private Button buttonCofnij;
    
    @FXML
    private Button buttonCofnijPrzejmij;
    
    @FXML
    private Button buttonNadajPrzejmij;

    @FXML
    private ChoiceBox<String> choiceUzytkownikPrzejmij;

    @FXML
    private Label komunikatUprawnienia;


     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        final String[] nazwyEncji = new String[]{ "Film", "Placowka", "Pracownik", "Recenzja", "Wypozyczenie", "Uzytkownicy"};
        final String[] nazwyEncji2 = new String[]{ "Film", "Placowka", "Pracownik", "Recenzja", "Wypozyczenie"};
             
        choiceTabela.setItems(FXCollections.observableArrayList(nazwyEncji2));
    } 
    
    @FXML
    public void wybierzPrzejmij(MouseEvent event)
    {
        int przejmij = 0;
        String uzytkownik = choiceUzytkownikPrzejmij.getValue();
        for(KontaUzytkownikow k: konta)
        {
            if(k.login.equals(uzytkownik))
            {
                przejmij = k.getPrzejmij();
            }
        }
        if(przejmij == 1)
        {
            buttonCofnijPrzejmij.setDisable(false);
            buttonNadajPrzejmij.setDisable(true);
        }
        else 
        {
            buttonCofnijPrzejmij.setDisable(true);
            buttonNadajPrzejmij.setDisable(false);
        }
    }
    
    
    void zwrocDzieci2(String nadawca, List<String> lista, String tabela) throws SQLException
    {
        for(Historia h: historia)
        {
            if(h.getNadawca() == nadawca && /*h.getRodzaj()== "Przekaz" &&*/ h.getTabela()== tabela)
            {
                lista.add(h.getOdbiorca());
                Statement st = con.createStatement();
                st.executeUpdate("UPDATE uzytkownik SET"+tabela+ "='----' WHERE login ='"+h.getOdbiorca()+"'");
                //usuwa uprawnienia do tabeli
                //znajduje uprawnieniai nie od h.getnadawcw
                zwrocDzieci2(h.getOdbiorca(), lista, tabela);
            }
        }
    }
    
    void zwrocPotomkow(String nadawca, List<String> lista)
    {
        for(Historia h : historia)
        {
            if(h.getNadawca().equals(nadawca))
            {
                lista.add(h.getOdbiorca());
                zwrocPotomkow(h.getOdbiorca(),lista);
            }
        }
    }
    
    void zwrocPotomkow2(String nadawca, List<String> lista, int indeks,String znak, String tablica)//zwraca potomkow, ktorzy dostali uprawnienie ode mnie, bo ja mam na imie Blaneczka
    {
        for(Historia h : historia)
        {
            if(h.getNadawca().equals(nadawca) && (h.getPrzekaz().contains(znak) || h.getPrzekaz().contains(znak.toUpperCase())) && h.getTabela().equals(tablica))
            {
                lista.add(h.getOdbiorca());
                zwrocPotomkow2(h.getOdbiorca(),lista, indeks, znak, tablica);
            }
        }
    }
    
    void zwrocDzieci(String nadawca, List<String> lista)
    {
        for(Historia h : historia)
        {
            if(h.getNadawca().equals(nadawca))
            {
                lista.add(h.getOdbiorca());
            }
        }
    }
    
    
    
     void zwrocRodzicow(String dziecko, List<String> lista)
     {
         for(Historia h: historia)
         {
             if(h.getOdbiorca().equals(dziecko))
             {
                 if(h.getNadawca().equals(zwrocRoota()))
                 {
                    lista.add(h.getNadawca());
                 }
                 else
                 {
                     lista.add(h.getNadawca());
                     zwrocRodzicow(h.getNadawca(), lista);
                 }
             }
         }
     }
    
 
    @FXML
    void wybierzUpAction(MouseEvent event) {
        try{
            String login = choiceUzytkownik.getValue();
            String tabela = choiceTabela.getValue();
            resetujCheckBoxy();
            blokowanieCheckBoxowPrzekaz(login, tabela);
            blokowanieCheckBoxowCofnij(login,tabela);

        }
        catch(Exception e){}
    }

    int zwrocIdOdbiorcy(String komu)
    {
        int idOdbiorcy =0;//osoba, ktorej chce cos przekazac
        for(KontaUzytkownikow e : konta)
        {
            if(e.getLogin().equals(komu))
            {
                return idOdbiorcy = konta.indexOf(e);
            }
        }
        return 0;
    }
    
    String[] resetuj()
    {
        String[] zwroc = {"----", "----"};
        return zwroc;
    }
      
     @FXML
     void zatwierdzAction(MouseEvent event) throws IOException, SQLException //wybor tabeli
     {
         try{
            String zmienna = encje.getValue();
            String zmiennaFXML = zmienna.concat(".fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(zmiennaFXML));
            Parent p = (Parent) loader.load();
            switch (zmienna)
            {
                case "Placowka": 
                    PlacowkaController placowka = loader.getController();
                    placowka.setParam(zalogowanyUzytkownik, idWLiscie, konta, con);
                    placowka.ustawUprawnienia();
                    break;
                case "Pracownik": 
                    PracownikController pracownik = loader.getController();
                    pracownik.setParam(zalogowanyUzytkownik, idWLiscie, konta, con);
                    pracownik.ustawUprawnienia();
                    break;
                case "Recenzja": 
                    RecenzjaController recenzja = loader.getController();
                    recenzja.setParam(zalogowanyUzytkownik, idWLiscie, konta, con);
                    recenzja.ustawUprawnienia();
                    break;
                case "Film": 
                    FilmController film = loader.getController();
                    film.setParam(zalogowanyUzytkownik, idWLiscie, konta, con);
                    film.ustawUprawnienia();
                    break;
                case "Wypozyczenie": 
                    WypozyczenieController wypozyczenie = loader.getController();
                    wypozyczenie.setParam(zalogowanyUzytkownik, idWLiscie, konta, con);
                    wypozyczenie.ustawUprawnienia();
                    break;
                case "Uzytkownicy":
                    UzytkownicyController uzytkownicy = loader.getController();
                    uzytkownicy.setParam(zalogowanyUzytkownik, idWLiscie, konta, con);
                    break;

            }
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.show();
        }
        catch(Exception e){}
    }
        
     public void setParamProg(Integer id, Integer idwLiscie, List<KontaUzytkownikow> konta, Connection con ) throws SQLException
    {
        this.con =con;
        this.idWLiscie = idwLiscie;
        this.zalogowanyUzytkownik= id;
        this.konta = konta;
        idUzytkownika.setText(konta.get(idWLiscie).getLogin());
        //wyłączanie okna do dodawania użytkowników
        final String[] nazwyEncji = new String[]{ "Film", "Placowka", "Pracownik", "Recenzja", "Wypozyczenie", "Uzytkownicy"};

        encje.setItems(FXCollections.observableArrayList(nazwyEncji));
        if(konta.get(idWLiscie).getCzy_root() == 1)
        {
            przejmijPane.setVisible(true);
        }
        else przejmijPane.setVisible(false);
      
       // wpiszPesele();
        //wczytajPesele();
        wpiszHistorie();
        
       // choiceUzytkownik.setItems(FXCollections.observableArrayList(listaLoginowUzytkownikow()));
       odswiezPESELE();
       //wyswietlanieUprawnien();
    }
     
     void wczytajPesele() throws SQLException
     {
        List<String> pesele = new ArrayList<String>();
        //wczytywanie listy peseli
        for(Pracownik e : pracownicyKtorychNieMaWBazie)
        {
            pesele.add(e.getPESEL());
        }
     }
     
     void wpiszPesele() throws SQLException
     {
            Statement stmt = con.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM pracownik");
            while ( rs.next() )
               {
                   //pobieram ID
                   int id = rs.getInt( 1 );
                   int k = sprawdzUzytkownika(id);
                   if(k ==1)
                   {
                    Pracownik obj = new Pracownik();
                    obj.setId_prac(rs.getInt( 1 ) );
                    obj.setImie( rs.getString( 2 ) );
                    obj.setNazwisko( rs.getString( 3 ) );
                    obj.setPESEL(rs.getString(4));
                    obj.setStanowisko(rs.getString(5));
                    obj.setFK_Plac(rs.getInt(6));
                    pracownicyKtorychNieMaWBazie.add(obj);
                   }
              }    
     }
     
     void wpiszHistorie() throws SQLException
     {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM historia");
            while(rs.next())
            {
                Historia obj = new Historia(rs.getInt(1), rs.getString(2), rs.getString(3), 
                rs.getString(4), rs.getString(5));
                historia.add(obj);
            }
     }
         
     String zwrocRoota()
     {
         for(KontaUzytkownikow k: konta)
        {
            if(k.getCzy_root() == 1)
            {
                return k.getLogin();
            }
        }
        return "";
     }
     
     int sprawdzUzytkownika(int id)
     {
         for(KontaUzytkownikow e : konta)
        {
            if(e.getFkPracownika() == id)
            {
                return 0; //olewam, ma już konto
            }
        }
         return 1; //nie ma jeszcze konta
     }
        
     List<String> listaLoginowUzytkownikow()
     {
         List<String> uzytkownicy = new ArrayList<String>();
         for(KontaUzytkownikow e :  konta)
         {
            if(!konta.get(idWLiscie).getLogin().equals(e.getLogin()))
            {
                uzytkownicy.add(e.getLogin());
            }  
         }
         return uzytkownicy;
     }
     
     void resetujCheckBoxy()
     {
         checkMS.setDisable(false);
         checkDS.setDisable(false);
         checkMU.setDisable(false);
         checkDU.setDisable(false);
         checkMI.setDisable(false);
         checkDI.setDisable(false);
         checkMD.setDisable(false);
         checkDD.setDisable(false);
         
         checkCS.setDisable(false);
         checkCU.setDisable(false);
         checkCI.setDisable(false);
         checkCD.setDisable(false);
         
         checkMS.setSelected(false);
         checkDS.setSelected(false);
         checkMU.setSelected(false);
         checkDU.setSelected(false);
         checkMI.setSelected(false);
         checkDI.setSelected(false);
         checkMD.setSelected(false);
         checkDD.setSelected(false);
         
         checkCS.setSelected(false);
         checkCU.setSelected(false);
         checkCI.setSelected(false);
         checkCD.setSelected(false);
           
     }
     
     void wylaczPrzekaz()
     {
         checkMS.setDisable(true);
         checkDS.setDisable(true);
         checkMU.setDisable(true);
         checkDU.setDisable(true);
         checkMI.setDisable(true);
         checkDI.setDisable(true);
         checkMD.setDisable(true);
         checkDD.setDisable(true);
     }
     
     void wylaczCofnij()
     {
         checkCS.setDisable(true);
         checkCU.setDisable(true);
         checkCI.setDisable(true);
         checkCD.setDisable(true);
     }
    
     void blokowanieCheckBoxowPrzekaz(String komu, String doCzego)
     {
         int idOd = zwrocIdOdbiorcy(komu);//komu dajemy
         String ja = konta.get(idWLiscie).getLogin();
         KontaUzytkownikow jaOb = konta.get(idWLiscie);
         KontaUzytkownikow komuOb = konta.get(idOd);
         String tabela = doCzego;//do jakiej tabeli
         
         List<String> rodzice = new ArrayList<String>();
         zwrocRodzicow(ja, rodzice);
         
         if(rodzice.contains(komu))
         {
             wylaczPrzekaz();
         }
         else
         {
             switch(tabela)
            {
                case "Film":
                {
                    if(komuOb.Film.getSelect() == '-')
                    {
                        if(jaOb.Film.getSelect() == 'S')
                        {
                            checkMS.setDisable(false);
                            checkDS.setDisable(false);
                        }
                        else
                        {
                            checkMS.setDisable(true);
                            checkDS.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMS.setDisable(true);
                        checkDS.setDisable(true);
                    }
                    //UPDATE
                    if(komuOb.Film.getUpdate() == '-')
                    {
                        if(jaOb.Film.getUpdate() == 'U')
                        {
                            checkMU.setDisable(false);
                            checkDU.setDisable(false);
                        }
                        else
                        {
                            checkMU.setDisable(true);
                            checkDU.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMU.setDisable(true);
                        checkDU.setDisable(true);
                    }
                    //INSERT
                    if(komuOb.Film.getInsert() == '-')
                    {
                        if(jaOb.Film.getInsert() == 'I')
                        {
                            checkMI.setDisable(false);
                            checkDI.setDisable(false);
                        }
                        else
                        {
                            checkMI.setDisable(true);
                            checkDI.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMI.setDisable(true);
                        checkDI.setDisable(true);
                    }
                    
                    //DELETE
                    if(komuOb.Film.getDelete() == '-')
                    {
                        if(jaOb.Film.getDelete() == 'D')
                        {
                            checkMD.setDisable(false);
                            checkDD.setDisable(false);
                        }
                        else
                        {
                            checkMD.setDisable(true);
                            checkDD.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMD.setDisable(true);
                        checkDD.setDisable(true);
                    }
                    break;
                }
                case "Placowka":
                {
                    if(komuOb.Placowka.getSelect() == '-')
                    {
                        if(jaOb.Placowka.getSelect() == 'S')
                        {
                            checkMS.setDisable(false);
                            checkDS.setDisable(false);
                        }
                        else
                        {
                            checkMS.setDisable(true);
                            checkDS.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMS.setDisable(true);
                        checkDS.setDisable(true);
                    }
                    //UPDATE
                    if(komuOb.Placowka.getUpdate() == '-')
                    {
                        if(jaOb.Placowka.getUpdate() == 'U')
                        {
                            checkMU.setDisable(false);
                            checkDU.setDisable(false);
                        }
                        else
                        {
                            checkMU.setDisable(true);
                            checkDU.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMU.setDisable(true);
                        checkDU.setDisable(true);
                    }
                    //INSERT
                    if(komuOb.Placowka.getInsert() == '-')
                    {
                        if(jaOb.Placowka.getInsert() == 'I')
                        {
                            checkMI.setDisable(false);
                            checkDI.setDisable(false);
                        }
                        else
                        {
                            checkMI.setDisable(true);
                            checkDI.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMI.setDisable(true);
                        checkDI.setDisable(true);
                    }
                    
                    //DELETE
                    if(komuOb.Placowka.getDelete() == '-')
                    {
                        if(jaOb.Placowka.getDelete() == 'D')
                        {
                            checkMD.setDisable(false);
                            checkDD.setDisable(false);
                        }
                        else
                        {
                            checkMD.setDisable(true);
                            checkDD.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMD.setDisable(true);
                        checkDD.setDisable(true);
                    }
                    break;
                }
                case "Pracownik":
                {
                    if(komuOb.Pracownik.getSelect() == '-')
                    {
                        if(jaOb.Pracownik.getSelect() == 'S')
                        {
                            checkMS.setDisable(false);
                            checkDS.setDisable(false);
                        }
                        else
                        {
                            checkMS.setDisable(true);
                            checkDS.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMS.setDisable(true);
                        checkDS.setDisable(true);
                    }
                    //UPDATE
                    if(komuOb.Pracownik.getUpdate() == '-')
                    {
                        if(jaOb.Pracownik.getUpdate() == 'U')
                        {
                            checkMU.setDisable(false);
                            checkDU.setDisable(false);
                        }
                        else
                        {
                            checkMU.setDisable(true);
                            checkDU.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMU.setDisable(true);
                        checkDU.setDisable(true);
                    }
                    //INSERT
                    if(komuOb.Pracownik.getInsert() == '-')
                    {
                        if(jaOb.Pracownik.getInsert() == 'I')
                        {
                            checkMI.setDisable(false);
                            checkDI.setDisable(false);
                        }
                        else
                        {
                            checkMI.setDisable(true);
                            checkDI.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMI.setDisable(true);
                        checkDI.setDisable(true);
                    }
                    
                    //DELETE
                    if(komuOb.Pracownik.getDelete() == '-')
                    {
                        if(jaOb.Pracownik.getDelete() == 'D')
                        {
                            checkMD.setDisable(false);
                            checkDD.setDisable(false);
                        }
                        else
                        {
                            checkMD.setDisable(true);
                            checkDD.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMD.setDisable(true);
                        checkDD.setDisable(true);
                    }
                    break;
                }
                case "Recenzja":
                {
                    if(komuOb.Recenzja.getSelect() == '-')
                    {
                        if(jaOb.Recenzja.getSelect() == 'S')
                        {
                            checkMS.setDisable(false);
                            checkDS.setDisable(false);
                        }
                        else
                        {
                            checkMS.setDisable(true);
                            checkDS.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMS.setDisable(true);
                        checkDS.setDisable(true);
                    }
                    //UPDATE
                    if(komuOb.Recenzja.getUpdate() == '-')
                    {
                        if(jaOb.Recenzja.getUpdate() == 'U')
                        {
                            checkMU.setDisable(false);
                            checkDU.setDisable(false);
                        }
                        else
                        {
                            checkMU.setDisable(true);
                            checkDU.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMU.setDisable(true);
                        checkDU.setDisable(true);
                    }
                    //INSERT
                    if(komuOb.Recenzja.getInsert() == '-')
                    {
                        if(jaOb.Recenzja.getInsert() == 'I')
                        {
                            checkMI.setDisable(false);
                            checkDI.setDisable(false);
                        }
                        else
                        {
                            checkMI.setDisable(true);
                            checkDI.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMI.setDisable(true);
                        checkDI.setDisable(true);
                    }
                    
                    //DELETE
                    if(komuOb.Recenzja.getDelete() == '-')
                    {
                        if(jaOb.Recenzja.getDelete() == 'D')
                        {
                            checkMD.setDisable(false);
                            checkDD.setDisable(false);
                        }
                        else
                        {
                            checkMD.setDisable(true);
                            checkDD.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMD.setDisable(true);
                        checkDD.setDisable(true);
                    }
                    break;
                }
                case "Wypozyczenie":
                {
                    if(komuOb.Wypozyczenie.getSelect() == '-')
                    {
                        if(jaOb.Wypozyczenie.getSelect() == 'S')
                        {
                            checkMS.setDisable(false);
                            checkDS.setDisable(false);
                        }
                        else
                        {
                            checkMS.setDisable(true);
                            checkDS.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMS.setDisable(true);
                        checkDS.setDisable(true);
                    }
                    //UPDATE
                    if(komuOb.Wypozyczenie.getUpdate() == '-')
                    {
                        if(jaOb.Wypozyczenie.getUpdate() == 'U')
                        {
                            checkMU.setDisable(false);
                            checkDU.setDisable(false);
                        }
                        else
                        {
                            checkMU.setDisable(true);
                            checkDU.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMU.setDisable(true);
                        checkDU.setDisable(true);
                    }
                    //INSERT
                    if(komuOb.Wypozyczenie.getInsert() == '-')
                    {
                        if(jaOb.Wypozyczenie.getInsert() == 'I')
                        {
                            checkMI.setDisable(false);
                            checkDI.setDisable(false);
                        }
                        else
                        {
                            checkMI.setDisable(true);
                            checkDI.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMI.setDisable(true);
                        checkDI.setDisable(true);
                    }
                    
                    //DELETE
                    if(komuOb.Wypozyczenie.getDelete() == '-')
                    {
                        if(jaOb.Wypozyczenie.getDelete() == 'D')
                        {
                            checkMD.setDisable(false);
                            checkDD.setDisable(false);
                        }
                        else
                        {
                            checkMD.setDisable(true);
                            checkDD.setDisable(true);
                        }
                    }
                    else
                    {
                        checkMD.setDisable(true);
                        checkDD.setDisable(true);
                    }
                    break;
                }

            }
         }

     }
     
     void blokowanieCheckBoxowCofnij(String komu, String doCzego)
     {
         int komuId = zwrocIdOdbiorcy(komu);//komu dajemy
         String ja = konta.get(idWLiscie).getLogin();
         KontaUzytkownikow jaOb = konta.get(idWLiscie);
         KontaUzytkownikow komuOb = konta.get(komuId);
         String tabela = doCzego;//do jakiej tabeli
         
         List<String> dzieci = new ArrayList<String>();
         zwrocDzieci(ja, dzieci);
         
         List<String> potomkowie = new ArrayList<String>();
         zwrocPotomkow(ja, potomkowie);
         
        
        
             
                if(dzieci.contains(komu) || jaOb.getCzy_root()==1)
                {
                    switch(tabela)
                   {
                       case "Film" :
                       {
                            if(jaOb.getCzy_root()==1)
                            {
                               if(komuOb.Film.getSelect()== '-'  )checkCS.setDisable(true);
                               if(komuOb.Film.getUpdate()== '-'  )checkCU.setDisable(true);
                               if(komuOb.Film.getInsert()== '-'  )checkCI.setDisable(true);
                               if(komuOb.Film.getDelete()== '-'  ) checkCD.setDisable(true);
                            }
                            else
                            {
                                    if(komuOb.Film.getSelect()== '-')
                                    {
                                        checkCS.setDisable(true);
                                    }
                                    else
                                    {
                                        if(jaOb.Film.getSelect() == 'S')
                                        {
                                            for(Historia h: historia)
                                            {
                                                if(h.getNadawca() == ja && h.getOdbiorca() == komu && h.getTabela() == tabela && h.getPrzekaz().charAt(0) != '-')
                                                {
                                                    checkCS.setDisable(false);
                                                    break;
                                                }
                                                else checkCS.setDisable(true);
                                            }
                                            //bylo ode mnie, zakladamy
                                        }
                                        else checkCS.setDisable(true);
                                    }
                                    if(komuOb.Film.getUpdate()== '-')
                                    {
                                        checkCU.setDisable(true);
                                    }
                                    if(komuOb.Film.getInsert()== '-')
                                    {
                                        checkCI.setDisable(true);
                                    }
                                    if(komuOb.Film.getDelete()== '-')
                                    {
                                        checkCD.setDisable(true);
                                    }
                            }
                           break;
                       }
                       case "Placowka" :
                       {
                            
                           if(jaOb.getCzy_root()==1)
                            {
                               if(komuOb.Placowka.getSelect()== '-'  )checkCS.setDisable(true);
                               if(komuOb.Placowka.getUpdate()== '-'  )checkCU.setDisable(true);
                               if(komuOb.Placowka.getInsert()== '-'  )checkCI.setDisable(true);
                               if(komuOb.Placowka.getDelete()== '-'  ) checkCD.setDisable(true);
                            }
                            else
                            {
                                    if(komuOb.Placowka.getSelect()== '-')
                                    {
                                        checkCS.setDisable(true);
                                    }
                                    else
                                    {
                                        if(jaOb.Placowka.getSelect() == 'S')
                                        {
                                            for(Historia h: historia)
                                            {
                                                if(h.getNadawca() == ja && h.getOdbiorca() == komu && h.getTabela() == tabela && h.getPrzekaz().charAt(0) != '-')
                                                {
                                                    checkCS.setDisable(false);
                                                    break;
                                                }
                                                else checkCS.setDisable(true);
                                            }
                                        }
                                        else checkCS.setDisable(true);
                                    }
                                    if(komuOb.Placowka.getUpdate()== '-')
                                    {
                                        checkCU.setDisable(true);
                                    }
                                    if(komuOb.Placowka.getInsert()== '-')
                                    {
                                        checkCI.setDisable(true);
                                    }
                                    if(komuOb.Placowka.getDelete()== '-')
                                    {
                                        checkCD.setDisable(true);
                                    }
                            }
                           break;
                       }
                       case "Pracownik" :
                       {
                          if(jaOb.getCzy_root()==1)
                            {
                               if(komuOb.Pracownik.getSelect()== '-'  )checkCS.setDisable(true);
                               if(komuOb.Pracownik.getUpdate()== '-'  )checkCU.setDisable(true);
                               if(komuOb.Pracownik.getInsert()== '-'  )checkCI.setDisable(true);
                               if(komuOb.Pracownik.getDelete()== '-'  ) checkCD.setDisable(true);
                            }
                            else
                            {
                                    if(komuOb.Pracownik.getSelect()== '-')
                                    {
                                        checkCS.setDisable(true);
                                    }
                                    else
                                    {
                                        if(jaOb.Pracownik.getSelect() == 'S')
                                        {
                                            for(Historia h: historia)
                                            {
                                                if(h.getNadawca() == ja && h.getOdbiorca() == komu && h.getTabela() == tabela && h.getPrzekaz().charAt(0) != '-')
                                                {
                                                    checkCS.setDisable(false);
                                                    break;
                                                }
                                                else checkCS.setDisable(true);
                                            }
                                            //bylo ode mnie, zakladamy
                                        }
                                        else checkCS.setDisable(true);
                                    }
                                    if(komuOb.Pracownik.getUpdate()== '-')
                                    {
                                        checkCU.setDisable(true);
                                    }
                                    if(komuOb.Pracownik.getInsert()== '-')
                                    {
                                        checkCI.setDisable(true);
                                    }
                                    if(komuOb.Pracownik.getDelete()== '-')
                                    {
                                        checkCD.setDisable(true);
                                    }
                            }
                           break;
                       }
                        case "Recenzja" :
                       {
                           if(jaOb.getCzy_root()==1)
                            {
                               if(komuOb.Recenzja.getSelect()== '-'  )checkCS.setDisable(true);
                               if(komuOb.Recenzja.getUpdate()== '-'  )checkCU.setDisable(true);
                               if(komuOb.Recenzja.getInsert()== '-'  )checkCI.setDisable(true);
                               if(komuOb.Recenzja.getDelete()== '-'  ) checkCD.setDisable(true);
                            }
                            else
                            {
                                    if(komuOb.Recenzja.getSelect()== '-')
                                    {
                                        checkCS.setDisable(true);
                                    }
                                    else
                                    {
                                        if(jaOb.Recenzja.getSelect() == 'S')
                                        {
                                            for(Historia h: historia)
                                            {
                                                if(h.getNadawca() == ja && h.getOdbiorca() == komu && h.getTabela() == tabela && h.getPrzekaz().charAt(0) != '-')
                                                {
                                                    checkCS.setDisable(false);
                                                    break;
                                                }
                                                else checkCS.setDisable(true);
                                            }
                                            //bylo ode mnie, zakladamy
                                        }
                                        else checkCS.setDisable(true);
                                    }
                                    if(komuOb.Recenzja.getUpdate()== '-')
                                    {
                                        checkCU.setDisable(true);
                                    }
                                    if(komuOb.Recenzja.getInsert()== '-')
                                    {
                                        checkCI.setDisable(true);
                                    }
                                    if(komuOb.Recenzja.getDelete()== '-')
                                    {
                                        checkCD.setDisable(true);
                                    }
                            }
                           break;
                       }
                        case "Wypozyczenie" :
                       {
                           if(jaOb.getCzy_root()==1)
                            {
                               if(komuOb.Wypozyczenie.getSelect()== '-'  )checkCS.setDisable(true);
                               if(komuOb.Wypozyczenie.getUpdate()== '-'  )checkCU.setDisable(true);
                               if(komuOb.Wypozyczenie.getInsert()== '-'  )checkCI.setDisable(true);
                               if(komuOb.Wypozyczenie.getDelete()== '-'  ) checkCD.setDisable(true);
                            }
                            else
                            {
                                    if(komuOb.Wypozyczenie.getSelect()== '-')
                                    {
                                        checkCS.setDisable(true);
                                    }
                                    else
                                    {
                                        if(jaOb.Wypozyczenie.getSelect() == 'S')
                                        {
                                            for(Historia h: historia)
                                            {
                                                if(h.getNadawca() == ja && h.getOdbiorca() == komu && h.getTabela() == tabela && h.getPrzekaz().charAt(0) != '-')
                                                {
                                                    checkCS.setDisable(false);
                                                    break;
                                                }
                                                else checkCS.setDisable(true);
                                            }
                                            //bylo ode mnie, zakladamy
                                        }
                                        else checkCS.setDisable(true);
                                    }
                                    if(komuOb.Wypozyczenie.getUpdate()== '-')
                                    {
                                        checkCU.setDisable(true);
                                    }
                                    if(komuOb.Wypozyczenie.getInsert()== '-')
                                    {
                                        checkCI.setDisable(true);
                                    }
                                    if(komuOb.Wypozyczenie.getDelete()== '-')
                                    {
                                        checkCD.setDisable(true);
                                    }
                            }
                           break;
                       }
                      }
                }
                else
                {
                    wylaczCofnij();
                }
     
             
         
         
     }
     
     @FXML
     void wylogujAction(MouseEvent event) throws IOException
     {
        ((Node)event.getSource()).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Log.fxml"));
        Parent p = (Parent) loader.load();
        FXMLDocumentController program = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();
     }
     

    public  List<KontaUzytkownikow> wczytajKonta() throws FileNotFoundException, IOException, SQLException
    {       
           List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
        
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery( "select * from uzytkownik" );
            ResultSetMetaData rsmd = rs.getMetaData();
    
            System.out.println( "Database connection successfully" );
             
            while ( rs.next() )
            {
                    int id = rs.getInt(1);
                    int fk =rs.getInt(2);
                    String log = rs.getString(3);
                    String pwd =  rs.getString(4);
                    String f = rs.getString(5);
                    //String fp = rs.getString(6);
                    String p= rs.getString(6);
                    //String pp = rs.getString(8);
                    String pr = rs.getString(7);
                    //String prp = rs.getString(10);
                    String r = rs.getString(8);
                    //String rp = rs.getString(12);
                    String w = rs.getString(9);
                    //String wp = rs.getString(14);
                    int przejmij = rs.getInt(10);
                    int czy_root = rs.getInt(11);
                    KontaUzytkownikow obj = new KontaUzytkownikow(id, fk, log, pwd, f, p, pr, r, w, przejmij, czy_root);
                    konta.add( obj);               
              }

           return konta; 
    }
    
    
        @FXML
    void odswiezUzytkownikow(MouseEvent event) throws SQLException {

        odswiezPESELE();
    }
    
    void odswiezPESELE() throws SQLException
    {
        pracownicyKtorychNieMaWBazie.clear();
        Statement stmt = con.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM pracownik");
            while ( rs.next() )
               {
                   //pobieram ID
                   int id = rs.getInt( 1 );
                   int k = sprawdzUzytkownika(id);
                   if(k ==1)
                   {
                    Pracownik obj = new Pracownik();
                    obj.setId_prac(rs.getInt( 1 ) );
                    obj.setImie( rs.getString( 2 ) );
                    obj.setNazwisko( rs.getString( 3 ) );
                    obj.setPESEL(rs.getString(4));
                    obj.setStanowisko(rs.getString(5));
                    obj.setFK_Plac(rs.getInt(6));
                    pracownicyKtorychNieMaWBazie.add(obj);
                   }
              }
            
            List<String> pesele = new ArrayList<String>();
        //wczytywanie listy peseli
        for(Pracownik e : pracownicyKtorychNieMaWBazie)
        {
            pesele.add(e.getPESEL());
        }
            
        choiceUzytkownik.setItems(FXCollections.observableArrayList(listaLoginowUzytkownikow()));
        choiceUzytkownikPrzejmij.setItems(FXCollections.observableArrayList(listaLoginowUzytkownikow()));
    }
    
    void cofnijUprawnienie(String komu, String tabela, List<String> lista, String znak, int indeks) throws SQLException
    {
        Statement st = con.createStatement();
        zwrocPotomkow2(komu, lista, indeks, znak, tabela);
        lista.add(komu);
            for(String s: lista)
            {
                String odbierzSUIDD = "----";
                char[] odbierzSUIDDC = odbierzSUIDD.toCharArray();
                
                ResultSet rs = st.executeQuery("SELECT " +tabela.toLowerCase()+" FROM uzytkownik WHERE Login ='"+s+"'");
                while(rs.next())
                {
                    odbierzSUIDD = rs.getString(1);
                }
                odbierzSUIDDC = odbierzSUIDD.toCharArray();
                odbierzSUIDDC[indeks]='-';
                odbierzSUIDD = String.valueOf(odbierzSUIDDC);
                st.executeUpdate("UPDATE uzytkownik SET " +tabela.toLowerCase()+"='"+odbierzSUIDD+"' WHERE login = '"+s+"'");

            }
    }
    
    @FXML
    void cofnijAction(MouseEvent event) throws SQLException, IOException 
    {
        String komu = choiceUzytkownik.getValue();//komu odbieramy
        String tabela = choiceTabela.getValue();//skad odbieramy
        String ja = konta.get(idWLiscie).getLogin();
        
        int indeks = 0;
        if(checkCS.isSelected())
        {
            List<String> lista = new ArrayList<String>();
            String znak = "s";
            indeks = 0;
            cofnijUprawnienie(komu,tabela,lista, znak, indeks);
        }
        if(checkCU.isSelected())
        {
            List<String> lista = new ArrayList<String>();
            String znak = "u";
            indeks = 1;
            cofnijUprawnienie(komu,tabela,lista, znak, indeks);
        }
        if(checkCI.isSelected())
        {
            List<String> lista = new ArrayList<String>();
            String znak = "i";
            indeks = 2;
            cofnijUprawnienie(komu,tabela,lista, znak, indeks);
        }
        if(checkCD.isSelected())
        {
            List<String> lista = new ArrayList<String>();
            String znak = "d";
            indeks = 3;
            cofnijUprawnienie(komu,tabela,lista, znak, indeks);
        }
        
        konta =wczytajKonta();
        resetujCheckBoxy();
    }

    @FXML
    void nadajPrzejmijAction(MouseEvent event) throws SQLException, IOException
    {
        String ziom = choiceUzytkownikPrzejmij.getValue();
        Statement st = con.createStatement();
        st.executeUpdate("UPDATE uzytkownik SET Przejmij ="+1+" WHERE login ='"+ziom+"'");
        konta = wczytajKonta();
    }

    @FXML
    void cofnijPrzejmijAction(MouseEvent event) throws IOException, SQLException 
    {
        String ziom = choiceUzytkownikPrzejmij.getValue();
        Statement st = con.createStatement();
        st.executeUpdate("UPDATE uzytkownik SET Przejmij ="+0+" WHERE login ='"+ziom+"'");
        konta = wczytajKonta();
    }
    
    @FXML
    void przekazUprawnieniaAction(MouseEvent event) throws SQLException, IOException 
    {
        String login = choiceUzytkownik.getValue();
        String tabela = choiceTabela.getValue();
        int idOd = zwrocIdOdbiorcy(login);//komu dajemy
        String ja = konta.get(idWLiscie).getLogin();
        KontaUzytkownikow jaOb = konta.get(idWLiscie);
        KontaUzytkownikow komuOb = konta.get(idOd);
        
        
        if(komuOb.getPrzejmij() == 0)
        {
            String SUIDHis;
            char[] SUIDHisC = {'-', '-', '-', '-'};
            Statement st = con.createStatement();
            //st.executeUpdate("UPDATE uzytkownik SET"+tabela+ "='----' WHERE login ='"+h.getOdbiorca()+"'");

            ResultSet rs = st.executeQuery("SELECT " +tabela.toLowerCase()+" FROM uzytkownik WHERE Login ='"+login+"'");
            String SUID = "";
            while(rs.next())
            {
                SUID = rs.getString(1);
            }
            char[] SUIDC = SUID.toCharArray();
            
            
            if(checkDS.isSelected())
            {
                SUIDHisC[0] = 'S';
                SUIDC[0] = 'S';
            } 
            else if (checkMS.isSelected()) 
            {
                SUIDHisC[0] = 's';
                SUIDC[0]= 's';
            }
            if(checkDU.isSelected())
            {
                SUIDHisC[1] = 'U';
                SUIDC[1]= 'U';
            }
            else if (checkMU.isSelected()) 
            {
                SUIDHisC[1] = 'u';
                SUIDC[1]= 'u';
            }
            if(checkDI.isSelected()) 
            {
                SUIDHisC[2] = 'I';
                SUIDC[2]= 'I';
            }
            else if (checkMI.isSelected()) 
            {
                SUIDHisC[2] = 'i';
                SUIDC[2]= 'i';
            }
            if(checkDD.isSelected()) 
            {
                SUIDHisC[3] = 'D';
                SUIDC[3]= 'D';
            }
            else if (checkMD.isSelected()) 
            {
                SUIDHisC[3] = 'd';
                SUIDC[3]= 'd';
            }
            SUID = String.valueOf(SUIDC);
            SUIDHis = String.valueOf(SUIDHisC);
            
            st.executeUpdate("UPDATE uzytkownik SET " +tabela.toLowerCase()+"='"+SUID+"' WHERE login = '"+login+"'");
            st.executeUpdate("INSERT INTO historia(Odbiorca, Nadawca, Przekaz, Tabela) values ('"+login+"', '"+ja+"', '"+SUIDHis+"', '"+tabela+"')");
            
            konta =wczytajKonta();
            resetujCheckBoxy();
        }
        else//ma przejmij
        {
            Statement st = con.createStatement();
            if(jaOb.getCzy_root() == 1)
            {

                String zero = "----";
                String suid = "SUID";
                
                for(KontaUzytkownikow k:konta)
                {

                    st.executeUpdate("UPDATE uzytkownik SET Film = '"+ zero + "', Placowka = '" + zero + "', Pracownik = '"+ zero+"', "
                            + "Recenzja = '" +zero +"' , Wypozyczenie = '"+ zero+ "', Przejmij = 0,Czy_root = " + 0+" WHERE login = '"+ k.getLogin() +"'");
                }
                //czyszcze cala historie
                st.executeUpdate( "DELETE FROM historia");
                
                //nadaje uprawnienia nowemu rootowi
                st.executeUpdate("UPDATE uzytkownik SET Film = '"+ suid + "', Placowka = '" + suid + "', Pracownik = '"+ suid+"', "
                            + "Recenzja = '" +suid +"' , Wypozyczenie = '"+ suid+ "',Przejmij = 1,  Czy_root = " + 1+" WHERE login = '"+ login+"'");
                
                konta=wczytajKonta();
                resetujCheckBoxy();
                
                if(konta.get(idWLiscie).getCzy_root() == 1)
                {
                    przejmijPane.setVisible(true);
                }
                else przejmijPane.setVisible(false); 
            }
            else
            {
                String film = jaOb.Film.getSUID();
                String placowka = jaOb.Placowka.getSUID();
                String pracownik = jaOb.Pracownik.getSUID();
                String recenzja = jaOb.Recenzja.getSUID();
                String wyp = jaOb.Wypozyczenie.getSUID();
                String zero = "----";
                
                List<String> pot = new ArrayList<String>();
                zwrocPotomkow(ja, pot);
                
                for(String s: pot)
                {
                    st.executeUpdate("UPDATE uzytkownik SET Film = '"+ zero + "', Placowka = '" + zero + "', Pracownik = '"+ zero+"', "
                            + "Recenzja = '" +zero +"' , Wypozyczenie = '"+ zero+ "', Przejmij = 0,Czy_root = " + 0+" WHERE login = '"+ s +"'");
                }
                st.executeUpdate("UPDATE uzytkownik SET Film = '"+ zero + "', Placowka = '" + zero + "', Pracownik = '"+ zero+"', "
                            + "Recenzja = '" +zero +"' , Wypozyczenie = '"+ zero+ "', Przejmij = 0,Czy_root = " + 0+" WHERE login = '"+ ja +"'");
                st.executeUpdate("UPDATE uzytkownik SET Film = '"+ film + "', Placowka = '" + placowka + "', Pracownik = '"+ pracownik+"', "
                            + "Recenzja = '" +recenzja +"' , Wypozyczenie = '"+ wyp+ "',Przejmij = 0,  Czy_root = " + 0+" WHERE login = '"+ login+"'");
                
                for(String s: pot)
                {
                    st.executeUpdate( "DELETE FROM historia WHERE Nadawca = '"+s+"'");
                    st.executeUpdate( "DELETE FROM historia WHERE Odbiorca = '"+s+"'");
                }
                st.executeUpdate( "DELETE FROM historia WHERE Odbiorca = '"+ja+"'");
                st.executeUpdate( "DELETE FROM historia WHERE Nadawca = '"+ja+"'");
                konta =wczytajKonta();
                resetujCheckBoxy();
            }
            
        }
        
    }
    
    @FXML
    void odswiezUzytkownikowPrzejmij(MouseEvent event) throws SQLException 
    {
        odswiezPESELE();
    }
}

