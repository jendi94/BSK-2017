/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;



public class Historia 
{
    private
    int id;
    String odbiorca, nadawca, przekaz, tabela;

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }
    
    Historia()
    {
        
    }
    
    Historia(int id, String odb, String nad, String przekaz, String tabela)
    {
        this.id = id;
        this.nadawca = nad;
        this.odbiorca = odb;
        this.przekaz = przekaz;
        this.tabela = tabela;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOdbiorca() {
        return odbiorca;
    }

    public void setOdbiorca(String odbiorca) {
        this.odbiorca = odbiorca;
    }

    public String getNadawca() {
        return nadawca;
    }

    public void setNadawca(String nadawca) {
        this.nadawca = nadawca;
    }


    public String getPrzekaz() {
        return przekaz;
    }

    public void setPrzekaz(String przekaz) {
        this.przekaz = przekaz;
    }
    
}
