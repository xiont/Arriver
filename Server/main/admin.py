from django.contrib import admin

# Register your models here.
from django.contrib import admin
from main.models import Arriver,Advise,Students,Teacher,Chats,Msg

# Register your models here.
class StudentsAdmin(admin.ModelAdmin):
    list_display = ('username', 'name','std_id')

class TeacherAdmin(admin.ModelAdmin):
    list_display = ('username', 'name')

class AdviseAdmin(admin.ModelAdmin):
    list_display = ('title', 'teacher','chats','Sign')

class ArriverAdmin(admin.ModelAdmin):
    list_display = ('user_id', 'std_name','chats')

class ChatsAdmin(admin.ModelAdmin):
    list_display = ('name', 'Teacher')

admin.site.register(Students,StudentsAdmin)
admin.site.register(Teacher,TeacherAdmin)
admin.site.register(Advise,AdviseAdmin)
admin.site.register(Arriver,ArriverAdmin)
admin.site.register(Chats,ChatsAdmin)
admin.site.register([Msg])