# paysafe-coding-assignment

1. To run the application execute the ``Application.java``. It resides in ``paysafe-coding-assignment/paysafe_assignment_test/src/main/java/com/paysafe/main/``.

2. The application has 3 endpoints. 

**startService**: It starts monitoring the server. It can be called ``http://localhost:8080/paysafe-test-app/start-service``. Pass the json in the body, example ``{
  "interval": 10600,  "url": "https://api.test.pahysafe.com/accountmanagement/monitor". 
}``. Set ``content-type`` as ``application/json`` in the header. Use POST as HTTP method.

**stopService** It stops the monitoring of the server. It can be called ``http://localhost:8080/paysafe-test-app/stop-service?url=https://api.test.pahysafe.com/accountmanagement/monitor``. Pass the url of the server that you want to stop monitoring. Use HTTP GET.

**getServerReport**: It tells when the server was up when it was down. It can be called ``http://localhost:8080/paysafe-test-app/server-report?url=https://api.test.pahysafe.com/accountmanagement/monitor``. Pass the url of the server whose report you would like to see. Report tells the lastUp time and lastDown time. Use HTTP GET.


**Note**: Technologies used Spring Boot to make rest endpoints.
