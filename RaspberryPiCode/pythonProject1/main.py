import cv2
import matplotlib.pyplot as plt
import cvlib as cv
import urllib.request
import numpy as np
from cvlib.object_detection import draw_bbox
import concurrent.futures
import paho.mqtt.client as paho
import threading
import time


url = 'http://10.0.0.161/cam-hi.jpg'
im = None
x = 'none'

class myThread (threading.Thread):
   def __init__(self, threadID, name):
      threading.Thread.__init__(self)
      self.threadID = threadID
      self.name = name
      self.detected = "none"
   def run(self):
       while True:
           publish(self.detected)
           time.sleep(3)

   def setdetection(self, x):
       self.detected = x






#This file runs the object detection
def run1(thread):


    classFile = 'coco.names'
    with open(classFile, 'rt') as f:
        className = f.read().rstrip('\n').split('\n')

    configPath = 'ssd_mobilenet_v3_large_coco_2020_01_14.pbtxt'
    weightsPath = 'frozen_inference_graph.pb'

    net = cv2.dnn_DetectionModel(weightsPath, configPath)
    net.setInputSize(320, 320)
    net.setInputScale(1.0 / 127.5)
    net.setInputMean((127.5, 127.5, 127.5))
    net.setInputSwapRB(True)

    hashmap = {16: 'bird', 17: 'cat', 18: 'dog', 19:'horse', 20: 'sheep', 21: 'cow', 22: 'bear'}
    while True:
        img_resp = urllib.request.urlopen(url)
        imgnp = np.array(bytearray(img_resp.read()), dtype=np.uint8)
        img = cv2.imdecode(imgnp, -1)

        classIds, confs, bbox = net.detect(img, confThreshold=0.60)
        print(classIds, bbox)

        if len(classIds) != 0:
            for classId, confidence, box in zip(classIds.flatten(), confs.flatten(), bbox):
                if classId in hashmap:
                    found = hashmap.get(classId)
                    thread.setdetection(found)
                    print(thread.detected)
                else:
                    thread.setdetection("safe")

                cv2.rectangle(img, box, color=(0, 255, 0), thickness=2)
                cv2.putText(img, className[classId - 1], (box[0] + 10, box[1] + 30),
                            cv2.FONT_HERSHEY_COMPLEX, 1, (0, 255, 0), 2)
        else:
            thread.setdetection("safe")
        cv2.imshow("output", img)
        key = cv2.waitKey(5)

        if key == ord('q'):
            break


def publish(detected):
    (rc, mid) = client.publish("GF45sfgge3ij/pythonScript", str(detected), qos=1)


def on_publish(client, userdata, mid):
    print("mid: " + str(mid))




if __name__ == '__main__':
    print("started")


    client = paho.Client()
    client.on_publish = on_publish
    client.connect("broker.mqttdashboard.com", 1883)
    client.loop_start()
    thread1 = myThread(1, "Thread-1")
    thread1.start()


    with concurrent.futures.ProcessPoolExecutor() as executer:
        f1 = executer.submit(run1(thread1))



