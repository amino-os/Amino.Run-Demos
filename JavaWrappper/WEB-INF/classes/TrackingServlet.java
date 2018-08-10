

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TrackingServlet
 */
@WebServlet("/TrackingServlet")
public class TrackingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TrackingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType("image/jpg");
//		response.setContentType("multipart/x-mixed-replace");
		Process p = Runtime.getRuntime().exec("python3 /home/root1/Desktop/EdgeCV/edgeCV/tracking.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String ret;
		byte[] asBytes;
		while((ret = in.readLine()) != null) {
			System.out.println("value is : "+ret);
			asBytes = Base64.getDecoder().decode(ret);
//			System.out.println(new String(asBytes, "utf-8"));
			response.setContentLength(asBytes.length);
			response.getOutputStream().write(asBytes);
			
			/*ByteArrayInputStream bis = new ByteArrayInputStream(asBytes);
			BufferedImage image = ImageIO.read(bis);
			bis.close();
			OutputStream out = response.getOutputStream();
			ImageIO.write(image, "jpg", out);
			out.close();*/
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
