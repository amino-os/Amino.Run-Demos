from flask import Flask, render_template, Response
from detection import face_detection
from recognition import face_tracking
from video_feed import feed

# laptop camera module
from camera_laptop import Camera

app = Flask(__name__)


@app.route('/')
def index():
    """Video streaming home page."""
    return render_template('index.html')


@app.route('/video_feed')
def video_feed():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(feed(Camera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')



@app.route('/face_detect')
def face_detect():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(face_detection(Camera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/face_track')
def face_track():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(face_tracking(Camera(), tracking_flag=True),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, threaded=True)