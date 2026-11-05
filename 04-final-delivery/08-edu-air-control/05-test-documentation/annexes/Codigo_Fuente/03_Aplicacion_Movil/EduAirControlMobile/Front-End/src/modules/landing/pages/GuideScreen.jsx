import React, { useState } from 'react'
import {
  View,
  Text,
  ScrollView,
  Pressable,
  SafeAreaView,
} from 'react-native'
import { useNavigation } from '@react-navigation/native'
import { Ionicons } from '@expo/vector-icons'
import { useTheme } from '../../../context/ThemeContext.jsx'
import { guideStyles as s } from './GuideScreen.styles'

const steps = [
  {
    number: '01',
    icon: 'people-outline',
    title: 'Ingresa a tu cuenta',
    description: 'Inicia sesión para acceder a los ambientes y a las funciones habilitadas para tu perfil institucional.',
  },
  {
    number: '02',
    icon: 'grid-outline',
    title: 'Lee el resumen',
    description: 'El dashboard reúne el estado general, las lecturas recientes y los avisos que requieren atención.',
  },
  {
    number: '03',
    icon: 'hand-left-outline',
    title: 'Elige un ambiente',
    description: 'Abre un salón o espacio para revisar sus indicadores, comparar su comportamiento y consultar su detalle.',
  },
  {
    number: '04',
    icon: 'bulb-outline',
    title: 'Decide y verifica',
    description: 'Usa la tendencia como guía para actuar, registra el cambio y vuelve a revisar si el ambiente mejora.',
  },
]

const variables = [
  {
    icon: 'thermometer-outline',
    name: 'Temperatura',
    symbol: '°C',
    description: 'Indica qué tan cálido o frío está el espacio. Una variación sostenida puede afectar el confort y la concentración.',
    reading: 'Confort térmico',
    tone: 'mint',
  },
  {
    icon: 'partly-sunny-outline',
    name: 'Humedad relativa',
    symbol: '%',
    description: 'Representa el vapor de agua en el aire. Permite reconocer ambientes muy secos, cargados o con sensación de bochorno.',
    reading: 'Sensación ambiental',
    tone: 'blue',
  },
  {
    icon: 'leaf-outline',
    name: 'Dióxido de carbono',
    symbol: 'ppm',
    description: 'Es un indicador útil de ventilación. Un aumento persistente sugiere revisar la renovación del aire y la ocupación del espacio.',
    reading: 'Calidad del aire',
    tone: 'amber',
  },
  {
    icon: 'pulse-outline',
    name: 'Nivel de ruido',
    symbol: 'dB',
    description: 'Mide la intensidad sonora. Sirve para identificar momentos o espacios que pueden generar distracción o incomodidad.',
    reading: 'Confort acústico',
    tone: 'purple',
  },
]

const signals = [
  {
    tag: 'Estable',
    title: 'El ambiente se mantiene',
    description: 'Las lecturas presentan un comportamiento consistente. Continúa el seguimiento y registra buenas prácticas.',
    tone: 'success',
  },
  {
    tag: 'Revisar',
    title: 'Hay una variación',
    description: 'Observa la tendencia, la hora y la actividad del espacio antes de tomar una medida correctiva.',
    tone: 'warning',
  },
  {
    tag: 'Atención',
    title: 'La alerta persiste',
    description: 'Prioriza una verificación en sitio, revisa el sensor y aplica el protocolo definido por tu institución.',
    tone: 'error',
  },
]

const actions = [
  {
    label: 'Temperatura',
    action: 'Compara con el confort percibido y revisa ventilación o climatización del espacio.',
  },
  {
    label: 'Humedad',
    action: 'Observa si la lectura se mantiene en el tiempo; revisa filtraciones, ventilación o condiciones del lugar.',
  },
  {
    label: 'CO₂',
    action: 'Verifica ocupación y renovación del aire; prioriza ventilar antes de sacar conclusiones.',
  },
  {
    label: 'Ruido',
    action: 'Identifica la hora y la actividad asociada para reducir fuentes de distracción o reorganizar el espacio.',
  },
]

const modules = [
  { icon: 'grid-outline', title: 'Dashboard', description: 'Vista general con indicadores, resumen de ambientes y prioridades.' },
  { icon: 'radio-outline', title: 'Ambientes', description: 'Listado y ficha detallada de los espacios monitoreados.' },
  { icon: 'bar-chart-outline', title: 'Análisis', description: 'Tendencias, comparativas y señales de cada variable ambiental.' },
  { icon: 'notifications-outline', title: 'Alertas', description: 'Avisos para detectar lecturas que merecen revisión o seguimiento.' },
  { icon: 'star-outline', title: 'Favoritos', description: 'Acceso rápido a los ambientes más consultados por cada usuario.' },
  { icon: 'options-outline', title: 'Configuración', description: 'Preferencias, idioma, accesibilidad y datos de la cuenta.' },
]

const faq = [
  {
    q: '¿Los datos se actualizan todo el tiempo?',
    a: 'La frecuencia depende del sensor y de la conectividad disponible. Si una lectura no cambia, revisa primero la hora de actualización y el estado del dispositivo.',
  },
  {
    q: '¿Una alerta significa que hay una emergencia?',
    a: 'No necesariamente. Es una señal para revisar el contexto, confirmar la lectura y seguir el protocolo de tu institución.',
  },
  {
    q: '¿Puedo comparar dos ambientes?',
    a: 'Sí. Usa el análisis del dashboard y el detalle de cada ambiente para observar diferencias, tendencias y oportunidades de mejora.',
  },
]

const TONES = {
  mint: { color: '#01805b', bg: 'rgba(1,128,91,0.15)' },
  blue: { color: '#2563eb', bg: 'rgba(37,99,235,0.15)' },
  amber: { color: '#d97706', bg: 'rgba(217,119,6,0.15)' },
  purple: { color: '#7c3aed', bg: 'rgba(124,58,237,0.15)' },
}

function SectionHeading({ kicker, title, note, c }) {
  return (
    <View style={{ marginBottom: 12 }}>
      <Text style={[s.kicker, { color: c.accent }]}>{kicker}</Text>
      <Text style={[s.h2, { color: c.textPrimary }]}>{title}</Text>
      {note ? <Text style={[s.note, { color: c.textMuted }]}>{note}</Text> : null}
    </View>
  )
}

export default function GuideScreen() {
  const navigation = useNavigation()
  const { currentColors: c } = useTheme()
  const [openFaq, setOpenFaq] = useState(null)

  return (
    <SafeAreaView style={[s.safe, { backgroundColor: c.bgBody }]}>
      <View style={[s.header, { borderBottomColor: c.borderColor }]}>
        <Pressable style={[s.backBtn, { backgroundColor: c.accentDim }]} onPress={() => navigation.goBack()} hitSlop={8}>
          <Ionicons name="arrow-back" size={15} color={c.accent} />
          <Text style={[s.backTxt, { color: c.accent }]}>Volver</Text>
        </Pressable>
        <View style={[s.statusPill, { backgroundColor: c.accentDim }]}>
          <View style={[s.statusDot, { backgroundColor: c.accent }]} />
          <Text style={[s.statusTxt, { color: c.accent }]}>Centro de demostraciones</Text>
        </View>
      </View>

      <ScrollView contentContainerStyle={s.content}>
        <View style={[s.card, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
          <Text style={[s.kicker, { color: c.accent }]}>Guía completa de EduAirControl</Text>
          <Text style={[s.h1, { color: c.textPrimary }]}>
            Todo lo que necesitas para <Text style={{ color: c.accent }}>leer tu ambiente.</Text>
          </Text>
          <Text style={[s.p, { color: c.textSecondary }]}>
            EduAirControl convierte las mediciones de sensores IoT en información visual para cuidar
            el bienestar, la concentración y la calidad de los espacios educativos.
          </Text>
          <View style={s.introMeta}>
            {['Monitoreo organizado', 'Decisiones basadas en datos', 'Seguimiento continuo'].map((label) => (
              <View key={label} style={[s.introMetaItem, { backgroundColor: c.accentDim }]}>
                <Ionicons name="checkmark-circle" size={13} color={c.accent} />
                <Text style={{ fontSize: 11.5, fontWeight: '700', color: c.accent }}>{label}</Text>
              </View>
            ))}
          </View>
        </View>

        <View style={[s.card, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
          <View style={s.missionRow}>
            <View style={[s.missionIcon, { backgroundColor: c.accentDim }]}>
              <Ionicons name="speedometer-outline" size={24} color={c.accent} />
            </View>
            <View style={s.missionBody}>
              <Text style={[s.kicker, { color: c.accent }]}>El propósito</Text>
              <Text style={[s.h2, { color: c.textPrimary }]}>Del sensor a una decisión útil</Text>
            </View>
          </View>
          <Text style={[s.p, { color: c.textSecondary }]}>
            La aplicación recibe datos de los dispositivos instalados en cada ambiente, los agrupa por
            espacio y los muestra con indicadores que cualquier usuario puede entender. Así puedes pasar
            de “algo se siente diferente” a “qué variable cambió, dónde ocurrió y qué debo revisar”.
          </Text>
        </View>

        <View>
          <SectionHeading kicker="Paso a paso" title="Así se usa la app" note="De la lectura a la acción" c={c} />
          <View style={s.grid2}>
            {steps.map(({ number, icon, title, description }) => (
              <View key={number} style={[s.stepCard, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
                <View style={s.stepTop}>
                  <Text style={[s.stepNumber, { color: c.accent }]}>{number}</Text>
                  <Ionicons name={icon} size={20} color={c.accent} />
                </View>
                <Text style={[s.stepTitle, { color: c.textPrimary }]}>{title}</Text>
                <Text style={[s.stepDesc, { color: c.textSecondary }]}>{description}</Text>
              </View>
            ))}
          </View>
        </View>

        <View>
          <SectionHeading kicker="Diccionario ambiental" title="¿Qué significa cada variable?" note="Lee la tendencia, no solo el número" c={c} />
          <View style={s.grid2}>
            {variables.map(({ icon, name, symbol, description, reading, tone }) => {
              const t = TONES[tone]
              return (
                <View key={name} style={[s.varCard, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
                  <View style={[s.varIcon, { backgroundColor: t.bg }]}>
                    <Ionicons name={icon} size={20} color={t.color} />
                  </View>
                  <View style={s.varTitleRow}>
                    <Text style={[s.varName, { color: c.textPrimary }]}>{name}</Text>
                    <Text style={[s.varSymbol, { color: t.color }]}>{symbol}</Text>
                  </View>
                  <Text style={[s.varDesc, { color: c.textSecondary }]}>{description}</Text>
                  <View style={[s.varReading, { borderColor: c.borderCard }]}>
                    <Text style={[s.varReadingLabel, { color: c.textMuted }]}>Te ayuda a leer</Text>
                    <Text style={[s.varReadingValue, { color: t.color }]}>{reading}</Text>
                  </View>
                </View>
              )
            })}
          </View>
        </View>

        <View>
          <SectionHeading kicker="Cómo interpretar la información" title="Mira el contexto completo" c={c} />
          {signals.map((sig) => {
            const dim = c[`${sig.tone}Dim`]
            const color = c[sig.tone]
            return (
              <View key={sig.tag} style={[s.signalCard, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
                <Text style={[s.signalTag, { backgroundColor: dim, color }]}>{sig.tag}</Text>
                <Text style={[s.signalTitle, { color: c.textPrimary }]}>{sig.title}</Text>
                <Text style={[s.signalDesc, { color: c.textSecondary }]}>{sig.description}</Text>
              </View>
            )
          })}
        </View>

        <View>
          <SectionHeading kicker="Guía práctica" title="¿Qué puedo hacer con cada lectura?" c={c} />
          <View style={[s.card, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
            {actions.map(({ label, action }, i) => (
              <View
                key={label}
                style={[
                  s.actionRow,
                  { borderColor: c.borderCard },
                  i === actions.length - 1 && { borderBottomWidth: 0 },
                ]}
              >
                <View style={[s.actionCheck, { backgroundColor: c.accentDim }]}>
                  <Ionicons name="checkmark-circle" size={18} color={c.accent} />
                </View>
                <View style={s.actionBody}>
                  <Text style={[s.actionLabel, { color: c.textPrimary }]}>{label}</Text>
                  <Text style={[s.actionDesc, { color: c.textSecondary }]}>{action}</Text>
                </View>
              </View>
            ))}
          </View>
        </View>

        <View>
          <SectionHeading kicker="Dentro de tu cuenta" title="Encuentra todo en un solo lugar" c={c} />
          <View style={[s.card, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
            {modules.map(({ icon, title, description }, i) => (
              <View
                key={title}
                style={[
                  s.moduleRow,
                  { borderColor: c.borderCard },
                  i === modules.length - 1 && { borderBottomWidth: 0 },
                ]}
              >
                <View style={[s.missionIcon, { width: 36, height: 36, backgroundColor: c.accentDim }]}>
                  <Ionicons name={icon} size={18} color={c.accent} />
                </View>
                <View style={s.moduleBody}>
                  <Text style={[s.moduleTitle, { color: c.textPrimary }]}>{title}</Text>
                  <Text style={[s.moduleDesc, { color: c.textSecondary }]}>{description}</Text>
                </View>
                <Ionicons name="chevron-forward" size={16} color={c.textMuted} />
              </View>
            ))}
          </View>
        </View>

        <View style={[s.card, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
          <Text style={[s.kicker, { color: c.accent }]}>Trabajo colaborativo</Text>
          <Text style={[s.h2, { color: c.textPrimary }]}>Cada perfil tiene una misión</Text>
          <Text style={[s.p, { color: c.textSecondary }]}>
            La plataforma puede ser usada por estudiantes, docentes, personal administrativo y
            responsables de la gestión ambiental. Todos consultan la misma realidad, pero actúan desde
            necesidades distintas.
          </Text>
          <View style={[s.roleRow, { borderColor: c.borderCard }]}>
            <View style={[s.roleIcon, { backgroundColor: c.accentDim }]}>
              <Ionicons name="people-outline" size={19} color={c.accent} />
            </View>
            <View style={s.roleBody}>
              <Text style={[s.roleTitle, { color: c.textPrimary }]}>Usuario</Text>
              <Text style={[s.roleDesc, { color: c.textSecondary }]}>Consulta ambientes, favoritos y tendencias.</Text>
            </View>
          </View>
          <View style={[s.roleRow, { borderColor: c.borderCard }]}>
            <View style={[s.roleIcon, { backgroundColor: c.accentDim }]}>
              <Ionicons name="build-outline" size={19} color={c.accent} />
            </View>
            <View style={s.roleBody}>
              <Text style={[s.roleTitle, { color: c.textPrimary }]}>Administrador</Text>
              <Text style={[s.roleDesc, { color: c.textSecondary }]}>Gestiona espacios, usuarios y seguimiento operativo.</Text>
            </View>
          </View>
        </View>

        <View style={[s.notice, { backgroundColor: c.warningDim, borderColor: c.warning }]}>
          <Ionicons name="information-circle-outline" size={22} color={c.warning} />
          <View style={s.noticeBody}>
            <Text style={[s.noticeTitle, { color: c.textPrimary }]}>
              Importante: una lectura es una señal, no un diagnóstico.
            </Text>
            <Text style={[s.noticeText, { color: c.textSecondary }]}>
              Los sensores pueden presentar variaciones, errores o interrupciones por conectividad y
              factores externos. Usa los datos como apoyo, verifica el contexto y acude a una evaluación
              técnica cuando sea necesario.
            </Text>
          </View>
        </View>

        <View>
          <SectionHeading kicker="Preguntas frecuentes" title="Antes de comenzar" c={c} />
          {faq.map((item, i) => {
            const open = openFaq === i
            return (
              <View key={item.q} style={[s.faqItem, { backgroundColor: c.bgCard, borderColor: c.borderCard }]}>
                <Pressable style={s.faqHead} onPress={() => setOpenFaq(open ? null : i)}>
                  <Text style={[s.faqQ, { color: c.textPrimary }]}>{item.q}</Text>
                  <Ionicons name={open ? 'chevron-up' : 'chevron-down'} size={16} color={c.accent} />
                </Pressable>
                {open && <Text style={[s.faqA, { color: c.textSecondary }]}>{item.a}</Text>}
              </View>
            )
          })}
        </View>
      </ScrollView>
    </SafeAreaView>
  )
}
