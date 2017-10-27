from django.http import JsonResponse, HttpResponse
from .utils.DimensionCalculator import calculateDims
import os, json
from django.views.decorators.csrf import csrf_exempt
from django.core.files import File
from django.shortcuts import render
from channels import Group
from django.conf import settings
from api.models import Cargo

path = os.path.dirname(os.path.abspath(__file__))
hostname = settings.HOST
def test(req):
	return JsonResponse({"status": "running"})

@csrf_exempt
def dims(req):
	if req.method == 'POST':
		mockArgs = {
			"id": 1401,
			"image": path+ "/resource/img/2.png",
			"width": "1"
		}
		print(req)
		print('Reqest: ', req.FILES, req.POST)
		if ( not req.FILES.get('image') or  not req.POST.get('width')):
			return HttpResponse(content="Bad request. Please include image and width", status=400)
		image = req.FILES['image']
		width = parse(req.POST['width'])
		imgpath = saveImage(image)
		realArgs = {
			"image": imgpath,
			"width": width
		}
		res = calculateDims(realArgs)
		print("Response: ", res)
		Group('staff').send({'text': json.dumps(res)}, immediately=True)
		return JsonResponse({"objects": res})

	if req.method == "GET":
		if req.GET.get('id'):
			id = req.GET['id']
			Group('cam').send({'text': '{"take_picture": true, "id": "'+ id + '"}'}, immediately=True)
			return JsonResponse({'status': 'pending',
				'subscribe_url': 'ws://' + hostname + '/staff/'
				})
		else:
			return JsonResponse({'message': 'please provide provide id of the cargo'})

def parse(width):
	w = width.split('\r\n')
	print(w)
	return w[0]

#image: class 'django.core.files.uploadedfile.InMemoryUploadedFile'
def saveImage(image):
	imgpath = path+ "/resource/img/" + image.name
	with open(imgpath, 'wb+') as f:
		for chunk in image.chunks():
			f.write(chunk)
	return imgpath

def testWSCam(req):
	return render(req, 'api/cam_ws_init.html')

def testWSStaff(req):
	return render(req, 'api/staff_ws_init.html')

@csrf_exempt
def cargo(req):
	if req.method == 'GET':
		cargo_list = Cargo.objects.all().value()
		return JsonResponse(list(cargo_list), safe=False)
	if req.method == 'POST':
		try :
			Cargo.objects.create(id="1401")
			return JsonResponse({'created': True})
		except e:
			print(e)
			return JsonResponse({'created': False})
		
		