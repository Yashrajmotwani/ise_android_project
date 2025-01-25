import re
from chooseScrapper import call_function_based_on_college
from selenium import webdriver # type: ignore
from selenium.webdriver.common.by import By # type: ignore
from selenium.webdriver.chrome.service import Service # type: ignore
from selenium.webdriver.chrome.options import Options # type: ignore
from webdriver_manager.chrome import ChromeDriverManager# type: ignore
from links import links


def scrape_website(college, department):    
    url = ""
    
    if college not in links.keys() or department not in links[college].keys():
         return {"error": "Invalid college or department or URL format. The correct format is /<college>/<department>."}
    
    url = links[college][department]
    
    
    
    # Set up Selenium with headless Chrome
    chrome_options = Options()
    chrome_options.add_argument("--headless")  # Run in headless mode
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    
    # Initialize the WebDriver
    # driver_path = r"C:\Users\dell\Downloads\chromedriver-win64\chromedriver-win64\chromedriver.exe" 
    # service = Service(driver_path) # For local execution
    # driver = webdriver.Chrome(service=service, options=chrome_options) # For local execution
    
    chrome_options.binary_location = "/usr/bin/google-chrome"  # Path to Chrome binary

    # Use the specified Chromedriver path
    service = Service("/usr/bin/chromedriver-linux64/chromedriver")  # Path to Chromedriver
    driver = webdriver.Chrome(service=service, options=chrome_options)# For Deployment
    data = call_function_based_on_college(college, department, url, driver)
    return data
    
        

def scrape_website_allInfo():
    scraped_data = []
    for college, departments in links.items():
        for department in departments:
            data = scrape_website(college, department)
            if "error" in data:
                continue
            scraped_data.append(data)
    
    return scraped_data
        
    
    