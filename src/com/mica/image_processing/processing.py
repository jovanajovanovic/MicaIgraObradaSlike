import numpy as np
import cv2 # OpenCV biblioteka
import matplotlib
import matplotlib.pyplot as plt

def start_processing(path):
    # ovde cemo izvuci sliku na osnovu putanje i proslediti je narednoj funkciji
    #orginalna slika
    image = cv2.cvtColor(cv2.imread(path),cv2.COLOR_BGR2RGB)
    #pozvacemo funkciju za sredjivanje slike
    result = image_processing(image)
    # povratna vrednost je string
    result += "|0,0"
    return result #samo za probu

def image_processing(image):
    # priprema slike za isdvajanje figura
    original_img = image.copy()
    img_gray = cv2.cvtColor(original_img, cv2.COLOR_RGB2GRAY)

#ovde cemo gledati sta nam vrati funkcija processing, pa cemo u zavisnosti od toga menjati value
    result = processing(img_gray, 7, original_img)
    return result

def processing(img_gray, value, original_img):
    image_gray = cv2.medianBlur(img_gray, 7)  # mozda bi trebalo stvaiti 7, jer smo za neke slike imali problema,
    # jer smo neke sumove na tabli detektovali kao krugove
    image_bin = cv2.adaptiveThreshold(image_gray, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 35, 15)

    plt.imshow(image_bin, 'gray')

    _, contours, hierarchy = cv2.findContours(image_bin, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)

    for i in range(2):  # radimo 2 iteracije jer obicno nakon jedne ne dobijemo da je kontura table najveca
        index_of_biggest, table_contour = find_biggest_rectangle(contours)
        del contours[index_of_biggest]

    if table_contour is not None:
        circles = cv2.HoughCircles(image_gray, cv2.HOUGH_GRADIENT, 1, 10, param1=50, param2=30, minRadius=60,
                                   maxRadius=150)
        # zaokruzujemo sve na celobrojne vrednosti
        circles = np.uint32(np.around(circles))
        circles = circles[0, :]
        print("len(circles) = " + str(len(circles)))

        circles_without_duplicates = get_circles_without_duplicates(circles, table_contour, original_img)
        print("len(circles_without_duplicates) = " + str(len(circles_without_duplicates)))

        if len(circles_without_duplicates) > 0:
            if len(circles_without_duplicates) == 24:
                for c in circles_without_duplicates:
                    # for c in circles:
                    # draw the outer circle
                    # cv2.circle(original_img,(c[0],c[1]),c[2],(0,255,0),3)
                    print(c["rgb"])
                    cv2.circle(original_img, (c["circle"][0], c["circle"][1]), c["circle"][2], (0, 255, 0),
                               3)  # cimg je pre stojalo

                matrix = get_matrix(table_contour, circles_without_duplicates)
                print("len(matrix) = " + str(len(matrix)))
                print("len(matrix[0]) = " + str(len(matrix[0])) + "  # matrix[0] - spoljasnji pravougaonik")
                print("len(matrix[1]) = " + str(len(matrix[1])) + "  # matrix[1] - srednji pravougaonik")
                print("len(matrix[2]) = " + str(len(matrix[2])) + "  # matrix[2] - unutrasnji pravougaonik")

                # odredimo boju na igraca cija je figura na osnovu boje
                for i in range(0, 3):  # matrica, odredjeni krug
                    for j in range(len(matrix[i])):  # setnja kroz listu krugova
                        print('indeks kruga: i= ' + str(i) + " j = " + str(j))
                        print('pozicija kruga: ' + str(matrix[i][j]['circle']))
                        print('rgb: ' + str(matrix[i][j]['rgb']))
                        if (matrix[i][j]['rgb'][0] > 90):
                            # red, prvi igrac 1
                            print('crveni igrac: ' + str(matrix[i][j]['rgb'][0]))
                            matrix[i][j]['player'] = 1
                        elif (matrix[i][j]['rgb'][2] > 65):
                            # plavi - slobodno polje
                            print('slobodno polje: ' + str(matrix[i][j]['rgb'][2]))
                            matrix[i][j]['player'] = 0
                        else:
                            # crno, igrac 2
                            print('crni igrac: ' + str(matrix[i][j]['rgb'][0]))
                            matrix[i][j]['player'] = 2

                # ispritamo da li smo dobro dodelili figure
                for i in range(0, 3):
                    for c in matrix[i]:
                        print("igrac u listi i=" + str(i) + " je: " + str(c['player']))

                result = ""
                for i in range(len(matrix[0])):
                    if (i == (len(matrix[0]) - 1)):
                        result += str(matrix[0][i]['player'])
                    else:
                        result += str(matrix[0][i]['player'])
                        result += ","
                result += ";"
                for i in range(len(matrix[1])):
                    if (i == (len(matrix[1]) - 1)):
                        result += str(matrix[1][i]['player'])
                    else:
                        result += str(matrix[1][i]['player'])
                        result += ","
                result += ";"
                for i in range(len(matrix[2])):
                    if (i == (len(matrix[2]) - 1)):
                        result += str(matrix[2][i]['player'])
                    else:
                        result += str(matrix[2][i]['player'])
                        result += ","

                print('result: ' + result)

                #  cv2.circle(original_img,(matrix[0][3]["circle"][0],matrix[0][3]["circle"][1]),matrix[0][3]["circle"][2],(0,255,0),3)
                # for c in matrix[0]:
                # draw the outer circle
                # cv2.circle(original_img,(c["circle"][0],c["circle"][1]),c["circle"][2],(255,0,0),3)

                # for c in matrix[1]:
                # draw the outer circle
                # cv2.circle(original_img,(c["circle"][0],c["circle"][1]),c["circle"][2],(0,255,0),3)

                # for c in matrix[2]:
                # draw the outer circle
                # cv2.circle(original_img,(c["circle"][0],c["circle"][1]),c["circle"][2],(0,0,255),3)

                cv2.drawContours(original_img, [table_contour], -1, (255, 0, 0), 3)
                plt.figure()
                plt.imshow(original_img)
                return result
            else:
                print("Mora biti detektovano tacno 24 circles_without_duplicates, a sada je detektovano " + str(
                    len(circles_without_duplicates)))
        else:
            print("Trenutno nema nijedan circles_without_duplicates!")
    else:
        print("Error!")


def find_biggest_rectangle(contours):
    biggest_rectangle = None
    index_of_biggest = -1
    max_p = 0

    for index, contour in enumerate(contours):
        center, size, angle = cv2.minAreaRect(contour)
        p = size[0] * size[1]
        # perimeter = cv2.arcLength(contour,True)
        if p > max_p:
            max_p = p
            biggest_rectangle = contour
            index_of_biggest = index

    return (index_of_biggest, biggest_rectangle)


def find_and_remove_nearest_circle(xy, circles):
    min_square_length = 9999999999
    n_circle = None
    n_index = -1

    if len(circles) == 1:
        n_circle = circles[0]
        del circles[0]
        return n_circle

    for index, c in enumerate(circles):
        square_length = (xy[0] - c["circle"][0]) * (xy[0] - c["circle"][0]) + (xy[1] - c["circle"][1]) * (
                    xy[1] - c["circle"][1])
        if square_length < min_square_length:
            min_square_length = square_length
            n_circle = c
            n_index = index
    if n_circle is None:
        raise Exception("The nearest circle was not found")
    else:
        del circles[n_index]

    return n_circle


def get_part_of_matrix(trenutni_unutrasnji_krug, trenutni_srednji_krug, trenutni_spoljasnji_krug,
                       circles_without_duplicates, n):
    spoljasnji = []
    srednji = []
    unutrasnji = []

    current_xy = (trenutni_unutrasnji_krug["circle"][0], trenutni_unutrasnji_krug["circle"][1])

    # u ovoj fazi pronalazimo n narednih polja za sva tri pravougaonika,
    # jer da bismo pronasli i ostale u ispravnom poretku,
    # prvo moramo pronaci i izbaciti neke druge krugove
    for i in range(n):
        # trazimo novi najblizi unutrasnji krug, pa prelazimo na trazenje novog najblizeg srednjeg kruga
        trenutni_unutrasnji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
        unutrasnji.append(trenutni_unutrasnji_krug)
        current_xy = (trenutni_srednji_krug["circle"][0], trenutni_srednji_krug["circle"][1])

        # trazimo novi najblizi srednji krug, pa prelazimo na trazenje novog najblizeg spoljasnjeg kruga
        trenutni_srednji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
        srednji.append(trenutni_srednji_krug)
        current_xy = (trenutni_spoljasnji_krug["circle"][0], trenutni_spoljasnji_krug["circle"][1])

        # trazimo novi najblizi spoljasnji krug, pa prelazimo na trazenje novog najblizeg unutrasnjeg kruga
        trenutni_spoljasnji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
        spoljasnji.append(trenutni_spoljasnji_krug)
        current_xy = (trenutni_unutrasnji_krug["circle"][0], trenutni_unutrasnji_krug["circle"][1])
    return [spoljasnji, srednji, unutrasnji]


def get_matrix(table_contour, circles_without_duplicates):
    x, y, w, h = cv2.boundingRect(table_contour)
    # cv2.rectangle(original_img, (x, y), (x + w, y + h), (255, 0, 0), 3)

    spoljasnji = []
    srednji = []
    unutrasnji = []

    current_xy = (x, y)  # krecemo od gornjeg-levog coska table

    # trenutni spoljasnji krug na pocetku je gornji-levi krug u spoljasnjem pravougaoniku
    trenutni_spoljasnji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
    spoljasnji.append(trenutni_spoljasnji_krug)
    current_xy = (trenutni_spoljasnji_krug["circle"][0], trenutni_spoljasnji_krug["circle"][1])

    # trenutni srednji krug na pocetku je gornji-levi krug u srednjem pravougaoniku
    trenutni_srednji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
    srednji.append(trenutni_srednji_krug)
    current_xy = (trenutni_srednji_krug["circle"][0], trenutni_srednji_krug["circle"][1])

    # trenutni unutrasnji krug na pocetku je gornji-levi krug u unutrasnjem pravougaoniku
    trenutni_unutrasnji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
    unutrasnji.append(trenutni_unutrasnji_krug)

    part_of_matrix = get_part_of_matrix(trenutni_unutrasnji_krug, trenutni_srednji_krug, trenutni_spoljasnji_krug,
                                        circles_without_duplicates, 3)
    spoljasnji.extend(part_of_matrix[0])
    srednji.extend(part_of_matrix[1])
    unutrasnji.extend(part_of_matrix[2])

    current_xy = (x + w, y + h)  # sad krecemo od donjeg-levog coska table

    # trenutni spoljasnji krug na pocetku je gornji-levi krug u spoljasnjem pravougaoniku
    trenutni_spoljasnji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
    spoljasnji.append(trenutni_spoljasnji_krug)
    current_xy = (trenutni_spoljasnji_krug["circle"][0], trenutni_spoljasnji_krug["circle"][1])

    # trenutni srednji krug na pocetku je gornji-levi krug u srednjem pravougaoniku
    trenutni_srednji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
    srednji.append(trenutni_srednji_krug)
    current_xy = (trenutni_srednji_krug["circle"][0], trenutni_srednji_krug["circle"][1])

    # trenutni unutrasnji krug na pocetku je gornji-levi krug u unutrasnjem pravougaoniku
    trenutni_unutrasnji_krug = find_and_remove_nearest_circle(current_xy, circles_without_duplicates)
    unutrasnji.append(trenutni_unutrasnji_krug)

    part_of_matrix = get_part_of_matrix(trenutni_unutrasnji_krug, trenutni_srednji_krug, trenutni_spoljasnji_krug,
                                        circles_without_duplicates, 3)
    spoljasnji.extend(part_of_matrix[0])
    srednji.extend(part_of_matrix[1])
    unutrasnji.extend(part_of_matrix[2])

    return [spoljasnji, srednji, unutrasnji]


def get_circles_without_duplicates(circles, table_contour, image):
    circles_without_duplicates = []

    # ovako uzecemo jedan krug i onda cemo za pronaci sve one sa kojima ima presek
    """ 
    for circle in circles:
        duplicates_list = [] #lista krugova koji se presecaju
        for c in circles:
            if haveIntersect(circle,c):
                #ako se poklapaju, onda u listu ubacimo c i radius
                radius = c[0] - c[1]
                duplicates_list.append({'circle': c, 'radius' : radius })
                #izbrisemo c iz liste 
          #      circles.remove(c)
        #pokupili smo sve koji se presecaju, sad u jednu listu pokupimo sve radiuse i sortiramo ih 
        list_radius = []
        for l in duplicates_list: 
            list_radius.append(l['radius'])
        list_radius.sort()
        #izvucemo srednji krug 
        middle = list_radius[int(round(len(list_radius)/2))] #srednji radius, sad treba da prondje krug koji ima toliki radius
        for c in duplicates_list:
            if not c['radius'] == middle in circles_without_duplicates: 
                #proverimo da vec ne postoji u listi 
                #ako ne postoji u listi dodamo ga u listu 
                x = circle[0]
                y = circle[1]
                if cv2.pointPolygonTest(table_contour,(x,y),False) == 1: #centar kruga se nalazi na  tabli
                       circle_img = np.zeros((original_img.shape[0],original_img.shape[1]), np.uint8) #Creamos mascara (matriz de ceros) del tamano de la imagen original
                        cv2.circle(circle_img,(x,y),circle[2],(255,255,255),-1) #Pintamos los circulos en la mascara
                        rgb = cv2.mean(original_img, mask=circle_img)[::-1][1:]
                        #rgb = (int(original_img[y][x][0]),int(original_img[y][x][1]),int(original_img[y][x][2]))
                        circles_without_duplicates.append({"circle":circle, "rgb":rgb})
    """

    for circle in circles:
        # orginalnu sliku moramo pretvoriti u hsv
        hsv = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)
        if not haveIntersect(circle, circles_without_duplicates):
            x = circle[0]
            y = circle[1]
            if cv2.pointPolygonTest(table_contour, (x, y), False) == 1:  # centar kruga se nalazi na  tabli
             #   circle_img = np.zeros((hsv.shape[0], hsv.shape[1]),
                                    #  np.uint8)  # Creamos mascara (matriz de ceros) del tamano de la imagen original
             #   cv2.circle(circle_img, (x, y), circle[2], (255, 255, 255), -1)  # Pintamos los circulos en la mascara
             #   rgb = cv2.mean(hsv, mask=circle_img)[::-1][1:]
                # rgb = (int(original_img[y][x][0]),int(original_img[y][x][1]),int(original_img[y][x][2]))
                 rgb = image[y][x]
                 p = -1
                 circles_without_duplicates.append({"circle": circle, "rgb": rgb, "player": p})
    return circles_without_duplicates

def haveIntersect(circle, circles_without_duplicates):
    """
    distSq = (circle[0] - c[0]) * (circle[0] - c[0]) + (circle[1] - c[1]) * (circle[1] - c[1])
    radSumSq = (circle[2] + c[2]) * (circle[2] + c[2])
    if distSq <= radSumSq:
        return True
    return False
    """

    for c in circles_without_duplicates:
        distSq = (circle[0] - c["circle"][0]) * (circle[0] - c["circle"][0]) + (circle[1] - c["circle"][1]) * (circle[1] - c["circle"][1])
        radSumSq = (circle[2] + c["circle"][2]) * (circle[2] + c["circle"][2])
        if distSq <= radSumSq:
            return True
    return False