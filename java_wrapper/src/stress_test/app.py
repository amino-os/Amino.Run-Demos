import cv2
import numpy as np
import face_recognition
from pathlib import Path
import pickle
import os
from imutils.video import FPS


fourcc = cv2.VideoWriter_fourcc(*"XVID")

# FROM SERVER
# out = cv2.VideoWriter('/media/neeraj/laptop_output_video_fps.avi', fourcc, 20.0, (640, 480))
# out = cv2.VideoWriter('/media/neeraj/laptop_output_video_detection_fps.avi', fourcc, 20.0, (640, 480))
# out = cv2.VideoWriter('/media/neeraj/laptop_output_video_recognition_tracking_fps.avi', fourcc, 20.0, (640, 480))
out = cv2.VideoWriter('/media/neeraj/laptop_output_video_recognition_fps.avi', fourcc, 20.0, (640, 480))

# FROM RPi
# out = cv2.VideoWriter('/media/neeraj/RPi_output_video_fps.avi', fourcc, 20.0, (640, 480))
# out = cv2.VideoWriter('/media/neeraj/RPi_output_video_tracking_fps.avi', fourcc, 20.0, (640, 480))
# out = cv2.VideoWriter('/media/neeraj/RPi_output_video_recognition_tracking_fps.avi', fourcc, 20.0, (640, 480))
# out = cv2.VideoWriter('/media/neeraj/RPi_output_video_recognition_fps.avi', fourcc, 20.0, (640, 480))

# Load a cascade file for detecting faces
face_cascade = cv2.CascadeClassifier('/home/root1/code/edgeCV/java_wrapper/src/haarcascade_frontalface_default.xml')
fps = FPS().start()

font = cv2.FONT_HERSHEY_DUPLEX

from flask import Flask, render_template, Response, request, abort
from flask.views import View

app = Flask(__name__)




def load_weights(encoding_file):
    if Path(encoding_file).is_file():
        f = open(encoding_file, "rb")
        known_face_names = pickle.load(f)
        known_face_encodings = pickle.load(f)
        f.close()
        return os.stat(encoding_file).st_mtime, known_face_names, known_face_encodings
    else:
        print("No encoding file!")


def face_recognize(image):
    # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
    rgb_image = image[:, :, ::-1]

    # Call facial recognition
    encoding_file = "/home/root1/code/edgeCV/java_wrapper/src/known_face_encodings.p"
    cached_stamp, known_face_names, known_face_encodings = load_weights(encoding_file)

    face_locations = face_recognition.face_locations(rgb_image)
    face_encodings = face_recognition.face_encodings(rgb_image, face_locations)

    bbox_list = []
    # Loop through each face found in the unknown image
    for (top, right, bottom, left), face_encoding in zip(face_locations, face_encodings):
        # See if the face is a match for the known face(s)
        matches = face_recognition.compare_faces(known_face_encodings, face_encoding, 0.5)

        name = "Unknown"

        if True in matches:
            if matches.count(True) > 1:
                first_match_index = np.argmin(face_recognition.face_distance(known_face_encodings, face_encoding))
            else:
                first_match_index = matches.index(True)
            name = known_face_names[first_match_index]

        bbox_list.append([(top, right, bottom, left), name])

    return bbox_list





@app.route('/')
def index():
    """Video streaming home page."""
    return render_template('index.html')


@app.route('/fps_video/', methods=['POST'])
def fps_video():
    r = request
    # convert string of image data to uint8
    nparr = np.fromstring(r.data, np.uint8)
    # decode image
    frame = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    (H, W) = frame.shape[:2]


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
    frame = cv2.imencode('.jpg', frame)[1].tobytes()

    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response((b'--frame\r\n'
                     b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n'),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/fps_detection/', methods=['POST'])
def fps_detection():
    r = request
    # convert string of image data to uint8
    nparr = np.fromstring(r.data, np.uint8)
    # decode image
    frame = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)

    # frame = camera.get_frame()
    # image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)
    (H, W) = frame.shape[:2]
    # Convert to grayscale
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    # Look for faces in the image using the loaded cascade file
    faces = face_cascade.detectMultiScale(gray, 1.1, 5)

    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 255, 0), 2)


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
    frame = cv2.imencode('.jpg', frame)[1].tobytes()

    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response((b'--frame\r\n'
                     b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n'),
                    mimetype='multipart/x-mixed-replace; boundary=frame')




class Recognition(View):
    methods = ['POST']

    refresh = 0
    new_boxes = None
    tracker = None
    bbox_list = None

    def __init__(self):
        pass

    @classmethod
    def dispatch_request(cls):
        if request.path == '/fps_recognition_tracking/':
            return cls.fps_recognition(tracking_flag=True)
        elif request.path == '/fps_recognition/':
            return cls.fps_recognition(tracking_flag=False)
        else:
            abort(404)

    # def tracking(self):

    # @app.route('/fps_recognition_tracking', methods=['POST'])
    @classmethod
    def fps_recognition(cls, tracking_flag=True):
        # with app.test_request_context():
        # from flask import request
        r = request
        if tracking_flag is False or (cls.refresh == 0 and tracking_flag is True): #while True:
            # convert string of image data to uint8
            nparr = np.fromstring(r.data, np.uint8)
            # decode image
            frame = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
            image = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)

            # frame = camera.get_frame()
            # image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)
            (H, W) = frame.shape[:2]
            # Convert to grayscale
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            # Look for faces in the image using the loaded cascade file
            faces = face_cascade.detectMultiScale(gray, 1.1, 5)

            if len(faces) > 0:
                cls.bbox_list = face_recognize(image)
                if len(cls.bbox_list) > 0:
                    if tracking_flag:
                        cls.tracker = cv2.MultiTracker_create()
                    for box in cls.bbox_list:
                        top, right, bottom, left = box[0]
                        name = box[1]
                        if tracking_flag:
                            track = cv2.TrackerTLD_create()  # TrackerMIL_create()#TrackerKCF_create()
                            ok = cls.tracker.add(track, frame, (left, top, right - left, bottom - top))
                        else:
                            # Draw a box around the face
                            cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 1)
                            # Draw a label with a name below the face
                            cv2.rectangle(frame, (left, bottom - 10), (right, bottom), (0, 0, 255), cv2.FILLED)
                            cv2.putText(frame, name, (left, bottom - 4), font, 0.3, (255, 255, 255), 1)

        if tracking_flag:
            # new_boxes = None
            # refresh = 0
            if cls.new_boxes is None or len(cls.new_boxes) > 0:
                r = request
                # convert string of image data to uint8
                nparr = np.fromstring(r.data, np.uint8)
                # decode image
                frame = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
                # image = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)

                ok, new_boxes = cls.tracker.update(frame)
                for i in range(len(new_boxes)):
                    p1 = (int(new_boxes[i][0]), int(new_boxes[i][1]))
                    p2 = (int(new_boxes[i][0] + new_boxes[i][2]), int(new_boxes[i][1] + new_boxes[i][3]))
                    p3 = (int(new_boxes[i][0]), int(new_boxes[i][1] + new_boxes[i][3]) - 10)
                    cv2.rectangle(frame, p1, p2, (0, 0, 255), 1)
                    cv2.rectangle(frame, p3, p2, (0, 0, 255), cv2.FILLED)
                    cv2.putText(frame, cls.bbox_list[i][1],
                                (int(new_boxes[i][0]), int(new_boxes[i][1] + new_boxes[i][3]) - 4), font,
                                0.3, (0, 0, 0), 1)

                cls.refresh += 1
                print('tracking refresh: '+ str(cls.refresh))
                if cls.refresh == 16:
                    cls.refresh = 0

                # info to be displayed in the frame
                fps.update()
                fps.stop()
                info = [
                    ("FPS", "{:.2f}".format(fps.fps()))
                ]

                # loop over the info tuples and draw them on our frame
                (H, W) = frame.shape[:2]
                for (i, (k, v)) in enumerate(info):
                    text = "{}: {}".format(k, v)
                    cv2.putText(frame, text, (10, H - ((i * 20) + 20)), cv2.FONT_HERSHEY_SIMPLEX, 0.6,
                                (0, 0, 255), 2)

                out.write(frame)
                frame = cv2.imencode('.jpg', frame)[1].tobytes()

                """Video streaming route. Put this in the src attribute of an img tag."""
                return Response((b'--frame\r\n'
                                 b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n'),
                                mimetype='multipart/x-mixed-replace; boundary=frame')

        cls.refresh += 1
        print('recognition refresh: ' + str(cls.refresh))

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
        frame = cv2.imencode('.jpg', frame)[1].tobytes()

        """Video streaming route. Put this in the src attribute of an img tag."""
        return Response((b'--frame\r\n'
                         b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n'),
                        mimetype='multipart/x-mixed-replace; boundary=frame')





if __name__ == '__main__':
    ### Not working...try saving state to ramdisk and read from there ###
    Recognize = Recognition()
    app.add_url_rule('/fps_recognition_tracking/', view_func=Recognize.as_view('fps_recognition_tracking'), methods=['POST'])
    app.add_url_rule('/fps_recognition/', view_func=Recognize.as_view('fps_recognition'),
                     methods=['POST'])

    app.run(host='0.0.0.0', threaded=True)
