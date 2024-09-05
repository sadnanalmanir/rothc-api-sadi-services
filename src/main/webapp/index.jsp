<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String hostName = request.getServerName();
%>
<html>
<head>
    <title>SADI Services exposing API for the farm platform data</title>
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
                <li><a href="./fetchRothcInputParams">fetchRothcInputParams</a></li>
                <li><a href="./fetchRothcInputClayParams">fetchRothcInputClayParams</a></li>
                <li><a href="./fetchRothcInputDepthParams">fetchRothcInputDepthParams</a></li>
                <li><a href="./fetchRothcInputIomParams">fetchRothcInputIomParams</a></li>
                <li><a href="./fetchRothcInputNstepsParams">fetchRothcInputNstepsParams</a></li>
                <li><a href="./fetchRothcMonthlyDataParams">fetchRothcMonthlyDataParams</a></li>
                <li><a href="./fetchRothcMonthlyDataParams2">fetchRothcMonthlyDataParams2</a></li>
            </Ul>
        </div> <!-- content -->
        <div id='footer'>
        </div> <!-- footer -->
    </div> <!-- inner-frame -->
</div> <!-- outer-frame -->
</body>
</html>
