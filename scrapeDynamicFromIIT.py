import re
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
    
    try:
        # Open the target URL
        driver.get(url)
        extracted_data = []
        
        if (department == "project_positions"):
            # Find the table rows inside the tbody
            rows = driver.find_elements(By.CSS_SELECTOR, "table tbody tr")
            
            # extracted_data = []
            for row in rows:
                # Get the HTML content of the row
                row_html = row.get_attribute("outerHTML")

                # Define patterns to extract required details
                patterns = {
                    "date": r'<td>\s*([\d.]+)\s*</td>',
                    "name_of_post": r'<td>\s*([^<]+Position[^<]+)\s*</td>',
                    "advertisement_link": r'<td><a href="([^"]+)" target="_blank">Advertisement',
                    "application_link": r'<a class="text-maroon [^"]*" href="([^"]+)" target="_blank">Click here to apply</a>',
                    "last_date": r'Last date for submission application\s*:\s*([\d.]+)',
                    "status": r'<td>\s*<div>.*?<a class="text-maroon [^"]*" href="([^"]*)"'
                }

                # Extract information using regex
                extracted_row = {}
                for key, pattern in patterns.items():
                    match = re.search(pattern, row_html, re.DOTALL)
                    if match:
                        extracted_row[key] = match.group(1).strip()
                    else:
                        extracted_row[key] = "N/A"

                extracted_row["college"] = college
                extracted_row["department"] = department
                extracted_data.append(extracted_row)

            return extracted_data
            
        
        
        # Find all elements with the "team-info" class
        team_info_elements = driver.find_elements(By.CLASS_NAME, "single-team")
        # print("team_info_elements", team_info_elements)
        # extracted_data = []
        for element in team_info_elements:
            # print("HTML of element:",  element.get_attribute("outerHTML"), "\n\n\n")
            element = element.get_attribute("outerHTML")

            patterns = {
                "name": r'<h4(?: class="[^"]*")?><a [^>]+>([^<]+)</a></h4>',
                "position": r'<p>\s*<i class="fal fa-graduation-cap [^>]+"></i>\s*([^<]+)</p>',
                "degree": r'<p class="text-dark">([^<]+)</p>',
                "areas_of_interest": r'(?:<b>Areas of Interest:?</b>:?|<span class="fw-bold text-dark">\s*Areas of Interest:?\s*</span>)\s*([^<]+)',
                "phone": r'<i class="fal fa-phone-alt [^>]+"></i>\s*([\d\s]+)',
                "email": r'(?:Email : )?([^<\s]+@[a-zA-Z0-9.-]+)',
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

        return extracted_data
    except Exception as e:
        return {"error": str(e)}
    finally:
        driver.quit()
        

def scrape_website_allInfo():
    scraped_data = []
    for college, departments in links.items():
        for department in departments:
            data = scrape_website(college, department)
            if "error" in data:
                continue
            scraped_data.append(data)
    return scraped_data
        
    
    