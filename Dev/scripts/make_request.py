import argparse, json, requests

if __name__ == "__main__":
	ap = argparse.ArgumentParser()
	ap.add_argument("-r", "--request", required=True,
		help="path to the request json file")
	ap.add_argument("-l", "--url", required=True,
		help="url to make request")
	args = vars(ap.parse_args())
	reqpath = args['request']
	url = args['url']
	reqstr = open(reqpath).read()
	print(type(reqstr))
	req = json.loads(reqstr)
	print("Request: " , req)

	imagereq = {'image': open(req['image'], 'rb')}
	res = requests.post(url, {'id': req['id']}, files = imagereq)
	print("Response: " , res.text)

