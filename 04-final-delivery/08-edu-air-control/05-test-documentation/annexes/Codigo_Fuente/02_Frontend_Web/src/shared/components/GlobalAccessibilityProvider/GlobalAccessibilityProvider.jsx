import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import AccessibilityWidget from "../../../modules/landing/components/AccessibilityWidget/AccessibilityWidget";
import {
    applyAccessibilitySettings,
    getAccessibilitySettings,
} from "../../accessibility/accessibilitySettings";

/**
 * GlobalAccessibilityProvider
 * Envuelve la app para proporcionar el widget de accesibilidad globalmente
 * y sincronizar el estado de accesibilidad en todas las pantallas.
 *
 * El widget de accesibilidad se muestra en TODAS las rutas.
 * En el landing, el widget se eleva (raised=true) para dejar espacio
 * al botón de "volver arriba" que está debajo.
 */
function GlobalAccessibilityProvider({ children }) {
    const location = useLocation();
    const isLanding = location.pathname === "/" || location.pathname === "/landing";

    const applyA11ySettings = () => {
        applyAccessibilitySettings(getAccessibilitySettings());
    };

    useEffect(() => {
        // Aplicar ajustes al montar
        applyA11ySettings();

        // Escuchar cambios del widget de accesibilidad
        const handleA11yChange = () => applyA11ySettings();
        window.addEventListener("a11y-change", handleA11yChange);

        // Seguir el tema del sistema mientras el usuario no elija uno explícito
        const mq = window.matchMedia?.("(prefers-color-scheme: dark)");
        const handleSystemTheme = () => {
            if (localStorage.getItem("darkMode") === null) applyA11ySettings();
        };
        mq?.addEventListener?.("change", handleSystemTheme);

        return () => {
            window.removeEventListener("a11y-change", handleA11yChange);
            mq?.removeEventListener?.("change", handleSystemTheme);
        };
    }, []);

    // Re-aplicar al cambiar de ruta para que ningún componente de página
    // pueda resetear las clases del body
    useEffect(() => {
        applyA11ySettings();
    }, [location]);

    return (
        <>
            {children}
            {/* El widget de accesibilidad aparece en TODAS las pantallas.
                En el landing se eleva para dejar espacio al botón scroll-to-top. */}
            <AccessibilityWidget raised={isLanding} />
        </>
    );
}

export default GlobalAccessibilityProvider;
