#encoding:utf-8
from __future__ import unicode_literals

from django.db import models
# Create your models here.
class Arriver(models.Model):
    user_id = models.IntegerField() #签到的随机数字
    name = models.CharField(max_length=60)  #签到的标签
    std_name = models.CharField(max_length=60,unique=True)     #签到学生用户
    chats = models.CharField(max_length=60)  #签到的归属群组
    qiandao_datetime = models.DateTimeField()   #学生签到时间
    is_late = models.BooleanField()     #签到是否晚了
    late_reason = models.TextField()    #晚的理由
    class Meta:
        db_table = u'Arriver'

class Chats(models.Model):
    name = models.CharField(max_length=60,unique=True) #群组名字
    advise = models.CharField(max_length=128) #通知与群组
    Teacher = models.CharField(max_length=60) #聊天组的老师
    class Meta:
        db_table = u'Chats'

class Students(models.Model):
    username = models.CharField(max_length=60,unique=True)  #帐号
    passwd = models.CharField(max_length=60)    #密码
    name = models.CharField(max_length=60) #学生姓名
    std_id = models.BigIntegerField()  #学生学号
    chats = models.ManyToManyField(Chats) #学生与群组
    img = models.ImageField(upload_to="/static/images/",default="static/images/user.png")
    class Meta:
        db_table = u'Students'


class Teacher(models.Model):
    username = models.CharField(max_length=60,unique= True)  #帐号
    passwd = models.CharField(max_length=60)    #密码
    name = models.CharField(max_length=60) #老师姓名
    chats = models.ManyToManyField(Chats) #老师对应多个群组
    img = models.ImageField(upload_to="/static/images/",default="static/images/user.png")
    class Meta:
        db_table = u'Teacher'

class Msg(models.Model):
    content = models.CharField(max_length=60)
    chats = models.ManyToManyField(Chats)
    students = models.OneToOneField(Students)
    time = models.DateTimeField()
    class Meta:
        db_table = u'Msg'

class Advise(models.Model):
    title = models.CharField(max_length=60)
    Sign = models.CharField(max_length=20) #由老师设置的签到标志
    teacher = models.OneToOneField(Teacher)
    chats = models.CharField(max_length=60)
    time = models.DateTimeField()
    class Meta:
        db_table = u'Advise'