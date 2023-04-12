from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy, ProxyType
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

driver = webdriver.Chrome()
# Navigate to the webpage
driver.get("https://www.lambdatest.com/")

# Find all the elements that match the specified class name
elements = driver.find_elements(By.XPATH, '//*[@id="header"]/nav/div/div/div[2]/div/div//a[contains(@class, "desktop:block")]')

print(len(elements))
for element in elements:
    print(element.text)
    
    try:
        initial_url = driver.current_url
        print(element.text)
        element.click()
        WebDriverWait(driver, 1).until(EC.url_changes(initial_url))

        
    except:
        print("not working" + element.text)
        driver.find_element(By.XPATH, '//*[@id="m_class"]/div/div/div/h2/span')
        
        continue

# Close the browser
driver.quit()
