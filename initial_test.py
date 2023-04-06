from browsermobproxy import Server

import json




server = Server("./browsermob-proxy-2.1.4/bin/browsermob-proxy")
server.start()
proxy = server.create_proxy()

from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy, ProxyType

# Configure Selenium to use the proxy
proxy_url = proxy.proxy
webdriver.DesiredCapabilities.CHROME['proxy'] = {
    "httpProxy": proxy_url,
    "ftpProxy": proxy_url,
    "sslProxy": proxy_url,
    "noProxy": None,
    "proxyType": "MANUAL",
    "class": "org.openqa.selenium.Proxy",
    "autodetect": False
}

# Launch the browser using the configured WebDriver
driver = webdriver.Chrome()
driver.get("https://www.lambdatest.com/")
title = driver.title
print(title)
driver.implicitly_wait(5)


logs = proxy.har['log']['entries']

with open('logs.json', 'w') as f:
    f.write(json.dumps(logs))
