import re
from selenium import webdriver # type: ignore
from selenium.webdriver.common.by import By # type: ignore
from selenium.webdriver.chrome.service import Service # type: ignore
from selenium.webdriver.chrome.options import Options # type: ignore
from webdriver_manager.chrome import ChromeDriverManager# type: ignore

def scrape_website(college, department):
    url = "https://iittp.ac.in/computer-science-engineering-department"
    
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
    
    try:
        # Open the target URL
        driver.get(url)
        
        
        
        # Find all elements with the "team-info" class
        team_info_elements = driver.find_elements(By.CLASS_NAME, "single-team")
        # print("team_info_elements", team_info_elements)
        extracted_data = []
        for element in team_info_elements:
            # print("HTML of element:",  element.get_attribute("outerHTML"))
            element = element.get_attribute("outerHTML")

            patterns = {
                "name": r'<h4><a [^>]+>([^<]+)</a></h4>',
                "position": r'<p>\s*<i class="fal fa-graduation-cap [^>]+"></i>([^<]+)</p>',
                "degree": r'<p class="text-dark">([^<]+)</p>',
                "areas_of_interest": r'<b>Areas of Interest</b>:(.*?)(<p|</div>)',
                "phone": r'<i class="fal fa-phone-alt [^>]+"></i>(\d{4} \d{3} \d{4})',
                "email": r'Email : ([^<]+)',
                 "image_link": r'<div class="team-thumb">.*?<img src="([^"]+)" alt="img">',
            }

                # Extract information using regex
            ext_data = {}

            for key, pattern in patterns.items():
                match = re.search(pattern, element)
                if match:
                    ext_data[key] = match.group(1).strip()
                else:
                        ext_data[key] = "N/A"
            extracted_data.append(ext_data)
            # extracted_data.append({
            #     "name": name,
            #     "position": position,
            #     "degree": degree,
            #     "areas_of_interest": areas_of_interest,
            #     "phone": phone,
            #     "email": email,
            # })

        return extracted_data
    except Exception as e:
        return {"error": str(e)}
    finally:
        driver.quit()
    