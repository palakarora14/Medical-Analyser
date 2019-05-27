import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from keras.utils import np_utils

dataset = pd.read_csv('dataset_final.csv')
dataset.head()

X = dataset.iloc[:, 0:132].values
df = dataset.iloc[:,-1].values
y=pd.DataFrame(df)


from sklearn.preprocessing import LabelEncoder
labelencoder_Y=LabelEncoder()
y=labelencoder_Y.fit_transform(y)


dummy_y=np_utils.to_categorical(y)

from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X,dummy_y, test_size = 0.01, random_state = 0)

from sklearn.preprocessing import StandardScaler
sc = StandardScaler()
X_train = sc.fit_transform(X_train)
X_test = sc.transform(X_test)
#%%
import keras
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import Dropout

classifier = Sequential()

classifier.add(Dense(units = 65, kernel_initializer = 'uniform', activation = 'relu', input_dim = 132))
classifier.add(Dropout(p = 0.5))

classifier.add(Dense(units = 33, kernel_initializer = 'uniform', activation = 'tanh'))
classifier.add(Dropout(p = 0.5))

classifier.add(Dense(units = 33, kernel_initializer = 'uniform', activation = 'relu'))
classifier.add(Dropout(p = 0.5))

classifier.add(Dense(units = 44, kernel_initializer = 'uniform', activation = 'softmax'))

classifier.compile(optimizer = 'adam', loss = 'categorical_crossentropy', metrics = ['accuracy'])

classifier.fit(X_train, y_train, batch_size = 25, epochs = 500)

#%%
classifier.save('model.h5')

#%%
from keras.models import load_model
classifier =load_model('model.h5')

classifier.summary()
classifier.get_weights()
#%%
y_pred = classifier.predict(X_test)

y_pred = (y_pred > 0.5)

from sklearn.metrics import confusion_matrix
from sklearn.metrics import accuracy_score

cm=confusion_matrix(y_test.argmax(axis=1), y_pred.argmax(axis=1))
accuracy_score(y_test, y_pred)
#cm = confusion_matrix(y_test, y_pred)

#%%
from tensorflow.contrib import lite
converter = lite.TocoConverter.from_keras_model_file('model.h5')

tflite_model=converter.convert()
open("model.tflite","wb").write(tflite_model)


#%%
diseases=list(pd.unique(dataset['prognosis']))
ff=pd.DataFrame(y)
ff.columns=['categorical_disease']
o=list(ff.columns.values)
cat_diseases=list(pd.unique(ff['categorical_disease']))
disease_dict=dict(zip(cat_diseases,diseases))

import csv

with open('disease_dict.csv', 'w') as csv_file:
    writer = csv.writer(csv_file)
    for key, value in disease_dict.items():
       writer.writerow([key, value])


#disease_dict_interchange = {y:x for x,y in disease_dict.items()}

#%%
symptoms=list(pd.unique(dataset.columns.values))
symptoms=symptoms[0:len(symptoms)-1]  
l = list(range(0,132))
symptoms_dict=dict(zip(symptoms,l))

import csv

with open('symptoms_dict.csv', 'w') as csv_file:
    writer = csv.writer(csv_file)
    for key, value in symptoms_dict.items():
       writer.writerow([key, value])
       
#%%

for i in range(0,44):
    if y_pred[1][i]==True:
        print("the inflicted disease is {0}".format(disease_dict[i]))
        
#%%        
for i in range(0,len(y_pred)):
    for j in range(0,44):
        if y_pred[i][j]==True:
            print("target row: {0}".format(i+1))
            print("the inflicted disease is {0}\n\n".format(disease_dict[j]))
   




    
