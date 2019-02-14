import numpy as np
import cv2 # OpenCV biblioteka
import matplotlib
import matplotlib.pyplot as plt

def start_processing(path):
    # ovde cemo izvuci sliku na osnovu putanje i proslediti je narednoj funkciji
    #orginalna slika
    image = cv2.cvtColor(cv2.imread(path),cv2.COLOR_BGR2RGB)
    #pozvacemo funkciju za sredjivanje slike
    image_processing(image)
    # povratna vrednost je string
    return "0,1,1,2,0,2,1,0;2,1,2,0,0,0,1,2;0,0,0,1,0,0,0,0|3,4".encode() #samo za probu

def image_processing(image):
    # priprema slike za isdvajanje figura
    original_img = image.copy()
    img_gray = cv2.cvtColor(original_img, cv2.COLOR_RGB2GRAY)

    #uklonimo sumove
    img_gray = cv2.medianBlur(img_gray, 5) #ovde cemo videti da li staviti 5 ili 7
    #probacemo i sa erozijom i dilacijom resiti se ostalih sumova

    image_bin = cv2.adaptiveThreshold(img_gray, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 35, 15)

    findFigures(image_bin)


def findFigures(image_bin):
    #trazenje figura na slici
    image = image_bin