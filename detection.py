import cv2
# import base64
import numpy as np
from imutils.video import FPS

def face_detection(camera):
    # Load a cascade file for detecting faces
    face_cascade = cv2.CascadeClassifier('/home/root1/code/edgeCV/haarcascade_frontalface_default.xml')
    fps = FPS().start()

    while True:
        # capture frames from the camera
        frame = camera.get_frame()
        image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)
        (H, W) = image.shape[:2]

        # Convert to grayscale
        gray = cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)

        # Look for faces in the image using the loaded cascade file
        faces = face_cascade.detectMultiScale(gray, 1.1, 5)

        for (x,y,w,h) in faces:
            cv2.rectangle(image,(x,y),(x+w,y+h),(255,255,0),2)

        ## info to be displayed in the frame
        fps.update()
        fps.stop()
        info = [
            ("FPS", "{:.2f}".format(fps.fps()))
        ]

        ## loop over the info tuples and draw them on our frame
        for (i, (k, v)) in enumerate(info):
            text = "{}: {}".format(k, v)
            cv2.putText(image, text, (10, H - ((i * 20) + 20)), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 255), 2)

        frame = cv2.imencode('.jpg', image)[1].tobytes()
        # frame_serialize = base64.b64encode(frame).decode("utf-8")

        yield (b'--frame\r\n'
                b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
        # print (frame_serialize)

# face_detection()