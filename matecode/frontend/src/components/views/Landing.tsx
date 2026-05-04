import LandingBody from '../shared/LandingBody';
import Publicaciones from '../shared/PublicacionesView';
import Footer from '../shared/Footer';
import Navbar from '../shared/Navbar';

export default function Landing() {
  return (
    <>
      <Navbar />
      <LandingBody />
      <Publicaciones />
      <Footer />
    </>
  )
}