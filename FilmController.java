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

/**
 * FXML Controller class
 *
 * @author Jendi94
 */
public class FilmController implements Initializable {

    Connection con;
    private Integer zalogowanyUzytkownik;
    private Integer idWLiscie;
    private List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
    
    @FXML
    private TableView tableVIEW;
    @FXML
    private TextField dodajGatunek;
    @FXML
    private TextField dodajTytul;
    @FXML
    private TextField edytujGatunek;
    @FXML
    private TextField edytujTytul;
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
    private TextField dodajRezyser;
    @FXML
    private TextField edytujRezyser;
    @FXML
    private ChoiceBox<String> dodajFK;
    @FXML
    private TextField dodajRok;
    @FXML
    private TextField edytujRok;
    @FXML
    private Label log;

    List<Film> filmy;
    List<Integer> indeksy;
    List<Placowka> placowki = new ArrayList<Placowka>();
    List<String> nazwyPlacowek = new ArrayList<String>();
    List<String> nazwyFilmow = new ArrayList<String>();
    private ObservableList<ObservableList> data;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                    try {
            // TODO
            filmy = new ArrayList<Film>();
            indeksy =new ArrayList<Integer>();
            
            con = DriverManager.getConnection(
                         "jdbc:mysql://localhost/wypozyczalnia",
                         "root",
                         "vertrigo");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM film");
            while ( rs.next() )
               {
                    Film obj = new Film();
                    obj.setId_fil(rs.getInt( 1 ) );
                    obj.setTytul(rs.getString( 2 ) );
                    obj.setGatunek(rs.getString( 3 ) );
                    obj.setRezyser(rs.getString(4));
                    obj.setRok(rs.getString(5));
                    obj.setFk_plac(rs.getInt(6));
                    filmy.add( obj);     
                    indeksy.add(obj.getId_fil());
                    nazwyFilmow.add(obj.getTytul());
              }
            
            rs = stmt.executeQuery("SELECT * FROM placowka");
            while(rs.next())
            {
                Placowka obj = new Placowka();
                    obj.setId( rs.getInt( 1 ) );
                    obj.setAdres( rs.getString( 2 ) );
                    obj.setKod( rs.getString( 3 ) );
                
                    placowki.add(obj);
                    nazwyPlacowek.add(obj.getAdres());
            }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            ObservableList<String> id2 = FXCollections.observableArrayList(nazwyPlacowek);
            ObservableList<String> nF = FXCollections.observableArrayList(nazwyFilmow);
            dodajFK.setItems(id2);
            edytujId.setItems(id);
            usunId.setItems(nF);     

        } catch (SQLException ex) {
            Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  

    @FXML
    private void dodaj(MouseEvent event) throws SQLException {
        String tyt = dodajTytul.getText();
        String gat = dodajGatunek.getText();
        String rez = dodajRezyser.getText();
        int rok = Integer.parseInt(dodajRok.getText());
        int FK = 0;
        for(int i = 0; i < placowki.size(); i++)
        {
            if(placowki.get(i).getAdres() == dodajFK.getValue())
            {
                FK = placowki.get(i).getId();
                break;
            }
        }
        if(!tyt.isEmpty() || !gat.isEmpty() || !rez.isEmpty())
        {
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO film(Tytul, Gatunek, Rezyser, Rok, FK_Plac) Values (" + "'" + tyt + "'" + "," +"'"+ gat + "'," + "'"+rez+"'"+ ","+rok+","+ FK+ ")");
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
        String tytul = null;
        String gatunek = null;
        String rezyser = null;
        int rok = 0;

        if(!edytujTytul.getText().isEmpty())
        {
            tytul = edytujTytul.getText();
        }
        if(!edytujGatunek.getText().isEmpty())
        {
            gatunek = edytujGatunek.getText();
        }
        if(!edytujRezyser.getText().isEmpty())
        {
            rezyser = edytujRezyser.getText();
        }
        if(!edytujRok.getText().isEmpty())
        {
            rok = Integer.parseInt(edytujRok.getText());
        }
        
        
        
        Statement st = con.createStatement();
        st.executeUpdate( "UPDATE film SET Tytul ='" + tytul + "' , Gatunek = '"+gatunek + "', Rezyser = '"+rezyser+"',"+rok+" WHERE id_fil =" + indeks );
        ini2();
    }

    @FXML
    private void usun(MouseEvent event) throws SQLException {
        String indeks = usunId.getValue();
        
        Statement st = con.createStatement();
        st.executeUpdate( "DELETE FROM film WHERE tytul= '" + indeks + "'");
        ini2();
    }
    
    public void ini2()
    {
        try {
            // TODO
            filmy = new ArrayList<Film>();
            indeksy =new ArrayList<Integer>();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM film");
            while ( rs.next() )
               {
                    Film obj = new Film();
                    obj.setId_fil(rs.getInt( 1 ) );
                    obj.setTytul(rs.getString( 2 ) );
                    obj.setGatunek(rs.getString( 3 ) );
                    obj.setRezyser(rs.getString(4));
                    obj.setRok(rs.getString(5));
                    obj.setFk_plac(rs.getInt(6));
                    filmy.add( obj);     
                    indeksy.add(obj.getId_fil());
                    nazwyFilmow.add(obj.getTytul());
              }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            ObservableList<String> nF = FXCollections.observableArrayList(nazwyFilmow);
            edytujId.setItems(id);
            usunId.setItems(nF);

            if(konta.get(idWLiscie).Film.getSelect() == 's' || konta.get(idWLiscie).Film.getSelect() == 'S')
            {
                data = FXCollections.observableArrayList();
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT * FROM film");
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
    public void setParam(Integer id, Integer idwLiscie, List<KontaUzytkownikow> konta, Connection con)
    {
        this.idWLiscie = idwLiscie;
        this.zalogowanyUzytkownik= id;
        this.konta = konta;
    }
    public void ustawUprawnienia() throws SQLException
    {
         //dodaj!!!
            if(konta.get(idWLiscie).Film.getInsert() != 'i' && konta.get(idWLiscie).Film.getInsert() != 'I')
            {
                dodajTytul.setDisable(true);
                dodajGatunek.setDisable(true);
                dodajRezyser.setDisable(true);
                dodajRok.setDisable(true);
                dodajFK.setDisable(true);
                dodajZapisz.setDisable(true);
            }
            //edytuj!!!
            if(konta.get(idWLiscie).Film.getUpdate() != 'u' && konta.get(idWLiscie).Film.getUpdate() != 'U')
            {
                edytujId.setDisable(true);
                edytujTytul.setDisable(true);
                edytujGatunek.setDisable(true);
                edytujRezyser.setDisable(true);
                edytujRok.setDisable(true);
                edytujZapisz.setDisable(true);
            }
           //usun~!!!
           if(konta.get(idWLiscie).Film.getDelete() != 'd' && konta.get(idWLiscie).Film.getDelete() != 'D')
            {
                usunId.setDisable(true);
                usunZapisz.setDisable(true);
            }    
               ///ODCZYT 
            if(konta.get(idWLiscie).Film.getSelect() != 's' && konta.get(idWLiscie).Film.getSelect() != 'S')
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
        ResultSet rs2 = stmt2.executeQuery("SELECT * FROM film");
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
