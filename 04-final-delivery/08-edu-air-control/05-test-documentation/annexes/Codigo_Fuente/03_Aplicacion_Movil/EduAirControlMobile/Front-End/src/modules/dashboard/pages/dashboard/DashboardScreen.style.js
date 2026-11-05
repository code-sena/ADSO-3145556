import { StyleSheet } from 'react-native'

export const srStyles = StyleSheet.create({
  ring: { borderWidth: 3, alignItems: 'center', justifyContent: 'center' },
  num: { fontWeight: 'bold' },
})

export const pdStyles = StyleSheet.create({
  card: {
    alignItems: 'center', borderRadius: 14, borderWidth: 2,
    paddingHorizontal: 8, paddingTop: 12, paddingBottom: 42, gap: 5,
    overflow: 'visible',
    shadowColor: '#000', shadowOffset: { width: 0, height: 2 }, shadowOpacity: 0.1, shadowRadius: 6, elevation: 4,
  },
  rank1: { width: 132, height: 300, marginTop: 5, zIndex: 2 },
  rank23: { width: 120, height: 260, marginTop: 24 },
  crown: { fontSize: 21, position: 'absolute', top: -20, zIndex: 3 },
  bubble: { width: 40, height: 40, borderRadius: 20, borderWidth: 2, alignItems: 'center', justifyContent: 'center' },
  bubbleWinner: { width: 46, height: 46, borderRadius: 23 },
  name: { textAlign: 'center', fontWeight: '800', fontSize: 11.5, lineHeight: 14, minHeight: 30, maxWidth: '100%' },
  nameWinner: { fontSize: 13, lineHeight: 16, minHeight: 34 },
  locRow: { flexDirection: 'row', alignItems: 'center', gap: 3 },
  loc: { fontSize: 10, maxWidth: 82 },
  stand: {
    position: 'absolute', left: 0, right: 0, bottom: 0,
    height: 34, alignItems: 'center', justifyContent: 'center',
    borderBottomLeftRadius: 11, borderBottomRightRadius: 11,
  },
  standWinner: { height: 38 },
  standN: { color: '#fff', fontWeight: '900', fontSize: 16 },
  standWinnerN: { fontSize: 18 },
})

export const rrStyles = StyleSheet.create({
  row: { flexDirection: 'row', alignItems: 'center', gap: 8, padding: 12, borderRadius: 12, borderWidth: 1, marginBottom: 8 },
  rank: { fontSize: 13, fontWeight: '700', width: 28 },
  name: { fontSize: 14, fontWeight: '600' },
  loc: { fontSize: 11, marginTop: 1 },
  metricScroller: { flexDirection: 'row', alignItems: 'center', gap: 2, width: 116, flexShrink: 1 },
  metricsScroll: { flex: 1, minWidth: 0 },
  pills: { flexDirection: 'row', gap: 4 },
  pill: { borderWidth: 1, borderRadius: 8, paddingHorizontal: 6, paddingVertical: 3 },
  pillTxt: { fontSize: 10, fontWeight: '600' },
})


export const styles = StyleSheet.create({
  safe: { flex: 1 },
  header: {
    flexDirection: 'row', alignItems: 'center', gap: 12,
    paddingHorizontal: 20, paddingTop: 55, paddingBottom: 16, borderBottomWidth: 1,
  },
  headerTitle: { fontSize: 18, fontWeight: 'bold' },
  headerSub: { fontSize: 12, marginTop: 1 },
  iconBtn: { width: 40, height: 40, borderRadius: 12, borderWidth: 1, alignItems: 'center', justifyContent: 'center' },
  scroll: { flex: 1 },
  scrollContent: { padding: 16, paddingBottom: 30 },

  hero: { marginBottom: 16 },
  eyebrow: { flexDirection: 'row', alignItems: 'center', gap: 6, marginBottom: 6 },
  eyebrowDot: { width: 7, height: 7, borderRadius: 4 },
  eyebrowTxt: { fontSize: 11, fontWeight: '800', letterSpacing: 1, textTransform: 'uppercase' },
  heroTitle: { fontSize: 24, fontWeight: '900', lineHeight: 29, letterSpacing: -0.5 },
  heroTitleEm: { fontStyle: 'italic' },
  heroDesc: { fontSize: 13, lineHeight: 19, marginTop: 6 },

  card: { borderRadius: 16, borderWidth: 1, padding: 14, marginBottom: 14 },
  cardHead: { flexDirection: 'row', alignItems: 'flex-start', justifyContent: 'space-between', gap: 8, marginBottom: 10 },
  cardKicker: { fontSize: 10, fontWeight: '800', letterSpacing: 1, textTransform: 'uppercase', marginBottom: 3 },
  cardTitle: { fontSize: 15, fontWeight: '800', lineHeight: 19 },
  cardSub: { fontSize: 11.5, marginTop: 3 },
  livePill: { flexDirection: 'row', alignItems: 'center', gap: 5, borderRadius: 20, paddingHorizontal: 8, paddingVertical: 4 },
  liveDot: { width: 6, height: 6, borderRadius: 3 },
  liveTxt: { fontSize: 9.5, fontWeight: '700' },

  periodRow: { flexDirection: 'row', gap: 6 },
  periodChip: { flex: 1, alignItems: 'center', paddingVertical: 9, borderRadius: 10, borderWidth: 1 },
  periodChipTxt: { fontSize: 12, fontWeight: '700' },
  periodContext: { flexDirection: 'row', alignItems: 'center', gap: 6, marginTop: 8 },
  periodContextTxt: { fontSize: 11.5 },

  chipScroll: { marginBottom: 14 },
  chipRow: { flexDirection: 'row', gap: 6, marginTop: 14, marginBottom: 14 },
  chip: { flex: 1, minWidth: 0, height: 58, flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 4, paddingHorizontal: 2, borderRadius: 12, borderWidth: 1 },
  chipTxt: { fontSize: 10, fontWeight: '700', textAlign: 'center', flexShrink: 1 },

  kpiGrid: { flexDirection: 'row', flexWrap: 'wrap', gap: 10, marginBottom: 14 },
  kpiCard: { width: '47.7%', borderRadius: 14, borderWidth: 1, padding: 12 },
  kpiTop: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 },
  kpiLabel: { fontSize: 11, fontWeight: '700', flex: 1 },
  kpiValue: { fontSize: 21, fontWeight: '900', letterSpacing: -0.5 },
  kpiNote: { fontSize: 10.5, marginTop: 3, flexDirection: 'row', alignItems: 'center', gap: 3 },

  legendRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 10, marginBottom: 8 },
  legendItem: { flexDirection: 'row', alignItems: 'center', gap: 5 },
  legendDot: { width: 8, height: 8, borderRadius: 4 },
  legendTxt: { fontSize: 11, fontWeight: '600' },
  legendComfort: { flexDirection: 'row', alignItems: 'center', gap: 5 },
  legendDash: { width: 14, height: 0, borderWidth: 1, borderStyle: 'dashed' },

  captionRow: { flexDirection: 'row', justifyContent: 'space-between', gap: 10, marginTop: 8 },
  captionTxt: { fontSize: 10.5, flex: 1 },

  networkBody: { flexDirection: 'row', alignItems: 'center', gap: 14 },
  donutWrap: { alignItems: 'center', justifyContent: 'center' },
  donutCenter: { alignItems: 'center' },
  donutValue: { fontSize: 22, fontWeight: '900' },
  donutLabel: { fontSize: 10, textAlign: 'center', marginTop: 2 },
  networkLegend: { flex: 1, gap: 8 },
  networkRow: { flexDirection: 'row', alignItems: 'center', gap: 8 },
  networkDot: { width: 9, height: 9, borderRadius: 5 },
  networkName: { flex: 1, fontSize: 12.5, fontWeight: '600' },
  networkCount: { fontSize: 13, fontWeight: '800' },
  networkFoot: { flexDirection: 'row', alignItems: 'center', gap: 5, marginTop: 4 },

  sectionLabel: { fontSize: 10, fontWeight: '800', letterSpacing: 1, textTransform: 'uppercase', marginBottom: 4 },
  panelDesc: { fontSize: 12, marginBottom: 10 },
  countBadge: { borderRadius: 20, paddingHorizontal: 9, paddingVertical: 3 },
  countBadgeTxt: { fontSize: 11, fontWeight: '800' },

  envList: { gap: 8 },
  envRow: { flexDirection: 'row', alignItems: 'center', gap: 10, borderRadius: 12, borderWidth: 1.5, paddingHorizontal: 10, paddingVertical: 10 },
  envIcon: { width: 32, height: 32, borderRadius: 9, alignItems: 'center', justifyContent: 'center' },
  envIconTxt: { fontSize: 12, fontWeight: '900' },
  envInfo: { flex: 1 },
  envName: { fontSize: 13, fontWeight: '800' },
  envMeta: { fontSize: 11, marginTop: 1 },
  envStatus: { width: 9, height: 9, borderRadius: 5 },
  envGo: { paddingLeft: 2 },

  updatedRow: { flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 12, paddingTop: 12, borderTopWidth: 1 },
  updatedTxt: { flex: 1, fontSize: 11.5 },
  refreshBtn: { width: 30, height: 30, borderRadius: 9, borderWidth: 1, alignItems: 'center', justifyContent: 'center' },

  readingTitle: { fontSize: 15, fontWeight: '800', marginTop: 4 },
  readingDesc: { fontSize: 12.5, lineHeight: 18, marginTop: 5 },
  readingBtn: { flexDirection: 'row', alignItems: 'center', gap: 6, marginTop: 10, alignSelf: 'flex-start' },
  readingBtnTxt: { fontSize: 12.5, fontWeight: '800' },

  footer: { flexDirection: 'row', flexWrap: 'wrap', gap: 4, justifyContent: 'space-between', marginTop: 6, marginBottom: 10 },
  footerTxt: { fontSize: 11 },

  pointer: { borderRadius: 10, paddingHorizontal: 10, paddingVertical: 7, gap: 3 },
  pointerLabel: { fontSize: 11, fontWeight: '800' },
  pointerRow: { flexDirection: 'row', alignItems: 'center', gap: 6 },
  pointerDot: { width: 7, height: 7, borderRadius: 4 },
  pointerName: { fontSize: 11, flex: 1 },
  pointerValue: { fontSize: 11, fontWeight: '800' },

  center: { flex: 1, alignItems: 'center', justifyContent: 'center', gap: 10 },
  centerTxt: { fontSize: 13 },
})
