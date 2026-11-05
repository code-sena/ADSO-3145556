import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import react from 'eslint-plugin-react'
import { defineConfig, globalIgnores } from 'eslint/config'

/**
 * ESLint para la app Expo/React Native, con la misma configuración plana que
 * `apps/web`. Antes no había lint en ninguna de las dos: ninguna referencia
 * rota ni hook mal usado se detectaba antes de ejecutar.
 */
export default defineConfig([
  globalIgnores(['dist-android/**', '.expo/**', 'android/**', 'ios/**']),
  {
    files: ['**/*.{js,jsx}'],
    extends: [js.configs.recommended],
    plugins: {
      react,
      // Registrado a mano y no vía `extends`: las versiones de este plugin
      // exponen `recommended` en formato legacy y `recommended-latest` en plano.
      'react-hooks': reactHooks,
    },
    languageOptions: {
      ecmaVersion: 2022,
      globals: { ...globals.browser, ...globals.node, __DEV__: 'readonly' },
      parserOptions: {
        ecmaVersion: 'latest',
        ecmaFeatures: { jsx: true },
        sourceType: 'module',
      },
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      'no-unused-vars': ['error', { varsIgnorePattern: '^[A-Z_]', argsIgnorePattern: '^_' }],
      'react/jsx-uses-vars': 'error',
      'react-refresh/only-export-components': 'off',
    },
  },
])
