import "./Hero.css";

import HeroContent from "../HeroContent/HeroContent";
import HeroCards from "../HeroCards/HeroCards";

function Hero() {
  return (
    <section id="hero" className="hero">
      <div className="hero-glow hero-glow-left"></div>
      <div className="hero-glow hero-glow-right"></div>

      <div className="hero-container">
        <HeroContent />
        <HeroCards />
      </div>

      <div className="hero-transition"></div>
    </section>
  );
}

export default Hero;
