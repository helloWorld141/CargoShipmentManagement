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
	req = json.loads(open(reqpath).read())
	print("Request: " , req)

	imagereq = {'image': open(req['image'], 'rb')}
	res = requests.post(url, {'width': req['width']}, files = imagereq)
	print("Response: " , res.text)

