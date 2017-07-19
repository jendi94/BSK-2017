/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class FXMLDocumentController implements Initializable {

    
    public int indeksK=-1;
    @FXML
    private Label label;
     @FXML
    private Label komunikat;
     @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField hasloTextField;
    
    Connection con;
    
    List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
        
/*
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            indeksK=-1;
            konta = wczytajKonta();
            
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    

    @FXML
    private void log(MouseEvent event) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException, SQLException {
        komunikat.setText("");
        String login = loginTextField.getText();
        String haslo = hasloTextField.getText();
        String zakodowane = getHash(haslo, "SHA-256");
        
       // int indeksK = -1;
        KontaUzytkownikow konto = new KontaUzytkownikow( login, haslo);
        int indeks = 0;
     
        while (indeksK<0 && indeks<konta.size())
        {
            if (konta.get(indeks).getLogin().equals(login))
            {
               if( konta.get(indeks).getPassword().equals(zakodowane))
               {
                   indeksK=konta.get(indeks).getId();
                   break;
               }
               break;
            }
            indeks++;
        }
        if(indeksK>=0)
        {
            //System.out.println("Zalogowano!");
            ((Node)event.getSource()).getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Program.fxml"));
            Parent p = (Parent) loader.load();
            ProgramController program = loader.getController();
            program.setParamProg(indeksK,indeks, konta, con);
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.show();
        }
        else
        {
            //komunikat.setText("Złe hasło lub login, spróbuj jeszcze raz.");
            //System.out.println("Nie zalogowano");
        }
//        

    }
    
    public List<KontaUzytkownikow> wczytajKonta() throws FileNotFoundException, IOException, SQLException
    {      
           List<KontaUzytkownikow> konta = new ArrayList<KontaUzytkownikow>();
           con = DriverManager.getConnection(
                         "jdbc:mysql://localhost/wypozyczalnia",
                         "root",
                         "vertrigo");
        
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
     

}
