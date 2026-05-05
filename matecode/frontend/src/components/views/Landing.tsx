import LandingBody from '../shared/LandingBody';
import PublicacionesView from '../shared/PublicacionesView';
import Footer from '../shared/Footer';
import NavBar from '../shared/NavBar';
import ImageContainer from '../shared/ImageContainer';
import './Landing.css'

export default function Landing() {
  return (
    <section className="landingSection">
      <NavBar />
      <LandingBody />
      <ImageContainer />
      <PublicacionesView />
      <Footer />
    </section>
  )
}