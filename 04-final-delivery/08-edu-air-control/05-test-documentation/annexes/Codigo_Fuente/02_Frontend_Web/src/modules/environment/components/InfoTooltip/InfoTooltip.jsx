import { useState, useRef, useEffect, useCallback } from "react";
import { createPortal } from "react-dom";
import { FaExclamation } from "react-icons/fa";
import "./InfoTooltip.css";

const GAP = 10;
const TOOLTIP_WIDTH = 260;
const TOOLTIP_HEIGHT_GUESS = 120;

function InfoTooltip({ text, label = "Más información" }) {
  const [open, setOpen] = useState(false);
  const [coords, setCoords] = useState({ top: 0, left: 0 });
  const btnRef = useRef(null);

  const calculatePosition = useCallback(() => {
    const rect = btnRef.current?.getBoundingClientRect();
    if (!rect) return;

    const vw = window.innerWidth;
    const vh = window.innerHeight;

    let left = rect.right - TOOLTIP_WIDTH;
    left = Math.max(8, Math.min(left, vw - TOOLTIP_WIDTH - 8));

    let top = rect.bottom + GAP;
    if (top + TOOLTIP_HEIGHT_GUESS > vh) {
      top = rect.top - TOOLTIP_HEIGHT_GUESS - GAP;
    }

    setCoords({ top, left });
  }, []);

  const toggle = (e) => {
    e.stopPropagation();
    if (!open) calculatePosition();
    setOpen((v) => !v);
  };

  useEffect(() => {
    if (!open) return;

    const close = () => setOpen(false);

    document.addEventListener("mousedown", close);
    window.addEventListener("scroll", close, true);
    window.addEventListener("resize", close);

    return () => {
      document.removeEventListener("mousedown", close);
      window.removeEventListener("scroll", close, true);
      window.removeEventListener("resize", close);
    };
  }, [open]);

  if (!text) return null;

  return (
    <>
      <button
        ref={btnRef}
        type="button"
        className="info-tooltip-btn"
        aria-label={label}
        onClick={toggle}
      >
        <FaExclamation />
      </button>

      {open &&
        createPortal(
          <div
            className="info-tooltip-popover"
            role="tooltip"
            style={{ top: coords.top, left: coords.left }}
            onMouseDown={(e) => e.stopPropagation()}
          >
            {text}
          </div>,
          document.body
        )}
    </>
  );
}

export default InfoTooltip;