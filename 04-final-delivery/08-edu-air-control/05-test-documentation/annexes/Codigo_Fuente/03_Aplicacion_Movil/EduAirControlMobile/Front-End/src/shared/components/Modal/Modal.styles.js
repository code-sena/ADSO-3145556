import { StyleSheet } from 'react-native'

export const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(1, 10, 18, 0.7)',
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 16,
    paddingVertical: 18,
  },
  content: {
    borderRadius: 24,
    borderWidth: 1,
    padding: 18,
    paddingTop: 20,
    paddingRight: 20,
    maxHeight: '76%',
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOpacity: 0.28,
    shadowRadius: 24,
    shadowOffset: { width: 0, height: 10 },
    elevation: 10,
    alignSelf: 'center',
  },
  closeBtn: { position: 'absolute', top: 12, right: 12, zIndex: 2 },
  title: { fontSize: 18, fontWeight: '700', marginBottom: 10, marginRight: 28, letterSpacing: -0.3 },
  body: { flexShrink: 1, maxHeight: '100%', minHeight: 0 },
})
