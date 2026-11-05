import Navbar from "../components/Navbar/Navbar";
import ProductGuide from "../components/ProductGuide/ProductGuide";
import Footer from "../components/Footer/Footer";

function GuideScreen() {
  return (
    <div className="guide-screen">
      <Navbar />
      <ProductGuide />
      <Footer />
    </div>
  );
}

export default GuideScreen;
