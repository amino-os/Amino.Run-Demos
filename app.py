#!/usr/bin/env python
from importlib import import_module
import os
from flask import Flask, render_template, Response
import cv2
import numpy as np
import requests
import json
from detection import face_detection
from tracking import face_tracking

# Raspberry Pi camera module (requires picamera package)
from camera_pi import Camera
font = cv2.FONT_HERSHEY_DUPLEX

app = Flask(__name__)


@app.route('/')
def index():
    """Video streaming home page."""
    return render_template('index.html')


def gen(camera):
    """Video streaming generator function."""
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')

def face_recognition(image):
    addr = 'http://10.175.20.253:5000'
    test_url = addr + '/api/test'

    # prepare headers for http request
    content_type = 'image/jpeg'
    headers = {'content-type': content_type}

    # encode image as jpeg
    _, img_encoded = cv2.imencode('.jpg', image)
    # send http request with image and receive response
    response = requests.post(test_url, data=img_encoded.tostring(), headers=headers)
    # decode response
    return json.loads(response.text)['bbox']


def face(camera,recog_flag=False):
    # Load a cascade file for detecting faces
    face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

    # capture frames from the camera
    while True:
        frame = camera.get_frame()
        image = cv2.imdecode(np.frombuffer(frame, np.uint8), 1)

        #Convert to grayscale
        gray = cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)

        #Look for faces in the image using the loaded cascade file
        faces = face_cascade.detectMultiScale(gray, 1.1, 5)
        if recog_flag:
            if len(faces) > 0:
		#Call facial recognition
                bbox = face_recognition(image)
                if len(bbox) > 0:
                    for box in bbox:
                        top,right,bottom,left = box[0]['py/tuple']
                        name = box[1]
                        # Draw a box around the face
                        cv2.rectangle(image, (left, top), (right, bottom), (0, 0, 255), 1)
                        # Draw a label with a name below the face
                        cv2.rectangle(image, (left, bottom - 10), (right, bottom), (0, 0, 255), cv2.FILLED)
                        cv2.putText(image, name, (left, bottom-4), font, 0.3, (255, 255, 255), 1)
        else:
            #Draw a rectangle around every found face
            for (x,y,w,h) in faces:
                cv2.rectangle(image,(x,y),(x+w,y+h),(255,255,0),2)

        frame = cv2.imencode('.jpg', image)[1].tobytes()
        yield (b'--frame\r\n'
                b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')

@app.route('/video_feed')
def video_feed():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(gen(Camera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/face_detect')
def face_detect():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(face_detection(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/face_track')
def face_track():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(face_tracking(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


if __name__ == '__main__':
    app.run(host='0.0.0.0', threaded=True)


