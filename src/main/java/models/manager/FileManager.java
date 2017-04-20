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
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import models.beans.FileClass;
import models.beans.PeasyUser;
import models.beans.ProfilePicture;
import models.beans.Project;
import models.beans.ProjectFile;
import models.beans.Task;
import models.beans.TaskFile;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.IllegalAddException;

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

        String fileIdJPA = "";

        //Second Persist Objects in Database
        if (type.equals("picture")) {
            log.log(Level.INFO, "Saving ProfilePicture: {0}", fileName);
            try {
                ImageIO.read(file);
                fileIdJPA = createPictureUser(idOfOwner, fileName);
            } catch (IOException e) {
                log.log(Level.WARNING, "Given File is not a image {0}", file.getName());
                throw new IllegalAddException("Given File is not a image " + file.getName());
            }

        } else if (type.equals("task")) {
            log.log(Level.INFO, "Saving Taskfile: {0}", fileName);
            long taskId = Long.parseLong(idOfOwner);
            fileIdJPA = createTaskFile(taskId, fileName);
            log.log(Level.INFO, "Generated Id for TaskFile {0}", fileIdJPA);

        } else if (type.equals("project")) {
            log.log(Level.INFO, "Saving Projectfile: {0}", fileName);
            long projectId = Long.parseLong(idOfOwner);
            fileIdJPA = createProjectFile(projectId, fileName);
        }

        //declare file output directory CAUTION: USE fileItem.getFileName not file.getName() !!!!
        File directory = new File("target" + File.separator + "tmp" + File.separator + type);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File saveFile = null;
        if (!fileIdJPA.isEmpty()) {
            String ext = "." + FilenameUtils.getExtension(fileName);
            saveFile = new File(directory.getCanonicalPath(), fileIdJPA + ext);
        } else {
            throw new IllegalArgumentException("fileIdJPA is not set in methods");
        }

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

    public void deleteFile(long fileId, String fileType, String type) throws FileNotFoundException, IOException {

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
            throw new FileNotFoundException("Could not find directory " + directory.getCanonicalPath());
        }

        File fileToDelete = new File(directory.getCanonicalPath(), String.valueOf(fileId) + "." + fileType);
        fileToDelete.delete();

    }

    @Transactional
    private String createPictureUser(String email, String fileName) {
        log.log(Level.INFO, "Start add ProfilePicture to User in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        PeasyUser peasyUser = entityManager.find(PeasyUser.class, email);

        ProfilePicture profilePicture = new ProfilePicture(fileName);
        profilePicture.setPeasyUser(peasyUser);
        peasyUser.setProfilePicture(profilePicture);

        entityManager.persist(profilePicture);

        log.log(Level.INFO, profilePicture.toString());

        return String.valueOf(profilePicture.getFileId());

    }

    @Transactional
    private String createTaskFile(long taskId, String fileName) {
        log.log(Level.INFO, "Start add TaskFile to Task in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        Task task = entityManager.find(Task.class, taskId);

        TaskFile taskFile = new TaskFile(fileName);
        taskFile.setTask(task);
        task.getTaskFiles().add(taskFile);

        entityManager.persist(taskFile);
        log.info(taskFile.toString());

        return String.valueOf(taskFile.getFileId());
    }

    @Transactional
    public String createProjectFile(long projectId, String fileName) {
        log.log(Level.INFO, "Start add ProjectFile to Project in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        ProjectFile projectFile = new ProjectFile(fileName);
        projectFile.setProject(project);
        project.getProjectFiles().add(projectFile);

        entityManager.persist(projectFile);
        log.log(Level.INFO, projectFile.toString());
        return String.valueOf(projectFile.getFileId());

    }

    @Transactional
    private void deletePictureUser(long fileId) {
        log.log(Level.INFO, "Start deleting ProfilPicture from User in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        ProfilePicture profilePicture = entityManager.find(ProfilePicture.class, fileId);
        PeasyUser peasyUser = entityManager.find(PeasyUser.class, profilePicture.getPeasyUser().getEmailAddress());

        peasyUser.setProfilePicture(null);
        profilePicture.setPeasyUser(null);

        entityManager.remove(profilePicture);

        log.log(Level.INFO, profilePicture.toString());

    }

    @Transactional
    private void deleteTaskFile(long fileId) {
        log.log(Level.INFO, "Start deleting TaskFile from Task in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        TaskFile taskFile = entityManager.find(TaskFile.class, fileId);
        Task task = entityManager.find(Task.class, taskFile.getTask().getTaskId());

        task.getTaskFiles().remove(taskFile);
        taskFile.setTask(null);

        //logs for testing
        log.info("Taskfile " + taskFile.toString());
        log.info("Taskfile " + task.getTaskFiles().toString());

        entityManager.remove(taskFile);
        log.log(Level.INFO, taskFile.toString());

    }

    @Transactional
    public void deleteProjectFile(long fileId) {
        log.log(Level.INFO, "Start deleting ProjectFile from Project in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        ProjectFile projectFile = entityManager.find(ProjectFile.class, fileId);
        Project project = entityManager.find(Project.class, projectFile.getProject().getProjectId());

        project.getProjectFiles().remove(projectFile);
        projectFile.setProject(null);

        entityManager.remove(projectFile);

        log.log(Level.INFO, projectFile.toString());

    }

    @Transactional
    public TaskFile getTaskFile(String id) {
        EntityManager entityManager = entitiyManagerProvider.get();
        long taskId = Long.parseLong(id);
        TaskFile taskFile = entityManager.find(TaskFile.class, taskId);
        return taskFile;
    }

    @Transactional
    public ProjectFile getProjectFile(String id) {
        EntityManager entityManager = entitiyManagerProvider.get();
        long projectId = Long.parseLong(id);
        ProjectFile projctFile = entityManager.find(ProjectFile.class, projectId);
        return projctFile;
    }

    @Transactional
    public ProfilePicture getProfilePicture(String id) {
        EntityManager entityManager = entitiyManagerProvider.get();
        long pictureId = Long.parseLong(id);
        ProfilePicture picture = entityManager.find(ProfilePicture.class, pictureId);
        return picture;
    }
}
