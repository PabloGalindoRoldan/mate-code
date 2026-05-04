import LandingBody from '../shared/LandingBody';
import Publicaciones from '../shared/PublicacionesView';
import Footer from '../shared/Footer';
import NavBar from '../shared/Navbar';
import './Landing.css'

export default function Landing() {
  return (
    <section className="landingSection">
      <NavBar />
      <LandingBody />
      <Publicaciones />
      <Footer />
    </section>
  )
}