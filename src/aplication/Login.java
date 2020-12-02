/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplication;

/**
 *
 * @author usuario 1
 */
public class Login {
     private String userNameLogar;
    private String passwordLogar;

    public Login(String userNameLogar, String passwordLogar) {
        this.userNameLogar = userNameLogar;
        this.passwordLogar = passwordLogar;
    }
    
    public String getUserNameLogar() {
        return userNameLogar;
    }

    public void setUserNameLogar(String userNameLogar) {
        this.userNameLogar = userNameLogar;
    }

    public String getPasswordLogar() {
        return passwordLogar;
    }

    public void setPasswordLogar(String passwordLogar) {
        this.passwordLogar = passwordLogar;
    }
    
    
    
    
}


