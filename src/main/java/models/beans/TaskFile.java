/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Tugrul
 */
@Entity
public class TaskFile {

    public TaskFile() {
    }

    public TaskFile(String title) {
        this.title = title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long fileId;

    @NotNull
    private String title;
    private String fileType;

    @ManyToOne
    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public long getFileId() {
        return fileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "TaskFile{" + "fileId=" + fileId + ", title=" + title + ", fileType=" + fileType + ", task=" + task + '}';
    }

}
