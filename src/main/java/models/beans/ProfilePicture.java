/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.beans;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Tugrul
 */
@Entity
public class ProfilePicture extends FileClass implements Serializable{

    public ProfilePicture() {
    }

    public ProfilePicture(String title) {
        super(title);
    }
    
    @OneToOne
    private PeasyUser peasyUser;

    public PeasyUser getPeasyUser() {
        return peasyUser;
    }

    public void setPeasyUser(PeasyUser peasyUser) {
        this.peasyUser = peasyUser;
    }
    
    
    
}
