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
    
    driver_path = r"C:\Users\dell\Downloads\chromedriver-win64\chromedriver-win64\chromedriver.exe" 
    service = Service(driver_path)
    driver = webdriver.Chrome(service=service, options=chrome_options)
    
    try:
        # Open the target URL
        driver.get(url)
        
        # Find all elements with the "team-info" class
        team_info_elements = driver.find_elements(By.CLASS_NAME, "team-info")
        print("team_info_elements", team_info_elements)
        extracted_data = []
        for element in team_info_elements:
            print("HTML of element:",  element.get_attribute("outerHTML"))
            name = element.find_element(By.TAG_NAME, "h4").text.strip()

        # Extracting position (this may be part of a degree or separate)
            position = "N/A"
            position_elements = element.find_elements(By.CSS_SELECTOR, "p > i.fal.fa-graduation-cap")
            if position_elements:
                position = position_elements[0].find_element(By.XPATH, "ancestor::p").text.strip()

            # Extracting degree
            degree = "N/A"
            degree_elements = element.find_elements(By.CSS_SELECTOR, "p.text-dark")
            for detail in degree_elements:
                text = detail.text.strip()
                if "Ph.D" in text:
                    degree = text

            # Extracting areas of interest
            areas_of_interest = "N/A"
            for detail in degree_elements:
                if "Areas of Interest" in detail.text:
                    areas_of_interest = detail.text.split(":")[1].strip()

            # Extracting phone number (office phone or general contact)
            phone = "N/A"
            for detail in degree_elements:
                if "Phone" in detail.text or "Tel" in detail.text:
                    phone = detail.text.split(":")[1].strip()

            # Extracting mobile number (if any)
            mobile = "N/A"
            phone_details = element.find_elements(By.CSS_SELECTOR, "p.text-dark")
            for detail in phone_details:
                if "mobile" in detail.text.lower() or "cell" in detail.text.lower():
                    mobile = detail.text.split(":")[1].strip()

            # Extracting email
            email = "N/A"
            for detail in degree_elements:
                if "Email" in detail.text:
                    email = detail.text.split(":")[1].strip()


            extracted_data.append({
                "name": name,
                "position": position,
                "degree": degree,
                "areas_of_interest": areas_of_interest,
                "phone": phone,
                "email": email,
            })

        return extracted_data
    except Exception as e:
        return {"error": str(e)}
    finally:
        driver.quit()
