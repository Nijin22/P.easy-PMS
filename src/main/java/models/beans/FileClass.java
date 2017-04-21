/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.beans;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author Tugrul
 * This Class is 
 */
//@Entity
@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class FileClass{
    
    static class IdContainer{
        private static long id;
        
        IdContainer(){
            this.id = 0;
        }
        
        static long getId(){
            long current = id;
            id++;
            return current;
        }
        
    }

    public FileClass() {
    }

    public FileClass(String title) {
        this.title = title;
     //   fileId = IdContainer.getId();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long fileId;
    @NotNull
    private String title;

    public long getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    @Override
    public String toString() {
        return "FileClass{" + "id=" + fileId + ", title=" + title + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.fileId ^ (this.fileId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileClass other = (FileClass) obj;
        if (this.fileId != other.fileId) {
            return false;
        }
        return true;
    }
   
    
    
}
