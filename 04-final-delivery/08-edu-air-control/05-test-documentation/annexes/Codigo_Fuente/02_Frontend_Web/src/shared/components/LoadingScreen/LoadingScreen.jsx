import './LoadingScreen.css';

function LoadingScreen() {
  return (
    <div className="loading-screen" role="status" aria-label="Cargando...">
      <div className="loading-screen__spinner" />
      <span className="loading-screen__text">Cargando...</span>
    </div>
  );
}

export default LoadingScreen;
