from django.conf.urls import url
import views

urlpatterns = [
    url(r'^index/$', views.index),
    url(r'^Student/regist/$', views.Std_Reg),
    url(r'^Teacher/regist/$', views.Tch_Reg),
    url(r'^Student/login/$', views.Std_Log),
    url(r'^Teacher/login/$', views.Tch_Log),
    url(r'^logout/$', views.logout),
    url(r'^Teacher/crechat/$', views.Cre_Chat),
    url(r'^Student/addchat/$', views.Add_Chat),
    url(r'^Teacher/puarri/$', views.Pub_Arri),
    url(r'^Teacher/printdelarri/$', views.Print_Del_Arri),
    url(r'^Student/stdarri/$', views.Std_Arri),
    url(r'^json/$', views.JsonTest),

    url(r'^Teacher/Slogin/$', views.S_Tch_Log),
    url(r'^Teacher/Sregist/$', views.S_Tch_Reg),
    url(r'^Teacher/Screchat/$', views.S_Cre_Chat),
    url(r'^Teacher/Spuarri/$', views.S_Pub_Arri),
    url(r'^Student/Sregist/$', views.S_Std_Reg),
    url(r'^Student/Slogin/$', views.S_Std_Log),
    url(r'^Student/Saddchat/$', views.S_Add_Chat),
    url(r'^Student/Sstdarri/$', views.S_Std_Arri),
    url(r'^Teacher/Sprintdelarri/$', views.S_Print_Del_Arri),
]
