<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String hostName = request.getServerName();
%>
<html>
<head>
    <title>NorthWykeFarmPlatform API Endpoint SADI Services</title>
</head>
<body>
<div id='outer-frame'>
    <div id='inner-frame'>
        <div id='header'>
            <h1>SADI services at <%=hostName%></h1>
        </div>
        <div id='nav'>
            <ul>
                <li>Services</li>
            </ul>
        </div>
        <div id='content'>
            <h2>SADI Services</h2>
            <ul>
                <li><a href="./getMeasurementByCatchmentName">getMeasurementByCatchmentName</a></li>
            </Ul>
        </div> <!-- content -->
        <div id='footer'>
        </div> <!-- footer -->
    </div> <!-- inner-frame -->
</div> <!-- outer-frame -->
</body>
</html>
