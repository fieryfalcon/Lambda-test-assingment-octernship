from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy, ProxyType
from selenium.webdriver.common.by import By

driver = webdriver.Chrome()
# Navigate to the webpage
driver.get("https://www.lambdatest.com/")

# Find all the elements that match the specified class name
right_elements = driver.find_elements(By.XPATH, '//*[@id="header"]/nav/div/div/div[2]/div/div/div[1]//a[contains(@class, "desktop:block")]')
left_elements = driver.find_elements(By.XPATH, '//*[@id="header"]/nav/div/div/div[2]/div/div/div[2]//*[contains(@class, "desktop:block")]')
elements = right_elements + left_elements
for element in elements:
    print(element.text)
    element.click()
    driver.back()
# Close the browser
driver.quit()
# Close the browser window


