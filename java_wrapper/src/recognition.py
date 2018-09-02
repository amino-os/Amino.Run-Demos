import cv2
import numpy as np
import face_recognition
from pathlib import Path
import pickle
import os
import io
from imageio import imread

import base64

from imutils.video import FPS
# import imutils
# import psutil


font = cv2.FONT_HERSHEY_DUPLEX


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


def face_tracking(tracking_flag=True):
    fps = FPS().start()
    fourcc = cv2.VideoWriter_fourcc(*"XVID")
    # fourcc = cv2.VideoWriter_fourcc(*'MJPG')
    out = cv2.VideoWriter('/media/neeraj/sapphirized_faster_tracking_fps.avi', fourcc, 20.0, (320, 240))
    # out = cv2.VideoWriter('/media/neeraj/RPi_sapphirized_output_tracking.avi', fourcc, 20.0, (640, 480))
    # out = cv2.VideoWriter('/home/root1/code/edgeCV/java_wrapper/src/output_recognition.avi', fourcc, 20.0, (640, 480))

    # Load a cascade file for detecting faces
    face_cascade = cv2.CascadeClassifier('/home/root1/code/edgeCV/java_wrapper/src/haarcascade_frontalface_default.xml')

    while True:
        # frame_ready = input()
        # if frame_ready == "ok":
        #     # capture frames from the scratchfile
        #     with open('/media/neeraj/scratchpad.txt') as fp:
        #         for line1 in fp:
        #             line = line1
        line = input()
        image = imread(io.BytesIO(base64.b64decode(line)))
        # ret, frame = video_capture.read()
        frame = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        # frame = camera.get_frame()
        # image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)
        (H, W) = frame.shape[:2]

        # Grab a single frame of video
        # frame = camera.get_frame()
        # image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)

        # Convert to grayscale
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # Look for faces in the image using the loaded cascade file
        faces = face_cascade.detectMultiScale(gray, 1.1, 5)

        if len(faces) > 0:
            bbox_list = face_recognize(image)
            print(bbox_list)
            if len(bbox_list) > 0:
                if tracking_flag:
                    tracker = cv2.MultiTracker_create()
                for box in bbox_list:
                    top, right, bottom, left = box[0]
                    name = box[1]
                    if tracking_flag:
                            track = cv2.TrackerTLD_create()  # TrackerMIL_create()#TrackerKCF_create()
                            ok = tracker.add(track, frame, (left, top, right - left, bottom - top))
                    else:
                        # Draw a box around the face
                        cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 1)
                        # Draw a label with a name below the face
                        cv2.rectangle(frame, (left, bottom - 10), (right, bottom), (0, 0, 255), cv2.FILLED)
                        cv2.putText(frame, name, (left, bottom - 4), font, 0.3, (255, 255, 255), 1)
                if tracking_flag:
                    new_boxes = None
                    refresh = 0
                    while new_boxes is None or len(new_boxes) > 0:
                        # frame = camera.get_frame()
                        # image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)
                        print("next")
                        line = input()
                        # frame_ready = input()
                        # if frame_ready == "ok":
                        #     # capture frames from the scratchfile
                        #     with open('/media/neeraj/scratchpad.txt') as fp:
                        #         for line1 in fp:
                        #             line = line1

                        image = imread(io.BytesIO(base64.b64decode(line)))
                        frame = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

                        ok, new_boxes = tracker.update(frame)
                        for i in range(len(new_boxes)):
                            p1 = (int(new_boxes[i][0]), int(new_boxes[i][1]))
                            p2 = (int(new_boxes[i][0] + new_boxes[i][2]), int(new_boxes[i][1] + new_boxes[i][3]))
                            p3 = (int(new_boxes[i][0]), int(new_boxes[i][1] + new_boxes[i][3]) - 10)
                            cv2.rectangle(frame, p1, p2, (0, 0, 255), 1)
                            cv2.rectangle(frame, p3, p2, (0, 0, 255), cv2.FILLED)
                            cv2.putText(frame, bbox_list[i][1],
                                        (int(new_boxes[i][0]), int(new_boxes[i][1] + new_boxes[i][3]) - 4), font,
                                        0.3, (0, 0, 0), 1)
                        refresh += 1
                        if refresh == 16:
                            out.write(frame)
                            break

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
                        # frame = cv2.imencode('.jpg', frame)[1].tobytes()
                        # # yield (b'--frame\r\n'
                        # #        b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
                        # frame_serialize = base64.b64encode(frame).decode("utf-8")
                        # print(frame_serialize)

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

        # get CPU percentage (average of all cores, but only one being used right now)
        # print(psutil.cpu_percent(None, True))

        # get network stats
        # print(psutil.net_io_counters(True))

        # yield (b'--frame\r\n'
        #        b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
        # frame_serialize = base64.b64encode(frame).decode("utf-8")
        # print(frame_serialize)
        print("done")


face_tracking(tracking_flag=True)