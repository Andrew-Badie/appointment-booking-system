"""Integration check for a NEW disposable Compose stack; creates a demo booking."""
import http.cookiejar
import os
import subprocess
import time
import urllib.error
import urllib.parse
import urllib.request

base = "http://127.0.0.1:" + os.environ.get("APP_PORT", "8080") + "/FrontEnd/"
cookies = http.cookiejar.CookieJar()
browser = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cookies))

def post(data):
    request = urllib.request.Request(base + "FrontEnd", data=urllib.parse.urlencode(data).encode())
    try:
        with browser.open(request, timeout=30) as response:
            return response.status, response.read().decode()
    except urllib.error.HTTPError as error:
        return error.code, error.read().decode()

def check(condition, message):
    if not condition:
        raise AssertionError(message)
    print("PASS: " + message, flush=True)

status, body = post(dict(pageName="search", query="Psychology"))
check(status == 200 and "Psychology" in body and "1234" in body, "anonymous search renders database result")
for fields in [dict(username="wrong", password="wrong"), dict(username="AndrewBadie"),
               dict(username="", password=""), dict(username="AndrewBadie", password="wrong")]:
    status, body = post(dict(pageName="login", **fields))
    check(status == 401 and "Login unsuccessful" in body, "invalid or missing credentials are rejected")
status, body = post(dict(pageName="login", username="AndrewBadie", password="1234"))
check(status == 200 and "Greetings AndrewBadie" in body, "demo login renders JSP")
status, body = post(dict(pageName="search", query="Psychology"))
check(status == 200 and 'value="1234"' in body, "authenticated search offers booking")
status, body = post(dict(pageName="book", code="1234"))
check(status == 200 and "Booked" in body, "booking succeeds and updated status renders")

def sql(service, database, query):
    return subprocess.check_output(
        ["docker", "compose", "exec", "-T", "-e", "MYSQL_PWD=student", service,
         "mysql", "-uroot", "-N", "-B", database, "-e", query],
        text=True, timeout=30).strip()

query = "SELECT COUNT(*) FROM APPOINTMENT_Book WHERE code='1234' AND username='AndrewBadie'"
for attempt in range(30):
    if sql("confirmdb", "confirm_LBS", query) == "1":
        break
    time.sleep(2)
else:
    raise AssertionError("Booking event did not reach the confirmation database")
check(True, "KubeMQ event reached confirmation database")
status, body = post(dict(pageName="book", code="1234"))
check(status == 409, "duplicate booking is rejected")
subprocess.run(["docker", "compose", "restart", "bookdb"], check=True, timeout=60)
for attempt in range(30):
    try:
        count = sql("bookdb", "book_LBS", "SELECT COUNT(*) FROM APPOINTMENT_Book WHERE code='1234' AND userid='AndrewBadie'")
        if count == "1":
            break
    except subprocess.CalledProcessError:
        pass
    time.sleep(2)
else:
    raise AssertionError("Booking did not survive database restart")
check(True, "booking survives database restart")
