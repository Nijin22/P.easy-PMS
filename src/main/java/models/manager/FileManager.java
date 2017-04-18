package models.manager;

import com.google.common.io.Files;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import ninja.uploads.FileItem;


public class FileManager {
    
   private static final Logger log = Logger.getLogger( FileManager.class.getName() );

    
    public String uploadFile(FileItem fileItem, String type) throws IOException, IllegalArgumentException{
        log.log(Level.INFO, "Start method with file: {0} and type: {1}", new Object[]{fileItem.getFileName(), type});
        
        File file = fileItem.getFile();
        if(!file.isFile() ){
            log.log(Level.WARNING, "File is null!");
            throw new IllegalArgumentException("File can't be null!");
        }
        if(type.isEmpty()){
            log.log(Level.WARNING, "Type is null!");
            throw new IllegalArgumentException("Type can't be null!");
        }
        
        //declare file output directory CAUTION: USE fileItem.getFileName not file.getName() !!!!
        File directory = new File("target" + File.separator + "tmp"  + File.separator + type);
        if(!directory.exists()){
            directory.mkdirs();
        }
        
        File saveFile = new File(directory.getCanonicalPath() ,fileItem.getFileName());
       
        if(saveFile.exists()){
            log.log(Level.WARNING, "File with Filename {0} exists already!", saveFile.getName());
            throw new IllegalArgumentException("File with Filename " + saveFile.getName() + " exists already!");
        }
        saveFile.createNewFile();
        //get Inputstream of File which shall be uploaded
        InputStream inputStream = Files.asByteSource(file).openStream();
        
        //Save File in System
        BufferedInputStream  is = null;
        BufferedOutputStream os = null;
        try {
          is = new BufferedInputStream(inputStream);
          os = new BufferedOutputStream(new FileOutputStream(saveFile));
          byte[] buff = new byte[8192];
          int len;
          while( 0 < (len = is.read( buff )) )
            os.write( buff, 0, len );
        } finally {
          if( is != null )
            is.close();
          if( os != null ) {
            os.flush();
            os.close();
          }
        }
       return saveFile.getAbsolutePath();

 }
    
     public void downloadFile(OutputStream outputstream, String id) throws IOException{

         log.log(Level.INFO, "Start method with FileId: {0}", id);
      
        String type = "picture";
        File directory = new File("target" + File.separator + "tmp"  + File.separator + type);
        File downloadFile = new File(directory.getCanonicalPath() ,"Hochschulzeugnis.pdf");
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

}
