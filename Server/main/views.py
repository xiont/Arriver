#encoding:utf-8
from django.shortcuts import render
from django.shortcuts import render,render_to_response
from django.http import HttpResponse,HttpResponseRedirect
from django.template import RequestContext
from django import forms
from models import Students,Teacher,Chats,Advise,Arriver
from itertools import chain
# Create your views here.
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

#表单
class StudentsForm(forms.Form):
    username = forms.CharField(label=u'用户名',max_length=100)
    name = forms.CharField(label=u'姓名',max_length=100)
    std_id = forms.IntegerField(label=u'学号')
    passwd = forms.CharField(label=u'密    码',widget=forms.PasswordInput())
    passwd2 =  forms.CharField(label=u'再次输入',widget=forms.PasswordInput())

class StudentsLogForm(forms.Form):
    username = forms.CharField(label=u'用户名',max_length=100)
    passwd = forms.CharField(label=u'密    码',widget=forms.PasswordInput())

class TeacherForm(forms.Form):
    username = forms.CharField(label=u'用户名',max_length=100)
    name = forms.CharField(label=u'姓名',max_length=100)
    passwd = forms.CharField(label=u'密    码',widget=forms.PasswordInput())
    passwd2 =  forms.CharField(label=u'再次输入',widget=forms.PasswordInput())

class TeacherLogForm(forms.Form):
    username = forms.CharField(label=u'用户名',max_length=100)
    passwd = forms.CharField(label=u'密    码',widget=forms.PasswordInput())

class CreChatForm(forms.Form):
    name = forms.CharField(label=u'群组名',max_length=100)
    teacher = forms.CharField(label=u'老师',max_length=100)
    passwd = forms.CharField(label=u'密    码',widget=forms.PasswordInput())
    advise = forms.CharField(label=u'通知',max_length=100)

class AddChatForm(forms.Form):
    chat_name = forms.CharField(label=u'群组名',max_length=100)
    std_name = forms.CharField(label=u'名字',max_length=100)
    teacher = forms.CharField(label=u'老师',max_length=100)

class PubAdviseForm(forms.Form):
    title = forms.CharField(label=u'标题',max_length=100)
    chat = forms.CharField(label=u'群组名',max_length=100)
    teacher = forms.CharField(label=u'老师',max_length=100)
    sign = forms.CharField(label=u'标识符',max_length=100)

class PrintDelArriForm(forms.Form):
    teacher = forms.CharField(label=u'老师',max_length=100)
    title = forms.CharField(label=u'通知标题',max_length=100)
    chat = forms.CharField(label=u'群组名',max_length=100)
    send_email = forms.EmailField(label=u'接收邮箱')

class ArriverForm(forms.Form):
    user_id = forms.IntegerField(label=u'标识符') #签到的随机数字
    name = forms.CharField(max_length=60,label=u'签到标签')  #签到的标签
    std_name = forms.CharField(max_length=60,label=u'学生名字')     #签到学生用户
    chats = forms.CharField(max_length=60,label = u'签到群组')  #签到的归属群组
    late_reason = forms.CharField(max_length=60,label=u'迟到理由')    #晚的理由


#登陆成功
def index(req):
    #username = req.COOKIES.get('username','')
    try :
        username = req.COOKIES.get('username','')
        #username = req.session['user']
    except:
        username =''
    return render_to_response('index.html' ,{'username':username})

def Std_Reg(req):
    if req.method == 'POST':
        uf = StudentsForm(req.POST)
        if uf.is_valid():
            #获得表单数据
            username = uf.cleaned_data['username']
            name = uf.cleaned_data['name']
            std_id = uf.cleaned_data['std_id']
            passwd = uf.cleaned_data['passwd']
            passwd2 = uf.cleaned_data['passwd2']
            if passwd==passwd2:
                #添加到数据库
                Students.objects.create(username= username,passwd=passwd,std_id = std_id,name =name)
                return HttpResponseRedirect('/index/')
            else:
                return HttpResponse("对不起 信息出错啦")
    else:
        uf = StudentsForm()
    return render(req,'std_reg.html',{'uf':uf})

def Std_Log(req):
    if req.method == 'POST':
        uf = StudentsLogForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            username = uf.cleaned_data['username']
            passwd = uf.cleaned_data['passwd']
            #获取的表单数据与数据库进行比较
            user = Students.objects.filter(username__exact = username,passwd__exact = passwd)
            if user:
                #比较成功，跳转index
                response = HttpResponseRedirect('/index/')
                #将username写入浏览器cookie,失效时间为3600
                response.set_cookie('username',username,3600,True)
                #req.session['is_login'] = True
                #req.session['user'] = username
                return response
            else:
                #比较失败，还在login
                return HttpResponse("对不起 信息出错啦")
    else:
        uf = StudentsLogForm()
    return render(req,'std_login.html',{'uf':uf})

def Tch_Reg(req):
    if req.method == 'POST':
        uf = TeacherForm(req.POST)
        if uf.is_valid():
            #获得表单数据
            username = uf.cleaned_data['username']
            name = uf.cleaned_data['name']
            passwd = uf.cleaned_data['passwd']
            passwd2 = uf.cleaned_data['passwd2']
            if passwd==passwd2:
                #添加到数据库
                Teacher.objects.create(username= username,passwd=passwd,name =name)
                return HttpResponseRedirect('/index/')
            else:
                return HttpResponse("对不起 信息出错啦")
    else:
        uf = TeacherForm()
    return render(req,'tch_reg.html',{'uf':uf})

def Tch_Log(req):
    if req.method == 'POST':
        uf = TeacherLogForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            username = uf.cleaned_data['username']
            passwd = uf.cleaned_data['passwd']
            #获取的表单数据与数据库进行比较
            user = Teacher.objects.filter(username__exact = username,passwd__exact = passwd)
            if user:
                #比较成功，跳转index
                response = HttpResponseRedirect('/index/')
                #将username写入浏览器cookie,失效时间为3600
                response.set_cookie('username',username,3600,True)
                #req.session['is_login'] = True
                #req.session['user'] = username
                return response
            else:
                #比较失败，还在login
                return HttpResponse("对不起 信息出错啦")
    else:
        uf = TeacherLogForm()
    return render(req,'tch_log.html',{'uf':uf})


#退出
def logout(req):
    response = HttpResponseRedirect('/index/')
    #清理cookie里保存username
    try:
        response.delete_cookie('username')
        # del req.session['user']
        # req.session['is_login'] = False
    except:
        pass
    return response


def Add_Chat(req):#学生加入群组
    if req.method == 'POST':
        uf = AddChatForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            chat_name = uf.cleaned_data['chat_name']
            std_name = uf.cleaned_data['std_name']
            username = req.COOKIES.get('username','') #学生的帐号
            teacher = uf.cleaned_data['teacher']
            #获取的表单数据与数据库进行比较
            user = Students.objects.filter(username__exact = username,name__exact = std_name)[0]
            chat = Chats.objects.filter(name = chat_name,Teacher = teacher)[0]
            if user and chat:
                user.chats.add(chat)
                user.save()
                return HttpResponseRedirect('/index/')
            else:
                return HttpResponse("对不起 信息出错啦")
    else:
        uf = AddChatForm()

    return render(req,'add_chat.html',{'uf':uf})

def Cre_Chat(req):#老师创建群组
     if req.method == 'POST':
        uf = CreChatForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            name = uf.cleaned_data['name']
            advise = uf.cleaned_data['advise']
            passwd = uf.cleaned_data['passwd']
            username = req.COOKIES.get('username','') #老师的帐号
            teacher = uf.cleaned_data['teacher']
            #获取的表单数据与数据库进行比较
            user = Teacher.objects.filter(username__exact = username,name = teacher,passwd__exact = passwd)[0]
            if user:
                chat = Chats.objects.create(name=name,Teacher=teacher,advise=advise)
                user.chats.add(chat)
                user.save()
                return HttpResponseRedirect('/index/')
            else:
                return HttpResponse("对不起 信息出错啦")
     else:
        uf = CreChatForm()
     return render(req,'cre_chat.html',{'uf':uf})

import pytz
def Std_Arri(req): #学生签到
    if req.method == 'POST':
        uf = ArriverForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            user_id = uf.cleaned_data['user_id']
            name = uf.cleaned_data['name']
            std_name = uf.cleaned_data['std_name']
            chats = uf.cleaned_data['chats']
            username = req.COOKIES.get('username','') #学生的帐号
            late_reason = uf.cleaned_data['late_reason']
            #获取的表单数据与数据库进行比较
            user = Students.objects.filter(username__exact = username,name__exact = std_name)[0]
            advise = Advise.objects.filter(Sign = str(user_id))[0]
            if user and advise:
                tz = pytz.timezone('Asia/Shanghai')
                cuttime = Advise.objects.filter(Sign = str(user_id))[0].time + datetime.timedelta(hours=1)
                print cuttime
                print datetime.datetime.now(tz)
                if((datetime.datetime.now(tz)) >= cuttime):
                    islate = True
                else :
                    islate = False
                Arriver.objects.create(user_id = user_id,name = name,std_name = std_name,chats = chats,qiandao_datetime = datetime.datetime.now(),is_late = islate,late_reason = late_reason)
                return HttpResponseRedirect('/index/')
            else:
                return HttpResponse("对不起 信息出错啦")
    else:
        uf = ArriverForm()
    return render(req,'std_arri.html',{'uf':uf})



import datetime
def Pub_Arri(req): #老师发布签到通知
     if req.method == 'POST':
        uf = PubAdviseForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            title = uf.cleaned_data['title']
            chat = uf.cleaned_data['chat']
            teacher = uf.cleaned_data['teacher']
            sign = uf.cleaned_data['sign']
            username = req.COOKIES.get('username','') #老师的帐号
            #获取的表单数据与数据库进行比较
            user = Teacher.objects.filter(username__exact = username,name = teacher)[0]
            if user:
                Advise.objects.create(title = title,Sign =sign,teacher = Teacher.objects.filter(name = teacher)[0],chats = chat,time = datetime.datetime.now())
                return HttpResponseRedirect('/index/')
            else:
                return HttpResponse("对不起 信息出错啦")
     else:
         uf = PubAdviseForm()
     return render(req,'pub_advise.html',{'uf':uf})


import  csv,os
from Server import settings
def Print_Del_Arri(req): #老师打印签到情况
    if req.method == 'POST':
        uf = PrintDelArriForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            title = uf.cleaned_data['title']
            teacher = uf.cleaned_data['teacher']
            chat = uf.cleaned_data['chat']
            send_email = uf.cleaned_data['send_email']
            username = req.COOKIES.get('username','') #老师的帐号
            #获取的表单数据与数据库进行比较
            user = Teacher.objects.filter(username__exact = username,name = teacher)[0]
            if user:
                Arrivers = Arriver.objects.filter(user_id = Advise.objects.filter(teacher = Teacher.objects.filter(name=teacher)[0])[0].Sign,chats = chat)
                dir = os.path.join(settings.BASE_DIR,'CSV_data',str(user.id))#+'\\Arriver.csv').replace("\\",'/')
                files = (os.path.join(settings.BASE_DIR,'CSV_data',str(user.id))+'\\Arriver.csv').replace("\\",'/')
                if os.path.exists(dir):
                    pass
                else:
                    os.makedirs(dir)
                print dir
                csvfile = file(files, 'wb')
                writer = csv.writer(csvfile)
                #先写入columns_name
                writer.writerow(["学生姓名","群组","学号","签到时间","是否迟到","迟到理由"])
                for item in Arrivers:
                    writer.writerow([str(item.std_name),str(item.chats),str(Students.objects.filter(name = item.std_name)[0].std_id),str(item.qiandao_datetime.strftime('%Y-%m-%d %H:%M:%S')),str(item.is_late),str(item.late_reason)])
                    print item.std_name,item.chats,Students.objects.filter(name = item.std_name)[0].std_id,item.qiandao_datetime,item.is_late,item.late_reason
                csvfile.close()
                txt = str(Advise.objects.filter(title = title,teacher = Teacher.objects.filter(name=teacher)[0])[0].time.strftime('%Y-%m-%d %H:%M:%S')+chat+"签到情况").encode("utf-8")
                sendemail(send_email,files,txt,dir)
                Advise.objects.filter(title = title,teacher = Teacher.objects.filter(name=teacher)[0]).delete()
                Arrivers.delete()

                return HttpResponseRedirect('/index/')
            else:
                return HttpResponse("对不起 信息出错啦")
    else:
        uf = PrintDelArriForm()
    return render(req,'print_del_arri.html',{'uf':uf})



import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.application import MIMEApplication

def sendemail(send_email,files,txt,dir):
    _user = "1018589158@qq.com"
    _pwd  = "xneipegwmzvcbbad"
    _to   = send_email

    #如名字所示Multipart就是分多个部分
    msg = MIMEMultipart()
    msg["Subject"] = "签到统计"
    msg["From"]    = _user
    msg["To"]      = _to

    #---这是文字部分---
    part = MIMEText(txt)
    msg.attach(part)

    gbkfile = (dir+'\\Arriver_GBK.csv').replace("\\",'/')
    #---这是附件部分---
    #xlsx类型附件
    UTF8_2_GBK(files,gbkfile)
    part = MIMEApplication(open(gbkfile,'rb').read())
    part.add_header('Content-Disposition', 'attachment', filename=gbkfile)
    msg.attach(part)

    s = smtplib.SMTP_SSL("smtp.qq.com", timeout=30)#连接smtp邮件服务器,端口默认是25
    s.login(_user, _pwd)#登陆服务器
    s.sendmail(_user, _to, msg.as_string())#发送邮件
    s.close()

import codecs
def ReadFile(filePath,encoding="utf-8"):
    with codecs.open(filePath,"r",encoding) as f:
        return f.read()

def WriteFile(filePath,u,encoding="gbk"):
    with codecs.open(filePath,"w",encoding) as f:
        f.write(u)

def UTF8_2_GBK(src,dst):
    content = ReadFile(src,encoding="utf-8")
    WriteFile(dst,content,encoding="gbk")

import json
def JsonTest(req):
    dict = {"first":"ff","second":"cc"}
    jsons = json.dumps(dict)
    return HttpResponse(jsons)


from django.views.decorators.csrf import csrf_exempt
@csrf_exempt
def S_Tch_Log(req):
    if req.method == 'POST':
        print req.POST
        uf = TeacherLogForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            username = uf.cleaned_data['username']
            passwd = uf.cleaned_data['passwd']
            #获取的表单数据与数据库进行比较
            user = Teacher.objects.filter(username__exact = username,passwd__exact = passwd)
            if user:
                #比较成功，跳转index
                #response = HttpResponseRedirect('/index/')
                #将username写入浏览器cookie,失效时间为3600
                #response.set_cookie('username',username,3600,True)
                #req.session['is_login'] = True
                #req.session['user'] = username
                return HttpResponse(json.dumps({'data':'True'}))
            else:
                #比较失败，还在login
                return HttpResponse(json.dumps({'data':'False'}))
    else:
        uf = TeacherLogForm()
    return HttpResponse(json.dumps({'data':'False'}))

@csrf_exempt
def S_Tch_Reg(req):
    if req.method == 'POST':
        uf = TeacherForm(req.POST)
        if uf.is_valid():
            #获得表单数据
            username = uf.cleaned_data['username']
            name = uf.cleaned_data['name']
            passwd = uf.cleaned_data['passwd']
            passwd2 = uf.cleaned_data['passwd2']
            if passwd==passwd2:
                #添加到数据库
                Teacher.objects.create(username= username,passwd=passwd,name =name)
                return HttpResponse(json.dumps({'data':'True'}))
            else:
                return HttpResponse(json.dumps({'data':'False'}))
    else:
        uf = TeacherForm()
    return HttpResponse(json.dumps({'data':'False'}))

@csrf_exempt
def S_Cre_Chat(req):#老师创建群组
    try:
        if req.method == 'POST':
            uf = CreChatForm(req.POST)
            if uf.is_valid():
                #获取表单用户密码
                name = uf.cleaned_data['name']
                advise = uf.cleaned_data['advise']
                passwd = uf.cleaned_data['passwd']
                teacher = uf.cleaned_data['teacher']
                #获取的表单数据与数据库进行比较
                user = Teacher.objects.filter(name = teacher,passwd__exact = passwd)[0]
                if user:
                    chat = Chats.objects.create(name=name,Teacher=teacher,advise=advise)
                    user.chats.add(chat)
                    user.save()
                    return HttpResponse(json.dumps({'data':'True'}))
                else:
                    return HttpResponse(json.dumps({'data':'False'}))
        else:
            uf = CreChatForm()
        return HttpResponse(json.dumps({'data':'False'}))
    except:
        return HttpResponse(json.dumps({'data':'False'}))

@csrf_exempt
def S_Pub_Arri(req): #老师发布签到通知
    try:
        if req.method == 'POST':
            uf = PubAdviseForm(req.POST)
            if uf.is_valid():
                #获取表单用户密码
                title = uf.cleaned_data['title']
                chat = uf.cleaned_data['chat']
                teacher = uf.cleaned_data['teacher']
                sign = uf.cleaned_data['sign']
                #获取的表单数据与数据库进行比较
                user = Teacher.objects.filter(name = teacher)[0]
                if user:
                    Advise.objects.create(title = title,Sign =sign,teacher = Teacher.objects.filter(name = teacher)[0],chats = chat,time = datetime.datetime.now())
                    return HttpResponse(json.dumps({'data':'True'}))
                else:
                    return HttpResponse(json.dumps({'data':'False'}))
        else:
            uf = PubAdviseForm()
        return HttpResponse(json.dumps({'data':'False'}))
    except:
        return HttpResponse(json.dumps({'data':'False'}))


@csrf_exempt
def S_Std_Reg(req):
    if req.method == 'POST':
        uf = StudentsForm(req.POST)
        if uf.is_valid():
            #获得表单数据
            username = uf.cleaned_data['username']
            name = uf.cleaned_data['name']
            std_id = uf.cleaned_data['std_id']
            passwd = uf.cleaned_data['passwd']
            passwd2 = uf.cleaned_data['passwd2']
            if passwd==passwd2:
                #添加到数据库
                Students.objects.create(username= username,passwd=passwd,std_id = std_id,name =name)
                return HttpResponse(json.dumps({'data':'True'}))
            else:
                return HttpResponse(json.dumps({'data':'False'}))
    else:
        uf = StudentsForm()
    return HttpResponse(json.dumps({'data':'False'}))


@csrf_exempt
def S_Std_Log(req):
    if req.method == 'POST':
        uf = StudentsLogForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            username = uf.cleaned_data['username']
            passwd = uf.cleaned_data['passwd']
            #获取的表单数据与数据库进行比较
            user = Students.objects.filter(username__exact = username,passwd__exact = passwd)
            if user:
                return HttpResponse(json.dumps({'data':'True'}))
            else:
                #比较失败，还在login
                return HttpResponse(json.dumps({'data':'False'}))
    else:
        uf = StudentsLogForm()
    return HttpResponse(json.dumps({'data':'False'}))


@csrf_exempt
def S_Add_Chat(req):#学生加入群组
    if req.method == 'POST':
        uf = AddChatForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            chat_name = uf.cleaned_data['chat_name']
            std_name = uf.cleaned_data['std_name']
            teacher = uf.cleaned_data['teacher']
            #获取的表单数据与数据库进行比较
            user = Students.objects.filter(name__exact = std_name)[0]
            chat = Chats.objects.filter(name = chat_name,Teacher = teacher)[0]
            if user and chat:
                user.chats.add(chat)
                user.save()
                return HttpResponse(json.dumps({'data':'True'}))
            else:
                return HttpResponse(json.dumps({'data':'False'}))
    else:
        uf = AddChatForm()

    return HttpResponse(json.dumps({'data':'False'}))


@csrf_exempt
def S_Std_Arri(req): #学生签到
    if req.method == 'POST':
        print req.POST
        uf = ArriverForm(req.POST)
        if uf.is_valid():
            #获取表单用户密码
            user_id = uf.cleaned_data['user_id']
            name = uf.cleaned_data['name']
            std_name = uf.cleaned_data['std_name']
            chats = uf.cleaned_data['chats']
            late_reason = uf.cleaned_data['late_reason']
            #获取的表单数据与数据库进行比较
            user = Students.objects.filter(name__exact = std_name)[0]
            advise = Advise.objects.filter(Sign = str(user_id))[0]
            if user and advise:
                tz = pytz.timezone('Asia/Shanghai')
                cuttime = Advise.objects.filter(Sign = str(user_id))[0].time + datetime.timedelta(hours=1)
                print cuttime
                print datetime.datetime.now(tz)
                if((datetime.datetime.now(tz)) >= cuttime):
                    islate = True
                else :
                    islate = False
                try:
                    Arriver.objects.create(user_id = user_id,name = name,std_name = std_name,chats = chats,qiandao_datetime = datetime.datetime.now(),is_late = islate,late_reason = late_reason)
                except:
                    return HttpResponse(json.dumps({'data':'False'}))
                return HttpResponse(json.dumps({'data':'True'}))
            else:
                return HttpResponse(json.dumps({'data':'False'}))
    else:
        uf = ArriverForm()
    return HttpResponse(json.dumps({'data':'False'}))



@csrf_exempt
def S_Print_Del_Arri(req): #老师打印签到情况
    try:
        if req.method == 'POST':
            uf = PrintDelArriForm(req.POST)
            if uf.is_valid():
                #获取表单用户密码
                title = uf.cleaned_data['title']
                teacher = uf.cleaned_data['teacher']
                chat = uf.cleaned_data['chat']
                send_email = uf.cleaned_data['send_email']
                #获取的表单数据与数据库进行比较
                user = Teacher.objects.filter(name = teacher)[0]
                if user:
                    Arrivers = Arriver.objects.filter(user_id = Advise.objects.filter(teacher = Teacher.objects.filter(name=teacher)[0])[0].Sign,chats = chat)
                    dir = os.path.join(settings.BASE_DIR,'CSV_data',str(user.id))#+'\\Arriver.csv').replace("\\",'/')
                    files = (os.path.join(settings.BASE_DIR,'CSV_data',str(user.id))+'\\Arriver.csv').replace("\\",'/')
                    if os.path.exists(dir):
                        pass
                    else:
                        os.makedirs(dir)
                    print dir
                    csvfile = file(files, 'wb')
                    writer = csv.writer(csvfile)
                    #先写入columns_name
                    writer.writerow(["学生姓名","群组","学号","签到时间","是否迟到","迟到理由"])
                    for item in Arrivers:
                        writer.writerow([str(item.std_name),str(item.chats),str(Students.objects.filter(name = item.std_name)[0].std_id),str(item.qiandao_datetime.strftime('%Y-%m-%d %H:%M:%S')),str(item.is_late),str(item.late_reason)])
                        print item.std_name,item.chats,Students.objects.filter(name = item.std_name)[0].std_id,item.qiandao_datetime,item.is_late,item.late_reason
                    csvfile.close()
                    txt = str(Advise.objects.filter(title = title,teacher = Teacher.objects.filter(name=teacher)[0])[0].time.strftime('%Y-%m-%d %H:%M:%S')+chat+"签到情况").encode("utf-8")
                    sendemail(send_email,files,txt,dir)
                    Advise.objects.filter(title = title,teacher = Teacher.objects.filter(name=teacher)[0]).delete()
                    Arrivers.delete()

                    return HttpResponse(json.dumps({'data':'True'}))
                else:
                    return HttpResponse(json.dumps({'data':'False'}))
        else:
            uf = PrintDelArriForm()
        return HttpResponse(json.dumps({'data':'False'}))
    except Exception :
        print Exception.message
        return HttpResponse(json.dumps({'data':'False'}))