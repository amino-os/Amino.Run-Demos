import cv2
from imutils.video import FPS
import base64
import io
from imageio import imread

def face_detection():
    fps = FPS().start()
    fourcc = cv2.VideoWriter_fourcc(*"XVID")
    # fourcc = cv2.VideoWriter_fourcc(*'MJPG')
    out = cv2.VideoWriter('/home/root1/code/edgeCV/java_wrapper/src/output_detection.avi', fourcc, 20.0, (640, 480))

    # Load a cascade file for detecting faces
    face_cascade = cv2.CascadeClassifier('/home/root1/code/edgeCV/java_wrapper/src/haarcascade_frontalface_default.xml')

    while True:
        frame_ready = input()
        if frame_ready == "ok":
            # capture frames from the scratchfile
            with open('/home/root1/code/edgeCV/java_wrapper/src/scratchpad.txt') as fp:
                for line1 in fp:
                    line = line1

        image = imread(io.BytesIO(base64.b64decode(line)))
        # ret, frame = video_capture.read()
        frame = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        # frame = camera.get_frame()
        # image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)
        (H, W) = frame.shape[:2]
        # Convert to grayscale
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        # Look for faces in the image using the loaded cascade file
        faces = face_cascade.detectMultiScale(gray, 1.1, 5)

        for (x, y, w, h) in faces:
            cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 255, 0), 2)

        # info to be displayed in the frame
        fps.update()
        fps.stop()
        info = [
            ("FPS", "{:.2f}".format(fps.fps()))
        ]

        # loop over the info tuples and draw them on our frame
        for (i, (k, v)) in enumerate(info):
            text = "{}: {}".format(k, v)
            cv2.putText(frame, text, (10, H - ((i * 20) + 20)), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 255), 2)

        out.write(frame)
        # frame = cv2.imencode('.jpg', frame)[1].tobytes()
        # frame_serialize = base64.b64encode(frame).decode("utf-8")

        # yield (b'--frame\r\n'
        #         b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
        # print(frame_serialize)
        print("done")

face_detection()