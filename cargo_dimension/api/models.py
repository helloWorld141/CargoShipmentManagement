from django.db import models

# Create your models here.
class Cargo(models.Model):
	id = models.CharField(max_length=50, primary_key=True)
	dims = models.CharField(max_length=50)
	stackable = models.NullBooleanField()
	tiltable = models.NullBooleanField()
	image_front = models.ImageField()
	image_top = models.ImageField()
