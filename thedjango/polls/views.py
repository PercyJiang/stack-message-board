from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.decorators import api_view
from django.utils import timezone
from .models import Question, Choice
from .serializers import QuestionSerializer


class QuestionViewSet(viewsets.ModelViewSet):
    queryset = Question.objects.all().order_by("-pub_date")
    serializer_class = QuestionSerializer


@api_view(["GET"])
def index(request):
    latest_question_list = Question.objects.order_by("-pub_date")[:5]
    serializer = QuestionSerializer(latest_question_list, many=True)
    return Response(serializer.data)


@api_view(["POST"])
def post(request):
    serializer = QuestionSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save(pub_date=timezone.now())
        return Response(serializer.data, status=status.HTTP_201_CREATED)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


@api_view(["GET"])
def detail(request, question_id):
    try:
        question = Question.objects.get(pk=question_id)
    except Question.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    serializer = QuestionSerializer(question)
    return Response(serializer.data)


@api_view(["GET"])
def results(request, question_id):
    try:
        question = Question.objects.get(pk=question_id)
    except Question.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    serializer = QuestionSerializer(question)
    return Response(serializer.data)


@api_view(["POST"])
def vote(request, question_id):
    try:
        question = Question.objects.get(pk=question_id)
    except Question.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    # Implement voting logic here
    return Response({"message": f"Voted on question {question_id}"})
