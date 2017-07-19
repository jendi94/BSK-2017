/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bskkk;

import com.sun.org.apache.xpath.internal.operations.Bool;


public class Uprawnienia {
    private
            char select;
            char update;
            char insert;
            char delete;
            
    Uprawnienia(String suma)
    {
        select = suma.charAt(0);
        update = suma.charAt(1);
        insert = suma.charAt(2);
        delete = suma.charAt(3);
    }

    public String getSUID()
    {
        char[] wynikC={'-','-','-','-'};
        if(getSelect()=='S')
        wynikC[0] = getSelect();
        if(getUpdate() == 'U')
        wynikC[1] = getUpdate();
        if(getInsert() == 'I')
        wynikC[2] = getInsert();
        if(getDelete() == 'D')
        wynikC[3] = getDelete();
        String wynik = String.valueOf(wynikC);
        return wynik;
    }
    
    public char getSelect() {
        return select;
    }

    public void setSelect(char select) {
        this.select = select;
    }

    public char getUpdate() {
        return update;
    }

    public void setUpdate(char update) {
        this.update = update;
    }

    public char getInsert() {
        return insert;
    }

    public void setInsert(char insert) {
        this.insert = insert;
    }

    public char getDelete() {
        return delete;
    }

    public void setDelete(char delete) {
        this.delete = delete;
    }  
}
