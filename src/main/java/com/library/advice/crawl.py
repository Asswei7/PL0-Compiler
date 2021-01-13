from bs4 import BeautifulSoup
import requests
import json
import sys
from requests.packages.urllib3.exceptions import InsecureRequestWarning
requests.packages.urllib3.disable_warnings(InsecureRequestWarning)
class github_crawl():
	
	def __init__(self):
		# 初始化一些必要的参数
		self.login_headers = {
			"Referer": "https://github.com/",
			"Host": "github.com",
			"User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
		}
 
		self.logined_headers = {
			"Referer": "https://github.com/login",
			"Host": "github.com",
			"User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
		}
 
		self.headers = {
			'User-Agent': 'Mozilla/5.0',
		    'Authorization': 'token 80531720f7835b5861e87812aa773256086498d6',#换上自己的token认证
		    'Content-Type': 'application/json',
		    'Accept': 'application/json'
		}
		self.login_url = "https://github.com/login"
		self.post_url = "https://github.com/session"
		self.session = requests.Session()
       
	def get_results(self, repository, keyword):  
		url = "https://api.github.com/search/code?q={keyword}+in:file+language:python+repo:{w}".format(w=repository, keyword=keyword)
		try:
			req = Request(url, headers=self.headers)
			response = urlopen(req).read()
			results = json.loads(response.decode())
			for item in results['items']:
				repo_url = item["repository"]["html_url"]
				file_path = item['html_url']
				fork = item["repository"]["fork"]
				self.loader_csv(repo_url, file_path, fork, keyword)
 
		except Exception as e:
			print("获取失败")
 
 
	def loader_csv(self, repo_url, file_path, keyword):
		try:
			with open("path", "a") as csv_file:
				writer = csv.writer(csv_file)
				writer.writerow([keyword, repo_url, file_path])
			csv_file.close()
		except Exception as e:
			print(e) 
	def parse_loginPage(self):
		# 对登陆页面进行爬取，获取token值
		html = self.session.get(url=self.login_url, headers=self.login_headers, verify=False)
		Soup = BeautifulSoup(html.text, "lxml")
		token = Soup.find("input", attrs={"name": "authenticity_token"}).get("value")
 
		return token
	# 获得了登陆的一个参数
 
	def login(self, user_name, password,keyword):
		# 传进必要的参数，然后登陆
		post_data = {
			"commit": "Sign in",
			"utf8": "✓",
			"authenticity_token": self.parse_loginPage(),
			"login": user_name,
			"password": password
		}
		
		logined_html = self.session.post(url=self.post_url, data=post_data, headers=self.logined_headers, verify=False)
		if logined_html.status_code == 200:
			ls = self.parse_keyword(keyword)  # 获取了页面
		else:
			print("false")
		return ls
 
	def parse_keyword(self, keyword):
		# 解析登陆以后的页面，筛选出包含这个关键字的python代码
		user_repositorys = set()  # 集合用来存放作者名和库名
		try:
			dic = {}
			for i in range(2):
				url = "https://github.com/search?l=Java&p={id}&q={keyword}&type=Code".format(id=i+1, keyword=keyword)  # 循环的爬取页面信息
				resp = self.session.get(url=url, headers=self.login_headers, verify=False)
				soup = BeautifulSoup(resp.text, "lxml")
				pattern = soup.find_all(attrs={"class": "blob-code-inner"})
				#print("OK")
				
				for item in pattern:		
					cur = item.find(attrs={"class": "pl-k"})
					ls=[]
					if(cur!=None):
						if(cur.string=="import"):
							temp = item.find(attrs={"class": "pl-smi"})
							if(temp!=None):
								temp = temp.string
								#print(temp.string)
								#生成一个字典，存储每个键值对
								if(temp in dic.keys()):
									dic[temp]+=1
								else:
									dic[temp]=1
					else:
						continue                                
			dicSort = sorted(dic.items(),key = lambda x:x[1],reverse = True)
			#print(dicSort)
		except Exception as e:
			print(e)
		#print(type(dicSort))
		with open('F:\\java\\IDEA\\WorkSpace\\helpForProgram\\src\\main\\java\\com\\library\\advice\\test.txt','w') as f:
			for item in dicSort:
				f.write(str(item))
				f.write('\r')
		return dicSort
	
 
 
if __name__ == "__main__":
	strs = sys.argv[1]
	#strs = "import java.awt.Color;"
	#print(strs)
	x = github_crawl()
	#arg = "import java.awt.Color"
	ls = x.login("374857303@qq.com", "s374857303",strs)
	#print("OK")
	#x.parse_keyword("import java.awt.Color")
    