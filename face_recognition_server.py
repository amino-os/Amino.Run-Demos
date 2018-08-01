from flask import Flask, request, Response
import jsonpickle
import numpy as np
import cv2
import face_recognition
from pathlib import Path
import pickle
import os

# Initialize the Flask application
app = Flask(__name__)

def load_weights(encoding_file):
    if Path(encoding_file).is_file():
        f = open(encoding_file,"rb")
        known_face_names = pickle.load(f)
        known_face_encodings = pickle.load(f)
        f.close()
        return os.stat(encoding_file).st_mtime,known_face_names,known_face_encodings
    else:
        print("No encoding file!")

# route http posts to this method
@app.route('/api/test', methods=['POST'])
def test():
    encoding_file = "known_face_encodings.p"
    cached_stamp,known_face_names,known_face_encodings = load_weights(encoding_file)
    
    while True:
        stamp = os.stat(encoding_file).st_mtime
        if stamp != cached_stamp:
            break

        r = request
        # convert string of image data to uint8
        nparr = np.fromstring(r.data, np.uint8)
        # decode image
        frame = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        face_locations = face_recognition.face_locations(frame)
        face_encodings = face_recognition.face_encodings(frame, face_locations)
        bbox = []
        # Loop through each face found in the unknown image
        for (top, right, bottom, left), face_encoding in zip(face_locations, face_encodings):
            # See if the face is a match for the known face(s)
            matches = face_recognition.compare_faces(known_face_encodings, face_encoding, 0.5)

            name = "Unknown"

            if True in matches:
                if matches.count(True)>1:
                    first_match_index = np.argmin(face_recognition.face_distance(known_face_encodings, face_encoding))
                else:
                    first_match_index = matches.index(True)
                name = known_face_names[first_match_index]
            
            bbox.append([(top, right, bottom, left),name])

        # build a response dict to send back to client
        response = {'bbox': bbox}

        # encode response using jsonpickle
        response_pickled = jsonpickle.encode(response)

        return Response(response=response_pickled, status=200, mimetype="application/json")


# start flask app
app.run(host="0.0.0.0", port=5000)