# Log de la ejecución del backend sin base de datos

> Evidencia histórica de la versión 1.0 (14/10/2026). En la versión 1.1 la suite completa aprueba con MySQL: ver [resumen-suite-completa.md](backend-full-suite-summary.md).

Log recortado de Maven: solo líneas de resultados. Sin variables de entorno ni credenciales.

```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.273 s -- in com.energymonitor.device.domain.ApiKeyHashTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.085 s -- in com.energymonitor.device.application.BrokerAccessServiceTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.314 s -- in com.energymonitor.device.application.HomeDeviceServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.082 s -- in com.energymonitor.device.application.DeviceConnectivityServiceTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.221 s -- in com.energymonitor.alert.domain.model.AlertTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.035 s -- in com.energymonitor.alert.application.ListAlertsServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.077 s -- in com.energymonitor.alert.application.RegisterLimitAlertServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.029 s -- in com.energymonitor.alert.application.GetAlertServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.042 s -- in com.energymonitor.alert.application.RegisterDeviceLinkedAlertServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.035 s -- in com.energymonitor.alert.application.ResolveAlertServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.064 s -- in com.energymonitor.alert.application.RegisterThresholdAlertServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 11.27 s -- in com.energymonitor.alert.adapter.in.web.AlertControllerTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in com.energymonitor.alert.adapter.in.event.MeasurementRecordedListenerTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.053 s -- in com.energymonitor.home.domain.model.HomeTypeTest
[INFO] Tests run: 26, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.162 s -- in com.energymonitor.home.domain.model.UserHomeTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.088 s -- in com.energymonitor.home.domain.model.HomeThresholdsTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.071 s -- in com.energymonitor.home.domain.model.HomeTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 s -- in com.energymonitor.home.domain.model.RoleTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.013 s -- in com.energymonitor.home.domain.model.OwnerPermissionsTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in com.energymonitor.home.domain.model.MemberPermissionsTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.020 s -- in com.energymonitor.home.application.GetHomeTypeServiceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.046 s -- in com.energymonitor.home.application.ListHomesServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.034 s -- in com.energymonitor.home.application.UpdateHomeThresholdsServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.150 s -- in com.energymonitor.home.application.DeleteHomeServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.020 s -- in com.energymonitor.home.application.JoinHomeServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in com.energymonitor.home.application.GetHomeThresholdsServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.024 s -- in com.energymonitor.home.application.ListMembersServiceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.033 s -- in com.energymonitor.home.application.RemoveUserServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.052 s -- in com.energymonitor.home.application.TransferHomeOwnershipServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.038 s -- in com.energymonitor.home.application.CreateHomeServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.020 s -- in com.energymonitor.home.application.LeaveHomeServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in com.energymonitor.home.application.ListHomeTypesServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.016 s -- in com.energymonitor.home.application.RemoveDeletedAccountServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in com.energymonitor.home.application.ToggleFavoriteServiceTest
[INFO] Tests run: 26, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.116 s -- in com.energymonitor.home.adapter.in.web.HomeControllerTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.299 s -- in com.energymonitor.home.adapter.in.web.HomeExceptionHandlerTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.085 s -- in com.energymonitor.recommendation.domain.RecommendationRulesTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.022 s -- in com.energymonitor.recommendation.application.RecommendationInboxServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.029 s -- in com.energymonitor.recommendation.application.EvaluateRecommendationsServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in when it fails
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in identity
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.037 s -- in on creation
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in when it is sent
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.099 s -- in Notification
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.038 s -- in com.energymonitor.notification.application.AdminNotificationsTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.217 s -- in Recovery mail
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.124 s -- in com.energymonitor.notification.application.NotifyAlertTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.526 s -- in com.energymonitor.notification.adapter.out.push.WebPushSenderTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.207 s -- in Email delivery of a recovery code
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.304 s -- in EmailSender
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.026 s -- in Abandoned delivery sweep
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.020 s -- in UserConfiguration
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.088 s -- in PasswordPolicy
[ERROR] Tests run: 4, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.039 s <<< FAILURE! -- in Person
[ERROR] com.energymonitor.security.domain.model.SecuritySupportModelTest.refusesMissingNames -- Time elapsed: 0.019 s <<< FAILURE!
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in SecurityConfiguration
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.026 s -- in UserStatus
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.091 s -- in LoginErrorLog
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.305 s -- in Security support model
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.050 s -- in rotation
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.035 s -- in invariants
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in issuance
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.021 s -- in revocation
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.012 s -- in expiry
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.023 s -- in state machine
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.175 s -- in RefreshToken
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.026 s -- in revocation
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in expiration
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 s -- in closure
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 s -- in fresh session
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.044 s -- in rotation
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in lifecycle state
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.149 s -- in UserSession
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.051 s -- in Email
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in PasswordHash
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.081 s -- in Value objects
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.030 s -- in UserSystemRole
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 s -- in SystemRole
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.013 s -- in SystemRolePermission
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.065 s -- in Roles and permissions
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in AuditAction
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.024 s -- in AuditLog
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.009 s -- in state transitions
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in successful login
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.009 s -- in registration
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.012 s -- in failed login attempts
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in credentials
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in authentication eligibility
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.080 s -- in User
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.051 s -- in PasswordResetToken
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.032 s -- in com.energymonitor.security.application.AccountLifecycleServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.022 s -- in com.energymonitor.security.application.DeleteAccountServiceTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.018 s -- in com.energymonitor.security.application.UserProfileQueryServiceTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.069 s -- in com.energymonitor.security.application.PasswordResetServiceTest
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.114 s -- in com.energymonitor.security.application.RefreshSessionServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.056 s -- in com.energymonitor.security.application.EmailVerificationServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.034 s -- in com.energymonitor.security.application.AuthenticateUserServiceTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.112 s -- in com.energymonitor.security.application.RbacServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in com.energymonitor.security.application.ChangePasswordServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.022 s -- in com.energymonitor.security.application.RegisterUserServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.031 s -- in com.energymonitor.security.application.SignInWithGoogleServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.034 s -- in com.energymonitor.security.application.UserSessionServiceTest
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.103 s -- in com.energymonitor.security.application.UserAdministrationServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.026 s -- in while the allowance lasts
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in once the window has passed
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in when configured
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.071 s -- in Password-recovery attempt limiter
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.033 s -- in when hashing a code
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in when two accounts hold the same code
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.207 s -- in when it serves the port
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in when the pepper is misconfigured
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.274 s -- in Peppered HMAC hasher for recovery codes
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.431 s -- in com.energymonitor.security.adapter.out.security.SecureTokenGeneratorAdapterTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.105 s -- in com.energymonitor.security.adapter.out.security.SecureIdentifierGeneratorAdapterTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.23 s -- in com.energymonitor.security.adapter.out.security.BCryptPasswordHasherAdapterTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.021 s -- in SHA-256 token hasher
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.026 s -- in com.energymonitor.security.infrastructure.SecurityCorsConfigurationTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.135 s -- in com.energymonitor.security.infrastructure.PemRsaKeysTest
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.646 s -- in com.energymonitor.security.infrastructure.JwtTokenIssuerTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.899 s -- in com.energymonitor.security.infrastructure.SecurityLoginFlowTest
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.028 s -- in com.energymonitor.measurement.domain.model.ConsumptionLevelTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.022 s -- in com.energymonitor.measurement.domain.model.MeasurementTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in com.energymonitor.measurement.application.GetLatestMeasurementServiceTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.064 s -- in com.energymonitor.measurement.application.HomeConsumptionUseCaseTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.018 s -- in com.energymonitor.measurement.application.GetConsumptionStatisticsServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s -- in com.energymonitor.measurement.application.RegisterMeasurementServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s -- in com.energymonitor.measurement.application.ListMeasurementsServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.009 s -- in com.energymonitor.measurement.application.ListConsumptionLevelsServiceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.778 s -- in com.energymonitor.measurement.adapter.in.web.MeasurementControllerTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.059 s -- in com.energymonitor.measurement.adapter.in.web.ConsumptionLevelControllerTest
[INFO] Results:
[ERROR] Failures: 
[ERROR]   SecuritySupportModelTest.refusesMissingNames 
[ERROR] Tests run: 796, Failures: 1, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
[INFO] Total time:  01:49 min
[INFO] Finished at: 2026-10-14T20:32:27-05:00
```
