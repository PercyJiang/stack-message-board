from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register(r"questions", views.QuestionViewSet)
urlpatterns = [
    path("", include(router.urls)),
    path("index/", views.index, name="index"),
    path("post/", views.post, name="post"),
    path("<int:question_id>/", views.detail, name="detail"),
    path("<int:question_id>/results/", views.results, name="results"),
    path("<int:question_id>/vote/", views.vote, name="vote"),
]
