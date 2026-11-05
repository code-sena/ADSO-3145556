import { useEffect, useState } from 'react'
import { Text } from 'react-native'
import { NavigationContainer, createNavigationContainerRef } from '@react-navigation/native'
import { createNativeStackNavigator } from '@react-navigation/native-stack'
import { GestureHandlerRootView } from 'react-native-gesture-handler'

// Auth screens (módulo reestructurado, conectado a servicio + schemas + i18n)
// Auth screens
import LoginScreen from './src/modules/auth/pages/login/LoginScreen';
import SignUpScreen from './src/modules/auth/pages/signUp/SignUpScreen';
import ForgotPasswordScreen from './src/modules/auth/pages/forgotPassword/ForgotPasswordScreen';
import VerifyCodeScreen from './src/modules/auth/pages/verifyCode/VerifyCodeScreen';
import ChangePasswordScreen from './src/modules/auth/pages/changePassword/ChangePasswordScreen';
import TermsScreen from './src/modules/auth/pages/terms/TermsScreen';
import GuideScreen from './src/modules/landing/pages/GuideScreen.jsx';

// App navigator (bottom tabs + stacks)
import AppNavigator from './src/navigation/AppNavigator'

// Context
import { EnvironmentProvider } from './src/context/EnvironmentContext'
import { ThemeProvider } from './src/context/ThemeContext'
import { ToastProvider } from './src/shared/components/Toast/Toast'

// Auth
import authService from './src/modules/auth/services/authService'
import storage from './src/shared/storage/storage'
import { setOnUnauthorized } from './src/shared/services/apiClient'

const Stack = createNativeStackNavigator()
const navigationRef = createNavigationContainerRef()

export default function App() {
  const [initialized, setInitialized] = useState(null)
  useEffect(() => {
    let isMounted = true

    const initialize = async () => {
      try {
      await storage.init()

      const authenticated = authService.isAuthenticated()

      if (isMounted) {
        setInitialized(authenticated ? 'App' : 'Login')
      }
    } catch (error) {
      console.error('Error checking authentication:', error)
    
      if (isMounted) {
        setInitialized('Login')
      }
    }
  }
      

    initialize()

    setOnUnauthorized(() => {
      if (navigationRef.isReady()) {
        navigationRef.reset({
          index: 0,
          routes: [{ name: 'Login' }],
        })
      }
    })

    return () => {
      isMounted = false
      setOnUnauthorized(null)
    }
  }, [])

  if (!initialized) {
  return (
    <GestureHandlerRootView style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text style={{ color: '#000' }}>Cargando EduAirControl...</Text>
    </GestureHandlerRootView>
  )
}

  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <ThemeProvider>
        <ToastProvider>
          <EnvironmentProvider>
            <NavigationContainer ref={navigationRef}>
              <Stack.Navigator
                initialRouteName={initialized}
                screenOptions={{ headerShown: false }}
              >
                {/* Auth */}
                <Stack.Screen name="Login" component={LoginScreen} />
                <Stack.Screen name="SignUp" component={SignUpScreen} />
                <Stack.Screen name="ForgotPassword" component={ForgotPasswordScreen} />
                <Stack.Screen name="VerifyCode" component={VerifyCodeScreen} />
                <Stack.Screen name="ChangePassword" component={ChangePasswordScreen} />
                <Stack.Screen name="Terms" component={TermsScreen} />
                <Stack.Screen name="Guide" component={GuideScreen} />

                {/* App (bottom tabs) */}
                <Stack.Screen name="App" component={AppNavigator} />
              </Stack.Navigator>
            </NavigationContainer>
          </EnvironmentProvider>
        </ToastProvider>
      </ThemeProvider>
    </GestureHandlerRootView>
  )
}