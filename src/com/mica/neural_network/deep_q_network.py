from keras.models import Sequential, model_from_json
from keras.layers import Dense
from keras.optimizers import Adam
import numpy as np


learning_rate = 0.1
gamma = 0.9
tau = 0.125
state_size = 33  # 32
action_size = 76 
path_to_model = "./model.json"
path_to_weights = "./weights.h5" 


def build_new_model_or_load_old_model(load_ws=False):
    if load_ws:
        model = load_model(path_to_model, path_to_weights)
    else:
        model = Sequential()
        model.add(Dense(128, activation='relu')) # input_dim=state_size,
        model.add(Dense(64, activation='relu'))
        model.add(Dense(action_size, activation='linear'))
    
    model.compile(loss="mean_squared_error", optimizer=Adam(lr=learning_rate))
    # kompajliranje modela za multiklasnu klasifikaciju
    #model.compile(loss=keras.losses.categorical_crossentropy, optimizer=keras.optimizers.Adadelta(), metrics=['accuracy'])
    
    #model.summary()
    
    return model    

def update_target_model(target_model, model):
    #target_model.set_weights(model.get_weights())
    weights = model.get_weights()
    target_weights = target_model.get_weights()
    for i in range(len(target_weights)):
        target_weights[i] = weights[i] * tau + target_weights[i] * (1 - tau)
    target_model.set_weights(target_weights)

def get_index_of_best_action_from_nn(model, state, selektovana_polja_i_akcije):
    state = np.reshape(state, [1,state_size])
    print(state)
    
    potencijalne_akcije = []
    
    for sp in selektovana_polja_i_akcije:
        state[0][state_size-2] = sp[0]
        predict_values = model.predict(state)[0]
        while True:
            index_of_action = np.argmax(predict_values)
            print("index_of_action: " + str(index_of_action))
            print("actions: " + str(sp[1]))
            if index_of_action in sp[1]:
                nova_potencijalna_akcija = (index_of_action, predict_values[index_of_action], sp[0])
                if not alreadyAddedAction(potencijalne_akcije, nova_potencijalna_akcija):
                    potencijalne_akcije.append( nova_potencijalna_akcija ) # index_of_action, value for action, selektovano polje
                break
            else:
                predict_values[index_of_action] = -9999999999 #setujemo vrednost ove akcije na jako malu vrednost, da ne bi ponovo bila izabrana
        
    print(potencijalne_akcije)
    
    index_of_best = 0
    for i, current in enumerate(potencijalne_akcije):
        if current[1] > potencijalne_akcije[index_of_best][1]:
            index_of_best = i
            
    return int(potencijalne_akcije[index_of_best][0]), int(potencijalne_akcije[index_of_best][2])

def alreadyAddedAction(potencijalne_akcije, nova_potencijalna_akcija):
    for pa in potencijalne_akcije:
        if pa[0] == nova_potencijalna_akcija[0] and pa[2] == nova_potencijalna_akcija[2]:
            return True
    return False

def train(minibatch, target_model, model):
    for state, index_of_action, reward, next_state in minibatch:
        state = np.reshape(state, [1,state_size])
        next_state = np.reshape(next_state, [1,state_size])
        done = next_state[0][state_size-1]
        target = reward
        if not done:
            target = (reward + gamma * np.amax(target_model.predict(next_state)[0]))
        target_f = target_model.predict(state)
        target_f[0][index_of_action] = target
        model.fit(state, target_f, epochs=1, verbose=0)
    save_model(model, path_to_model, path_to_weights)


def load_model(path_to_model, path_to_weights):
    with open(path_to_model, "r") as json_file:
        json_string = json_file.read()
        model = model_from_json(json_string)
    model.load_weights(path_to_weights)
    print("Loaded model from disk")
    return model

def save_model(model, path_to_model, path_to_weights):
    model_json = model.to_json()
    with open(path_to_model, "w") as json_file:
        json_file.write(model_json)
    model.save_weights(path_to_weights)
    print("Saved model to disk")    

