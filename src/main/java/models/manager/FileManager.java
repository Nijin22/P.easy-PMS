package models.manager;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import models.beans.PeasyUser;
import models.beans.ProfilePicture;
import models.beans.Project;
import models.beans.ProjectFile;
import models.beans.Task;
import models.beans.TaskFile;

public class FileManager {

    private static final Logger log = Logger.getLogger(FileManager.class.getName());

    @Inject
    Provider<EntityManager> entitiyManagerProvider;

    public String uploadFile(File file, String fileName, String type, String idOfOwner) throws IOException, IllegalArgumentException {
        log.log(Level.INFO, "Start method with file: {0} and type: {1}", new Object[]{fileName, type});

        //First check attributes
        if (!file.isFile()) {
            log.log(Level.WARNING, "File is null!");
            throw new IllegalArgumentException("File can't be null!");
        }
        if (type.isEmpty()) {
            log.log(Level.WARNING, "Type is null!");
            throw new IllegalArgumentException("Type can't be null!");
        }

        //Second Persist Objects in Database
        if (type.equals("picture")) {
            log.log(Level.INFO, "Saving ProfilePicture: {0}", fileName);
            createPictureUser(idOfOwner, fileName);

        } else if (type.equals("task")) {
            log.log(Level.INFO, "Saving Taskfile: {0}", fileName);
            long taskId = Long.parseLong(idOfOwner);
            createTaskFile(taskId, fileName);

        } else if (type.equals("project")) {
            log.log(Level.INFO, "Saving Projectfile: {0}", fileName);
            long projectId = Long.parseLong(idOfOwner);
            createProjectFile(projectId, fileName);
        }

        //declare file output directory CAUTION: USE fileItem.getFileName not file.getName() !!!!
        File directory = new File("target" + File.separator + "tmp" + File.separator + type);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File saveFile = new File(directory.getCanonicalPath(), fileName);

        if (saveFile.exists()) {
            log.log(Level.WARNING, "File with Filename {0} exists already!", saveFile.getName());
            throw new IllegalArgumentException("File with Filename " + saveFile.getName() + " exists already!");
        }
        saveFile.createNewFile();
        //get Inputstream of File which shall be uploaded
        InputStream inputStream = Files.asByteSource(file).openStream();

        //Save File in System
        BufferedInputStream is = null;
        BufferedOutputStream os = null;
        try {
            is = new BufferedInputStream(inputStream);
            os = new BufferedOutputStream(new FileOutputStream(saveFile));
            byte[] buff = new byte[8192];
            int len;
            while (0 < (len = is.read(buff))) {
                os.write(buff, 0, len);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.flush();
                os.close();
            }
        }
        return saveFile.getCanonicalPath();

    }

    public void downloadFile(OutputStream outputstream, String id) throws IOException {

        log.log(Level.INFO, "Start method with FileId: {0}", id);

        String type = "picture";
        File directory = new File("target" + File.separator + "tmp" + File.separator + type);
        File downloadFile = new File(directory.getCanonicalPath(), "Hochschulzeugnis.pdf");
        byte[] buf = new byte[8192];
        InputStream is = new FileInputStream(downloadFile);
        int c = 0;
        while ((c = is.read(buf, 0, buf.length)) > 0) {
            outputstream.write(buf, 0, c);
            outputstream.flush();
        }
        outputstream.close();
        is.close();

    }

    public void deleteFile(long fileId, String type) throws FileNotFoundException, IOException {

        //Second Persist Objects in Database
        if (type.equals("picture")) {
            log.log(Level.INFO, "Deleting ProfilePicture with id: {0}", fileId);
            deletePictureUser(fileId);

        } else if (type.equals("task")) {
            log.log(Level.INFO, "Deleting Taskfile with id: {0}", fileId);
            deleteTaskFile(fileId);

        } else if (type.equals("project")) {
            log.log(Level.INFO, "Deleting Projectfile with id: {0}", fileId);
            deleteProjectFile(fileId);
        }

        File directory = new File("target" + File.separator + "tmp" + File.separator + type);
        if (!directory.exists()) {
            throw new FileNotFoundException("Could not find directory where File: " + fileId + " could be");
        }
        //uncomment this after changing logic
        //File fileToDelete = new File(directory.getCanonicalPath(), String.valueOf(fileId));
        //fileToDelete.delete();

    }

    @Transactional
    private void createPictureUser(String email, String fileName) {
        log.log(Level.INFO, "createPictureUser: Persist Object in Database with email {0} and Filename: {1}", new Object[]{email, fileName});
        EntityManager entityManager = entitiyManagerProvider.get();
        PeasyUser peasyUser = entityManager.find(PeasyUser.class, email);

        ProfilePicture profilePicture = new ProfilePicture(fileName);
        entityManager.persist(profilePicture);

        profilePicture.setPeasyUser(peasyUser);
        peasyUser.setProfilePicture(profilePicture);

    }

    @Transactional
    private void createTaskFile(long taskId, String fileName) {
        log.log(Level.INFO, "createTaskFile: Persist Object in Database with taskID {0} and Filename: {1}", new Object[]{taskId, fileName});
        EntityManager entityManager = entitiyManagerProvider.get();
        Task task = entityManager.find(Task.class, taskId);

        TaskFile taskFile = new TaskFile(fileName);
        entityManager.persist(taskFile);

        taskFile.setTask(task);
        task.getTaskFiles().add(taskFile);
    }

    @Transactional
    public ProjectFile createProjectFile(long projectId, String fileName) {
        log.log(Level.INFO, "Start add ProjectFile to Project in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        ProjectFile projectFile = new ProjectFile(fileName);
        projectFile.setProject(project);
        project.getProjectFiles().add(projectFile);

        entityManager.persist(projectFile);

        log.log(Level.INFO, "Succesfull add ProjectFile to Project in Database");

        return projectFile;

    }

    @Transactional
    private void deletePictureUser(long fileId) {
        log.log(Level.INFO, "Start deleting ProfilPicture from Project in Database");

    }

    @Transactional
    private void deleteTaskFile(long fileId) {
    }

    @Transactional
    public void deleteProjectFile(long fileId) {
        log.log(Level.INFO, "Start deleting ProfilPicture from Project in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        ProjectFile projectFile = entityManager.find(ProjectFile.class, fileId);
        Project project = entityManager.find(Project.class, projectFile.getProject().getProjectId());
        
        project.getProjectFiles().remove(projectFile);
        projectFile.setProject(null);

        entityManager.remove(projectFile);
        
        log.log(Level.INFO, "Succesfull deleting ProfilPicture from Project in Database");
    }
}
