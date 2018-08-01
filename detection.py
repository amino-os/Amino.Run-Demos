import cv2


def face_detection():
    # Load a cascade file for detecting faces
    face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

    # Load an image
    image = cv2.imread('images/obama.jpg', 1)

    # Convert to grayscale
    gray = cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)

    # Look for faces in the image using the loaded cascade file
    faces = face_cascade.detectMultiScale(gray, 1.1, 5)

    for (x,y,w,h) in faces:
        cv2.rectangle(image,(x,y),(x+w,y+h),(255,255,0),2)

    frame = cv2.imencode('.jpg', image)[1].tobytes()
    return (b'--frame\r\n'
            b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')