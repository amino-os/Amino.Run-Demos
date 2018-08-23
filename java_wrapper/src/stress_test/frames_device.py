import cv2
import requests

def extractFrames(pathIn):
    addr = 'http://0.0.0.0:5000'
    # test_url = addr + '/fps_video/'
    # test_url = addr + '/fps_detection/'
    test_url = addr + '/fps_recognition_tracking/'
    # test_url = addr + '/fps_recognition/'

    # prepare headers for http request
    content_type = 'image/jpeg'
    headers = {'content-type': content_type}

    cap = cv2.VideoCapture(pathIn)
    # cap = cv2.VideoCapture(0)
    while True:
        # Capture frame-by-frame
        ret, frame = cap.read()
        if ret == True:
            _, img_encoded = cv2.imencode('.jpg', frame)
            response = requests.post(test_url, data=img_encoded.tostring(), headers=headers)
        else:
            break
    # When done, release the capture
    cap.release()

if __name__ == "__main__":
   extractFrames('/home/root1/code/edgeCV/java_wrapper/src/sample_video.webm')