import re
from selenium.webdriver.common.by import By # type: ignore
function_registry = {}

# Decorator to register functions
def register_function(name):
    def wrapper(func):
        function_registry[name] = func
        return func
    return wrapper

# Define your functions and register them
# @register_function("iit")
# def abce(input_string):
#     print(f"Called abce with input: {input_string}")

# @register_function("bcd")
# def bcde(input_string):
#     print(f"Called bcde with input: {input_string}")

    
    
@register_function("iittp")
def scrape_iittp(college, department, url, driver):
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
                "posting_date": r'<td>\s*([\d.]+)\s*</td>',
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
    else:
        
    
    
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
            ext_data["college"] = college
            ext_data["department"] = department
            extracted_data.append(ext_data)
        
    # for doc in extracted_data:
    #     if "_id" in doc.keys():
    #         print("changed doc id \n\n")
    #         doc["_id"] = str(doc["_id"])
    return extracted_data


@register_function("iitg")
def scrape_iittp(college, department, url, driver):
    # Open the target URL
    driver.get(url)
    extracted_data = []
    
    if (department == "project_positions"):
        # Find the table rows inside the tbody
        rows = driver.find_elements(By.CSS_SELECTOR, "table tbody tr")

    # Loop through each row and extract data
        for row in rows:
            # Get the HTML content of the row
            row_html = row.get_attribute("outerHTML")

            
            # Extract the individual columns from the row
            if "Post" in row_html and "Discipline" in row_html:  # Skip the header row
                continue

        # Extract the individual columns from the row
            td_tags = row.find_elements(By.TAG_NAME, 'td')

            if len(td_tags) == 7:  # Expected number of columns (7)
                # Extract and clean the data from each column
                extracted_row = {
                    "name_of_post": td_tags[0].text.strip(),
                    "discipline": td_tags[1].text.strip(),
                    "pi_name": td_tags[2].text.strip(),
                    "posting_date": td_tags[3].text.strip(),
                    "last_date": td_tags[4].text.strip(),
                    "advertisement_link": td_tags[5].find_element(By.TAG_NAME, 'a').get_attribute('href') if len(td_tags[5].find_elements(By.TAG_NAME, 'a')) > 0 else "N/A",  # Extract PDF link
                    "status": td_tags[6].find_element(By.TAG_NAME, 'b').text.strip()  # Extract status inside <b> tag
                }
            extracted_row["college"] = college
            extracted_row["department"] = department
            extracted_data.append(extracted_row)
    else:
        
    
    
        # Find all elements with the "team-info" class
        faculty_cards = driver.find_elements(By.CSS_SELECTOR, ".masonry__item .card")

        for card in faculty_cards:
            # Get the outer HTML of the card
            card_html = card.get_attribute("outerHTML")

            # Define regex patterns for the required details
            patterns = {
                "profile_link": r'<a href="([^"]+)"',  # Link to the profile
                "image_src": r'<img[^>]+src="([^"]+)"',  # Image source URL
                "name": r'<a class="h5"[^>]*>([^<]+)</a>',  # Name of the faculty
                "position": r'<span><strong><b>([^<]+)</b>',  # Position/Title
                "qualification": r'<span>(PhD[^<]*)</span>',  # Qualification
                "research_areas": r'([A-Za-z, \n]+(?:, [A-Za-z ]+)*)(?:<br>)?'  # Research areas, before or after <br>
            }

            # Extract information using regex
            extracted_row = {}
            for key, pattern in patterns.items():
                match = re.search(pattern, card_html)
                if match:
                    extracted_row[key] = match.group(1).strip()
                else:
                    extracted_row[key] = "N/A"

            # Add college and department metadata
            extracted_row["college"] = college
            extracted_row["department"] = department

            # Append to the extracted data
            extracted_data.append(extracted_row)
            
    return extracted_data
    
    
        


    
def call_function_based_on_college(college, department, url, driver):
    if college in function_registry:
        try:
            data = function_registry[college](college, department, url, driver)
            return data
        except Exception as e:
            return {"error": str(e)}
        finally:
            driver.quit()
    else:
        return (f"No function found for input: {college}")
    
    