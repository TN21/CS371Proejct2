/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;
import db.*;
import javax.swing.JFrame;
/**
 *
 * @author tonitan
 */
public class main {
    
    public static void main(String[] args){
        DBManager DB=new DBManager();
        try{
            
            DB.connect("dthwb","fHTgAEq2ifBwF4v7lHUD","KC-SCE-APPDB01","3306","cs371");
            JFrame LoginFrame=new LoginFrame(DB);
            LoginFrame.setVisible(true);
        }
        catch(Exception e){
            
        }
    }
}
