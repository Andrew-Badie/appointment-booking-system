<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ryerson.ca.helper.Appointment,ryerson.ca.helper.AppointmentsXML"%>
<%! private String escape(Object value) { return value == null ? "" : value.toString().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;"); } %>
<% AppointmentsXML data = (AppointmentsXML) request.getAttribute("appointmentResults");
boolean searched = "search".equals(request.getParameter("pageName"));
int count = data == null || data.getAppointments() == null ? 0 : data.getAppointments().size(); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Appointment Booking System</title>
<link rel="stylesheet" href="assets/app.css">
</head>
<body>
<a class="skip" href="#main">Skip to content</a>
<div class="shell">
<header>
<a class="brand" href="./">COE692 <span>Appointment project</span>
</a>
<nav aria-label="Main">
<a href="./">Search</a>
</nav>
</header>
<main id="main">
<section class="page-heading">
<h1>Appointment Booking System</h1>
<p>Search services and book an available appointment.</p>
</section>
<div class="layout">
<div>
<section class="panel search-panel" aria-labelledby="search-heading">
<div class="section-top">
<h2 id="search-heading">Search appointments</h2>
</div>
<form class="search-form" action="FrontEnd" method="post">
<input type="hidden" name="pageName" value="search">
<div class="field">
<label for="query">Service name</label>
<input id="query" name="query" type="search" placeholder="e.g. Psychology" value="<%=escape(request.getParameter("query"))%>" required>
</div>
<button type="submit">Search</button>
</form>
<p class="hint">Example: <a href="FrontEnd?pageName=search&amp;query=Psychology">Psychology</a>.</p>
</section>
<section aria-labelledby="results-heading">
<div class="section-top">
<h2 id="results-heading">Search results</h2>
<span class="number">
<%=count%> result<%=count == 1 ? "" : "s"%>
</span>
</div>
<% if (count > 0) { for (Appointment appointment : data.getAppointments()) { %>
<article class="result">
<div class="result-left">
<div>
<p class="meta">APPOINTMENT · <%=escape(appointment.getId())%>
</p>
<h3>
<%=escape(appointment.getServiceName())%>
</h3>
<p class="meta">Sign in to check availability</p>
</div>
</div>
<a class="button" href="#sign-in">Sign in to book</a>
</article>
<% } } else { %>
<div class="empty">
<h3>
<%= searched ? "No appointments found." : "No search yet" %>
</h3>
<p>
<%= searched ? "Try a different service name, such as Psychology." : "Enter a service name to see matching appointments." %>
</p>
</div>
<% } %>
</section>
</div>
<aside class="side" id="sign-in">
<section class="panel">
<h2>Sign in</h2>
<p>Sign in to check availability and book.</p>
<form class="login-form" action="FrontEnd" method="post">
<input type="hidden" name="pageName" value="login">
<div class="field">
<label for="username">Username</label>
<input id="username" name="username" autocomplete="username" required>
</div>
<div class="field">
<label for="password">Password</label>
<input id="password" type="password" name="password" autocomplete="current-password" required>
</div>
<button type="submit">Sign in</button>
</form>
<div class="demo">Demo credentials<br>Username: <code>AndrewBadie</code>
<br>Password: <code>1234</code>
</div>
</section>
</aside>
</div>
</main>
<footer class="footer">
<span>Appointments · COE692</span>
<span>Course project · Demo data</span>
</footer>
</div>
</body>
</html>
