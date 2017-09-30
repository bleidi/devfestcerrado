package me.bleidi.devfestcerrado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@WebServlet(name="pic", urlPatterns="/api/pic")
@MultipartConfig
public class PicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2886064304808175598L;
	
	private static final String template = "img/twittercard.png";

	private static final Logger log = Logger.getLogger(PicServlet.class.getCanonicalName());
	
	private final Storage storage;
	
	public PicServlet() {
		this.storage = StorageOptions.getDefaultInstance().getService();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info(" ===> request");
		
		Part filePart = request.getPart("file");
		InputStream inputStream = filePart.getInputStream();
		byte[] buffer = new byte[inputStream.available()];
	    inputStream.read(buffer);
	 
	    String filename = "temp/".concat(UUID.randomUUID().toString());
		File targetFile = new File(filename.concat(".webm"));
	    OutputStream outStream = new FileOutputStream(targetFile);
	    outStream.write(buffer);
	    
	    String url = criarVideo(targetFile, filename);
		
	    response.setStatus(200);
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		writer.write("{\"url\" : \""+url+"\"}");
		writer.flush();
	    
	}

	private String criarVideo(File targetFile, String filename) throws ServletException {
		criarMiniatura(targetFile, filename);
		String fileName = filename.concat(".gif");
		String mergeCommand = "ffmpeg -i "+targetFile.getAbsolutePath()+" -i "+template+" -filter_complex \"nullsrc=size=1024x580 [base]; [base][0:v] overlay=595:106[tmp1]; [tmp1][1:v] overlay=0:0\" -pix_fmt rgb8 -y -t 00:00:05 "+fileName;
		try {
			Process p1 = Runtime.getRuntime().exec(mergeCommand);
			while(p1.isAlive()) {				
				Thread.sleep(1000);
			}
			
			BlobInfo blobInfo =
				      storage.create(
				          BlobInfo
				              .newBuilder("bleidi-devfestcerrado", fileName)
				              .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
				              .build(),
				              new FileInputStream(new File(fileName)));
			return blobInfo.getMediaLink();
		} catch (IOException| InterruptedException e) {
			throw new ServletException(e);
		}
	}

	private void criarMiniatura(File targetFile, String filename) throws ServletException {
		String scaleCommand = "ffmpeg -i "+targetFile.getAbsolutePath()+" -vf \"scale=(iw*sar)*max(370/(iw*sar)\\,370/ih):ih*max(370/(iw*sar)\\,370/ih), crop=370:370\" "+filename.concat(".mp4");
		try {
			Process p1 = Runtime.getRuntime().exec(scaleCommand);
			while(p1.isAlive()) {				
				Thread.sleep(1000);
			}
		} catch (IOException|InterruptedException e) {
			throw new ServletException(e);
		}
		
	}
	
}
