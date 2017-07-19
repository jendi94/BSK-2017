/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;

import java.net.URL;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 *
 * @author Jendi94
 */
public class RecenzjaController implements Initializable{
    
    Connection con;
    private Integer zalogowanyUzytkownik;
    private Integer idWLiscie;
    private List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
    
    @FXML
    private TableView tableVIEW;
    @FXML
    private TextField dodajOcena;
    @FXML
    private TextField dodajAutor;
    @FXML
    private TextField edytujOcena;
    @FXML
    private TextField edytujAutor;
    @FXML
    private ChoiceBox<String> usunId;
    @FXML
    private ChoiceBox<Integer> edytujId;
    @FXML 
    private ChoiceBox<String> dodajFK;
    @FXML
    private Button dodajZapisz;
    @FXML
    private Button edytujZapisz;
    @FXML
    private Button usunZapisz;
    @FXML
    private TextField dodajData;
    @FXML
    private TextField edytujData;
    @FXML
    private Label log;
    
    @FXML
    private Label nazwaRec;

    @FXML
    private TextField dodajNazwa;

    @FXML
    private TextField edytujNazwa;

    List <Recenzja> recenzje;
    List <Integer> indeksy;
    List <String> nazwyRecenzji = new ArrayList<String>();
    List <Film> filmy = new ArrayList<Film>();
    List <String> nazwyFilm = new ArrayList<String>();
    private ObservableList<ObservableList> data;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            recenzje = new ArrayList<Recenzja>();
            indeksy =new ArrayList<Integer>();
            
            con = DriverManager.getConnection(
                         "jdbc:mysql://localhost/wypozyczalnia",
                         "root",
                         "vertrigo");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM recenzja");
            while ( rs.next() )
               {
                    Recenzja obj = new Recenzja();
                    obj.setId( rs.getInt( 1 ) );
                    obj.setNazwa(rs.getString(2));
                    obj.setAutor( rs.getString( 3 ) );
                    obj.setOcena( rs.getInt( 4 ) );
                    obj.setData(rs.getString(5));
                    obj.setFK(rs.getInt(6));
                    recenzje.add( obj);     
                    indeksy.add(obj.getId());
                    nazwyRecenzji.add(obj.getNazwa());
              }
            
            rs = stmt.executeQuery("SELECT * FROM film");
            while(rs.next())
            {
                Film obj = new Film();
                obj.setId_fil(rs.getInt(1));
                obj.setTytul(rs.getString(2));
                obj.setGatunek(rs.getString(3));
                obj.setRezyser(rs.getString(4));
                obj.setRok(rs.getString(5));
                obj.setFk_plac(rs.getInt(6));
                
                filmy.add(obj);
                nazwyFilm.add(obj.getTytul());
            }
            ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
            ObservableList<String> id2 = FXCollections.observableArrayList(nazwyFilm);
            ObservableList<String> nR = FXCollections.observableArrayList(nazwyRecenzji);
            dodajFK.setItems(id2);
            edytujId.setItems(id);
            usunId.setItems(nR);     

            
        } catch (SQLException ex) {
            Logger.getLogger(RecenzjaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @FXML
    private void dodaj(MouseEvent event) throws SQLException, ParseException {
        String autor = dodajAutor.getText();
        String nazwa = dodajNazwa.getText();
        double ocena = Double.parseDouble(dodajOcena.getText());
        String data = dodajData.getText();
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        Date p = d.parse(data);
        Timestamp dataFinal = new Timestamp(p.getTime()); 
        int FK = 0;
        for(int i = 0; i < filmy.size();i++)
        {
            if(filmy.get(i).getTytul() == dodajFK.getValue())
            {
                FK = filmy.get(i).getId_fil();
                break;
            }
        }
        Statement st = con.createStatement();
        st.executeUpdate("INSERT INTO recenzja(Nazwa_rec, Autor, Ocena, Data_wykonania, FK_Film) Values (" + "'" + nazwa + "'" + "," + "'" + autor + "'" + "," + ocena + "," + "'"+dataFinal+"'"+ ","+ FK+ ")");
        ini2();
    }

    @FXML
    private void edytuj(MouseEvent event) throws ParseException, SQLException {
        int indeks = edytujId.getValue();
        String autor = null;
        String nazwa = null;
        double ocena = 0;
        String data = null;
        int indeksWTablicy=0;
        for( indeksWTablicy = 0; indeksWTablicy < recenzje.size(); indeksWTablicy++)
        {
            if(recenzje.get(indeksWTablicy).getId()== indeks)
            {
                autor = recenzje.get(indeksWTablicy).getAutor();
                nazwa = recenzje.get(indeksWTablicy).getNazwa();
                ocena =  recenzje.get(indeksWTablicy).getOcena();
                data = recenzje.get(indeksWTablicy).getData();
                break;
            }
        }
        if(!edytujNazwa.getText().isEmpty())
        {
            nazwa=edytujNazwa.getText();
        }
        if(!edytujAutor.getText().isEmpty())
        {
            autor=edytujAutor.getText();
        }
        if(!edytujOcena.getText().isEmpty())
        {
            ocena=Double.parseDouble(edytujOcena.getText());;
        }
        if(!edytujData.getText().isEmpty())
        {
            
            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            Date p = d.parse(data);
            Timestamp dataFinal = new Timestamp(p.getTime()); 
        }
        
        Statement st = con.createStatement();
        st.executeUpdate( "UPDATE recenzja SET Nazwa_rec = '" + nazwa + "' ,Autor ='" + autor + "' , Ocena = "+ocena + ", Data_wykonania = "+"'"+data+"'"+" WHERE id_rec =" + indeks );
        ini2();
    }

    @FXML
    private void usun(MouseEvent event) throws SQLException {
        String indeks = usunId.getValue();
        
        Statement st = con.createStatement();
        st.executeUpdate( "DELETE FROM recenzja WHERE Nazwa_rec = '" + indeks + "'");
        ini2();
    }
    
    
public void ini2()
{
            try {
        // TODO
        recenzje = new ArrayList<Recenzja>();
        indeksy =new ArrayList<Integer>();

        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM recenzja");
        while ( rs.next() )
           {
                   Recenzja obj = new Recenzja();
                obj.setId( rs.getInt( 1 ) );
                obj.setNazwa(rs.getString(2));
                obj.setAutor( rs.getString( 3 ) );
                obj.setOcena( rs.getInt( 4 ) );
                obj.setData(rs.getString(5));
                obj.setFK(rs.getInt(6));
                recenzje.add( obj);     
                indeksy.add(obj.getId());
                nazwyRecenzji.add(obj.getNazwa());
          }
        ObservableList <Integer> id = FXCollections.observableArrayList(indeksy);
        ObservableList<String> nR = FXCollections.observableArrayList(nazwyRecenzji);
        edytujId.setItems(id);
        usunId.setItems(nR);

        if(konta.get(idWLiscie).Recenzja.getSelect()=='s' || konta.get(idWLiscie).Recenzja.getSelect()=='S')
        {
            data = FXCollections.observableArrayList();
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM recenzja");
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
        this.con = con;
        this.idWLiscie = idwLiscie;
        this.zalogowanyUzytkownik= id;
        this.konta = konta;
    }
    public void ustawUprawnienia() throws SQLException
    {
         //dodaj!!!
            if(konta.get(idWLiscie).Recenzja.getInsert() != 'i' && konta.get(idWLiscie).Recenzja.getInsert() != 'I')
            {
                dodajNazwa.setDisable(true);
                dodajAutor.setDisable(true);
                dodajOcena.setDisable(true);
                dodajData.setDisable(true);
                dodajFK.setDisable(true);
                dodajZapisz.setDisable(true);
            }
            //edytuj!!!
            if(konta.get(idWLiscie).Recenzja.getUpdate() != 'u' && konta.get(idWLiscie).Recenzja.getUpdate() != 'U')
            {
                edytujNazwa.setDisable(true);
                edytujId.setDisable(true);
                edytujAutor.setDisable(true);
                edytujOcena.setDisable(true);
                edytujData.setDisable(true);
                edytujZapisz.setDisable(true);
            }
           //usun~!!!
           if(konta.get(idWLiscie).Recenzja.getDelete() != 'd' && konta.get(idWLiscie).Recenzja.getDelete() != 'D')
            {
                usunId.setDisable(true);
                usunZapisz.setDisable(true);
            }    
               ///ODCZYT 
            if(konta.get(idWLiscie).Recenzja.getSelect() != 's' && konta.get(idWLiscie).Recenzja.getSelect() != 'S')
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
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM recenzja");
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
