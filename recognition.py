import cv2
import numpy as np
import face_recognition
from pathlib import Path
import pickle
import os


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


def face_recognize():
    # Load a cascade file for detecting faces
    face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

    # Load an image
    image = cv2.imread('images/obama.jpg', 1)

    # Convert to grayscale
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    # Look for faces in the image using the loaded cascade file
    faces = face_cascade.detectMultiScale(gray, 1.1, 5)
    if len(faces) > 0:
        # Call facial recognition
        encoding_file = "known_face_encodings.p"
        cached_stamp, known_face_names, known_face_encodings = load_weights(encoding_file)

        face_locations = face_recognition.face_locations(image)
        face_encodings = face_recognition.face_encodings(image, face_locations)
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

        if len(bbox_list) > 0:
            for box in bbox_list:
                top, right, bottom, left = box[0]
                name = box[1]
                # Draw a box around the face
                cv2.rectangle(image, (left, top), (right, bottom), (0, 0, 255), 1)
                # Draw a label with a name below the face
                cv2.rectangle(image, (left, bottom - 10), (right, bottom), (0, 0, 255), cv2.FILLED)
                cv2.putText(image, name, (left, bottom - 4), font, 0.3, (255, 255, 255), 1)

    frame = cv2.imencode('.jpg', image)[1].tobytes()
    return (b'--frame\r\n'
            b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
