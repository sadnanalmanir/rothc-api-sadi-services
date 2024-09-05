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
                <li><a href="./fetchRothcMonthlyDataCinpParams">fetchRothcMonthlyDataCinpParams</a></li>
                <li><a href="./fetchRothcMonthlyDataDpmRpmParams">fetchRothcMonthlyDataDpmRpmParams</a></li>
                <li><a href="./fetchRothcMonthlyDataEvapParams">fetchRothcMonthlyDataEvapParams</a></li>
                <li><a href="./fetchRothcMonthlyDataFymParams">fetchRothcMonthlyDataFymParams</a></li>
                <li><a href="./fetchRothcMonthlyDataPcParams">fetchRothcMonthlyDataPcParams</a></li>
                <li><a href="./fetchRothcMonthlyDataParams">fetchRothcMonthlyDataParams</a></li>
                <li><a href="./fetchRothcMonthlyDataParams2">fetchRothcMonthlyDataParams2</a></li>
                <li><a href="./fetchRothcMonthlyDataModernParams">fetchRothcMonthlyDataModernParams</a></li>
                <li><a href="./fetchRothcMonthlyDataRainParams">fetchRothcMonthlyDataRainParams</a></li>
                <li><a href="./fetchRothcMonthlyDataTmpParams">fetchRothcMonthlyDataTmpParams</a></li>
                <li><a href="./fetchRothcMonthlyDataMonthParams">fetchRothcMonthlyDataMonthParams</a></li>
                <li><a href="./fetchRothcMonthlyDataYearParams">fetchRothcMonthlyDataYearParams</a></li>
            </Ul>
        </div> <!-- content -->
        <div id='footer'>
        </div> <!-- footer -->
    </div> <!-- inner-frame -->
</div> <!-- outer-frame -->
</body>
</html>
