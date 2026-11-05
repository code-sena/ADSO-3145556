import { StyleSheet } from 'react-native'

export const styles = StyleSheet.create({
  safe: {
    flex: 1,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingBottom: 12,
    borderBottomWidth: 1,
  },
  headerLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    flex: 1,
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: '700',
  },
  refreshBtn: {
    width: 36,
    height: 36,
    borderRadius: 18,
    alignItems: 'center',
    justifyContent: 'center',
  },
  scroll: {
    flex: 1,
  },
  scrollContent: {
    padding: 16,
    gap: 16,
  },
  alert: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    padding: 12,
    borderRadius: 12,
    borderWidth: 1,
  },
  alertText: {
    fontSize: 13,
    fontWeight: '500',
    flex: 1,
  },
  card: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 16,
  },
  cardTitle: {
    fontSize: 16,
    fontWeight: '700',
    marginBottom: 16,
  },
  formGroup: {
    marginBottom: 12,
  },
  label: {
    fontSize: 13,
    fontWeight: '600',
    marginBottom: 6,
  },
  input: {
    borderWidth: 1,
    borderRadius: 12,
    paddingHorizontal: 14,
    paddingVertical: 10,
    fontSize: 14,
  },
  createBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    paddingVertical: 12,
    borderRadius: 12,
    marginTop: 4,
  },
  createBtnText: {
    color: '#fff',
    fontSize: 15,
    fontWeight: '700',
  },
  loadingContainer: {
    alignItems: 'center',
    paddingVertical: 32,
    gap: 12,
  },
  emptyContainer: {
    alignItems: 'center',
    paddingVertical: 32,
    gap: 12,
  },
  mutedText: {
    fontSize: 14,
    textAlign: 'center',
  },
  institutionRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingVertical: 14,
    borderBottomWidth: 1,
    gap: 12,
  },
  institutionInfo: {
    flex: 1,
    gap: 2,
  },
  institutionCode: {
    fontSize: 14,
    fontWeight: '700',
  },
  institutionName: {
    fontSize: 13,
  },
  institutionType: {
    fontSize: 12,
  },
  institutionActions: {
    alignItems: 'flex-end',
    gap: 8,
  },
  statusBadge: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 20,
    borderWidth: 1,
  },
  statusText: {
    fontSize: 11,
    fontWeight: '700',
  },
  toggleBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 8,
    borderWidth: 1,
  },
  toggleBtnText: {
    fontSize: 12,
    fontWeight: '600',
  },
})
