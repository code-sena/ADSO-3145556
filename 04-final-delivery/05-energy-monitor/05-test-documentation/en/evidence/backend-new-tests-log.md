# Log of the run of the new tests

> Historical evidence from version 1.0 (10/14/2026). In version 1.1 `RecommendationControllerTest.listRequiresHomeId` passes (DEF-02 closed).

Trimmed Maven log: result lines only. No environment variables or credentials.

```text
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.41 s -- in com.energymonitor.device.adapter.in.web.BrokerAuthControllerTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.272 s -- in com.energymonitor.device.adapter.in.web.HomeDeviceControllerTest
[ERROR] Tests run: 7, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 1.720 s <<< FAILURE! -- in com.energymonitor.recommendation.adapter.in.web.RecommendationControllerTest
[ERROR] com.energymonitor.recommendation.adapter.in.web.RecommendationControllerTest.listRequiresHomeId -- Time elapsed: 0.043 s <<< FAILURE!
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.595 s -- in com.energymonitor.measurement.adapter.in.mqtt.TelemetryMqttConsumerTest
[INFO] Results:
[ERROR] Failures: 
[ERROR]   RecommendationControllerTest.listRequiresHomeId:88 Status expected:<400> but was:<500>
[ERROR] Tests run: 39, Failures: 1, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
[INFO] Total time:  38.863 s
[INFO] Finished at: 2026-10-14T20:35:35-05:00
```
