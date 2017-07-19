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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class WypozyczenieController implements Initializable {

    Connection con;
       private Integer zalogowanyUzytkownik;
    private Integer idWLiscie;
    private List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
    
    @FXML
    private TableView tableVIEW;
    @FXML
    private TextField dodajZwr;
    @FXML
    private TextField dodajWyp;
    @FXML
    private TextField edytujZwr;
    @FXML
    private TextField edytujWyp;
    @FXML
    private ChoiceBox<Integer> usunId;
    @FXML
    private ChoiceBox<Integer> edytujId;
    @FXML
    private Button dodajZapisz;
    @FXML
    private Button edytujZapisz;
    @FXML
    private Button usunZapisz;
    @FXML
    private TextField dodajCena;
    @FXML
    private TextField edytujCena;
    @FXML
    private ChoiceBox<String> dodajFK;
    @FXML
    private TextField dodajKara;
    @FXML
    private TextField edytujKara;
    @FXML
    private Label log;

    List<Wypozyczenie> wypozyczenia;
    List<Integer> indeksy;
    List<Film> filmy = new ArrayList<Film>();
    List<String> nazwyFilmow = new ArrayList<String>();
    private ObservableList<ObservableList> data;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                    try {
            // TODO
            wypozyczenia = new ArrayList<Wypozyczenie>();
            indeksy =new ArrayList<Integer>();
            
            con = DriverManager.getConnection(
                         "jdbc:mysql://localhost/wypozyczalnia",
                         "root",
                         "vertrigo");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM wypozyczenie");
            while ( rs.next() )
               {
                    Wypozyczenie obj = new Wypozyczenie();
                    obj.setId_wyp(rs.getInt( 1 ) );
                    obj.setData_wyp(rs.getString( 2 ) );
                    obj.setData_zwr(rs.getString( 3 ) );
                    obj.setCena(rs.getInt(4));
                    obj.setKara(rs.getInt(5));
                    obj.setFK(rs.getInt(6));
                    wypozyczenia.add( obj);     
                    indeksy.add(obj.getId_wyp());
              }
            
            rs = stmt.executeQuery("SELECT * FROM film");
            while(rs.next())
            {
                Film obj = new Film();
                    obj.setId_fil(rs.getInt( 1 ) );
                    obj.setTytul(rs.getString( 2 ) );
                    obj.setGatunek(rs.getString( 3 ) );
                    obj.setRezyser(rs.getString(4));
                    obj.setRok(rs.getString(5));
                    obj.setFk_plac(rs.getInt(6));
                
                    filmy.add(obj);
                    nazwyFilmow.add(obj.getTytul());
            }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            ObservableList<String> id2 = FXCollections.observableArrayList(nazwyFilmow);
            dodajFK.setItems(id2);
            edytujId.setItems(id);
            usunId.setItems(id);     

           
        } catch (SQLException ex) {
            Logger.getLogger(RecenzjaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void dodaj(MouseEvent event) throws ParseException, SQLException {
        String wyp = dodajWyp.getText();
        String zwr = dodajZwr.getText();
        int cena = Integer.parseInt(dodajCena.getText());
        int kara = Integer.parseInt(dodajKara.getText());
        
        SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
        Date p = data.parse(wyp);
        Timestamp dataFinal = new Timestamp(p.getTime());
        
        p = data.parse(zwr);
        Timestamp dataFinal2 = new Timestamp(p.getTime());
        
        int FK =0;
        for(int i=0; i< filmy.size();i++)
        {
            if(filmy.get(i).getTytul() == dodajFK.getValue())
            {
                FK = filmy.get(i).getId_fil();
                break;
            }
        }
        if(!wyp.isEmpty() &&  !zwr.isEmpty())
        {
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO wypozyczenie(Data_wypozyczenia, "
                + "Data_zwrotu, Cena_dzien, "
                + "Kara_dzien, FK_Film) Values ("
                + "'"+dataFinal + "','" + dataFinal2+"',"
                + cena + "," + kara + ","+FK+")");
            log.setText("Dodano rekord");
            ini2();
        }
        else
        {
            log.setText("Nie udało się dodać rekordu");
        }  
       
    }

    @FXML
    private void edytuj(MouseEvent event) throws ParseException, SQLException {
        int indeks = edytujId.getValue();
        String wyp = null;
        String zwr = null;
        int cena = 0;
        int kara = 0;
         Timestamp dataFinal = null;
         Timestamp dataFinal2 = null;
        
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        
        int indeksWTablicy=0;
        for( indeksWTablicy = 0; indeksWTablicy < wypozyczenia.size();indeksWTablicy++)
        {
            if(wypozyczenia.get(indeksWTablicy).getId_wyp()== indeks)
            {
                wyp = wypozyczenia.get(indeksWTablicy).getData_wyp();
                Date p = d.parse(wyp);
                dataFinal = new Timestamp(p.getTime()); 
                zwr =  wypozyczenia.get(indeksWTablicy).getData_zwr();
                Date p2 = d.parse(zwr);
                dataFinal2 = new Timestamp(p2.getTime()); 
                cena = wypozyczenia.get(indeksWTablicy).getCena();
                kara = wypozyczenia.get(indeksWTablicy).getKara();
                break;
            }
        }
        if(!edytujWyp.getText().isEmpty())
        {
            wyp = edytujWyp.getText();
            Date p = d.parse(wyp);
            dataFinal = new Timestamp(p.getTime()); 
        }
        if(!edytujZwr.getText().isEmpty())
        {
          zwr = edytujZwr.getText();
          Date p = d.parse(zwr);
          dataFinal2 = new Timestamp(p.getTime()); 
        }
        if(!edytujCena.getText().isEmpty())
        {
            cena = Integer.parseInt(edytujCena.getText());
        }
        if(!edytujKara.getText().isEmpty())
        {
            kara = Integer.parseInt(edytujKara.getText());
        }
        
        Statement st = con.createStatement();
        st.executeUpdate( "UPDATE wypozyczenie SET Data_wypozyczenia ='" + dataFinal + "' , Data_zwrotu = '"+dataFinal2 + "', Cena_dzien = "+cena+","+ "Kara_dzien = "+kara+" WHERE id_wyp =" + indeks );
        ini2();
    }

    @FXML
    private void usun(MouseEvent event) throws SQLException {
        try{
            int indeks = usunId.getValue();
        
            Statement st = con.createStatement();
            st.executeUpdate( "DELETE FROM wypozyczenie WHERE id_wyp=" + indeks);
            ini2();
        }
        catch(Exception e){}
        
    }

    public void ini2()
    {
        try {
            // TODO
            wypozyczenia = new ArrayList<Wypozyczenie>();
            indeksy =new ArrayList<Integer>();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM wypozyczenie");
            while ( rs.next() )
               {
                    Wypozyczenie obj = new Wypozyczenie();
                    obj.setId_wyp(rs.getInt( 1 ) );
                    obj.setData_wyp(rs.getString( 2 ) );
                    obj.setData_zwr(rs.getString( 3 ) );
                    obj.setCena(rs.getInt(4));
                    obj.setKara(rs.getInt(5));
                    obj.setFK(rs.getInt(6));
                    wypozyczenia.add( obj);     
                    indeksy.add(obj.getId_wyp());
              }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            edytujId.setItems(id);
            usunId.setItems(id);
                
            if(konta.get(idWLiscie).Wypozyczenie.getSelect() == 's' || konta.get(idWLiscie).Wypozyczenie.getSelect()=='S')
            {
                data = FXCollections.observableArrayList();
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT * FROM wypozyczenie");
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
        this.con =con;
        this.idWLiscie = idwLiscie;
        this.zalogowanyUzytkownik= id;
        this.konta = konta;
    }
    
     public void ustawUprawnienia() throws SQLException
    {
         //dodaj!!!
            if(konta.get(idWLiscie).Wypozyczenie.getInsert() != 'i' && konta.get(idWLiscie).Wypozyczenie.getInsert()!='I')
            {
                dodajWyp.setDisable(true);
                dodajZwr.setDisable(true);
                dodajCena.setDisable(true);
                dodajKara.setDisable(true);
                dodajZapisz.setDisable(true);
                dodajFK.setDisable(true);
            }
            //edytuj!!!
            if(konta.get(idWLiscie).Wypozyczenie.getUpdate() != 'u' && konta.get(idWLiscie).Wypozyczenie.getUpdate() != 'U')
            {
                edytujId.setDisable(true);
                edytujWyp.setDisable(true);
                edytujZwr.setDisable(true);
                edytujCena.setDisable(true);
                edytujKara.setDisable(true);
                edytujZapisz.setDisable(true);
            }
           //usun~!!!
           if(konta.get(idWLiscie).Wypozyczenie.getDelete() != 'd' && konta.get(idWLiscie).Wypozyczenie.getDelete() != 'D')
            {
                usunId.setDisable(true);
                usunZapisz.setDisable(true);
            }    
               ///ODCZYT 
            if(konta.get(idWLiscie).Wypozyczenie.getSelect() != 's' && konta.get(idWLiscie).Wypozyczenie.getSelect() != 'S')
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
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM wypozyczenie");
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
