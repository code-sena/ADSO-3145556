function Checkbox({ label, checked, onChange }) {
  return (
    <label className="remember-me">
      <input type="checkbox" checked={checked} onChange={onChange} />
      {label}
    </label>
  )
}

export default Checkbox
