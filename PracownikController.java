/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;


public class PracownikController implements Initializable {

    Connection con;
    private Integer zalogowanyUzytkownik;
    private Integer idWLiscie;
    private List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
    
    @FXML
    private TableView tableVIEW;
    private ObservableList<ObservableList> data;
    @FXML
    private Label imie;
    @FXML
    private Label nazwisko;
    @FXML
    private Label id_prac;
    @FXML
    private TextField dodajNazwisko;
    @FXML
    private TextField dodajImie;
    @FXML
    private TextField edytujNazwisko;
    @FXML
    private TextField edytujImie;
    @FXML
    private ChoiceBox<String> usunId;
    @FXML
    private ChoiceBox<Integer> edytujId;
    @FXML
    private Button dodajZapisz;
    @FXML
    private Button edytujZapisz;
    @FXML
    private Button usunZapisz;
    @FXML
    private Label pesel;
    @FXML
    private TextField dodajPesel;
    @FXML
    private TextField edytujPesel;
    @FXML
    private TextField dodajStanowisko;
    @FXML
    private TextField edytujStanowisko;
    @FXML
    private ChoiceBox<String> dodajFK_plac;

    @FXML
    private ChoiceBox<String> edytujFK_plac;
     @FXML
    private Label log;
    
    
    List <Pracownik> pracownicy;
    List <Integer> indeksy;
    List <Placowka> placowki;
    List <String> adresyPlac;
    List <String> peselePracownikow = new ArrayList<String>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            // TODO
            
            pracownicy = new ArrayList<Pracownik>();
            indeksy =new ArrayList<Integer>();
            placowki = new ArrayList<Placowka>();
            adresyPlac = new ArrayList<String>();
            con = DriverManager.getConnection(
                         "jdbc:mysql://localhost/wypozyczalnia",
                         "root",
                         "vertrigo");
           
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM pracownik");
            while ( rs.next() )
               {
                    Pracownik obj = new Pracownik();
                    obj.setId_prac(rs.getInt( 1 ) );
                    obj.setImie( rs.getString( 2 ) );
                    obj.setNazwisko( rs.getString( 3 ) );
                    obj.setPESEL(rs.getString(4));
                    obj.setStanowisko(rs.getString(5));
                    obj.setFK_Plac(rs.getInt(6));
                    pracownicy.add( obj);     
                    indeksy.add(obj.getId_prac());
                    peselePracownikow.add(obj.getPESEL());
              }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            ObservableList <String> nPr = FXCollections.observableArrayList(peselePracownikow);
            edytujId.setItems(id);
            usunId.setItems(nPr);

            rs = stmt.executeQuery("SELECT * FROM placowka");
            while ( rs.next() )
               {
                    Placowka obj = new Placowka();
                    obj.setId( rs.getInt( 1 ) );
                    obj.setAdres( rs.getString( 2 ) );
                    obj.setKod( rs.getString( 3 ) );
                    placowki.add(obj);     
                    adresyPlac.add(obj.getAdres());
              }
            ObservableList <String> id2 = FXCollections.observableArrayList(adresyPlac);
            dodajFK_plac.setItems(id2);
            edytujFK_plac.setItems(id2);
            
        } catch (SQLException ex) {
            Logger.getLogger(PlacowkaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void dodaj(MouseEvent event) throws SQLException {
        String imie = dodajImie.getText();
        String nazwisko = dodajNazwisko.getText();
        String pesel = dodajPesel.getText();
        String stanowisko = dodajStanowisko.getText();
        int FK =0;
        for(int i = 0; i < placowki.size();i++)
        {
            if(placowki.get(i).getAdres() == dodajFK_plac.getValue())
            {
                FK = placowki.get(i).getId();
                break;
            }
        }
        if(!imie.isEmpty() && !nazwisko.isEmpty() && !pesel.isEmpty() && !stanowisko.isEmpty())
        {
            Statement st = con.createStatement();
            st.executeUpdate( "INSERT INTO pracownik (Imie, Nazwisko, Pesel, Stanowisko, FK_plac) Values("+ "'"+ imie + "'"+"," + "'"+ nazwisko + "'"+","+"'" + pesel + "'"+ ","+"'" + stanowisko +"'" +","+ FK+")");
            log.setText("Dodano rekord");
            ini2();
        }
        else
        {
            log.setText("Nie udało się dodać rekordu");
        }  
    }

    @FXML
    private void edytuj(MouseEvent event) throws SQLException {
        int indeks = edytujId.getValue();
        String imie = null;
        String nazwisko = null;
        String pesel = null;
        String stanowisko = null;
        int FK =0;
        for(int i = 0; i < placowki.size();i++)
        {
            if(placowki.get(i).getAdres() == edytujFK_plac.getValue())
            {
                FK = placowki.get(i).getId();
                break;
            }
        }
        int indeksWTablicy=0;
        for( indeksWTablicy = 0; indeksWTablicy < pracownicy.size();indeksWTablicy++)
        {
            if(pracownicy.get(indeksWTablicy).getId_prac()== indeks)
            {
                imie = pracownicy.get(indeksWTablicy).getImie();
                nazwisko =  pracownicy.get(indeksWTablicy).getNazwisko();
                pesel =  pracownicy.get(indeksWTablicy).getPESEL();
                stanowisko =  pracownicy.get(indeksWTablicy).getStanowisko();
                break;
            }
        }
        if(!edytujImie.getText().isEmpty())
        {
            imie = edytujImie.getText();
        }
        if(!edytujNazwisko.getText().isEmpty())
        {
            nazwisko = edytujNazwisko.getText();
        }
        if(!edytujPesel.getText().isEmpty())
        {
            pesel = edytujPesel.getText();
        }
        if(!edytujStanowisko.getText().isEmpty())
        {
            stanowisko = edytujStanowisko.getText();
        }
        Statement st = con.createStatement();
        st.executeUpdate( "UPDATE pracownik SET Imie ='" + imie + "' , Nazwisko = '"+nazwisko + "' , PESEL = '"+pesel +"' , Stanowisko = '"+stanowisko + "' , FK_plac = "+FK +" WHERE id_prac =" + indeks );
        ini2();
    }

    @FXML
    private void usun(MouseEvent event) throws SQLException {
        String indeks = usunId.getValue();
        
        Statement st = con.createStatement();
        st.executeUpdate( "DELETE FROM pracownik WHERE pesel= ' " + indeks + "'");
        ini2(); 
    }
    
    public void ini2()
    {
            try {
            // TODO
            pracownicy = new ArrayList<Pracownik>();
            indeksy =new ArrayList<Integer>();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM pracownik");
            while ( rs.next() )
               {
                    Pracownik obj = new Pracownik();
                    obj.setId_prac(rs.getInt( 1 ) );
                    obj.setImie( rs.getString( 2 ) );
                    obj.setNazwisko( rs.getString( 3 ) );
                    obj.setPESEL(rs.getString(4));
                    obj.setStanowisko(rs.getString(5));
                    obj.setFK_Plac(rs.getInt(6));
                    pracownicy.add( obj);     
                    indeksy.add(obj.getId_prac());
                    peselePracownikow.add(obj.getPESEL());
              }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            ObservableList <String> nPr = FXCollections.observableArrayList(peselePracownikow);
            edytujId.setItems(id);
            usunId.setItems(nPr);

            if(konta.get(idWLiscie).Pracownik.getSelect()== 's' || konta.get(idWLiscie).Pracownik.getSelect() == 'S')
            {
                data = FXCollections.observableArrayList();
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Pracownik");
                for(int i=0 ; i<rs2.getMetaData().getColumnCount(); i++)
                {
                    while(rs2.next())
                    {
                    //Iterate Row
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for(int k=1 ; k<=rs2.getMetaData().getColumnCount(); k++){
                            //Iterate Column
                            row.add(rs2.getString(k));
                        }
                    data.add(row);
                    }
                tableVIEW.setItems(data);
               }

            }
        } catch (SQLException ex) {
            Logger.getLogger(PlacowkaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setParam(Integer id, Integer idwLiscie, List<KontaUzytkownikow> konta, Connection con )
    {
        this.con = con;
        this.idWLiscie = idwLiscie;
        this.zalogowanyUzytkownik= id;
        this.konta = konta;
    }
    public void ustawUprawnienia() throws SQLException
    {
         //dodaj!!!
            if(konta.get(idWLiscie).Pracownik.getInsert() != 'i' && konta.get(idWLiscie).Pracownik.getInsert() != 'I')
            {
                dodajImie.setDisable(true);
                dodajNazwisko.setDisable(true);
                dodajPesel.setDisable(true);
                dodajStanowisko.setDisable(true);
                dodajFK_plac.setDisable(true);
                dodajZapisz.setDisable(true);
            }
            //edytuj!!!
            if(konta.get(idWLiscie).Pracownik.getUpdate() != 'u' && konta.get(idWLiscie).Pracownik.getUpdate() != 'U')
            {
                edytujId.setDisable(true);
                edytujImie.setDisable(true);
                edytujNazwisko.setDisable(true);
                edytujPesel.setDisable(true);
                edytujStanowisko.setDisable(true);
                edytujFK_plac.setDisable(true);
                edytujZapisz.setDisable(true);
            }
           //usun~!!!
           if(konta.get(idWLiscie).Pracownik.getDelete() != 'd' && konta.get(idWLiscie).Pracownik.getDelete() != 'D')
            {
                usunId.setDisable(true);
                usunZapisz.setDisable(true);
            }    
               ///ODCZYT 
            if(konta.get(idWLiscie).Pracownik.getSelect() != 's' && konta.get(idWLiscie).Pracownik.getSelect() != 'S')
            {
                tableVIEW.setDisable(true);
            }
            else
            {
                wyswietlanie();
            }    
    }
        void wyswietlanie() throws SQLException
    {
        data = FXCollections.observableArrayList();
        Statement stmt2 = con.createStatement();
        ResultSet rs2 = stmt2.executeQuery("SELECT * FROM pracownik");
        for(int i=0 ; i<rs2.getMetaData().getColumnCount(); i++)
        {
            //We are using non property style for making dynamic table
            final int j = i;               
            TableColumn col = new TableColumn(rs2.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   

                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {                                                                                             
                    return new SimpleStringProperty(param.getValue().get(j).toString());                       
                }                   
            });
            tableVIEW.getColumns().addAll(col);
            while(rs2.next())
            {
            //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int k=1 ; k<=rs2.getMetaData().getColumnCount(); k++){
                    //Iterate Column
                    row.add(rs2.getString(k));
                }
            data.add(row);
            }
            tableVIEW.setItems(data);
        }
    }
        
    
    
}
