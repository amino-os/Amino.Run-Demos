import cv2
import base64

def extractFrames(pathIn):
    cap = cv2.VideoCapture(pathIn)
    # cap = cv2.VideoCapture(0)
    while True:
        # Capture frame-by-frame
        ret, frame = cap.read()
        if ret == True:
            frame_serialize = base64.b64encode(cv2.imencode('.jpg', frame)[1].tobytes()).decode("utf-8")
            print(frame_serialize)
        else:
            break
    # When done, release the capture
    cap.release()

if __name__ == "__main__":
   extractFrames('/home/root1/code/edgeCV/java_wrapper/src/sample_video.webm')