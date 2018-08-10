<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Demo Application</title>
</head>
<body>
	<div>
		<span style="display: inline-block; width: 50;"></span> Face Tracking
		Demonstration <span style="display: inline-block; width: 100;"></span>
		Face Detection Demonstration <span
			style="display: inline-block; width: 100;"></span> Face Recognition
		Demonstration
	</div>
	<div>
		<!-- <%
			// Directly working on JSP
			Process p = Runtime.getRuntime().exec("python3 /home/root1/Desktop/EdgeCV/edgeCV/detection.py");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String ret = in.readLine();
		%> -->
		<!-- <img src="data:image/jpeg;base64,<%=ret%>">  -->
		<img src="/JavaWrappper/TrackingServlet"> <!-- tracking -->
		<img src="/JavaWrappper/DetectionServlet">    <!-- detection -->
		<img src="/JavaWrappper/RecognitionServlet">  <!-- recognition -->
	</div>
</body>
</html>