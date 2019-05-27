from django.urls import path
from . import views

urlpatterns = [
    path('predict/',views.predict,name='predict'),
    path('show/<str:foo>/',views.show,name='show'),
    path('show2/<str:foo>/',views.show2,name='show2')
    ]
