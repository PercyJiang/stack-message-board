from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.decorators import api_view
from django.utils import timezone
from .models import Question, Choice
from .serializers import QuestionSerializer, ChoiceSerializer


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


@api_view(["POST"])
def post(request):
    question_serializer = QuestionSerializer(data=request.data)
    choices_serializer = ChoiceSerializer(data=request.data.get("choices"), many=True)

    if not question_serializer.is_valid():
        return Response(question_serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    if not choices_serializer.is_valid():
        return Response(choices_serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    question = question_serializer.save()
    for choice_data in choices_serializer.validated_data:
        Choice.objects.create(question=question, **choice_data)

    return Response(question_serializer.data, status=status.HTTP_201_CREATED)


@api_view(["GET"])
def get_all(request):
    questions = Question.objects.all()
    questions_data = []

    for question in questions:
        question_serializer = QuestionSerializer(question)
        choices = Choice.objects.filter(question=question)
        choices_serializer = ChoiceSerializer(choices, many=True)
        question_data = question_serializer.data
        question_data["choices"] = choices_serializer.data
        questions_data.append(question_data)

    return Response(questions_data)
