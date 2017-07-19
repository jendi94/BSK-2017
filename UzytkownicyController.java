package bskkk;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class UzytkownicyController {

    Connection con;
    private Integer zalogowanyUzytkownik;
    private Integer idWLiscie;
    private List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
    
    @FXML
    private TableView tableVIEW;

    @FXML
    private Button dodajZapisz;
    
    @FXML
    private AnchorPane uPane;

    @FXML
    private ChoiceBox<String> dodajPESEL;

    @FXML
    private Label log;



    @FXML
    private TextField dodajLogin;

    @FXML
    private PasswordField dodajHaslo2;

    @FXML
    private PasswordField dodajHaslo;
    
        @FXML
    private Label komunikatLabel;
    
    private List<Pracownik> pracownicyKtorychNieMaWBazie = new ArrayList<Pracownik>();
    private List<String> peseleKtorychNieMaWBazie = new ArrayList<String>();
    private ObservableList<ObservableList> data;

    
    public void initialize(URL url, ResourceBundle rb) throws SQLException {
       
    }
    
    public void setParam(Integer id, Integer idwLiscie, List<KontaUzytkownikow> konta, Connection con)
    {
        try {
            this.con = con;
            this.idWLiscie = idwLiscie;
            this.zalogowanyUzytkownik= id;
            this.konta = konta;
            
             con = DriverManager.getConnection(
                         "jdbc:mysql://localhost/wypozyczalnia",
                         "root",
                         "vertrigo");
             
            if(konta.get(idWLiscie).getCzy_root() == 1) uPane.setVisible(true);
            else uPane.setVisible(false);
            wyswietlanie();
            odswiezPesele();
           
            
        } catch (Exception ex) {
            Logger.getLogger(UzytkownicyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void odswiezPesele() throws SQLException
    {
        pracownicyKtorychNieMaWBazie.clear();
        peseleKtorychNieMaWBazie.clear();
        dodajPESEL.getItems().clear();
        
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
                        peseleKtorychNieMaWBazie.add(obj.getPESEL());
                    }  
            }
              dodajPESEL.setItems(FXCollections.observableArrayList(peseleKtorychNieMaWBazie)); 
    }
    
    @FXML
    void dodaj(MouseEvent event) {
            try{
                String pesel = dodajPESEL.getValue();
                Integer fk =0;
                for(Pracownik e: pracownicyKtorychNieMaWBazie)
                {
                    if(e.getPESEL() == pesel)
                    {
                        fk = e.getId_prac();
                        break;
                    }
                }
                String login = dodajLogin.getText();
                String haslo = getHash(dodajHaslo.getText(), "SHA-256");
                String haslo2 = getHash(dodajHaslo2.getText(), "SHA-256");
                if(!haslo.equals(haslo2))
                {
                    komunikatLabel.setText("Hasła nie są identyczne.");
                    return;
                }
                if(fk ==0)
                {
                    komunikatLabel.setText("Wybierz pesel.");
                    return;
                }
                String uprawnienia = "----";
                Statement st = con.createStatement();
                st.executeUpdate("INSERT INTO uzytkownik(FK_pracownik, Login, Haslo,Film, Placowka,  Pracownik,  Recenzja,  Wypozyczenie, Przejmij, Czy_root"
                    + ") Values (" + "'" + fk + "'" + "," +"'"+ login + "'," + "'"+haslo+"'"+ ",'"+uprawnienia+"','"+ uprawnienia+ "','"+ uprawnienia+"','"+ uprawnienia+ "','"+
                    uprawnienia+"',"+0 +"," +0+")");
                
                ResultSet rs = st.executeQuery( "select * from uzytkownik WHERE login = '" +login + "'");
                ResultSetMetaData rsmd = rs.getMetaData();
                
                while ( rs.next() )
                {
                        int id = rs.getInt(1);
                        int fk2 =rs.getInt(2);
                        String log = rs.getString(3);
                        String pwd =  rs.getString(4);
                        String f = rs.getString(5);
                        String p= rs.getString(6);
                        String pr = rs.getString(7);
                        String r = rs.getString(8);
                        String w = rs.getString(9);
                        int przejmij = rs.getInt(10);
                        int czy_root = rs.getInt(11);
                        KontaUzytkownikow obj = new KontaUzytkownikow(id, fk, log, pwd, f, p, pr, r, w, przejmij, czy_root);
                        konta.add( obj);             
                  }
                
                odswiezPesele();
                ini2();
            }    
            catch(Exception e){}
            
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
    
     public static String getHash(String txt, String hashType) 
     {
        try {
                    java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
                    byte[] array = md.digest(txt.getBytes());
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < array.length; ++i) {
                        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
                 }
                    return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
                //error action
            }
            return null;
    }
     
     
    void wyswietlanie() throws SQLException
    {
        data = FXCollections.observableArrayList();
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM uzytkownik");
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

    public void ini2()
    {
        try {
            // TODO
                data = FXCollections.observableArrayList();
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT * FROM uzytkownik");
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
        } catch (SQLException ex) {
            Logger.getLogger(PlacowkaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}