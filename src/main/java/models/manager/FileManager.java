package models.manager;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import models.beans.PeasyUser;
import models.beans.ProfilePicture;
import models.beans.Project;
import models.beans.ProjectFile;
import models.beans.Task;
import models.beans.TaskFile;
import models.manager.enums.FileType;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.IllegalAddException;

/**
 *
 * @author Tugrul
 */
public class FileManager {

    private static final Logger log = Logger.getLogger(FileManager.class.getName());

    @Inject
    Provider<EntityManager> entitiyManagerProvider;

    /**
     * This Method is saving the File in the FileSystem and creates a FileObject
     * in the Database
     *
     *
     * @param file
     * @param fileName
     * @param type
     * @param idOfOwner
     * @return Path, where File is saved in the FileSystem
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    public String uploadFile(File file, String fileName, String type, String idOfOwner) throws IOException, IllegalArgumentException, NoSuchElementException {
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
        if (type.equals(FileType.picture.name())) {
            log.log(Level.INFO, "Saving ProfilePicture: {0}", fileName);
            try {
                ImageIO.read(file);
                fileIdJPA = createPictureUser(idOfOwner, fileName);
            } catch (IOException e) {
                log.log(Level.WARNING, "Given File is not a image {0}", file.getName());
                throw new IllegalAddException("Given File is not a image " + file.getName());
            }

        } else if (type.equals(FileType.task.name())) {
            log.log(Level.INFO, "Saving Taskfile: {0}", fileName);
            long taskId = Long.parseLong(idOfOwner);
            fileIdJPA = createTaskFile(taskId, fileName);
            log.log(Level.INFO, "Generated Id for TaskFile {0}", fileIdJPA);

        } else if (type.equals(FileType.project.name())) {
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

    /**
     *
     * This Method is deleting the File in the FileSystem and deleting the
     * FileObject in the Database
     *
     * @param fileId
     * @param fileType
     * @param type
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchElementException
     */
    public void deleteFile(long fileId, String fileType, String type) throws FileNotFoundException, IOException, NoSuchElementException {

        //Second Persist Objects in Database
        if (type.equals(FileType.picture.name())) {
            log.log(Level.INFO, "Deleting ProfilePicture with id: {0}", fileId);
            deletePictureUser(fileId);

        } else if (type.equals(FileType.task.name())) {
            log.log(Level.INFO, "Deleting Taskfile with id: {0}", fileId);
            deleteTaskFile(fileId);

        } else if (type.equals(FileType.project.name())) {
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

    /**
     *
     * This Method is creating a ProfilePicture-Object in the Database. It is
     * used by the method uploadFile
     *
     * @param email
     * @param fileName
     * @return id of created rofilePicture-Object
     * @throws NoSuchElementException
     */
    @Transactional
    private String createPictureUser(String email, String fileName) throws NoSuchElementException {
        log.log(Level.INFO, "Start add ProfilePicture to User in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        PeasyUser peasyUser = entityManager.find(PeasyUser.class, email);

        if (peasyUser == null) {
            throw new NoSuchElementException("PeasyUser with email " + email + "is not in the database");
        }

        ProfilePicture profilePicture = new ProfilePicture(fileName);
        profilePicture.setPeasyUser(peasyUser);
        peasyUser.setProfilePicture(profilePicture);

        entityManager.persist(profilePicture);

        log.log(Level.INFO, profilePicture.toString());

        return String.valueOf(profilePicture.getFileId());

    }

    /**
     *
     * This Method is creating a TaskFile-Object in the Database. It is used by
     * the method uploadFile
     *
     * @param taskId
     * @param fileName
     * @return id of created TaskFile-Object
     * @throws NoSuchElementException
     */
    @Transactional
    private String createTaskFile(long taskId, String fileName) throws NoSuchElementException {
        log.log(Level.INFO, "Start add TaskFile to Task in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        Task task = entityManager.find(Task.class, taskId);

        if (task == null) {
            throw new NoSuchElementException("Task with taskFileId " + taskId + "is not in the database");
        }

        TaskFile taskFile = new TaskFile(fileName);
        taskFile.setTask(task);
        task.getTaskFiles().add(taskFile);

        entityManager.persist(taskFile);
        log.info(taskFile.toString());

        return String.valueOf(taskFile.getFileId());
    }

    /**
     *
     * This Method is creating a ProjectFile-Object in the Database. It is used
     * by the method uploadFile
     *
     * @param projectId
     * @param fileName
     * @return id of created ProjectFile-Object
     * @throws NoSuchElementException
     */
    @Transactional
    private String createProjectFile(long projectId, String fileName) throws NoSuchElementException {
        log.log(Level.INFO, "Start add ProjectFile to Project in Database");

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        if (project == null) {
            throw new NoSuchElementException("Project with taskFileId " + projectId + "is not in the database");
        }

        ProjectFile projectFile = new ProjectFile(fileName);
        projectFile.setProject(project);
        project.getProjectFiles().add(projectFile);

        entityManager.persist(projectFile);
        log.log(Level.INFO, projectFile.toString());
        return String.valueOf(projectFile.getFileId());

    }

    /**
     *
     * This Method is deleting a ProjectFile-Object in the Database. It is used
     * by the method deleteFile
     *
     * @param fileId
     * @throws NoSuchElementException
     */
    @Transactional
    private void deletePictureUser(long fileId) throws NoSuchElementException {
        log.log(Level.INFO, "Start deleting ProfilPicture from User in Database");

        EntityManager entityManager = entitiyManagerProvider.get();

        ProfilePicture profilePicture = entityManager.find(ProfilePicture.class, fileId);

        if (profilePicture == null) {
            throw new NoSuchElementException("ProfilePicture with projectFile " + fileId + "is not in the database");
        }

        PeasyUser peasyUser = entityManager.find(PeasyUser.class, profilePicture.getPeasyUser().getEmailAddress());
        if (peasyUser == null) {
            throw new NoSuchElementException("PeasyUser with projetId " + profilePicture.getPeasyUser().getEmailAddress() + "is not in the database");
        }

        peasyUser.setProfilePicture(null);
        profilePicture.setPeasyUser(null);

        entityManager.remove(profilePicture);

        log.log(Level.INFO, profilePicture.toString());

    }

    /**
     *
     * This Method is deleting a TaskFile-Object in the Database. It is used by
     * the method deleteFile
     *
     * @param fileId
     * @throws NoSuchElementException
     */
    @Transactional
    private void deleteTaskFile(long fileId) throws NoSuchElementException {
        log.log(Level.INFO, "Start deleting TaskFile from Task in Database");

        EntityManager entityManager = entitiyManagerProvider.get();

        TaskFile taskFile = entityManager.find(TaskFile.class, fileId);
        if (taskFile == null) {
            throw new NoSuchElementException("TaskFile with projectFile " + fileId + "is not in the database");
        }

        Task task = entityManager.find(Task.class, taskFile.getTask().getTaskId());
        if (task == null) {
            throw new NoSuchElementException("Task with projetId " + taskFile.getTask().getTaskId() + "is not in the database");
        }

        task.getTaskFiles().remove(taskFile);
        taskFile.setTask(null);

        entityManager.remove(taskFile);
        log.log(Level.INFO, taskFile.toString());

    }

    /**
     *
     * This Method is deleting a ProjectFile-Object in the Database. It is used
     * by the method deleteFile
     *
     * @param fileId
     * @throws NoSuchElementException
     */
    @Transactional
    private void deleteProjectFile(long fileId) throws NoSuchElementException {
        log.log(Level.INFO, "Start deleting ProjectFile from Project in Database");

        EntityManager entityManager = entitiyManagerProvider.get();

        ProjectFile projectFile = entityManager.find(ProjectFile.class, fileId);
        if (projectFile == null) {
            throw new NoSuchElementException("ProjectFile with projectFile " + fileId + "is not in the database");
        }

        Project project = entityManager.find(Project.class, projectFile.getProject().getProjectId());
        if (project == null) {
            throw new NoSuchElementException("Project with projetId " + projectFile.getProject().getProjectId() + "is not in the database");
        }

        project.getProjectFiles().remove(projectFile);
        projectFile.setProject(null);

        entityManager.remove(projectFile);

        log.log(Level.INFO, projectFile.toString());

    }

    /**
     *
     * This Method returns a TaskFile for given ID
     *
     * @param id
     * @return TaskFile which is founded by id
     * @throws NoSuchElementException
     */
    @Transactional
    public TaskFile getTaskFile(String id) throws NoSuchElementException {
        EntityManager entityManager = entitiyManagerProvider.get();
        long taskFileId = Long.parseLong(id);
        TaskFile taskFile = entityManager.find(TaskFile.class, taskFileId);

        if (taskFile == null) {
            throw new NoSuchElementException("TaskFile with taskFileId " + taskFileId + "is not in the database");
        } else {
            return taskFile;
        }

    }

    /**
     *
     * This Method returns a ProjectFile for given ID
     *
     * @param id
     * @return ProjectFile which is founded by id
     * @throws NoSuchElementException
     */
    @Transactional
    public ProjectFile getProjectFile(String id) throws NoSuchElementException {
        EntityManager entityManager = entitiyManagerProvider.get();
        long projectFileId = Long.parseLong(id);
        ProjectFile projctFile = entityManager.find(ProjectFile.class, projectFileId);

        if (projctFile == null) {
            throw new NoSuchElementException("ProjectFile with projctFileId " + projectFileId + "is not in the database");
        } else {
            return projctFile;
        }

    }

    /**
     *
     * This Method returns a profilePicture for given ID
     *
     * @param id
     * @return ProfilePicture which is founded by id
     * @throws NoSuchElementException
     */
    @Transactional
    public ProfilePicture getProfilePicture(String id) throws NoSuchElementException {
        EntityManager entityManager = entitiyManagerProvider.get();
        long pictureId = Long.parseLong(id);
        ProfilePicture picture = entityManager.find(ProfilePicture.class, pictureId);

        if (picture == null) {
            throw new NoSuchElementException("Picture with pictureId " + pictureId + "is not in the database");
        } else {
            return picture;
        }

    }
}
