import Navbar from '../shared/navbar';
import LandingBody from '../shared/LandingBody';
import Publicaciones from '../shared/PublicacionesView';
import Footer from '../shared/Footer';

export default function Landing() {
  return (
    <>
      <Navbar />
      <LandingBody/>
      <Publicaciones/>
      <Footer/>
    </>
  )
}