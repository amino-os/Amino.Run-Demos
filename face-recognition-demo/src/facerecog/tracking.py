import cv2
import numpy as np
import face_recognition
from pathlib import Path
import pickle
import os
import io
from imageio import imread
import ast

import base64

from imutils.video import FPS
# import imutils
# import psutil


font = cv2.FONT_HERSHEY_DUPLEX



# def face_tracking():
#     fps = FPS().start()
#     fourcc = cv2.VideoWriter_fourcc(*"XVID")
#     out = cv2.VideoWriter('/media/neeraj/sapphirized_faster_tracking_fps.avi', fourcc, 20.0, (320, 240))
#
#     # Load a cascade file for detecting faces
#     face_cascade = cv2.CascadeClassifier('/home/root1/code/edgeCV/face-recognition-demo/src/facerecog/haarcascade_frontalface_default.xml')
#
#     while True:
#         line = input()
#         image = imread(io.BytesIO(base64.b64decode(line)))
#         frame = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
#
#         # Convert to grayscale
#         gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
#
#         # Look for faces in the image using the loaded cascade file
#         faces = face_cascade.detectMultiScale(gray, 1.1, 5)
#
#         if len(faces) > 0:
#             # send the image for recognition to java wrapper
#             frame_serialize = base64.b64encode(cv2.imencode('.jpg', frame)[1].tobytes()).decode("utf-8")
#             print(frame_serialize, flush=True)
#             # get the box list from the java layer
#             bbox_list_str = input()
#             bbox_list = ast.literal_eval(bbox_list_str)
#             if len(bbox_list) > 0:
#                 tracker = cv2.MultiTracker_create()
#                 for box in bbox_list:
#                     top, right, bottom, left = box[0]
#                     name = box[1]
#                     track = cv2.TrackerTLD_create()  # TrackerMIL_create()#TrackerKCF_create()
#                     ok = tracker.add(track, frame, (left, top, right - left, bottom - top))
#                 new_boxes = None
#                 refresh = 0
#                 while new_boxes is None or len(new_boxes) > 0:
#                     # frame = camera.get_frame()
#                     # image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)
#                     line = input()
#                     print("next", flush=True)
#
#                     image = imread(io.BytesIO(base64.b64decode(line)))
#                     frame = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
#
#                     ok, new_boxes = tracker.update(frame)
#                     for i in range(len(new_boxes)):
#                         p1 = (int(new_boxes[i][0]), int(new_boxes[i][1]))
#                         p2 = (int(new_boxes[i][0] + new_boxes[i][2]), int(new_boxes[i][1] + new_boxes[i][3]))
#                         p3 = (int(new_boxes[i][0]), int(new_boxes[i][1] + new_boxes[i][3]) - 10)
#                         cv2.rectangle(frame, p1, p2, (0, 0, 255), 1)
#                         cv2.rectangle(frame, p3, p2, (0, 0, 255), cv2.FILLED)
#                         cv2.putText(frame, bbox_list[i][1],
#                                     (int(new_boxes[i][0]), int(new_boxes[i][1] + new_boxes[i][3]) - 4), font,
#                                     0.3, (0, 0, 0), 1)
#                     refresh += 1
#                     if refresh == 16:
#                         out.write(frame)
#                         break
#
#                     out.write(frame)
#
#         out.write(frame)
#
#         # print("done", flush=True)























def face_tracking():
    fps = FPS().start()
    fourcc = cv2.VideoWriter_fourcc(*"XVID")
    # fourcc = cv2.VideoWriter_fourcc(*'MJPG')
    out = cv2.VideoWriter('/media/neeraj/sapphirized_faster_tracking_fps.avi', fourcc, 20.0, (320, 240))
    # out = cv2.VideoWriter('/media/neeraj/RPi_sapphirized_output_tracking.avi', fourcc, 20.0, (640, 480))
    # out = cv2.VideoWriter('/home/root1/code/edgeCV/java_wrapper/src/output_recognition.avi', fourcc, 20.0, (640, 480))

    # Load a cascade file for detecting faces
    face_cascade = cv2.CascadeClassifier('/home/root1/code/edgeCV/face-recognition-demo/src/facerecog/haarcascade_frontalface_default.xml')

    while True:
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
        found_face = False

        if len(faces) > 0:
            found_face = True
            # bbox_list = face_recognize(image)
            # send the image for recognition to java wrapper
            frame_serialize = base64.b64encode(cv2.imencode('.jpg', frame)[1].tobytes()).decode("utf-8")
            print(frame_serialize, flush=True)
            # get the box list from the java layer
            bbox_list_str = input()
            bbox_list = ast.literal_eval(bbox_list_str)
            if len(bbox_list) > 0:
                tracker = cv2.MultiTracker_create()
                for box in bbox_list:
                    top, right, bottom, left = box[0]
                    name = box[1]
                    track = cv2.TrackerTLD_create()  # TrackerMIL_create()#TrackerKCF_create()
                    ok = tracker.add(track, frame, (left, top, right - left, bottom - top))
                new_boxes = None
                refresh = 0
                while new_boxes is None or len(new_boxes) > 0:
                    # frame = camera.get_frame()
                    # image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)
                    line = input()
                    print("next", flush=True)

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
                        # out.write(frame)
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

        if found_face == False:
            print("done", flush=True)
        # print("done", flush=True)


face_tracking()