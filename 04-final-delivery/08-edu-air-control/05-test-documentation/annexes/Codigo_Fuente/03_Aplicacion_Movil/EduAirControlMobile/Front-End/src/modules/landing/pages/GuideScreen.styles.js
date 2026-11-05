import { StyleSheet } from 'react-native'

export const guideStyles = StyleSheet.create({
  safe: { flex: 1 },
  header: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    paddingHorizontal: 20, paddingTop: 55, paddingBottom: 14, borderBottomWidth: 1,
  },
  backBtn: { flexDirection: 'row', alignItems: 'center', gap: 6 },
  backTxt: { fontSize: 13, fontWeight: '700' },
  statusPill: { flexDirection: 'row', alignItems: 'center', gap: 5, borderRadius: 20, paddingHorizontal: 9, paddingVertical: 4 },
  statusDot: { width: 6, height: 6, borderRadius: 3 },
  statusTxt: { fontSize: 10, fontWeight: '700' },
  scroll: { flex: 1 },
  content: { padding: 16, paddingBottom: 40, gap: 14 },

  card: { borderRadius: 16, borderWidth: 1, padding: 16 },
  kicker: { fontSize: 10, fontWeight: '800', letterSpacing: 1, textTransform: 'uppercase', marginBottom: 4 },
  h1: { fontSize: 24, fontWeight: '900', lineHeight: 30, letterSpacing: -0.5 },
  h1Accent: { color: '#01805b' },
  h2: { fontSize: 17, fontWeight: '800', lineHeight: 22 },
  p: { fontSize: 13, lineHeight: 19, marginTop: 6 },
  note: { fontSize: 12, marginTop: 6, fontStyle: 'italic' },

  introMeta: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginTop: 12 },
  introMetaItem: { flexDirection: 'row', alignItems: 'center', gap: 5, borderRadius: 20, paddingHorizontal: 10, paddingVertical: 6 },

  missionRow: { flexDirection: 'row', gap: 12 },
  missionIcon: { width: 46, height: 46, borderRadius: 13, alignItems: 'center', justifyContent: 'center' },
  missionBody: { flex: 1 },

  grid2: { flexDirection: 'row', flexWrap: 'wrap', gap: 10 },
  stepCard: { width: '47.7%', borderRadius: 14, borderWidth: 1, padding: 12 },
  stepTop: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 },
  stepNumber: { fontSize: 15, fontWeight: '900' },
  stepTitle: { fontSize: 13.5, fontWeight: '800' },
  stepDesc: { fontSize: 11.5, lineHeight: 16, marginTop: 4 },

  varCard: { width: '47.7%', borderRadius: 14, borderWidth: 1, padding: 12 },
  varIcon: { width: 38, height: 38, borderRadius: 11, alignItems: 'center', justifyContent: 'center', marginBottom: 8 },
  varTitleRow: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', gap: 6 },
  varName: { fontSize: 13.5, fontWeight: '800', flex: 1 },
  varSymbol: { fontSize: 11, fontWeight: '800' },
  varDesc: { fontSize: 11, lineHeight: 15.5, marginTop: 6 },
  varReading: { marginTop: 8, paddingTop: 8, borderTopWidth: 1 },
  varReadingLabel: { fontSize: 10 },
  varReadingValue: { fontSize: 11.5, fontWeight: '800', marginTop: 2 },

  signalCard: { borderRadius: 14, borderWidth: 1, padding: 14, marginBottom: 10 },
  signalTag: { alignSelf: 'flex-start', borderRadius: 20, paddingHorizontal: 10, paddingVertical: 4, fontSize: 10.5, fontWeight: '800', overflow: 'hidden' },
  signalTitle: { fontSize: 14.5, fontWeight: '800', marginTop: 8 },
  signalDesc: { fontSize: 12.5, lineHeight: 18, marginTop: 5 },

  actionRow: { flexDirection: 'row', gap: 10, paddingVertical: 10, borderBottomWidth: 1 },
  actionCheck: { width: 30, height: 30, borderRadius: 9, alignItems: 'center', justifyContent: 'center' },
  actionBody: { flex: 1 },
  actionLabel: { fontSize: 13, fontWeight: '800' },
  actionDesc: { fontSize: 12, lineHeight: 17, marginTop: 3 },

  moduleRow: { flexDirection: 'row', alignItems: 'center', gap: 12, paddingVertical: 11, borderBottomWidth: 1 },
  moduleBody: { flex: 1 },
  moduleTitle: { fontSize: 13.5, fontWeight: '800' },
  moduleDesc: { fontSize: 12, lineHeight: 17, marginTop: 2 },

  roleRow: { flexDirection: 'row', alignItems: 'center', gap: 12, paddingVertical: 10 },
  roleIcon: { width: 38, height: 38, borderRadius: 11, alignItems: 'center', justifyContent: 'center' },
  roleBody: { flex: 1 },
  roleTitle: { fontSize: 13.5, fontWeight: '800' },
  roleDesc: { fontSize: 12, marginTop: 2 },

  notice: { flexDirection: 'row', gap: 12, borderRadius: 14, borderWidth: 1, padding: 14 },
  noticeBody: { flex: 1 },
  noticeTitle: { fontSize: 13.5, fontWeight: '800' },
  noticeText: { fontSize: 12.5, lineHeight: 18, marginTop: 5 },

  faqItem: { borderRadius: 12, borderWidth: 1, marginBottom: 8, overflow: 'hidden' },
  faqHead: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', gap: 10, padding: 13 },
  faqQ: { flex: 1, fontSize: 13, fontWeight: '700' },
  faqA: { fontSize: 12.5, lineHeight: 18, paddingHorizontal: 13, paddingBottom: 13 },
})
