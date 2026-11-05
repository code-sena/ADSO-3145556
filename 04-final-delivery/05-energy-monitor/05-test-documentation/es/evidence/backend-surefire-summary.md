# Resumen de Surefire (backend)

> Evidencia histórica de la versión 1.0. La ejecución completa de la versión 1.1 está en [resumen-suite-completa.md](backend-full-suite-summary.md).

Ejecución del 14 de octubre de 2026 con `./mvnw -o test` limitada a las clases que no levantan contexto con base de datos, más 4 clases nuevas. Rama `dev`, commit `d03bbd3` más 4 archivos de prueba nuevos sin confirmar. Java 21.0.12, Spring Boot 4.1.1.

**Pruebas por módulo**

| Módulo | Clases | Ejecutadas | Aprobadas | Fallidas | Omitidas | Tiempo (s) |
|---|---|---|---|---|---|---|
| alert | 9 | 48 | 48 | 0 | 0 | 1.2 |
| device | 6 | 48 | 48 | 0 | 0 | 2.0 |
| home | 23 | 159 | 159 | 0 | 0 | 1.5 |
| measurement | 11 | 67 | 67 | 0 | 0 | 0.8 |
| notification | 8 | 60 | 60 | 0 | 0 | 1.4 |
| recommendation | 4 | 23 | 22 | 1 | 0 | 0.3 |
| security | 31 | 430 | 429 | 1 | 0 | 18.8 |
| **Total** | 92 | 835 | 833 | 2 | 0 | 26.0 |

**Fallas**

| Clase | Prueba | Defecto |
|---|---|---|
| RecommendationControllerTest | listRequiresHomeId | DEF-02 |
| SecuritySupportModelTest | refusesMissingNames | DEF-01 |

**Clases no ejecutadas (requieren MySQL y SPRING_DATASOURCE_PASSWORD): 37**

| Clase | Módulo | Métodos declarados |
|---|---|---|
| AccountDeletionIntegrationTest | (raíz) | 3 |
| AdminRbacIntegrationTest | (raíz) | 6 |
| DeviceApiKeyStorageIntegrationTest | (raíz) | 3 |
| EnergyMonitorBackendApplicationTests | (raíz) | 1 |
| HomeCallerIdentityIntegrationTest | (raíz) | 4 |
| HomeDeletionIntegrationTest | (raíz) | 2 |
| HomeOwnershipTransferIntegrationTest | (raíz) | 4 |
| ModularityTests | (raíz) | 2 |
| AlertPersistenceTest | alert | 4 |
| HomePersistenceTest | home | 6 |
| HomeThresholdsPersistenceTest | home | 3 |
| HomeTypePersistenceTest | home | 3 |
| HomeTypeSeedTest | home | 1 |
| UserHomePersistenceTest | home | 7 |
| ConsumptionLevelPersistenceTest | measurement | 4 |
| MeasurementPersistenceTest | measurement | 4 |
| AdminNotificationsControllerWebTest | notification | 5 |
| NotificationPersistenceTest | notification | 4 |
| RecommendationPersistenceTest | recommendation | 3 |
| AdminControllerWebTest | security | 15 |
| AuthControllerWebTest | security | 66 |
| AuthRefreshEndpointWebTest | security | 18 |
| ConfigurationPolicyPersistenceTest | security | 6 |
| LogPersistenceTest | security | 5 |
| PasswordPolicySeedTest | security | 5 |
| PasswordResetTokenReservationTest | security | 6 |
| PoolSaturatedLimiterTest | security | 2 |
| RbacAssignmentPersistenceTest | security | 2 |
| RefreshLifecycleIntegrationTest | security | 15 |
| RefreshSessionTransactionTest | security | 6 |
| RefreshTokenPersistenceTest | security | 10 |
| ResetAttemptLimiterBeforeDatabaseTest | security | 3 |
| SecurityCorsWebTest | security | 23 |
| SecurityTransactionBoundaryTest | security | 5 |
| SecurityTransactionRollbackTest | security | 3 |
| TokenSessionPersistenceTest | security | 6 |
| UserPersonPersistenceTest | security | 5 |

**Cobertura:** JaCoCo no está en el `pom.xml`; no hay reporte de cobertura. Se anota como recomendación.

**Por clase ejecutada**

| Módulo | Clase | Pruebas | Fallidas | Omitidas |
|---|---|---|---|---|
| alert | AlertControllerTest | 7 | 0 | 0 |
| alert | AlertTest | 16 | 0 | 0 |
| alert | GetAlertServiceTest | 2 | 0 | 0 |
| alert | ListAlertsServiceTest | 3 | 0 | 0 |
| alert | MeasurementRecordedListenerTest | 1 | 0 | 0 |
| alert | RegisterDeviceLinkedAlertServiceTest | 4 | 0 | 0 |
| alert | RegisterLimitAlertServiceTest | 4 | 0 | 0 |
| alert | RegisterThresholdAlertServiceTest | 7 | 0 | 0 |
| alert | ResolveAlertServiceTest | 4 | 0 | 0 |
| device | ApiKeyHashTest | 3 | 0 | 0 |
| device | BrokerAccessServiceTest | 8 | 0 | 0 |
| device | BrokerAuthControllerTest | 12 | 0 | 0 |
| device | DeviceConnectivityServiceTest | 6 | 0 | 0 |
| device | HomeDeviceControllerTest | 10 | 0 | 0 |
| device | HomeDeviceServiceTest | 9 | 0 | 0 |
| home | CreateHomeServiceTest | 6 | 0 | 0 |
| home | DeleteHomeServiceTest | 4 | 0 | 0 |
| home | GetHomeThresholdsServiceTest | 2 | 0 | 0 |
| home | GetHomeTypeServiceTest | 2 | 0 | 0 |
| home | HomeControllerTest | 26 | 0 | 0 |
| home | HomeExceptionHandlerTest | 2 | 0 | 0 |
| home | HomeTest | 16 | 0 | 0 |
| home | HomeThresholdsTest | 17 | 0 | 0 |
| home | HomeTypeTest | 8 | 0 | 0 |
| home | JoinHomeServiceTest | 4 | 0 | 0 |
| home | LeaveHomeServiceTest | 4 | 0 | 0 |
| home | ListHomeTypesServiceTest | 2 | 0 | 0 |
| home | ListHomesServiceTest | 5 | 0 | 0 |
| home | ListMembersServiceTest | 4 | 0 | 0 |
| home | MemberPermissionsTest | 3 | 0 | 0 |
| home | OwnerPermissionsTest | 3 | 0 | 0 |
| home | RemoveDeletedAccountServiceTest | 2 | 0 | 0 |
| home | RemoveUserServiceTest | 5 | 0 | 0 |
| home | RoleTest | 3 | 0 | 0 |
| home | ToggleFavoriteServiceTest | 2 | 0 | 0 |
| home | TransferHomeOwnershipServiceTest | 6 | 0 | 0 |
| home | UpdateHomeThresholdsServiceTest | 7 | 0 | 0 |
| home | UserHomeTest | 26 | 0 | 0 |
| measurement | ConsumptionLevelControllerTest | 1 | 0 | 0 |
| measurement | ConsumptionLevelTest | 14 | 0 | 0 |
| measurement | GetConsumptionStatisticsServiceTest | 5 | 0 | 0 |
| measurement | GetLatestMeasurementServiceTest | 2 | 0 | 0 |
| measurement | HomeConsumptionUseCaseTest | 8 | 0 | 0 |
| measurement | ListConsumptionLevelsServiceTest | 2 | 0 | 0 |
| measurement | ListMeasurementsServiceTest | 4 | 0 | 0 |
| measurement | MeasurementControllerTest | 5 | 0 | 0 |
| measurement | MeasurementTest | 12 | 0 | 0 |
| measurement | RegisterMeasurementServiceTest | 4 | 0 | 0 |
| measurement | TelemetryMqttConsumerTest | 10 | 0 | 0 |
| notification | AbandonedDeliverySweeperTest | 4 | 0 | 0 |
| notification | AdminNotificationsTest | 5 | 0 | 0 |
| notification | EmailPasswordResetDeliveryAdapterTest | 5 | 0 | 0 |
| notification | EmailSenderTest | 9 | 0 | 0 |
| notification | NotificationTest | 20 | 0 | 0 |
| notification | NotifyAlertTest | 4 | 0 | 0 |
| notification | SendPasswordResetEmailTest | 11 | 0 | 0 |
| notification | WebPushSenderTest | 2 | 0 | 0 |
| recommendation | EvaluateRecommendationsServiceTest | 4 | 0 | 0 |
| recommendation | RecommendationControllerTest | 7 | 1 | 0 |
| recommendation | RecommendationInboxServiceTest | 4 | 0 | 0 |
| recommendation | RecommendationRulesTest | 8 | 0 | 0 |
| security | AccountLifecycleServiceTest | 9 | 0 | 0 |
| security | AuditLogTest | 9 | 0 | 0 |
| security | AuthenticateUserServiceTest | 6 | 0 | 0 |
| security | BCryptPasswordHasherAdapterTest | 12 | 0 | 0 |
| security | ChangePasswordServiceTest | 3 | 0 | 0 |
| security | DeleteAccountServiceTest | 2 | 0 | 0 |
| security | EmailVerificationServiceTest | 6 | 0 | 0 |
| security | InMemoryResetAttemptLimiterTest | 8 | 0 | 0 |
| security | JwtTokenIssuerTest | 18 | 0 | 0 |
| security | PasswordResetServiceTest | 16 | 0 | 0 |
| security | PasswordResetTokenTest | 19 | 0 | 0 |
| security | PemRsaKeysTest | 19 | 0 | 0 |
| security | PepperedHmacResetCodeHasherTest | 15 | 0 | 0 |
| security | RbacServiceTest | 17 | 0 | 0 |
| security | RbacTest | 14 | 0 | 0 |
| security | RefreshSessionServiceTest | 20 | 0 | 0 |
| security | RefreshTokenTest | 41 | 0 | 0 |
| security | RegisterUserServiceTest | 7 | 0 | 0 |
| security | SecureIdentifierGeneratorAdapterTest | 5 | 0 | 0 |
| security | SecureTokenGeneratorAdapterTest | 13 | 0 | 0 |
| security | SecurityCorsConfigurationTest | 11 | 0 | 0 |
| security | SecurityLoginFlowTest | 11 | 0 | 0 |
| security | SecuritySupportModelTest | 28 | 1 | 0 |
| security | Sha256TokenHasherTest | 9 | 0 | 0 |
| security | SignInWithGoogleServiceTest | 7 | 0 | 0 |
| security | UserAdministrationServiceTest | 18 | 0 | 0 |
| security | UserProfileQueryServiceTest | 1 | 0 | 0 |
| security | UserSessionServiceTest | 6 | 0 | 0 |
| security | UserSessionTest | 45 | 0 | 0 |
| security | UserTest | 21 | 0 | 0 |
| security | ValueObjectTest | 14 | 0 | 0 |
