from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse
from django.template import loader
from django.core.files.storage import FileSystemStorage

from django.http import JsonResponse
from django.http import StreamingHttpResponse
from django.views.decorators.csrf import csrf_exempt

import cv2
#import imutils
import numpy as np
import os
import pandas as pd

from keras.models import Sequential
from keras.layers import Dense

import json


@csrf_exempt
def predict(request):
    '''lis = ['a','b','c','d']
    return HttpResponse("<h1>welcome to my app !</h1>")
    return JsonResponse(lis,safe=False)
    json_file = open('/home/pythonestpython/mysite/serve/model.json', 'r')
    loaded_model_json = json_file.read()
    json_file.close()
    loaded_model = model_from_json(loaded_model_json)
    symptoms_reported=["Heberden's node","Murphy's sign","Stahli's line","abdomen acute","abdominal bloating","abdominal tenderness","abnormal sensation","abnormally hard consistency","abortion","abscess bacterial"
    ,"absences finding","achalasia","ache","adverse effect","adverse reaction","agitation"]
    '''

    if request.method =='POST':

        data=json.loads(request.body.decode("utf-8"))
        symptoms_reported = []
        size = data["size"]
        for i in range(size):
            symptoms_reported.append(data[str(i)])
        loaded_model = Sequential()
        loaded_model.add(Dense(12, input_dim=404, init='uniform', activation='relu'))
        loaded_model.add(Dense(404, init='uniform', activation='relu'))
        loaded_model.add(Dense(149, init='uniform', activation='softmax'))
        loaded_model.load_weights("/home/pythonestpython/mysite/serve/model.h5")
        loaded_model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
        disease_df = pd.read_csv('/home/pythonestpython/mysite/serve/diseaselist.csv')
        symptom_df = pd.read_csv('/home/pythonestpython/mysite/serve/symptomlist.csv')
        x=np.zeros(404)
        for i in range(len(symptom_df['0'])):
            for j in symptoms_reported:
                if j==symptom_df['0'].values[i]:
                    x[i]=1
                    break
        arr=np.empty([5,404])
        for i in range(5):
            arr[i]=x
        pred=loaded_model.predict(arr)
        return StreamingHttpResponse(disease_df['Source'][np.argmax(pred,axis=1)[0]])

    #return JsonResponse(disease_df['Source'][np.argmax(pred,axis=1)].tolist(),safe=False)

def show(request,foo):
    df = pd.read_csv('/home/pythonestpython/mysite/serve/treatmentswiki.csv')
    return HttpResponse(df[df['Disease']==foo.replace('_',' ')]['Treatment'])
    #return HttpResponse(foo)

def show2(request,foo):
    df = pd.read_csv('/home/pythonestpython/mysite/serve/treatments2.csv')
    return HttpResponse(df[df['Disease']==foo.replace('_',' ')]['Treatment'])

