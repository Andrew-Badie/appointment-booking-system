"""Capture the running demo for visual review; no database writes."""
import base64
from pathlib import Path
from playwright.sync_api import sync_playwright

out = Path("ui-previews")
out.mkdir(exist_ok=True)
with sync_playwright() as p:
    browser = p.chromium.launch()
    page = browser.new_page(viewport={"width": 1440, "height": 1050}, device_scale_factor=1)
    page.goto("http://localhost:8080/FrontEnd/", wait_until="networkidle")
    for label, width, height in [("desktop", 1440, 1050), ("mobile", 390, 844)]:
        page.set_viewport_size({"width": width, "height": height})
        assert page.evaluate("document.documentElement.scrollWidth") <= width, "Page overflows " + label + " viewport"
        data = page.screenshot(path=str(out / (label + ".jpg")), full_page=True, type="jpeg", quality=75)
        print("UI_PREVIEW_" + label.upper() + ":" + base64.b64encode(data).decode(), flush=True)
    page.set_viewport_size({"width": 1440, "height": 1050})
    page.get_by_label("Username", exact=True).fill("AndrewBadie")
    page.get_by_label("Password", exact=True).fill("1234")
    page.get_by_role("button", name="Sign in").click()
    page.get_by_label("Service name").fill("Psychology")
    page.get_by_role("button", name="Search").click()
    page.wait_for_load_state("networkidle")
    data = page.screenshot(path=str(out / "results.jpg"), full_page=True, type="jpeg", quality=75)
    print("UI_PREVIEW_RESULTS:" + base64.b64encode(data).decode(), flush=True)
    browser.close()
