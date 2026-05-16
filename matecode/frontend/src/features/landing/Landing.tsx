import LandingBody from './LandingBody';
import PublicacionesView from '../publicaciones/PublicacionesView';
import Footer from '../../ui/footer/Footer';
import NavBar from '../../ui/navBar/NavBar';
import ImageContainer from './ImageContainer';
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