import base64

with open("/home/root1/eclipse-workspace/DemoApp/biden.jpg", "rb") as image_file:
 str = base64.b64encode(image_file.read())
 print str
