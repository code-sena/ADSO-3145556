import EnvironmentCard from "../EnvironmentCard/EnvironmentCard";
import "./EnvironmentGrid.css";

function EnvironmentGrid({ environments, onToggleFavorite }) {
  return (
    <div className="environment-grid">
      {environments.map((environment) => (
        <EnvironmentCard
          key={environment.id}
          environment={environment}
          onToggleFavorite={onToggleFavorite}
        />
      ))}
    </div>
  );
}

export default EnvironmentGrid;