import "./RecommendationBox.css";

function RecommendationBox({ recommendations = [] }) {
  return (
    <section className="recommendation-box">
      <h3>💡 Recomendaciones</h3>

      <ul>
        {recommendations.map((item, index) => (
          <li key={index}>{item}</li>
        ))}
      </ul>
    </section>
  );
}

export default RecommendationBox;