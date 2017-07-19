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
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;


public class PlacowkaController implements Initializable {

    Connection con;
    private Integer zalogowanyUzytkownik;
    private Integer idWLiscie;
    private List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
    
    @FXML
    private TableView tableVIEW;
    private ObservableList<ObservableList> data;

    @FXML
    private TextField dodajKod;
    @FXML
    private TextField dodajAdres;
    @FXML
    private TextField edytujKod;
    @FXML
    private TextField edytujAdres;
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
    private Label log;

    List <Placowka> placowki;
    List <Integer> indeksy;
    List <String> nazwyPlacowek = new ArrayList<String>();
    
      
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize( URL url, ResourceBundle rb) {
                    try {
            // TODO
            
            placowki = new ArrayList<Placowka>();
            indeksy =new ArrayList<Integer>();
            con = DriverManager.getConnection(
                         "jdbc:mysql://localhost/wypozyczalnia",
                         "root",
                         "vertrigo");
           
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM placowka");
            while ( rs.next() )
               {
                    Placowka obj = new Placowka();
                    obj.setId( rs.getInt( 1 ) );
                    obj.setAdres( rs.getString( 2 ) );
                    obj.setKod( rs.getString( 3 ) );
                    placowki.add( obj);     
                    indeksy.add(obj.getId());
                    nazwyPlacowek.add(obj.getAdres());
              }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            ObservableList <String> nP = FXCollections.observableArrayList(nazwyPlacowek);
            edytujId.setItems(id);
            usunId.setItems(nP);
           
                    
        } catch (SQLException ex) {
            Logger.getLogger(PlacowkaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void dodaj(MouseEvent event) throws SQLException {
        String adres = dodajAdres.getText();
        String kod = dodajKod.getText();
        if(!adres.isEmpty() && !kod.isEmpty())
        {
            Statement st = con.createStatement();
            st.executeUpdate( "INSERT INTO placowka (Adres, Kod_pocztowy) Values("+ "'"+ adres + "'"+"," + "'"+ kod + "'"+" )");
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
        Integer indeks = edytujId.getValue();
        String adres =null;
        String kod= null;
        
        int indeksWTablicy=0;
        for( indeksWTablicy = 0; indeksWTablicy < placowki.size();indeksWTablicy++)
        {
            if(placowki.get(indeksWTablicy).getId()== indeks)
            {
                adres = placowki.get(indeksWTablicy).getAdres();
                kod =  placowki.get(indeksWTablicy).getKod();
                break;
            }
        }
        
        if(!edytujAdres.getText().isEmpty())
        {
             adres = edytujAdres.getText();
        }
        if(!edytujKod.getText().isEmpty())
        {
             kod = edytujKod.getText();
        }
        Statement st = con.createStatement();
        st.executeUpdate( "UPDATE placowka SET Adres ='" + adres + "' , Kod_pocztowy = '"+kod + "' WHERE id_plac =" + indeks );
        ini2();
    }

    @FXML
    private void usun(MouseEvent event) throws SQLException {
        String indeks = usunId.getValue();
        
        Statement st = con.createStatement();
        st.executeUpdate( "DELETE FROM placowka WHERE Adres= '" + indeks + "'");
        ini2();     
    }
       
    public void ini2()
    {
            try {
            // TODO
            placowki = new ArrayList<Placowka>();
            indeksy =new ArrayList<Integer>();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM placowka");
            while ( rs.next() )
               {
                    Placowka obj = new Placowka();
                    obj.setId( rs.getInt( 1 ) );
                    obj.setAdres( rs.getString( 2 ) );
                    obj.setKod( rs.getString( 3 ) );
                    placowki.add( obj);     
                    indeksy.add(obj.getId());
              }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            ObservableList <String> nP = FXCollections.observableArrayList(nazwyPlacowek);
            edytujId.setItems(id);
            usunId.setItems(nP);

            if(konta.get(idWLiscie).Placowka.getSelect() == 's' || konta.get(idWLiscie).Placowka.getSelect() == 'S')
            {
                data = FXCollections.observableArrayList();
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT * FROM placowka");
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
        this.idWLiscie = idwLiscie;
        this.zalogowanyUzytkownik= id;
        this.konta = konta;
        this.con = con;
    }
    public void ustawUprawnienia() throws SQLException
    {
         //dodaj!!!
            if(konta.get(idWLiscie).Placowka.getInsert() != 'i' && konta.get(idWLiscie).Placowka.getInsert() != 'I')
            {
                dodajKod.setDisable(true);
                dodajAdres.setDisable(true);
                dodajZapisz.setDisable(true);
            }
            //edytuj!!!
            if(konta.get(idWLiscie).Placowka.getUpdate() != 'u' && konta.get(idWLiscie).Placowka.getUpdate() != 'U')
            {
                edytujId.setDisable(true);
                edytujKod.setDisable(true);
                edytujAdres.setDisable(true);
                edytujZapisz.setDisable(true);
            }
           //usun~!!!
           if(konta.get(idWLiscie).Placowka.getDelete() != 'd' && konta.get(idWLiscie).Placowka.getDelete() != 'D')
            {
                usunId.setDisable(true);
                usunZapisz.setDisable(true);
            }    
               ///ODCZYT 
            if(konta.get(idWLiscie).Placowka.getSelect() != 's' && konta.get(idWLiscie).Placowka.getSelect() != 'S')
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
        ResultSet rs2 = stmt2.executeQuery("SELECT * FROM placowka");
        for(int i=0 ; i<rs2.getMetaData().getColumnCount(); i++)
        {
            //We are using non property style for making dynamic table
            final int j = i;               
            TableColumn col = new TableColumn(rs2.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   

                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
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
